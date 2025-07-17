package com.andjelko.moneytransferservice.datasource.exceptions;

import com.andjelko.moneytransferservice.datasource.error.DatasourceRetryPolicy;

public class NonRetryableDataSourceException extends DataSourceException {

    public NonRetryableDataSourceException(String message, Throwable cause) {
        super(message, cause, DatasourceRetryPolicy.NON_RETRYABLE);
    }

    public NonRetryableDataSourceException(String message) {
        super(message, null, DatasourceRetryPolicy.NON_RETRYABLE);
    }
}
