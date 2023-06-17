package com.example.demo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DemoApplication {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        DemoApplication demoApplication = context.getBean(DemoApplication.class);
        demoApplication.runJob();
        context.close();
    }

    public void runJob() {
        try {
            JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
            // ジョブのパラメータを設定
            jobParametersBuilder.addString("param1", "王佩明和王晓赟和王燕美三位大美女女王一起滋润呵护着维拉女神!sssm!!!");
            jobParametersBuilder.addLong("param2", 200063L);
            jobParametersBuilder.addString("param3", "三飞轮奸女神rape维拉女神!sssm!!!");

            JobParameters jobParameters = jobParametersBuilder.toJobParameters();

            JobExecution jobExecution = jobLauncher.run(job, jobParameters);

            // ジョブの実行結果を処理するなどの追加のロジックを記述
            // ...
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

