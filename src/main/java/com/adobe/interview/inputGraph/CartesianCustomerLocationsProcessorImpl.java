package com.adobe.interview.inputGraph;

import com.adobe.interview.exceptions.VehicleRoutingRuntimeException;
import com.adobe.interview.utils.ConfigLoader;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CartesianCustomerLocationsProcessorImpl implements CustomerLocationsProcessor {

    private int customerSize;
    private Map<Integer,CustomerLocation> customerIDToCustomerLocationMap;
    private static final Logger logger = Logger.getLogger(CartesianCustomerLocationsProcessorImpl.class);
    private static final int SOURCE_DEPOT_ID=0;



    public CartesianCustomerLocationsProcessorImpl() {
        this.customerSize = 0;
        this.customerIDToCustomerLocationMap = new HashMap<>();
    }

    @Override
    public void loadLocationsFromFile(String filePath) {

        logger.info("loading customer locations from filepath: " + filePath);
        if(filePath != null && !filePath.isEmpty()){
            loadCustomerLocationsIntoMap(filePath);
        }else{
            logger.error("Cannot load empty file: " + filePath);
            throw new VehicleRoutingRuntimeException("Cannot load empty file: " + filePath);
        }
    }

    private void loadCustomerLocationsIntoMap(String filePath){
        try {
            addSourceDepotLocationToMap();
            tryLoadCustomerLocationsIntoMap(filePath);
        } catch (IOException e) {
            throw new VehicleRoutingRuntimeException("Cannot load file along the path : " + filePath +"\n"+ e.getMessage());
        }
    }

    private void addSourceDepotLocationToMap() {
        CustomerLocation sourceDepotLocation = ConfigLoader.getSourceLocation();
        customerIDToCustomerLocationMap.put(SOURCE_DEPOT_ID,sourceDepotLocation);
    }

    private void tryLoadCustomerLocationsIntoMap(String filePath) throws IOException {
        BufferedReader customerLocationFileReader = new BufferedReader(new FileReader(new File(filePath)));
        customerLocationFileReader.readLine();   //first line is not loaded into Map because it is the file header
        String customerLocationCoordinate = customerLocationFileReader.readLine();
        while(customerLocationCoordinate != null && !customerLocationCoordinate.isEmpty()){
            int generatedCustomerID = generateCustomerID();
            loadCustomerLocationIntoMap(generatedCustomerID, customerLocationCoordinate);
            customerLocationCoordinate = customerLocationFileReader.readLine();
        }
    }

    private int generateCustomerID() {
        return ++customerSize;
    }

    private void loadCustomerLocationIntoMap(int customerID, String customerLocationCoordinate) {

            CustomerLocation customerLocation= CustomerLocation.parseTextWithSpaceDelimitedCoordinates(customerLocationCoordinate);
            logger.debug("Putting customer location details into store Customer: " + customerID);
            customerIDToCustomerLocationMap.put(customerID, customerLocation);
    }


    @Override
    public int getNumOfCustomers() {
        return customerSize;
    }

    @Override
    public double getDistanceBetweenTwoLocations(int firstCustomerID, int secondCustomerID) {

            CustomerLocation firstCustomerLocation = getLocation(firstCustomerID);
            CustomerLocation secondCustomerLocation = getLocation(secondCustomerID);
            return firstCustomerLocation.computeDistanceFromAnotherCustomerLocation(secondCustomerLocation);
    }



    @Override
    public CustomerLocation getLocation(int customerID) {
        logger.debug("Retrieving customer location for ID" + customerID);
        if(customerID < getNumOfCustomers()){
            return customerIDToCustomerLocationMap.get(customerID);
        }else{
            return null;
        }
    }

    @Override
    public String toString(){

        String output = "";
        for (int customerID =1; customerID < customerSize+1; customerID++ ){
            CustomerLocation location = customerIDToCustomerLocationMap.get(customerID);
            output += location.toString() + "\n";
        }
        return output;
    }


}
