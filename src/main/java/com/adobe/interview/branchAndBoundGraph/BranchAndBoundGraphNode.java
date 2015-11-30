package com.adobe.interview.branchAndBoundGraph;


import com.adobe.interview.exceptions.VehicleRoutingRuntimeException;
import com.adobe.interview.matrix.SymmetricTwoDimenMatrix;

public class BranchAndBoundGraphNode implements Comparable<BranchAndBoundGraphNode>{

    private PathFromDepot pathFromDepot;
    private BranchAndBoundGraphNode nextTraversedChildNode;
    private BranchAndBoundGraphNode nextNonTraversedChildNode;
    private SymmetricTwoDimenMatrix matrix;
    private Double branchScore;


    public BranchAndBoundGraphNode(PathFromDepot pathFromDepot,double branchScore,SymmetricTwoDimenMatrix matrix){
        this.pathFromDepot = pathFromDepot;
        this.nextNonTraversedChildNode = null;
        this.nextTraversedChildNode = null;
        this.matrix = matrix;
        this.branchScore = branchScore;
    }

    public double getBranchScore() {
        return branchScore;
    }

    public void setNextTraversedChildNode(BranchAndBoundGraphNode childNode) {

        exitIfNullNode(childNode);
        this.nextTraversedChildNode = childNode;
    }

    private void exitIfNullNode(BranchAndBoundGraphNode node){

        if(node == null){
            throw new VehicleRoutingRuntimeException("Null node argument provided...Exiting.");
        }
    }

    public void setNextNonTraversedChildNode(BranchAndBoundGraphNode childNode) {

        exitIfNullNode(childNode);
        this.nextNonTraversedChildNode = childNode;
    }

    public PathFromDepot getPathFromDepot() {
        return pathFromDepot;
    }

    public SymmetricTwoDimenMatrix getMatrix() {

        return matrix;
    }


    @Override
    public int compareTo(BranchAndBoundGraphNode otherNode) {

        exitIfNullNode(otherNode);
        return branchScore.compareTo(otherNode.getBranchScore());
    }
}
