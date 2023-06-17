package com.example.demo;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class BatchStepListener extends StepExecutionListenerSupport {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("beforeStep");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("aftefStep");
        return stepExecution.getExitStatus();
    }
}