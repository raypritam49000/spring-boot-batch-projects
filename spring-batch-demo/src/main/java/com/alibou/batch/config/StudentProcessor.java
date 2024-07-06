package com.alibou.batch.config;

import com.alibou.batch.model.Student;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.Nullable;

public class StudentProcessor implements ItemProcessor<Student,Student> {

    @Override
    public Student process(@Nullable Student student) {
        return student;
    }
}
