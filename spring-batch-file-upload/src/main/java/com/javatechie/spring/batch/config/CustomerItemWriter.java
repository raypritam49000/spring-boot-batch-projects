package com.javatechie.spring.batch.config;

import com.javatechie.spring.batch.entity.Customer;
import com.javatechie.spring.batch.repository.CustomerRepository;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerItemWriter implements ItemWriter<Customer> {

    @Autowired
    private CustomerRepository repository;

    @Override
    public void write(@Nullable List<? extends Customer> list) throws Exception {
        System.out.println("Writer Thread "+Thread.currentThread().getName());
        assert list != null;
        repository.saveAll(list);
    }
}
