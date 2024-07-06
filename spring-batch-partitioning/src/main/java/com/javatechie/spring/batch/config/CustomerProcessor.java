package com.javatechie.spring.batch.config;

import com.javatechie.spring.batch.entity.Customer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;

public class CustomerProcessor implements ItemProcessor<Customer, Customer> {
    @Override
    public Customer process(@NonNull Customer customer) throws Exception {
        return customer;
    }
}
