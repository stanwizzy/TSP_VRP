package com.adobe.interview.matrix;


import com.adobe.interview.exceptions.VehicleRoutingException;
import com.adobe.interview.exceptions.VehicleRoutingRuntimeException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SymmetricTwoDimenMatrixFunctionFactoryTest {

    private static final double ERROR_DELTA = 0.001;

    @Test
    public void testGetSumRowMin() throws VehicleRoutingException {
        SymmetricTwoDimenMatrix matrix = buildMatrix();
        double sumRowMin = SymmetricTwoDimenMatrixFunctionFactory.getSumRowMinAndComputeRowReductions(matrix);
        assertEquals(10, sumRowMin,ERROR_DELTA);
    }

    @Test
    public void testRowMinReductions() throws VehicleRoutingException {
        SymmetricTwoDimenMatrix matrix = buildMatrix();
        SymmetricTwoDimenMatrixFunctionFactory.getSumRowMinAndComputeRowReductions(matrix);
        SymmetricTwoDimenMatrixCoordinate cellCoordinate = new SymmetricTwoDimenMatrixCoordinate(3,3);
        assertEquals(0, matrix.getCellValue(cellCoordinate), ERROR_DELTA);
    }

    @Test(expected=VehicleRoutingRuntimeException.class)
    public void testRowMinReductionsWithNullArgument() throws VehicleRoutingException {
        SymmetricTwoDimenMatrix matrix = buildMatrix();
        SymmetricTwoDimenMatrixFunctionFactory.getSumRowMinAndComputeRowReductions(null);
    }

    @Test
    public void testGetSumColMin() throws VehicleRoutingException {
        SymmetricTwoDimenMatrix matrix = buildMatrix();
        double sumRowMin = SymmetricTwoDimenMatrixFunctionFactory.getSumColMinAndComputeColReductions(matrix);
        assertEquals(4, sumRowMin, ERROR_DELTA);
    }

    @Test
    public void testColMinReductions() throws VehicleRoutingException {
        SymmetricTwoDimenMatrix matrix = buildMatrix();
        SymmetricTwoDimenMatrixFunctionFactory.getSumColMinAndComputeColReductions(matrix);
        SymmetricTwoDimenMatrixCoordinate cellCoordinate = new SymmetricTwoDimenMatrixCoordinate(3,3);
        assertEquals(3, matrix.getCellValue(cellCoordinate), ERROR_DELTA);
    }

    @Test
    public void testComputePenaltyForZeroValuedCoordinates() throws VehicleRoutingException {

        SymmetricTwoDimenMatrix matrix = buildMatrix();
        System.out.println(matrix);
        SymmetricTwoDimenMatrixFunctionFactory.getSumColMinAndComputeColReductions(matrix);
        System.out.println(matrix);
        SymmetricTwoDimenMatrixFunctionFactory.computePenaltyForZeroValuedCoordinates(matrix);

        String output = "";
        for(int i =0; i < 4; i++){
            for(int j =0; j < 4; j++){
                output += matrix.getCellPenalty(new SymmetricTwoDimenMatrixCoordinate(i,j)) + " ";
            }
            output += "\n";
        }
        System.out.println(output);
    }

    private SymmetricTwoDimenMatrix buildMatrix(){
        SymmetricTwoDimenMatrixCellContent[][] cells = new SymmetricTwoDimenMatrixCellContent[4][4];
        for(int row = 0;row < 4;row++){
            for(int col = 0;col < 4;col++){
                cells[row][col] = new SymmetricTwoDimenMatrixCellContent(row +1);//cells have values of their row index + 1
            }
        }
        return new SymmetricTwoDimenMatrix(cells);
    }
}
