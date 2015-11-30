package com.adobe.interview.matrix;


import com.adobe.interview.exceptions.VehicleRoutingException;
import com.adobe.interview.exceptions.VehicleRoutingRuntimeException;

import java.util.ArrayList;
import java.util.List;

public class SymmetricTwoDimenMatrixFunctionFactory {

    public static double getSumRowMinAndComputeRowReductions(SymmetricTwoDimenMatrix matrix ) throws VehicleRoutingException {

        exitIfMatrixIsNull(matrix);
        double sumRowMinimum = 0.0;
        int matrixDimension = matrix.getLength();
        for (int rowIndex = 0; rowIndex < matrixDimension; rowIndex++){
            double rowMin = getRowMinForNonVisitedCells(matrix, rowIndex);
            if(rowMin != Double.MAX_VALUE){
                sumRowMinimum += rowMin;
                reduceNonVisitedRowCellValuesWithMinValue(matrix, rowIndex, rowMin);
            }
        }
        return sumRowMinimum;
    }


    private static void exitIfMatrixIsNull(SymmetricTwoDimenMatrix matrix) {
        if(matrix == null){
            throw new VehicleRoutingRuntimeException("Null matrix argument provided");
        }
    }
    public static double getRowMinForNonVisitedCells(SymmetricTwoDimenMatrix matrix, int rowIndex) throws VehicleRoutingException {   //Customer ID is the same as row index

        exitIfMatrixIsNull(matrix);
        double rowMinimum = Double.MAX_VALUE;
        int colLength = matrix.getLength();
        for(int colIndex = 0; colIndex < colLength; colIndex++){
            SymmetricTwoDimenMatrixCoordinate cellCoordinate = new SymmetricTwoDimenMatrixCoordinate(rowIndex,colIndex);
            if(!matrix.hasCellBeenVisited(cellCoordinate) && matrix.getCellValue(cellCoordinate) < rowMinimum){
                rowMinimum = matrix.getCellValue(cellCoordinate);
            }
        }
        if(rowMinimum == Double.MAX_VALUE){
            return 0;
        }
        return  rowMinimum;
    }

    private static void reduceNonVisitedRowCellValuesWithMinValue(SymmetricTwoDimenMatrix matrix, int rowIndex, double rowMin) throws VehicleRoutingException {

        int matrixDimension = matrix.getLength();
        for (int colIndex = 0; colIndex < matrixDimension; colIndex++){
            SymmetricTwoDimenMatrixCoordinate cellCoordinate = new SymmetricTwoDimenMatrixCoordinate(rowIndex,colIndex);
            if(!matrix.hasCellBeenVisited(cellCoordinate) && !isCellValueInfinity(matrix, cellCoordinate)){
                matrix.setCellValue(cellCoordinate, matrix.getCellValue(cellCoordinate) - rowMin);
            }
        }
    }

    private static boolean isCellValueInfinity(SymmetricTwoDimenMatrix matrix,SymmetricTwoDimenMatrixCoordinate cellCoordinate) throws VehicleRoutingException {

        return matrix.getCellContent(cellCoordinate).getCellValue() == Double.MAX_VALUE;
    }

    public static double getSumColMinAndComputeColReductions(SymmetricTwoDimenMatrix matrix ) throws VehicleRoutingException {

        exitIfMatrixIsNull(matrix);
        double sumColMinimum = 0.0;
        int matrixDimension = matrix.getLength();
        for (int colIndex = 0; colIndex < matrixDimension; colIndex++){
            if(!colHasAZeroValue(matrix,colIndex)){
                double colMinimum = getColMinForNonVisitedCells(matrix, colIndex);
                if(colMinimum != Double.MAX_VALUE){
                    sumColMinimum += colMinimum;
                    reduceNonVisitedColValuesWithMinValue(matrix, colIndex, colMinimum);
                }
            }
        }
        return sumColMinimum;
    }


    private static boolean colHasAZeroValue(SymmetricTwoDimenMatrix matrix,int colIndex) throws VehicleRoutingException {

        boolean cellHasZeroValue = false;
        int matrixDimension = matrix.getLength();
        for (int rowIndex = 0; rowIndex < matrixDimension; rowIndex++){
            SymmetricTwoDimenMatrixCoordinate cellCoordinate = new SymmetricTwoDimenMatrixCoordinate(rowIndex,colIndex);
            if(isCellValueZero(matrix,cellCoordinate)){
                return true;
            }
        }
        return cellHasZeroValue;
    }

    private static boolean isCellValueZero(SymmetricTwoDimenMatrix matrix,SymmetricTwoDimenMatrixCoordinate coordinate) throws VehicleRoutingException {

        if(!matrix.hasCellBeenVisited(coordinate) && matrix.getCellValue(coordinate) == 0.0){
            return true;
        }else{
            return false;
        }

    }


