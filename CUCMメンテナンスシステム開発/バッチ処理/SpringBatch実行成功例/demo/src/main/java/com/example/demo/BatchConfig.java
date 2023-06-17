package com.example.demo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
//@Configuration: 内部で @Bean を利用するため
//@EnableBatchProcessing: JobBuilderFactory, StepBuilderFactory を利用するため
//@RequiredArgsConstructor: 上記を DI するため
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final Tasklet tasklet1;

    @Autowired
    public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, Tasklet tasklet1) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.tasklet1 = tasklet1;
    }

    /**
     * Step を作成、helloStep1 という名前のステップで、
     * 実際の処理は tasklet1 を行う
     */
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("helloStep1")
                .tasklet(tasklet1)
                //.listener(stepListener)
                .build();
    }

    /**
     * Job の作成、helloJob というジョブ
     * 実行毎に実行IDをインクリメントすることでパラメータ重複しないようにする
     * 実際の処理は step1 を行う
     */
    @Bean
    public Job job() {
        return jobBuilderFactory.get("helloJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                //.listener(jobListener)
                .build();
    }
 //   ジョブの前後に処理を挟む場合は JobExecutionListenerSupport を利用します。
 //   .listener(jobListener)
//    ステップの場合は、StepExecutionListenerSupport を利用します。
  //.listener(stepListener)
}

