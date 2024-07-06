package com.sc.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.sc.batch.DataProcessor;
import com.sc.batch.DataReader;
import com.sc.batch.DataWriter;

@Configuration
public class BatchConfig {

    @Bean
    Step readData(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("readData", jobRepository)
				.allowStartIfComplete(true)
				.tasklet(new DataReader(), transactionManager)
				.build();
	}

    @Bean
    Step processData(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("processData", jobRepository)
				.allowStartIfComplete(true)
				.tasklet(new DataProcessor(), transactionManager)
				.build();
	}

    @Bean
    Step writeData(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("writeData", jobRepository)
				.allowStartIfComplete(true)
				.tasklet(new DataWriter(), transactionManager)
				.build();
	}

    @Bean
    Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new JobBuilder("job", jobRepository)
				.start(readData(jobRepository, transactionManager))
				.next(processData(jobRepository, transactionManager))
				.next(writeData(jobRepository, transactionManager))
				.build();
	}

}
