package com.adobe.interview.exceptions;


public class VehicleRoutingRuntimeException extends RuntimeException {

    private String errorMessage;

    public VehicleRoutingRuntimeException(String errorMessage){
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
