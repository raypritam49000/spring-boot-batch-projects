package com.sc.batch;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.sc.config.Data;
import com.sc.util.FileUtils;

public class DataReader implements Tasklet, StepExecutionListener {

    private final Logger logger = LoggerFactory.getLogger(DataReader.class);

    private List<Data> datas;
    private FileUtils fileUtil;

    @Override
    public void beforeStep(StepExecution stepExecution) {
    	datas = new ArrayList<>();
    	fileUtil = new FileUtils("data.csv");
        logger.info("Data Reader initialized.");
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        Data data = fileUtil.readLine();
        
        while (data != null) {
        	datas.add(data);
            logger.info("Read data: " + data);
            data = fileUtil.readLine();
        }
        
        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
    	fileUtil.closeReader();
      
    	stepExecution
          .getJobExecution()
          .getExecutionContext()
          .put("datas", datas);
        
    	logger.debug("Data Reader ended.");
        
    	return ExitStatus.COMPLETED;
    }
}
