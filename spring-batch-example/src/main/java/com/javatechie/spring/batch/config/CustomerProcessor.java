package com.javatechie.spring.batch.config;

import com.javatechie.spring.batch.entity.Customer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.Nullable;

public class CustomerProcessor implements ItemProcessor<Customer,Customer> {

    @Override
    public Customer process(@Nullable Customer customer) throws Exception {
        assert customer != null;
        if(customer.getCountry().equals("United States")) {
            return customer;
        }else{
            return null;
        }
    }
}
