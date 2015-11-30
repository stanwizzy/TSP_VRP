package com.adobe.interview.branchAndBoundGraph;


import com.adobe.interview.exceptions.VehicleRoutingException;
import com.adobe.interview.exceptions.VehicleRoutingRuntimeException;
import com.adobe.interview.matrix.SymmetricTwoDimenMatrix;
import com.adobe.interview.matrix.SymmetricTwoDimenMatrixCoordinate;
import com.adobe.interview.matrix.SymmetricTwoDimenMatrixFunctionFactory;
import com.adobe.interview.utils.VRPFunctionFactory;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Set;

public class BranchAndBoundGraphBuilder {

    private BranchAndBoundGraph branchAndBoundGraph;
    private int customerSize;
    private int employeeCount;
    private double lowerBoundScore;
    private static final int DEFAULT_EMPLOYEE_SIZE = 1;
    private static final Logger logger = Logger.getLogger(BranchAndBoundGraphBuilder.class);

    private BranchAndBoundGraphBuilder(double lowerBoundScore, SymmetricTwoDimenMatrix customerSeparationMatrix) {

        this.customerSize = customerSeparationMatrix.getLength() - 1;
        this.branchAndBoundGraph = new BranchAndBoundGraph(lowerBoundScore, customerSize, customerSeparationMatrix);
        this.lowerBoundScore = lowerBoundScore;
        this.employeeCount = DEFAULT_EMPLOYEE_SIZE ;
    }


    public static BranchAndBoundGraphBuilder newInstance(SymmetricTwoDimenMatrix customerSeparationMatrix) throws VehicleRoutingException {

        exitIfObjectIsNull(customerSeparationMatrix);
        double sumRowMin = SymmetricTwoDimenMatrixFunctionFactory.getSumRowMinAndComputeRowReductions(customerSeparationMatrix);
        double lowerBoundScore = sumRowMin + SymmetricTwoDimenMatrixFunctionFactory.getSumColMinAndComputeColReductions(customerSeparationMatrix);
        logger.info("Lower bound score: " + lowerBoundScore + " computed");
        BranchAndBoundGraphBuilder graphBuilder = new BranchAndBoundGraphBuilder(lowerBoundScore, customerSeparationMatrix);
        return graphBuilder;
    }

    public BranchAndBoundGraphBuilder setEmployeeCount(int employeeSize) throws VehicleRoutingException {

        this.employeeCount = employeeSize;
        if(employeeCount > 1){
            addServiceStaffNodesToGraph();
        }
        return this;
    }

    private void addServiceStaffNodesToGraph() throws VehicleRoutingException {

        SymmetricTwoDimenMatrix customerSeparationMatrix = branchAndBoundGraph.getRootNode().getMatrix();
        SymmetricTwoDimenMatrix appendedMatrix = VRPFunctionFactory.getAppendedMatrix(customerSeparationMatrix, employeeCount);
        this.branchAndBoundGraph = new BranchAndBoundGraph(lowerBoundScore, customerSize + employeeCount, customerSeparationMatrix);
    }


    private static void exitIfObjectIsNull(Object o) {

        if(o == null){
            throw new VehicleRoutingRuntimeException("Cannot provide null argument to method");
        }
    }

    public BranchAndBoundGraphNode buildGraph() throws VehicleRoutingException {

        if (hasOneOrMoreEmployees()) {
            return recursivelyFindOptimalPath(branchAndBoundGraph.getRootNode());
        } else {
            logger.error("Cannot proceed further, employee count must be greater than one");
            throw new VehicleRoutingRuntimeException("Cannot proceed further, employee count must be greater than one");
        }
    }


    private boolean hasOneOrMoreEmployees() {
        return employeeCount > 0;
    }


