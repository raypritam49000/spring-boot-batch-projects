package com.sc.batch;

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
import com.sc.util.FileUtils;

public class DataWriter implements Tasklet, StepExecutionListener {

	private final Logger logger = LoggerFactory.getLogger(DataWriter.class);

	private List<Data> datas;
	private FileUtils fileUtil;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		ExecutionContext executionContext = stepExecution
				.getJobExecution()
				.getExecutionContext();

		datas = (List<Data>) executionContext.get("datas");
		fileUtil = new FileUtils("output.csv");
		logger.info("Data Writer initialized.");
	}

	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
		for (Data data : datas) {
			fileUtil.writeLine(data);
			logger.info("Write data {} ", data);
		}

		return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		fileUtil.closeWriter();
		logger.debug("Data Writer ended.");
		return ExitStatus.COMPLETED;
	}
}
