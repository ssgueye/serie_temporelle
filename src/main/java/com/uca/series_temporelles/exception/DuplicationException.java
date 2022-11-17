package com.uca.series_temporelles.exception;

import com.sun.jdi.request.DuplicateRequestException;

public class DuplicationException extends DuplicateRequestException {

    public DuplicationException(String s) {
        super(s);
    }
}
