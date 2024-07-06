package com.spring.boot.batch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class JobExecutionListenerImpl implements JobExecutionListener {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void beforeJob(@Nullable JobExecution jobExecution) {
        LOGGER.info("Job Started");
    }

    @Override
    public void afterJob(@Nullable JobExecution jobExecution) {
        assert jobExecution != null;
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            LOGGER.info("Job Completed");
        }
    }
}
