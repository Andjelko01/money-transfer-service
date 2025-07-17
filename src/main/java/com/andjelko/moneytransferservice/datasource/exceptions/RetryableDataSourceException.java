package com.andjelko.moneytransferservice.datasource.exceptions;

import com.andjelko.moneytransferservice.datasource.error.DatasourceRetryPolicy;

public class RetryableDataSourceException extends DataSourceException {

    public RetryableDataSourceException(String message, Throwable cause) {
        super(message, cause, DatasourceRetryPolicy.RETRYABLE);
    }
}
