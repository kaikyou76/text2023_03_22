import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import jp.co.netmarks.persistence.OrganizationService;
import jp.co.netmarks.persistence.BatRuntimeException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Assert;

@Configuration
@EnableBatchProcessing
public class OrganizationBatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ItemReader<String> csvItemReader;

    @Autowired
    private ItemProcessor<String, OrganizationEntity> csvItemProcessor;

    @Autowired
    private ItemWriter<OrganizationEntity> csvItemWriter;

    @Autowired
    private JobLauncher jobLauncher;

    @Bean
    public Job organizationJob(Step organizationStep) {
        return jobBuilderFactory.get("organizationJob")
                .flow(organizationStep)
                .end()
                .build();
    }

    @Bean
    public Step organizationStep() {
        return stepBuilderFactory.get("organizationStep")
                .<String, OrganizationEntity>chunk(10)
                .reader(csvItemReader)
                .processor(csvItemProcessor)
                .writer(csvItemWriter)
                .build();
    }

    @Bean
    public ItemReader<String> csvItemReader() {
        FlatFileItemReader<String> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource("path/to/csv/organization.csv")); // CSVファイルのパスを指定してください
        reader.setLinesToSkip(1); // ヘッダー行をスキップする場合は1に設定してください
        reader.setLineMapper(new DefaultLineMapper<String>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames("organization_cd", "organization_nm", "organization_no", "organization_abbreviated_nm",
                        "print_order", "class_sales", "class_data_input", "update_date");
                setDelimiter(",");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<String>() {{
                setTargetType(String.class);
            }});
        }});
        return reader;
    }

    @Bean
    public ItemProcessor<String, OrganizationEntity> csvItemProcessor() {
        return item -> {
            String[] fields = item.split(",");
            OrganizationEntity organization = new OrganizationEntity();
            organization.setOrganizationCd(fields[0]);
            organization.setOrganizationNm(fields[1]);
            organization.setOrganizationNo(fields[2]);
            organization.setOrganizationAbbreviatedNm(fields[3]);
            organization.setPrintOrder(Integer.parseInt(fields[4]));
            organization.setClassSales(Integer.parseInt(fields[5]));
            organization.setClassDataInput(Integer.parseInt(fields[6]));
            organization.setUpdateDate(LocalDateTime.parse(fields[7], DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            return organization;
        };
    }

    @Bean
    public ItemWriter<OrganizationEntity> csvItemWriter(OrganizationMapper organizationMapper) {
        return items -> {
            for (OrganizationEntity item : items) {
                organizationMapper.insertOrganization(item);
            }
        };
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        if (!file.isEmpty()) {
            try {
                // アップロードされたCSVファイルを一時ディレクトリに保存
                String tempFilePath = "path/to/temp/directory/file.csv"; // 一時ディレクトリのパスを指定してください
                Files.copy(file.getInputStream(), new File(tempFilePath).toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Jobのパラメータを設定
                JobParameters jobParameters = new JobParametersBuilder()
                        .addString("inputFile", tempFilePath)
                        .toJobParameters();

                // Jobを実行
                JobExecution jobExecution = jobLauncher.run(organizationJob(), jobParameters);

                // Jobの実行結果を取得
                String exitStatus = jobExecution.getExitStatus().getExitCode();

                model.addAttribute("message", "ファイルのアップロードとデータベースへの挿入が完了しました。");
            } catch (IOException | org.springframework.batch.core.JobExecutionException e) {
                model.addAttribute("message", "ファイルのアップロードやデータベースへの挿入処理中にエラーが発生しました。");
                e.printStackTrace();
            }
        } else {
            model.addAttribute("message", "アップロードされたファイルが空です。");
        }
        return "upload";
    }
}
