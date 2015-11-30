package com.adobe.interview.inputGraph;


import com.adobe.interview.exceptions.VehicleRoutingRuntimeException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CartesianCustomerLocationGraphImplTest {

    private static final double ERROR_DELTA = 0.001;

 /*   @Test
    public void testLocationCountAfterParsingFile(){
        CartesianCustomerLocationsProcessorImpl cartesianGraph = new CartesianCustomerLocationsProcessorImpl();
        cartesianGraph.loadCustomerLocationsFromFile("src/test/resources/CustomerLocationTest.txt");
        assertEquals(13, cartesianGraph.getNumOfCustomers(),ERROR_DELTA);
    }

    @Test
    public void testValidContentAfterParsingFile(){
        CartesianCustomerLocationsProcessorImpl cartesianGraph = new CartesianCustomerLocationsProcessorImpl();
        cartesianGraph.loadCustomerLocationsFromFile("src/test/resources/CustomerLocationTest.txt");
        int testCustomerId = 2;
        assertEquals(421, cartesianGraph.getCustomerLocation(testCustomerId).getXCoordinate(),ERROR_DELTA);
    }*/

    @Test(expected=VehicleRoutingRuntimeException.class)
    public void testParsingEmptyFile(){
        CartesianCustomerLocationsProcessorImpl cartesianGraph = new CartesianCustomerLocationsProcessorImpl();
        cartesianGraph.loadLocationsFromFile("");

    }

    @Test(expected=VehicleRoutingRuntimeException.class)
    public void testParsingNullFile(){
        CartesianCustomerLocationsProcessorImpl cartesianGraph = new CartesianCustomerLocationsProcessorImpl();
        cartesianGraph.loadLocationsFromFile(null);
    }
}