    private BranchAndBoundGraphNode recursivelyFindOptimalPath(BranchAndBoundGraphNode currentNode) throws VehicleRoutingException {

        logger.info("Finding optimal path for node with branchScore of " + currentNode.getBranchScore() + "and path: " + currentNode.getPathFromDepot().toString());
        logger.debug("Current Matrix: \n" + currentNode.getMatrix().toString());
        if (hasRouteToAllCustomersCompleted(currentNode)) {  //first base condition
            if (isRouteOptimal(currentNode)) {
                return currentNode;
            } else {
                return recursivelyFindOptimalPath(branchAndBoundGraph.popNonTraversedNodeWithMinScore());
            }
        }

        if(isMatrixSizeTwo(currentNode.getMatrix())){  //base condition that checks if the matrix is a 2 * 2 matrix
            return processNodeWithMatrixSizeOfTwo(currentNode);
        }

        computePenaltyForZeroValuedCoordinates(currentNode.getMatrix());
        SymmetricTwoDimenMatrixCoordinate zeroValuedCoordinateWithMaxPenalty = getZeroValuedCoordinateWithMaxPenalty(currentNode);
        BranchAndBoundGraphNode nextTraversedNode = addChildNodesAndGetTraversedChildNode(currentNode, zeroValuedCoordinateWithMaxPenalty);
        return recursivelyFindOptimalPath(nextTraversedNode);
    }


    private boolean hasRouteToAllCustomersCompleted(BranchAndBoundGraphNode currentNode){

        PathFromDepot sourcePath = currentNode.getPathFromDepot();
        return sourcePath.isPathCompleted();
    }

    private boolean isRouteOptimal(BranchAndBoundGraphNode currentNode) {

        try {
            return tryIsRouteOptimal(currentNode);
        }catch(VehicleRoutingException e){
            logger.info("Received empty priority queue exception which implies current path is optimal ");
            return true;
        }
    }

    private boolean tryIsRouteOptimal(BranchAndBoundGraphNode currentNode) throws VehicleRoutingException{

        double nodeScore = currentNode.getBranchScore();
        double minScoreOfNonTraversedNodes = branchAndBoundGraph.getMinScoreOfNonTraversedNodes();
        return nodeScore <= minScoreOfNonTraversedNodes;
    }

    private boolean isMatrixSizeTwo(SymmetricTwoDimenMatrix matrix) {

        return (matrix.getSize() == 2);
    }

    private BranchAndBoundGraphNode processNodeWithMatrixSizeOfTwo(BranchAndBoundGraphNode currentNode) throws VehicleRoutingException {

        SymmetricTwoDimenMatrix matrix = currentNode.getMatrix();
        List<SymmetricTwoDimenMatrixCoordinate> coordinates = SymmetricTwoDimenMatrixFunctionFactory.getCoordinatesForMatrixOfSizeTwo(matrix);
        for(SymmetricTwoDimenMatrixCoordinate coordinate: coordinates){
            BranchAndBoundGraphNode traversedChildNode = addChildNodesAndGetTraversedChildNode(currentNode,coordinate);
            currentNode = traversedChildNode;
        }
        return currentNode;

    }

    private void computePenaltyForZeroValuedCoordinates(SymmetricTwoDimenMatrix matrix) throws VehicleRoutingException {

        SymmetricTwoDimenMatrixFunctionFactory.computePenaltyForZeroValuedCoordinates(matrix);
    }

    public SymmetricTwoDimenMatrixCoordinate getZeroValuedCoordinateWithMaxPenalty(BranchAndBoundGraphNode currentNode) throws VehicleRoutingException {

        exitIfObjectIsNull(currentNode);
        SymmetricTwoDimenMatrix currentNodeMatrix = currentNode.getMatrix();
        List<SymmetricTwoDimenMatrixCoordinate> zeroValueCoordinates = currentNodeMatrix.getCoordinatesWithZeroValue();

        SymmetricTwoDimenMatrixCoordinate zeroValueCoordinateWithMaxPenalty = null;
        double maxPenalty = 0;
        for (SymmetricTwoDimenMatrixCoordinate zeroValueCoordinate : zeroValueCoordinates) {
            double penalty = currentNodeMatrix.getCellPenalty(zeroValueCoordinate);
            if (penalty >= maxPenalty) {
                maxPenalty = penalty;
                zeroValueCoordinateWithMaxPenalty = zeroValueCoordinate;
            }
        }
        exitIfObjectIsNull(zeroValueCoordinateWithMaxPenalty);
        return zeroValueCoordinateWithMaxPenalty;
    }


    private BranchAndBoundGraphNode addChildNodesAndGetTraversedChildNode(BranchAndBoundGraphNode currentNode, SymmetricTwoDimenMatrixCoordinate zeroValuedCoordinateWithMaxPenalty) throws VehicleRoutingException {

        addNonTraversedNodeChildToCurrentNode(currentNode, zeroValuedCoordinateWithMaxPenalty);
        BranchAndBoundGraphNode nextTraversedNode = addTraversedNodeChildToCurrentNode(currentNode, zeroValuedCoordinateWithMaxPenalty);
        return nextTraversedNode;
    }


