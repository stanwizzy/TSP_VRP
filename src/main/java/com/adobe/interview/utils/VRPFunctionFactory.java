package com.adobe.interview.utils;


import com.adobe.interview.exceptions.VehicleRoutingException;
import com.adobe.interview.exceptions.VehicleRoutingRuntimeException;
import com.adobe.interview.matrix.SymmetricTwoDimenMatrix;
import com.adobe.interview.matrix.SymmetricTwoDimenMatrixCellContent;
import com.adobe.interview.matrix.SymmetricTwoDimenMatrixCoordinate;

public class VRPFunctionFactory {

    public static SymmetricTwoDimenMatrix getAppendedMatrix(SymmetricTwoDimenMatrix matrix, int numServicePersonnel) throws VehicleRoutingException {

        assert (numServicePersonnel > 1);
        exitIfObjectIsNull(matrix);
        SymmetricTwoDimenMatrixCellContent[][] newCells = getAppendedCells(matrix,numServicePersonnel);
        appendEmptyCellsWithContent(matrix,numServicePersonnel,matrix.getLength());
        exitIfObjectIsNull(newCells);
        return new SymmetricTwoDimenMatrix(newCells);
    }

    private static void exitIfObjectIsNull(Object o) {

        if(o == null){
            throw new VehicleRoutingRuntimeException("Null argument provided/returned from method");
        }
    }

    private static SymmetricTwoDimenMatrixCellContent[][] getAppendedCells(SymmetricTwoDimenMatrix matrix, int numServicePersonnel) throws VehicleRoutingException {

        int matrixLength = matrix.getLength();
        int newMatrixLength = matrixLength + numServicePersonnel;
        SymmetricTwoDimenMatrixCellContent[][] newMatrix = new SymmetricTwoDimenMatrixCellContent[newMatrixLength][newMatrixLength];
        copyContentsToNewMatrix(matrix,newMatrix);
        return newMatrix;
    }

    private static void copyContentsToNewMatrix(SymmetricTwoDimenMatrix oldMatrix, SymmetricTwoDimenMatrixCellContent[][] newMatrix) throws VehicleRoutingException {

        int oldMatrixLength = oldMatrix.getLength();
        for(int i = 0; i < oldMatrixLength; i++){
            for(int j = 0; j < oldMatrixLength; j++){
                SymmetricTwoDimenMatrixCoordinate coordinate = new SymmetricTwoDimenMatrixCoordinate(i,j);
                newMatrix[i][j] = oldMatrix.getCellContent(coordinate);
            }
        }
    }

    private static void appendEmptyCellsWithContent(SymmetricTwoDimenMatrix newMatrix, int numServicePersonnel,int oldMatrixlength) {

    }
}
