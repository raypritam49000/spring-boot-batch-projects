package com.spring.batch.config;

import com.spring.batch.model.Student;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.util.logging.Logger;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
    private static final Logger LOGGER = Logger.getLogger(BatchConfig.class.getName());
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<Student> reader() {
        FlatFileItemReader<Student> reader = new FlatFileItemReader<Student>();
        reader.setResource(new ClassPathResource("students.csv"));
        reader.setLineMapper(getLineMapper());
        reader.setLinesToSkip(1);
        return reader;
    }

    private LineMapper<Student> getLineMapper() {
        DefaultLineMapper<Student> lineMapper = new DefaultLineMapper<Student>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("id", "firstName", "lastName", "age");
        lineTokenizer.setIncludedFields(0, 1, 2, 3);
        BeanWrapperFieldSetMapper<Student> fieldsSetter = new BeanWrapperFieldSetMapper<>();
        fieldsSetter.setTargetType(Student.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldsSetter);
        return lineMapper;
    }

    @Bean
    public StudentItemProcessor processor() {
        return new StudentItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Student> writer() {
        JdbcBatchItemWriter<Student> writer = new JdbcBatchItemWriter<Student>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Student>());
        String sql_query = "insert into student(id, firstname, lastname, age) values (:id, :firstname, :lastname, :age)";
        LOGGER.info("SQL query: "+sql_query);
        writer.setSql(sql_query);
        writer.setDataSource(this.dataSource);
        return writer;
    }

    @Bean
    public Job importStudentJob() {
        return jobBuilderFactory.get("student-import-job")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Student, Student>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

}
