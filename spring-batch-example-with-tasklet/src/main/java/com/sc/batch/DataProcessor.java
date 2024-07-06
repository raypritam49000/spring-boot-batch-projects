package com.sc.batch;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import com.sc.config.Data;

public class DataProcessor implements Tasklet, StepExecutionListener {

	private final Logger logger = LoggerFactory.getLogger(DataProcessor.class);

	private List<Data> datas;

	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
		for (Data data : datas) {
			long age = ChronoUnit.YEARS.between(data.getDob(), LocalDate.now());
			logger.info("Calculating age {} for data {}", age, data);
			data.setAge(age);
		}

		return RepeatStatus.FINISHED;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();

		datas = (List<Data>) executionContext.get("datas");
		logger.debug("Data Processor initialized.");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		logger.debug("Data Processor ended.");
		return ExitStatus.COMPLETED;
	}
}
