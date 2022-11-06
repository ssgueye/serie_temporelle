package com.uca.series_temporelles.exception;

import org.springframework.dao.DataAccessException;

public class NoAccessDataException extends DataAccessException {

    public NoAccessDataException(String msg) {
        super(msg);
    }

}
