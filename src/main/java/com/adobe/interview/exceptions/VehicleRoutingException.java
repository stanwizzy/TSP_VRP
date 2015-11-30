package com.adobe.interview.exceptions;


public class VehicleRoutingException extends Exception{

    private String errorMessage;

    public VehicleRoutingException(String errorMessage){
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
