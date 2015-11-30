package com.adobe.interview.matrix;


import com.adobe.interview.exceptions.VehicleRoutingException;
import com.adobe.interview.exceptions.VehicleRoutingRuntimeException;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class SymmetricTwoDimenMatrix {

    private SymmetricTwoDimenMatrixCellContent[][] cells;
    private static final Logger logger = Logger.getLogger(SymmetricTwoDimenMatrix.class);
    private int length;
    private int size;


    public SymmetricTwoDimenMatrix(SymmetricTwoDimenMatrixCellContent[][] cells) {
        if(cells == null){
            throw new VehicleRoutingRuntimeException("Null argument provided to SymmetricTwoDimenMatrix class");
        }
        this.cells = cells;
        this.length = cells.length;
        this.size = length;  //as the branchTree grows , the dimension of the matrix becomes smaller

    }

    public SymmetricTwoDimenMatrixCellContent getCellContent(SymmetricTwoDimenMatrixCoordinate cellCoordinate) throws VehicleRoutingException {

        exitIfCellCoordinateIsNull(cellCoordinate);
        exitIfCoordinateOutOfBounds(cellCoordinate);
        int rowIndex = cellCoordinate.getRowId();
        int colIndex = cellCoordinate.getColId();
        logger.debug("Retrieving Content for the following coordinate " + rowIndex + " " + colIndex);
        SymmetricTwoDimenMatrixCellContent cellContent = cells[rowIndex][colIndex];
        return cellContent;
    }

    private void exitIfCoordinateOutOfBounds(SymmetricTwoDimenMatrixCoordinate cellCoordinate) throws VehicleRoutingException {
        int rowIndex = cellCoordinate.getRowId();
        int colIndex = cellCoordinate.getColId();
        if(rowIndex >= getLength() || colIndex >= getLength()){
            throw new VehicleRoutingException("Coordinate provided is out of bound. Current size of matrix is " + getLength() + " Coordinate provided: " + rowIndex + " "+ colIndex);
        }
    }

    private static void exitIfCellCoordinateIsNull(SymmetricTwoDimenMatrixCoordinate cellCoordinate) {
        if(cellCoordinate == null){
            throw new VehicleRoutingRuntimeException("Null cellCoordinate argument provided");
        }
    }

    public void setCellValue(SymmetricTwoDimenMatrixCoordinate cellCoordinate, double cellValue) throws VehicleRoutingException {

        exitIfCellCoordinateIsNull(cellCoordinate);
        SymmetricTwoDimenMatrixCellContent cellContent = getCellContent(cellCoordinate);
        cellContent.setCellValue(cellValue);

    }

    public double getCellValue(SymmetricTwoDimenMatrixCoordinate cellCoordinate) throws VehicleRoutingException {

        exitIfCellCoordinateIsNull(cellCoordinate);
        SymmetricTwoDimenMatrixCellContent cellContent = getCellContent(cellCoordinate);
        return cellContent.getCellValue();
    }

    public void setCellPenaltyIfZeroValued(SymmetricTwoDimenMatrixCoordinate cellCoordinate, double penalty) throws VehicleRoutingException {

        exitIfCellCoordinateIsNull(cellCoordinate);
        if(isCellContentValueZero(cellCoordinate)) {
            SymmetricTwoDimenMatrixCellContent cellContent = getCellContent(cellCoordinate);
            cellContent.setPenaltyForZeroValue(penalty);
        }else{
            logger.error("Cannot set penalty for a zero valued cell content");
            throw new VehicleRoutingException("Cannot set penalty for a non-zero valued cell content");
        }
    }

    public double getCellPenalty(SymmetricTwoDimenMatrixCoordinate cellCoordinate) throws VehicleRoutingException {

        exitIfCellCoordinateIsNull(cellCoordinate);
        SymmetricTwoDimenMatrixCellContent cellContent = getCellContent(cellCoordinate);
        return cellContent.getPenaltyForZeroValue();
    }

    private boolean isCellContentValueZero(SymmetricTwoDimenMatrixCoordinate cellCoordinate) throws VehicleRoutingException {
        SymmetricTwoDimenMatrixCellContent cellContent = getCellContent(cellCoordinate);
        return cellContent.getCellValue() == 0.0;
    }

    public void setHasCellBeenVisited(SymmetricTwoDimenMatrixCoordinate cellCoordinate, boolean visited) throws VehicleRoutingException {

        exitIfCellCoordinateIsNull(cellCoordinate);
        SymmetricTwoDimenMatrixCellContent cellContent = getCellContent(cellCoordinate);
        cellContent.setIsVisited(visited);

    }

    public boolean hasCellBeenVisited(SymmetricTwoDimenMatrixCoordinate cellCoordinate) throws VehicleRoutingException {

        exitIfCellCoordinateIsNull(cellCoordinate);
        SymmetricTwoDimenMatrixCellContent cellContent = getCellContent(cellCoordinate);
        return cellContent.isVisited();

    }

    public List<SymmetricTwoDimenMatrixCoordinate> getCoordinatesWithZeroValue(){

        List<SymmetricTwoDimenMatrixCoordinate> coordinatesWithZeroValues = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < getLength();rowIndex++){
            for (int colIndex = 0; colIndex < getLength();colIndex++){
                if(cells[rowIndex][colIndex].getCellValue() == 0.0 && !cells[rowIndex][colIndex].isVisited()){
                    coordinatesWithZeroValues.add(new SymmetricTwoDimenMatrixCoordinate(rowIndex,colIndex));
                }
            }
        }
        return coordinatesWithZeroValues;
    }


    public int getLength() {
        return length;
    }

    public void setRowCellsVisited(int rowIndex) throws VehicleRoutingException {

            for (int colIndex = 0; colIndex < getLength();colIndex++){
                SymmetricTwoDimenMatrixCoordinate cellCoordinate = new SymmetricTwoDimenMatrixCoordinate(rowIndex,colIndex);
                exitIfCoordinateOutOfBounds(cellCoordinate);
                setHasCellBeenVisited(cellCoordinate,true);
            }
    }

    public void setColCellsVisited(int colIndex) throws VehicleRoutingException {

        for (int rowIndex = 0; rowIndex < getLength();rowIndex++){
            SymmetricTwoDimenMatrixCoordinate cellCoordinate = new SymmetricTwoDimenMatrixCoordinate(rowIndex,colIndex);
            exitIfCoordinateOutOfBounds(cellCoordinate);
            setHasCellBeenVisited(cellCoordinate,true);
        }
    }

    public SymmetricTwoDimenMatrix clone(){
        SymmetricTwoDimenMatrixCellContent[][] cellContents = new SymmetricTwoDimenMatrixCellContent[getLength()][getLength()];
        for(int i =0; i < getLength(); i++){
            for(int j =0; j < getLength(); j++){
                SymmetricTwoDimenMatrixCellContent cellContent = cells[i][j].clone();
                cellContents[i][j] = cellContent;
            }
        }
        SymmetricTwoDimenMatrix matrix =  new SymmetricTwoDimenMatrix(cellContents);
        matrix.setSize(size);
        return matrix;
    }

    @Override
    public String toString(){

        String output = "";
        for(int i =0; i < getLength(); i++){
            for(int j =0; j < getLength(); j++){

                String isVisitedPlaceholder = cells[i][j].isVisited() ? "" : "*";
                String cellValue = (cells[i][j].getCellValue() == Double.MAX_VALUE ) ? " -" + isVisitedPlaceholder + "    " : cells[i][j].getCellValue() +  isVisitedPlaceholder + "   ";
                output += cellValue;
            }
            output += "\n";
        }
        return output;
    }

    public void decrementSize(){
        --size;
    }

    public void setSize(int dimension) {
        this.size = dimension;
    }

    public int getSize() {
        return size;
    }
}