    public static double getColMinForNonVisitedCells(SymmetricTwoDimenMatrix matrix, int colIndex) throws VehicleRoutingException {   //Customer ID is the same as col index

        exitIfMatrixIsNull(matrix);
        double colMinimum = Double.MAX_VALUE;
        int rowLength = matrix.getLength();
        for(int rowIndex = 0; rowIndex < rowLength; rowIndex++){
            SymmetricTwoDimenMatrixCoordinate cellCoordinate = new SymmetricTwoDimenMatrixCoordinate(rowIndex,colIndex);
            if(!matrix.hasCellBeenVisited(cellCoordinate) && matrix.getCellValue(cellCoordinate) < colMinimum){
                colMinimum = matrix.getCellValue(cellCoordinate);
            }
        }
        return  colMinimum;
    }

    private static void reduceNonVisitedColValuesWithMinValue(SymmetricTwoDimenMatrix matrix, int colIndex, double minValue) throws VehicleRoutingException {

        int rowLength = matrix.getLength();
        for(int rowIndex = 0; rowIndex < rowLength; rowIndex++){
            SymmetricTwoDimenMatrixCoordinate cellCoordinate = new SymmetricTwoDimenMatrixCoordinate(rowIndex,colIndex);
            if(!matrix.hasCellBeenVisited(cellCoordinate) && !isCellValueInfinity(matrix, cellCoordinate)){
                matrix.setCellValue(cellCoordinate, matrix.getCellValue(cellCoordinate) - minValue);
            }
        }
    }

    public static void computePenaltyForZeroValuedCoordinates(SymmetricTwoDimenMatrix matrix) throws VehicleRoutingException {

        List<SymmetricTwoDimenMatrixCoordinate> coordinates = matrix.getCoordinatesWithZeroValue();
        for(SymmetricTwoDimenMatrixCoordinate coordinate: coordinates){
            SymmetricTwoDimenMatrix clonedMatrix = matrix.clone();
            clonedMatrix.setHasCellBeenVisited(coordinate,true);
            double rowMin = getRowMinForNonVisitedCells(clonedMatrix, coordinate.getRowId());
            double colMin = getColMinForNonVisitedCells(clonedMatrix, coordinate.getColId());
            double penalty = (rowMin > colMin) ? rowMin: colMin;
            matrix.setCellPenaltyIfZeroValued(coordinate, penalty);
        }
    }

    public static List<SymmetricTwoDimenMatrixCoordinate> getCoordinatesForMatrixOfSizeTwo(SymmetricTwoDimenMatrix matrix) throws VehicleRoutingException {

        exitIfMatrixIsNull(matrix);
        assert (matrix.getSize() == 2);
        SymmetricTwoDimenMatrixCoordinate[][] cells = getNonVisitedCells(matrix);
        List<SymmetricTwoDimenMatrixCoordinate> coordinates = getCoordinatesFromNonVisitedCellsOfSizeTwo(matrix, cells);
        System.out.println(coordinates.get(0).toString());
        System.out.println(coordinates.get(1).toString());

        return coordinates;
    }

    private static SymmetricTwoDimenMatrixCoordinate[][] getNonVisitedCells(SymmetricTwoDimenMatrix matrix) throws VehicleRoutingException {

        Integer rowTracker = 0;
        Integer colTracker = 0;
        int matrixLength = matrix.getLength();
        SymmetricTwoDimenMatrixCoordinate[][] cells = new SymmetricTwoDimenMatrixCoordinate[2][2];

        for(int row = 0; row < matrixLength; row++){
            for(int col = 0; col < matrixLength; col++){
                SymmetricTwoDimenMatrixCoordinate coordinate = new SymmetricTwoDimenMatrixCoordinate(row,col);
                if(rowTracker < 2){
                    if(!matrix.hasCellBeenVisited(coordinate)){
                        cells[rowTracker][colTracker] = coordinate;
                        colTracker++;
                        if(colTracker == 2 ) {
                            rowTracker++;
                            colTracker = 0;
                        }
                    }
                }
            }
        }
        return cells;
    }

    private static List<SymmetricTwoDimenMatrixCoordinate> getCoordinatesFromNonVisitedCellsOfSizeTwo(SymmetricTwoDimenMatrix matrix,SymmetricTwoDimenMatrixCoordinate[][] cells) throws VehicleRoutingException {

        List<SymmetricTwoDimenMatrixCoordinate> coordinates = new ArrayList<>();
        if(matrix.getCellValue(cells[0][0]) == 0.0){
            coordinates.add(cells[0][0]);
            coordinates.add(cells[1][1]);
        }
        else if(matrix.getCellValue(cells[0][1]) == 0.0){
            coordinates.add(cells[0][1]);
            coordinates.add(cells[1][0]);
        }else if(matrix.getCellValue(cells[1][0]) == 0.0){
            coordinates.add(cells[1][0]);
            coordinates.add(cells[0][1]);
        }else if(matrix.getCellValue(cells[1][1]) == 0.0){
            coordinates.add(cells[1][1]);
            coordinates.add(cells[0][0]);
        }
        return coordinates;
    }


}
