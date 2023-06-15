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

//Spring Batchのジョブの設定とジョブのステップの実装
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
    private ItemProcessor<String, Organization> csvItemProcessor;
    
    @Autowired
    private ItemWriter<Organization> csvItemWriter;
    
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
            .<String, Organization>chunk(10)
            .reader(csvItemReader)
            .processor(csvItemProcessor)
            .writer(csvItemWriter)
            .build();
    }
    
    @Bean
    public ItemReader<String> csvItemReader() {
        // CSVファイルを読み込むItemReaderを実装してください
    }
    
    @Bean
    public ItemProcessor<String, Organization> csvItemProcessor() {
        // CSVデータをバリデーションチェックし、Organizationオブジェクトに変換するItemProcessorを実装してください
    }
    
    @Bean
    public ItemWriter<Organization> csvItemWriter() {
        // Organizationオブジェクトをデータベースに登録するItemWriterを実装してください
    }
   
    public void runOrganizationJob(MultipartFile file) throws Exception {
        Resource resource = new FileSystemResource(file.getOriginalFilename());
        file.transferTo(resource.getFile());
        
        JobParameters jobParameters = new JobParametersBuilder()
            .addString("inputFile", resource.getFile().getAbsolutePath())
            .toJobParameters();
        
        JobExecution jobExecution = jobLauncher.run(organizationJob(), jobParameters);
        // ジョブの実行結果などの処理を行う場合、必要に応じて記述してください
    }
}
/**
file.transferTo(resource.getFile())は、MultipartFileオブジェクトから取得したファイルデータを、指定されたresourceのファイルに転送（コピー）する処理を行います。

MultipartFileは、HTTPリクエストでアップロードされたファイルを表すためのインターフェースです。getFile()メソッドは、MultipartFileオブジェクトのバッキングストレージ（一時ファイルなど）を表すFileオブジェクトを返します。

transferTo()メソッドは、MultipartFileのデータを指定されたFileオブジェクトに転送するメソッドです。つまり、アップロードされたファイルの内容を、指定されたファイルにコピーすることができます。

したがって、file.transferTo(resource.getFile())は、MultipartFileオブジェクトから取得したファイルデータを、指定されたresourceのファイルに転送（コピー）する処理を行うことを示しています。これにより、アップロードされたファイルの内容をファイルシステム上の別の場所に保存することができます。

.addString("inputFile", resource.getFile().getAbsolutePath())は、ジョブのパラメータに名前と値を追加する操作を行っています。

.addString()メソッドは、ジョブパラメータを追加するためのメソッドであり、引数にはパラメータの名前と値を指定します。

"inputFile"は、パラメータの名前です。この場合、入力ファイルのパラメータを表すために使用されています。

resource.getFile().getAbsolutePath()は、resourceのファイルの絶対パスを取得するためのメソッドです。getFile()メソッドはFileオブジェクトを返し、getAbsolutePath()メソッドはそのファイルの絶対パスを文字列として取得します。

したがって、.addString("inputFile", resource.getFile().getAbsolutePath())は、入力ファイルのパラメータ名を「inputFile」として、resourceのファイルの絶対パスを値としてジョブのパラメータに追加する操作を行っています。このようにして、ジョブの実行時に入力ファイルのパスをパラメータとして渡すことができます。
*/ 