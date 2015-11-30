package com.adobe.interview.branchAndBoundGraph;

import com.adobe.interview.exceptions.VehicleRoutingException;
import com.adobe.interview.exceptions.VehicleRoutingRuntimeException;

import java.util.*;

public class PathFromDepot {

    private List<PathBtwTwoCustomerLocations> paths;
    private static final int DEPOT_ID=0;

    private int numPaths;

    public PathFromDepot(int numPaths){

        this. paths = new ArrayList<>();
        this.numPaths = numPaths;
    }


    public void addPath(PathBtwTwoCustomerLocations path){

        exitIfNullPathArgument(path);
         paths.add(path);
    }

    private static void exitIfNullPathArgument(PathBtwTwoCustomerLocations path) {

        if(path == null){
            throw new VehicleRoutingRuntimeException("Null argument provided");
        }
    }


    public Set<Integer> getLocationsVisited(){

        Set<Integer> customersVisited = new LinkedHashSet<>();
        for(PathBtwTwoCustomerLocations pathBtwTwoCustomerLocations :  paths){
            customersVisited.add(pathBtwTwoCustomerLocations.getStartCustomerLocation());
            customersVisited.add(pathBtwTwoCustomerLocations.getEndCustomerLocation());
        }
        return customersVisited;
    }

    public boolean isPathCompleted() {

        int numOfTraversedPaths = getNumOfTraversedPaths();
        return hasSumOfTraversedPathsReachedExpected(numOfTraversedPaths);
    }

    private int getNumOfTraversedPaths() {

        int numOfTraversedPaths = 0;
        for(PathBtwTwoCustomerLocations path :  paths){
            if(path.isTraversed()){
                numOfTraversedPaths++;
            }
        }
        return numOfTraversedPaths;
    }

    private boolean hasSumOfTraversedPathsReachedExpected(int numOfTraversedPaths){

        boolean pathComplete = false;

        if(numOfTraversedPaths == numPaths + 1){
            pathComplete = true;
        }
        return pathComplete;
    }

    public boolean orderAndValidate() throws VehicleRoutingException {

        orderPaths(paths);
        return validatePaths();
    }

    private void orderPaths(List<PathBtwTwoCustomerLocations> paths) throws VehicleRoutingException {

        Map<Integer,PathBtwTwoCustomerLocations> map = putTraversedPathsInMap();
        List<PathBtwTwoCustomerLocations> organisedPathFromDepot = getOrganisedList(map);
        checkIfListIsEmptyOrNull(organisedPathFromDepot);
        this.paths = organisedPathFromDepot;
    }

    private Map<Integer, PathBtwTwoCustomerLocations> putTraversedPathsInMap() {

        Map<Integer,PathBtwTwoCustomerLocations> map = new HashMap<>();
        for(PathBtwTwoCustomerLocations path: paths){
            if(path.isTraversed()){
                map.put(path.getStartCustomerLocation(),path);
            }
        }
        return map;
    }

    private List<PathBtwTwoCustomerLocations> getOrganisedList(Map<Integer, PathBtwTwoCustomerLocations> map) {

        int tracker = 0;
        Integer customerID = DEPOT_ID;
        List<PathBtwTwoCustomerLocations> reOrganisedPathFromDepot = new ArrayList<>();
        while(tracker < numPaths + 1){
            PathBtwTwoCustomerLocations path = map.get(customerID);
            if(path == null){
                return new ArrayList<PathBtwTwoCustomerLocations>();
            }
            reOrganisedPathFromDepot.add(path);
            customerID = path.getEndCustomerLocation();
            tracker++;
        }
        return reOrganisedPathFromDepot;
    }

    private void checkIfListIsEmptyOrNull(List<PathBtwTwoCustomerLocations> organisedPathFromDepot) throws VehicleRoutingException {

        if(organisedPathFromDepot == null || organisedPathFromDepot.isEmpty()){
            throw new VehicleRoutingException("Cannot re-order optimal route. Either input graph is acyclic or ...");
        }
    }

    private boolean validatePaths() {

        if(paths.size() != numPaths +1){
            return false;
        }
        int endCustomerLocationOfPreviousPath = getEndCustomerLocationOfFirstPath();
        for (PathBtwTwoCustomerLocations path : paths){
            if(isPathContiguousWithPreviousPath(path, endCustomerLocationOfPreviousPath)){
                endCustomerLocationOfPreviousPath = path.getEndCustomerLocation();
            }else{
                return false;
            }
        }
        if(endCustomerLocationOfPreviousPath != DEPOT_ID){
            return false;
        }
        return true;
    }


    public int getEndCustomerLocationOfFirstPath() {

        PathBtwTwoCustomerLocations firstPath = paths.get(0);
        return firstPath.getStartCustomerLocation();
    }

    private boolean isPathContiguousWithPreviousPath(PathBtwTwoCustomerLocations path,int endCustomerOfPriorPath) {

        return path.getStartCustomerLocation() == endCustomerOfPriorPath;
    }

    public PathFromDepot clone (){

        PathFromDepot pathFromDepot = new PathFromDepot(numPaths);
        pathFromDepot.setPaths(new ArrayList<PathBtwTwoCustomerLocations>(paths));
        return pathFromDepot;
    }

    private void setPaths(List<PathBtwTwoCustomerLocations> paths) {
        this. paths = paths;
    }

    @Override
    public String toString(){

        String output = "";
        for (PathBtwTwoCustomerLocations path : paths){
            output += path.toString() + "\n";
        }
        return output;
    }
}
