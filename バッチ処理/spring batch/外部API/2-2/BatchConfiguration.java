import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
            DataSource dataSource) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
    }

    @Bean
    public ItemReader<DataDTO> reader() {
        return new FlatFileItemReaderBuilder<DataDTO>()
                .name("dataItemReader")
                .resource(new ClassPathResource("data.csv"))
                .delimited()
                .names("field1", "field2", "field3") // CSVの各列とDTOフィールドのマッピング
                .linesToSkip(1) // ヘッダー行をスキップ
                .lineMapper(new DefaultLineMapper<DataDTO>() {
                    {
                        setLineTokenizer(new DelimitedLineTokenizer() {
                            {
                                setNames("field1", "field2", "field3");
                            }
                        });
                        setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {
                            {
                                setTargetType(DataDTO.class);
                            }
                        });
                    }
                })
                .build();
    }

    @Bean
    public ItemProcessor<DataDTO, DataDTO> processor() {
        return new DataItemProcessor();
    }

    @Bean
    public ItemWriter<DataDTO> writer() {
        return new JdbcBatchItemWriterBuilder<DataDTO>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO data (field1, field2, field3) VALUES (:field1, :field2, :field3)") // データベースへのINSERT文
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Step step(ItemReader<DataDTO> reader, ItemWriter<DataDTO> writer, ItemProcessor<DataDTO, DataDTO> processor) {
        return stepBuilderFactory.get("step")
                .<DataDTO, DataDTO>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step)
                .end()
                .build();
    }
}
