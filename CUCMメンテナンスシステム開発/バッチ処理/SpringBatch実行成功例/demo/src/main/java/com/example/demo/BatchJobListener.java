package com.example.demo;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class BatchJobListener extends JobExecutionListenerSupport {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("beforeJob");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("aftefJob");
    }
}