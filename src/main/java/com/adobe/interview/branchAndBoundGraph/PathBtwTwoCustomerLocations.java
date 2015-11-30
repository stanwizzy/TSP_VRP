package com.adobe.interview.branchAndBoundGraph;


public class PathBtwTwoCustomerLocations{

    private Integer startCustomerLocation;
    private Integer endCustomerLocation;
    private boolean isTraversed;


    public PathBtwTwoCustomerLocations(int startCustomerID, int endCustomerID, boolean isTraversed){
        this.startCustomerLocation = startCustomerID;
        this. endCustomerLocation = endCustomerID;
        this.isTraversed = isTraversed;

    }

    public Integer getStartCustomerLocation(){
        return startCustomerLocation;
    }

    public Integer getEndCustomerLocation(){
        return  endCustomerLocation;
    }

    public boolean isTraversed() {
        return isTraversed;
    }

    @Override
    public String toString(){

        String wasPathTaken = (isTraversed) ? " Traversed" : " Non-traversed";
        return "Path from: " + startCustomerLocation + " --> " +  endCustomerLocation + " " + wasPathTaken;
    }
}
