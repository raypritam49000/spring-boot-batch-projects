package com.spring.boot.batch.config;

import com.spring.boot.batch.model.Student;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CustomItemProcessor implements ItemProcessor<Student, Student> {

    @Override
    public Student process(@Nullable Student student) throws Exception {
        return student;
    }
}
