package com.andjelko.moneytransferservice.datasource.exceptions;

import com.andjelko.moneytransferservice.datasource.error.DatasourceRetryPolicy;
import lombok.Getter;

@Getter
public class DataSourceException extends RuntimeException {

    private final DatasourceRetryPolicy retryPolicy;

    public DataSourceException(String message, Throwable cause, DatasourceRetryPolicy retryPolicy) {
        super(message, cause);
        this.retryPolicy = retryPolicy;
    }
}
