package com.spring.batch.config;

import com.spring.batch.model.Student;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.Nullable;

public class StudentItemProcessor implements ItemProcessor<Student, Student> {

    @Override
    public Student process(@Nullable Student student) throws Exception {
        return student;
    }
}
