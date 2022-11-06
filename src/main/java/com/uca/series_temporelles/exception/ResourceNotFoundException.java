package com.uca.series_temporelles.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String exception_message){
        super(exception_message);
    }
}
