import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private DataSource dataSource;

    @Bean
    public Step lotteryStep() {
        return stepBuilderFactory.get("lotteryStep")
                .tasklet((stepContribution, chunkContext) -> {
                    // データベース接続を使用して proc_lottery.sql の処理を実行するコードを記述する
                    // 例えば、JDBCを使用してストアドプロシージャを呼び出す場合は次のようになる可能性があります：
                    // Connection connection = dataSource.getConnection();
                    // CallableStatement statement = connection.prepareCall("{call proc_lottery()}");
                    // statement.execute();
                    // statement.close();
                    // connection.close();
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Job lotteryJob(Step lotteryStep) {
        return jobBuilderFactory.get("lotteryJob")
                .incrementer(new RunIdIncrementer())
                .flow(lotteryStep)
                .end()
                .build();
    }

    // ジョブを起動するメソッド
    public void runJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution jobExecution = jobLauncher.run(lotteryJob(null), jobParameters);
        // ジョブの実行結果に関する処理を記述する（例: ログ出力、エラーハンドリングなど）
    }
}
