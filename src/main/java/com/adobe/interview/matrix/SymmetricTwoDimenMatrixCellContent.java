package com.adobe.interview.matrix;


public class SymmetricTwoDimenMatrixCellContent {

    private double cellValue;
    private double penaltyForZeroValue;
    private boolean isVisited;

    public SymmetricTwoDimenMatrixCellContent(double cellValue) {

        this.cellValue = cellValue;
        penaltyForZeroValue = 0.0;
        isVisited = false;
    }

    public void setCellValue(double cellValue) {
        this.cellValue = cellValue;
    }

    public void setPenaltyForZeroValue(double penaltyForZeroValue) {

        if(penaltyForZeroValue > 0.0){
            assert (getCellValue() == 0.0);
        }
        this.penaltyForZeroValue = penaltyForZeroValue;
    }

    public void setIsVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }

    public double getCellValue() {
        return cellValue;
    }

    public double getPenaltyForZeroValue() {
        return penaltyForZeroValue;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public SymmetricTwoDimenMatrixCellContent clone(){
        SymmetricTwoDimenMatrixCellContent cellContent = new SymmetricTwoDimenMatrixCellContent(cellValue);
        cellContent.setIsVisited(isVisited);
        if(cellValue == 0 && !isVisited) {
            cellContent.setPenaltyForZeroValue(penaltyForZeroValue);
        }
        return cellContent;

    }


}
