package com.adobe.interview.branchAndBoundGraph;


import com.adobe.interview.exceptions.VehicleRoutingException;
import com.adobe.interview.matrix.SymmetricTwoDimenMatrix;
import com.adobe.interview.matrix.SymmetricTwoDimenMatrixCellContent;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BranchAndBoundGraphBuilderTest {

    private static final int CUSTOMER_SIZE = 4;

    @Test
    public void testAddTraversedNode() throws VehicleRoutingException {

        SymmetricTwoDimenMatrix matrix = new SymmetricTwoDimenMatrix(getCells());
        BranchAndBoundGraphNode node = BranchAndBoundGraphBuilder.newInstance(matrix).buildGraph();
        PathFromDepot expectedPath = getExpectedOptimalPath();
        assertEquals(expectedPath.toString(),node.getPathFromDepot().toString());
    }

    private PathFromDepot getExpectedOptimalPath() {

        PathFromDepot paths = new PathFromDepot(4);
        paths.addPath(new PathBtwTwoCustomerLocations(0,2,true));
        paths.addPath(new PathBtwTwoCustomerLocations(4,0,true));
        paths.addPath(new PathBtwTwoCustomerLocations(3,1,true));
        paths.addPath(new PathBtwTwoCustomerLocations(2,3,true));
        paths.addPath(new PathBtwTwoCustomerLocations(1,4,true));
        return paths;
    }

    private SymmetricTwoDimenMatrixCellContent[][] getCells() {

        List<double[]> rows = new ArrayList<>();
        double[] firstRow = {Double.MAX_VALUE,10,8,9,7};
        rows.add(firstRow);
        double[] secondRow = {10,Double.MAX_VALUE,10,5,6};
        rows.add(secondRow);
        double[] thirdRow = {8,10,Double.MAX_VALUE,8,9};
        rows.add(thirdRow);
        double[] fourthRow = {9,5,8,Double.MAX_VALUE,6};
        rows.add(fourthRow);
        double[] fifthRow = {7,6,9,6,Double.MAX_VALUE};
        rows.add(fifthRow);

        SymmetricTwoDimenMatrixCellContent[][] cells = new SymmetricTwoDimenMatrixCellContent[CUSTOMER_SIZE + 1 ][CUSTOMER_SIZE + 1];
        int i = 0;
        for (double[] row : rows){
            for(int j =0; j < CUSTOMER_SIZE + 1; j++){
                cells[i][j] = new SymmetricTwoDimenMatrixCellContent(row[j]);
            }
            i++;
        }
        return cells;
    }
}
