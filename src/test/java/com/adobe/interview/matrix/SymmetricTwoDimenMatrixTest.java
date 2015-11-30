package com.adobe.interview.matrix;


import com.adobe.interview.exceptions.VehicleRoutingException;
import com.adobe.interview.exceptions.VehicleRoutingRuntimeException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class SymmetricTwoDimenMatrixTest {

    private static final double ERROR_DELTA = 0.001;

    @Test
    public void testGetCellContent() throws VehicleRoutingException {
        SymmetricTwoDimenMatrix matrix = buildMatrix();
        SymmetricTwoDimenMatrixCoordinate cellCoordinate = new SymmetricTwoDimenMatrixCoordinate(0,0);
        SymmetricTwoDimenMatrixCellContent cellContent = matrix.getCellContent(cellCoordinate);
        assertEquals(10.0, cellContent.getCellValue(),ERROR_DELTA);
    }

    @Test(expected=VehicleRoutingRuntimeException.class)
    public void testGetCellContentWithNullCellCoordinate() throws VehicleRoutingException {
        SymmetricTwoDimenMatrix matrix = buildMatrix();
        SymmetricTwoDimenMatrixCellContent cellContent = matrix.getCellContent(null);
    }

    @Test(expected=VehicleRoutingException.class)
    public void testGetCellContentForOutOfBoundCoordinate() throws VehicleRoutingException {
        SymmetricTwoDimenMatrix matrix = buildMatrix();
        SymmetricTwoDimenMatrixCoordinate cellCoordinate = new SymmetricTwoDimenMatrixCoordinate(2,2);
        matrix.getCellContent(cellCoordinate);
    }

    @Test
    public void testGetSizeOfZeroValuedCoordinates() throws VehicleRoutingException {
        SymmetricTwoDimenMatrix matrix = buildMatrix();
        List<SymmetricTwoDimenMatrixCoordinate> cellCoordinates = matrix.getCoordinatesWithZeroValue();
        assertEquals(2, cellCoordinates.size(),ERROR_DELTA);
    }

    @Test
    public void testGetSetPenaltyOfZeroValuedCoordinates() throws VehicleRoutingException {
        SymmetricTwoDimenMatrix matrix = buildMatrix();
        SymmetricTwoDimenMatrixCoordinate cellCoordinate = new SymmetricTwoDimenMatrixCoordinate(1,0);
        matrix.setCellPenaltyIfZeroValued(cellCoordinate,13);
        assertEquals(13, matrix.getCellPenalty(cellCoordinate),ERROR_DELTA);
    }

    @Test(expected=VehicleRoutingException.class)
    public void testGetSetPenaltyForNonZeroValuedCoordinates() throws VehicleRoutingException {
        SymmetricTwoDimenMatrix matrix = buildMatrix();
        SymmetricTwoDimenMatrixCoordinate cellCoordinate = new SymmetricTwoDimenMatrixCoordinate(0,0);
        matrix.setCellPenaltyIfZeroValued(cellCoordinate, 13);
    }

    @Test
    public void testSetAllCellsInARowVisited() throws VehicleRoutingException {
        SymmetricTwoDimenMatrix matrix = buildMatrix();
        SymmetricTwoDimenMatrixCoordinate cellCoordinate = new SymmetricTwoDimenMatrixCoordinate(1,1);
        matrix.setRowCellsVisited(1);
        assertEquals(true, matrix.hasCellBeenVisited(cellCoordinate));
    }

    @Test
    public void testContentOfMatrixCloneAfterUpdate() throws VehicleRoutingException {
        SymmetricTwoDimenMatrix matrix = buildMatrix();
        SymmetricTwoDimenMatrixCoordinate cellCoordinate = new SymmetricTwoDimenMatrixCoordinate(1,0);
        SymmetricTwoDimenMatrix clonedMatrix = matrix.clone();
        matrix.setCellValue(cellCoordinate,10);
        assertEquals(0, clonedMatrix.getCellValue(cellCoordinate), ERROR_DELTA);
    }

    private SymmetricTwoDimenMatrix buildMatrix(){
        SymmetricTwoDimenMatrixCellContent[][] cells = new SymmetricTwoDimenMatrixCellContent[2][2];
        cells[0][0] = new SymmetricTwoDimenMatrixCellContent(10.0);
        cells[0][1] = new SymmetricTwoDimenMatrixCellContent(11.0);
        cells[1][0] = new SymmetricTwoDimenMatrixCellContent(0.0);
        cells[1][1] = new SymmetricTwoDimenMatrixCellContent(0.0);
        return new SymmetricTwoDimenMatrix(cells);
    }
}
