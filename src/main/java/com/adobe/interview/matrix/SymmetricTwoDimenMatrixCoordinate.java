package com.adobe.interview.matrix;


import com.adobe.interview.branchAndBoundGraph.PathBtwTwoCustomerLocations;

public class SymmetricTwoDimenMatrixCoordinate {

    private int rowId;
    private int colId;

    public SymmetricTwoDimenMatrixCoordinate(int rowId, int colId) {

        this.rowId = rowId;
        this.colId = colId;
    }

    public int getRowId() {
        return rowId;
    }

    public int getColId() {
        return colId;
    }

    public PathBtwTwoCustomerLocations mapToPath(boolean isTraversed){

        PathBtwTwoCustomerLocations path =new PathBtwTwoCustomerLocations(getRowId(), getColId(), isTraversed);
        return path;

    }

    @Override
    public String toString(){
        return "Coordinate: " + rowId + " " + colId;
    }


}
