package com.adobe.interview.inputGraph;


import com.adobe.interview.exceptions.VehicleRoutingRuntimeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class CustomerLocationTest {

    private static final double ERROR_DELTA = 0.001;

    @Test
    public void testTwoTokenDelimitedStringParsing(){
        String testInputString = "230 400";
        CustomerLocation customerLocation = CustomerLocation.parseTextWithSpaceDelimitedCoordinates(testInputString);
        assertEquals(230.0, customerLocation.getXCoordinate(),ERROR_DELTA);
    }

    @Test(expected=VehicleRoutingRuntimeException.class)
    public void testThreeTokenDelimitedStringParsing(){
        String testInputString = "230 400 400";
        CustomerLocation.parseTextWithSpaceDelimitedCoordinates(testInputString);
    }

    @Test(expected=VehicleRoutingRuntimeException.class)
    public void testNullParsing(){
        String testInputString = null;
        CustomerLocation.parseTextWithSpaceDelimitedCoordinates(testInputString);
    }

    @Test(expected=VehicleRoutingRuntimeException.class)
    public void testEmptyStringParsing(){
        String testInputString = "";
        CustomerLocation.parseTextWithSpaceDelimitedCoordinates(testInputString);
    }

    @Test(expected=VehicleRoutingRuntimeException.class)
    public void testParsingNonNumericValues(){
        String testInputString = "230 location";
        CustomerLocation customerLocation = CustomerLocation.parseTextWithSpaceDelimitedCoordinates(testInputString);
        assertEquals(230.0, customerLocation.getXCoordinate(), ERROR_DELTA);
    }

    @Test
    public void testDistanceTwoCustomerLocationsWithPositiveCoordinates (){

        CustomerLocation[] customerLocations = getTwoCustomerLocations("200 200","300 300");
        CustomerLocation firstCustomerLocation = customerLocations[0];
        CustomerLocation secondCustomerLocation = customerLocations[1];
        double distance = firstCustomerLocation.computeDistanceFromAnotherCustomerLocation(secondCustomerLocation);
        assertEquals(141.4, distance,ERROR_DELTA);
    }

    private CustomerLocation[] getTwoCustomerLocations(String firstLocation,  String secondLocation) {
        CustomerLocation[] customerLocations = new CustomerLocation[2];
        customerLocations[0] = CustomerLocation.parseTextWithSpaceDelimitedCoordinates(firstLocation);
        customerLocations[1] = CustomerLocation.parseTextWithSpaceDelimitedCoordinates(secondLocation);
        return customerLocations;
    }

    @Test
    public void testDistanceTwoCustomerLocationsWithNegativeCoordinates (){

        CustomerLocation[] customerLocations = getTwoCustomerLocations("-200 -200","-300 -300");
        CustomerLocation firstCustomerLocation = customerLocations[0];
        CustomerLocation secondCustomerLocation = customerLocations[1];
        double distance = firstCustomerLocation.computeDistanceFromAnotherCustomerLocation(secondCustomerLocation);
        assertEquals(141.4, distance,ERROR_DELTA);
    }

    @Test
    public void testDistanceTwoSimilarCustomerLocations (){

        CustomerLocation[] customerLocations = getTwoCustomerLocations("200 200","200 200");
        CustomerLocation firstCustomerLocation = customerLocations[0];
        CustomerLocation secondCustomerLocation = customerLocations[1];
        double distance = firstCustomerLocation.computeDistanceFromAnotherCustomerLocation(secondCustomerLocation);
        assertEquals(0, distance,ERROR_DELTA);
    }
}
