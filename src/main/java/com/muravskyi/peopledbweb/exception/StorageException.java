package com.muravskyi.peopledbweb.exception;

public class StorageException extends RuntimeException {

    public StorageException(Exception e) {
        super(e);
    }
    
}
