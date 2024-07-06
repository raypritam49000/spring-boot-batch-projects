package com.spring.boot.batch.config;

import com.spring.boot.batch.model.Student;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {

    @Bean
    public Job jobBean(JobRepository jobRepository,
                       JobExecutionListener listener,
                       Step steps) {
        return new JobBuilder("job", jobRepository)
                .listener(listener)
                .start(steps)
                .build();
    }

    @Bean
    public Step steps(JobRepository jobRepository,
                      DataSourceTransactionManager transactionManager,
                      ItemReader<Student> itemReader,
                      ItemProcessor<Student, Student> itemProcessor,
                      ItemWriter<Student> itemWriter
    ) {
        return new StepBuilder("jobStep", jobRepository)
                .<Student, Student>chunk(5, transactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    // reader
    @Bean
    public FlatFileItemReader<Student> reader() {
        return new FlatFileItemReaderBuilder<Student>()
                .name("itemReader")
                .resource(new ClassPathResource("students.csv"))
                .delimited()
                .names("id", "firstName", "lastName", "age")
                .targetType(Student.class)
                .linesToSkip(1)
                .build();
    }

    // processor
    @Bean
    public ItemProcessor<Student, Student> itemProcessor() {
        return new CustomItemProcessor();
    }

    // writer
    @Bean
    public ItemWriter<Student> itemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Student>()
                .sql("insert into student(id, firstname, lastname, age) values (:id, :firstname, :lastname, :age)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }
}
