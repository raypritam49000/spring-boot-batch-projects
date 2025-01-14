package com.javatechie.spring.batch.config;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.lang.Nullable;

public class ExceptionSkipPolicy implements SkipPolicy {

    @Override
    public boolean shouldSkip(@Nullable Throwable throwable, int i) throws SkipLimitExceededException {
        return throwable instanceof NumberFormatException ;
    }
}
