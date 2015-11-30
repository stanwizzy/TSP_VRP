package com.adobe.interview.utils;

import com.adobe.interview.inputGraph.CustomerLocationsProcessor;
import com.adobe.interview.matrix.SymmetricTwoDimenMatrix;
import com.adobe.interview.matrix.SymmetricTwoDimenMatrixCellContent;

/**
 * Class is an adapter for the class - SymmetricTwoDimenMatrixFunctionFactory
 * It transforms implementations of CustomerLocationsProcessor To SymmetricTwoDimenMatrix
 */
public class CustomerSeparationMatrixAdapter {

    private static int numCustomers;

    public static SymmetricTwoDimenMatrix getCustomerSeparationMatrixFromInputGraph(CustomerLocationsProcessor locationGraph){

         numCustomers = locationGraph.getNumOfCustomers();
        SymmetricTwoDimenMatrixCellContent[][] customerSeparations = new SymmetricTwoDimenMatrixCellContent[numCustomers][numCustomers];
        loadCustomerSeparationArray(customerSeparations, locationGraph);
        return new SymmetricTwoDimenMatrix(customerSeparations);
    }

    private static void loadCustomerSeparationArray(SymmetricTwoDimenMatrixCellContent[][] customerSeperations, CustomerLocationsProcessor locationGraph) {
        for(int customerID =0; customerID < numCustomers; customerID++){
            loadCustomerSeparationArrayForOneCustomer(customerID, customerSeperations, locationGraph);
        }
    }

    private static void loadCustomerSeparationArrayForOneCustomer(int customerID, SymmetricTwoDimenMatrixCellContent[][] customerSeperations, CustomerLocationsProcessor locationGraph) {
        for(int otherCustomerID =0; otherCustomerID < numCustomers; otherCustomerID++){
            if(customerID != otherCustomerID){
                customerSeperations[customerID][otherCustomerID] = new SymmetricTwoDimenMatrixCellContent(locationGraph.getDistanceBetweenTwoLocations(customerID, otherCustomerID));
            }else{
                customerSeperations[customerID][otherCustomerID] = new SymmetricTwoDimenMatrixCellContent(Double.MAX_VALUE);

            }
        }
    }
}
