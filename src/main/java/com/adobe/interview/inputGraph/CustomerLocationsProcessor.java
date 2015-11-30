package com.adobe.interview.inputGraph;


public interface CustomerLocationsProcessor {

    public void loadLocationsFromFile(String filePath);

    public int getNumOfCustomers();

    public double getDistanceBetweenTwoLocations(int firstCustomerID, int secondCustomerID);

    public CustomerLocation getLocation(int customerID);

    public String toString();
}
