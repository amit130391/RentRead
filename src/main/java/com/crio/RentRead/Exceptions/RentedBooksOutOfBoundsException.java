package com.crio.RentRead.Exceptions;

public class RentedBooksOutOfBoundsException extends RuntimeException{
    public RentedBooksOutOfBoundsException(String message){
        super(message);
    }
}
