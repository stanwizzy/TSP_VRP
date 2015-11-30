package com.adobe.interview.inputGraph;


import com.adobe.interview.exceptions.VehicleRoutingRuntimeException;
import org.apache.log4j.Logger;

import java.text.DecimalFormat;

public class CustomerLocation {

    private double xCoordinate;
    private double yCoordinate;
    private static final int NUM_OF_COORDINATES_SUPPORTED = 2;
    private static final Logger logger = Logger.getLogger(CustomerLocation.class);

    private CustomerLocation(double xCoordinate, double yCoordinate){
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public static CustomerLocation parseTextWithSpaceDelimitedCoordinates(String textWithSpaceDelimitedCoordinates){

        logger.debug("Parsing line with x and y coordinates: " + textWithSpaceDelimitedCoordinates);

        exitIfNullArgument(textWithSpaceDelimitedCoordinates);
        String[] coordinates = textWithSpaceDelimitedCoordinates.split("\\s+");
        if(coordinates.length == NUM_OF_COORDINATES_SUPPORTED){
            return getCustomerLocation(coordinates);
        }else{
            logger.error("Program supports 2 dimensional coordinates only. Could not parse String with Coordinates: " + textWithSpaceDelimitedCoordinates);
            throw new VehicleRoutingRuntimeException("Could not parse the line: " + textWithSpaceDelimitedCoordinates );
        }
    }

    private static void exitIfNullArgument(Object o) {
        if(o == null){
            throw new VehicleRoutingRuntimeException("Null argument provided");
        }
    }

    private static CustomerLocation getCustomerLocation(String[] coordinates) {

        try{
            return tryGetCustomerLocation(coordinates);
        }catch(NumberFormatException e){
            logger.error("Cannot parse non-numeric coordinate values");
            throw new VehicleRoutingRuntimeException("Cannot parse non-numeric coordinate values " + e.getMessage());
        }
    }

    private static CustomerLocation tryGetCustomerLocation(String[] coordinates) throws NumberFormatException{

        double xCoordinateOfCustomerLocation = Double.valueOf(coordinates[0]);
        double yCoordinateOfCustomerLocation = Double.valueOf(coordinates[1]);
        return new CustomerLocation(xCoordinateOfCustomerLocation,yCoordinateOfCustomerLocation);
    }

    public double computeDistanceFromAnotherCustomerLocation(CustomerLocation anotherCustomerLocation){

        logger.debug("Computing distance between the customer locations: (1): " + xCoordinate +" " + yCoordinate + " (2) " + anotherCustomerLocation.getXCoordinate() + " " + anotherCustomerLocation.getYCoordinate()) ;

        exitIfNullArgument(anotherCustomerLocation);
        double xCoordinateDifference = xCoordinate - anotherCustomerLocation.getXCoordinate();
        double yCoordinateDifference = yCoordinate - anotherCustomerLocation.getYCoordinate();
        double squareOfDistanceBetweenTwoCustomerLocations = Math.pow(xCoordinateDifference,2) + Math.pow(yCoordinateDifference,2);
        if(squareOfDistanceBetweenTwoCustomerLocations > 0){
            return formatToOneDecimalPlace(Math.sqrt(squareOfDistanceBetweenTwoCustomerLocations));
        }else{
            logger.warn("Tried computing distance between 2 identical locations, returning 0");
            return 0;
        }
    }

    private double formatToOneDecimalPlace(double distance) {
        DecimalFormat oneDigit = new DecimalFormat("###0.0");//format to 1 decimal place
        return Double.valueOf(oneDigit.format(distance));
    }

    public double getXCoordinate(){
        return xCoordinate;
    }

    public double getYCoordinate(){
        return yCoordinate;
    }

    @Override
    public String toString(){

        return xCoordinate + " " + yCoordinate;
    }
}