    private BranchAndBoundGraphNode addNonTraversedNodeChildToCurrentNode(BranchAndBoundGraphNode currentNode, SymmetricTwoDimenMatrixCoordinate zeroValuedCoordinateWithMaxPenalty) throws VehicleRoutingException {

        boolean isTraversed = false;
        SymmetricTwoDimenMatrix currentNodeMatrix = currentNode.getMatrix();
        double penalty = currentNodeMatrix.getCellPenalty(zeroValuedCoordinateWithMaxPenalty);
        double newBranchScore = currentNode.getBranchScore() + penalty;
        SymmetricTwoDimenMatrix newMatrix = currentNodeMatrix.clone();
        newMatrix.setCellValue(zeroValuedCoordinateWithMaxPenalty, Double.MAX_VALUE);
        PathBtwTwoCustomerLocations newPath = zeroValuedCoordinateWithMaxPenalty.mapToPath(isTraversed);
        return branchAndBoundGraph.addNonTraversedChildNode(currentNode, newBranchScore, newPath, newMatrix);
    }

    private BranchAndBoundGraphNode addTraversedNodeChildToCurrentNode(BranchAndBoundGraphNode currentNode, SymmetricTwoDimenMatrixCoordinate zeroValuedCoordinateWithMaxPenalty) throws VehicleRoutingException {

        boolean isTraversed = true;
        SymmetricTwoDimenMatrix cellMatrix = currentNode.getMatrix().clone();
        cellMatrix.decrementSize();
        setVisitedStatusOfAccessibleCoordinatesToTrue(cellMatrix, zeroValuedCoordinateWithMaxPenalty);
        disableSubToursInMatrix(currentNode, cellMatrix,zeroValuedCoordinateWithMaxPenalty);
        double penalty = SymmetricTwoDimenMatrixFunctionFactory.getSumColMinAndComputeColReductions(cellMatrix);
        double newBranchScore  = currentNode.getBranchScore() + penalty;
        PathBtwTwoCustomerLocations newPath = zeroValuedCoordinateWithMaxPenalty.mapToPath(isTraversed);
        return branchAndBoundGraph.addNonTraversedChildNode(currentNode, newBranchScore, newPath, cellMatrix);
    }

    private void setVisitedStatusOfAccessibleCoordinatesToTrue(SymmetricTwoDimenMatrix cellMatrix, SymmetricTwoDimenMatrixCoordinate zeroValuedCoordinateWithMaxPenalty) throws VehicleRoutingException {

        int rowId = zeroValuedCoordinateWithMaxPenalty.getRowId();
        int colId = zeroValuedCoordinateWithMaxPenalty.getColId();
        cellMatrix.setRowCellsVisited(rowId);
        cellMatrix.setColCellsVisited(colId);
    }

    private void disableSubToursInMatrix(BranchAndBoundGraphNode currentNode, SymmetricTwoDimenMatrix clonedMatrix,SymmetricTwoDimenMatrixCoordinate cellCoordinate) throws VehicleRoutingException {

        int lastCustomerLocationVisited = cellCoordinate.getColId();
        Set<Integer> customerLocationsVisited = currentNode.getPathFromDepot().getLocationsVisited();
        SymmetricTwoDimenMatrixCoordinate revCoordinate = new SymmetricTwoDimenMatrixCoordinate(cellCoordinate.getColId(),cellCoordinate.getRowId());
        if (!clonedMatrix.hasCellBeenVisited(revCoordinate)) {
            clonedMatrix.setCellValue(revCoordinate, Double.MAX_VALUE);
        }
        for(int customerLocation : customerLocationsVisited){
            if(customerLocation != 0) {
                SymmetricTwoDimenMatrixCoordinate coordinate = new SymmetricTwoDimenMatrixCoordinate(lastCustomerLocationVisited, customerLocation);
                if (!clonedMatrix.hasCellBeenVisited(coordinate)) {
                    clonedMatrix.setCellValue(coordinate, Double.MAX_VALUE);
                }
            }
        }

    }
}
