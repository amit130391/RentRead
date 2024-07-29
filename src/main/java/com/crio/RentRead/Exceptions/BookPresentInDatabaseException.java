package com.crio.RentRead.Exceptions;

public class BookPresentInDatabaseException extends RuntimeException{
    public BookPresentInDatabaseException(String message){
        super(message);
    }
}
