package com.crio.RentRead.Exceptions;

public class BookNotRentedByUserException extends RuntimeException{
    public BookNotRentedByUserException(String message){
        super(message);
    }
}
