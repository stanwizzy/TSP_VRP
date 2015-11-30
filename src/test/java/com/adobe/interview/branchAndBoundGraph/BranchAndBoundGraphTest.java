package com.adobe.interview.branchAndBoundGraph;


import com.adobe.interview.exceptions.VehicleRoutingException;
import com.adobe.interview.matrix.SymmetricTwoDimenMatrix;
import com.adobe.interview.matrix.SymmetricTwoDimenMatrixCellContent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BranchAndBoundGraphTest {

    private static final double ERROR_DELTA = 0.001;
    private static final double LOWER_BOUND_BRANCH_SCORE = 20;
    private static final int CUSTOMER_SIZE = 3;

    @Test
    public void testAddTraversedNode(){

        BranchAndBoundGraph graph = buildBaseGraph();

        boolean isTraversed = true;
        BranchAndBoundGraphNode rootNode = graph.getRootNode();
        double branchScore = 20.0;
        PathBtwTwoCustomerLocations newPath = buildPath(0,1,isTraversed);
        BranchAndBoundGraphNode childNode = graph.addTraversedChildNode(rootNode,branchScore, newPath, buildMatrix());

        assertEquals(branchScore, childNode.getBranchScore(), ERROR_DELTA);
    }

    @Test
    public void testRootNodeContentAfterAddingChildNode(){

        BranchAndBoundGraph graph = buildBaseGraph();

        boolean isTraversed = true;
        BranchAndBoundGraphNode rootNode = graph.getRootNode();
        double branchScore = 30.0;
        PathBtwTwoCustomerLocations newPath = buildPath(0,1,isTraversed);
        BranchAndBoundGraphNode childNode = graph.addTraversedChildNode(rootNode,branchScore, newPath, buildMatrix());

        assertEquals(LOWER_BOUND_BRANCH_SCORE, rootNode.getBranchScore(), ERROR_DELTA);
    }

    @Test
    public void testTraversedChildNodeContentAfterAddingOtherChildNodes(){

        BranchAndBoundGraph graph = buildBaseGraph();

        boolean isTraversed = true;
        BranchAndBoundGraphNode rootNode = graph.getRootNode();
        double branchScore = 30.0;
        PathBtwTwoCustomerLocations newPath1 = buildPath(0, 1, isTraversed);
        BranchAndBoundGraphNode traversedChildNode = graph.addTraversedChildNode(rootNode, branchScore, newPath1, buildMatrix());
        PathBtwTwoCustomerLocations newPath2 = buildPath(1, 2, !isTraversed);
        graph.addTraversedChildNode(traversedChildNode,branchScore+1, newPath2, buildMatrix());

        assertEquals(branchScore, traversedChildNode.getBranchScore(), ERROR_DELTA);
    }

    @Test
    public void testNonTraversedChildNodeContentAfterToTraversedParent(){

        BranchAndBoundGraph graph = buildBaseGraph();

        boolean isTraversed = true;
        BranchAndBoundGraphNode rootNode = graph.getRootNode();
        double branchScore = 30.0;
        PathBtwTwoCustomerLocations newPath1 = buildPath(0, 1, isTraversed);
        BranchAndBoundGraphNode traversedChildNode = graph.addTraversedChildNode(rootNode, branchScore, newPath1, buildMatrix());
        PathBtwTwoCustomerLocations newPath2 = buildPath(1, 2, !isTraversed);
        BranchAndBoundGraphNode nonTraversedChildNode = graph.addTraversedChildNode(traversedChildNode, branchScore + 1, newPath2, buildMatrix());

        assertEquals(branchScore + 1, nonTraversedChildNode.getBranchScore(), ERROR_DELTA);
    }

    @Test
    public void testAddNonTraversedNode(){

        BranchAndBoundGraph graph = buildBaseGraph();

        boolean isTraversed = true;
        BranchAndBoundGraphNode rootNode = graph.getRootNode();
        double branchScore = 21.0;
        PathBtwTwoCustomerLocations newPath = buildPath(0,1,isTraversed);
        BranchAndBoundGraphNode childNode = graph.addNonTraversedChildNode(rootNode, branchScore, newPath, buildMatrix());

        assertEquals(branchScore, childNode.getBranchScore(), ERROR_DELTA);
    }

    @Test
    public void testMinScoreOfNonTraversedNodes() throws VehicleRoutingException {

        BranchAndBoundGraph graph = buildBaseGraph();

        boolean isTraversed = true;
        BranchAndBoundGraphNode rootNode = graph.getRootNode();
        double branchScore = 21.0;
        PathBtwTwoCustomerLocations newPath = buildPath(0,1,isTraversed);
        BranchAndBoundGraphNode childNode1 = graph.addNonTraversedChildNode(rootNode, branchScore, newPath, buildMatrix());
        BranchAndBoundGraphNode childNode2 = graph.addNonTraversedChildNode(childNode1, --branchScore, newPath, buildMatrix());
        BranchAndBoundGraphNode childNode3 = graph.addNonTraversedChildNode(childNode2, --branchScore, newPath, buildMatrix());
        BranchAndBoundGraphNode childNode4 = graph.addNonTraversedChildNode(childNode3, --branchScore, newPath, buildMatrix());

        assertEquals(18, graph.getMinScoreOfNonTraversedNodes(), ERROR_DELTA);
    }

    @Test
    public void testNonTraversedNodeWithMinScore() throws VehicleRoutingException {

        BranchAndBoundGraph graph = buildBaseGraph();

        boolean isTraversed = true;
        BranchAndBoundGraphNode rootNode = graph.getRootNode();
        double branchScore = 21.0;
        PathBtwTwoCustomerLocations newPath = buildPath(0,1,isTraversed);
        BranchAndBoundGraphNode childNode1 = graph.addNonTraversedChildNode(rootNode, branchScore, newPath, buildMatrix());
        BranchAndBoundGraphNode childNode2 = graph.addNonTraversedChildNode(childNode1, --branchScore, newPath, buildMatrix());
        BranchAndBoundGraphNode childNode3 = graph.addNonTraversedChildNode(childNode2, --branchScore, newPath, buildMatrix());
        BranchAndBoundGraphNode childNode4 = graph.addNonTraversedChildNode(childNode3, ++branchScore, newPath, buildMatrix());

        assertEquals(childNode3, graph.popNonTraversedNodeWithMinScore());
    }


    private BranchAndBoundGraph buildBaseGraph() {

        SymmetricTwoDimenMatrix matrix = buildMatrix();
        BranchAndBoundGraph graph = new BranchAndBoundGraph(LOWER_BOUND_BRANCH_SCORE,CUSTOMER_SIZE,matrix);
        return graph;
    }

    private SymmetricTwoDimenMatrix buildMatrix(){

        SymmetricTwoDimenMatrixCellContent[][] cells = new SymmetricTwoDimenMatrixCellContent[CUSTOMER_SIZE][CUSTOMER_SIZE];
        for(int row = 0;row < CUSTOMER_SIZE ;row++){
            for(int col = 0;col < CUSTOMER_SIZE ;col++){
                cells[row][col] = new SymmetricTwoDimenMatrixCellContent(row +1);//cells have values of their row index + 1
            }
        }
        return new SymmetricTwoDimenMatrix(cells);
    }

    private PathBtwTwoCustomerLocations buildPath(int startLocation,int endLocation,boolean isTraversed){
        return new PathBtwTwoCustomerLocations(startLocation,endLocation,isTraversed);
    }


}
