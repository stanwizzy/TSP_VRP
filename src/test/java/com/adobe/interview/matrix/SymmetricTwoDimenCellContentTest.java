package com.adobe.interview.matrix;

import static org.junit.Assert.*;
import org.junit.Test;

public class SymmetricTwoDimenCellContentTest {

    private static final double ERROR_DELTA = 0.001;

    @Test
    public void testCellContentClone(){
        SymmetricTwoDimenMatrixCellContent cellContent = new SymmetricTwoDimenMatrixCellContent(10.0);
        SymmetricTwoDimenMatrixCellContent cellContentClone = cellContent.clone();
        assertEquals(10.0,cellContentClone.getCellValue(),ERROR_DELTA);
    }

    @Test
    public void testCellContentCloneAfterUpdatingOriginal(){
        SymmetricTwoDimenMatrixCellContent cellContent = new SymmetricTwoDimenMatrixCellContent(10.0);
        SymmetricTwoDimenMatrixCellContent cellContentClone = cellContent.clone();
        cellContent.setCellValue(15.0);
        assertEquals(10.0,cellContentClone.getCellValue(),ERROR_DELTA);
    }

    @Test
    public void testCellContentClonePenaltyValue(){
        SymmetricTwoDimenMatrixCellContent cellContent = new SymmetricTwoDimenMatrixCellContent(0.0);
        cellContent.setPenaltyForZeroValue(15.0);
        SymmetricTwoDimenMatrixCellContent cellContentClone = cellContent.clone();
        assertEquals(15.0, cellContentClone.getPenaltyForZeroValue(), ERROR_DELTA);
    }
}
