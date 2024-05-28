package com.libraryManagement.library_management.exceptions;

public class MemberNotAvailableException extends RuntimeException{
    public MemberNotAvailableException(String message){
        super(message);
    }
}
