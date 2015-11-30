package com.adobe.interview.branchAndBoundGraph;


import com.adobe.interview.exceptions.VehicleRoutingException;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class PathsFromCompanyLocationTest {

    private static final double ERROR_DELTA = 0.001;
    private static final int CUSTOMER_SIZE = 2;

    @Test
    public void testCountOfGetCustomersVisited(){

        PathFromDepot paths = buildPaths();
        Set<Integer> locations = paths.getLocationsVisited();
        assertEquals(3, locations.size(), ERROR_DELTA);
    }

    @Test
    public void testIsPathNotCompleted(){

        PathFromDepot paths = buildPaths();
        assertEquals(false,paths.isPathCompleted());
    }

    @Test
    public void testIsPathCompleted(){

        PathFromDepot paths = buildTraversedPaths();
        paths.addPath(new PathBtwTwoCustomerLocations(2,0,true));
        assertEquals(true,paths.isPathCompleted());
    }

    @Test
    public void testPathSortingAndValidation() throws VehicleRoutingException {

        PathFromDepot paths = buildTraversedPaths();
        paths.addPath(new PathBtwTwoCustomerLocations(2,0,true));
        boolean result = paths.orderAndValidate();
        assertEquals(true,result);
    }

    @Test
    public void testFalsePathSortingAndValidation() throws VehicleRoutingException {

        PathFromDepot paths = buildTraversedPaths();
        paths.addPath(new PathBtwTwoCustomerLocations(2,5,true));
        boolean result = paths.orderAndValidate();
        assertEquals(false, result);
    }

    @Test
    public void testClone(){

        PathFromDepot paths = buildTraversedPaths();
        paths.addPath(new PathBtwTwoCustomerLocations(2,0,true));
        PathFromDepot clonedPaths = paths.clone();
        clonedPaths.addPath(new PathBtwTwoCustomerLocations(3,4,true));
        assertEquals(false, clonedPaths.isPathCompleted());
    }



    private PathFromDepot buildPaths(){

        PathFromDepot paths = new PathFromDepot(CUSTOMER_SIZE);
        paths.addPath(new PathBtwTwoCustomerLocations(2,0,false));
        paths.addPath(new PathBtwTwoCustomerLocations(1,2,false));
        paths.addPath(new PathBtwTwoCustomerLocations(0,1, false));
        return paths;
    }

    private PathFromDepot buildTraversedPaths(){
        PathFromDepot paths = new PathFromDepot(CUSTOMER_SIZE);
        paths.addPath(new PathBtwTwoCustomerLocations(0,1,true));
        paths.addPath(new PathBtwTwoCustomerLocations(1,2,true));
        return paths;
    }
}
