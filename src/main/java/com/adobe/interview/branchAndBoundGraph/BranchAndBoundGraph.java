package com.adobe.interview.branchAndBoundGraph;


import com.adobe.interview.exceptions.VehicleRoutingException;
import com.adobe.interview.matrix.SymmetricTwoDimenMatrix;
import org.apache.log4j.Logger;

import java.util.PriorityQueue;

class BranchAndBoundGraph {

     private BranchAndBoundGraphNode root;
     private PriorityQueue<BranchAndBoundGraphNode> minQueueOfNonTraversedNodes;
     private static final Logger logger = Logger.getLogger(BranchAndBoundGraph.class);


    public BranchAndBoundGraph(double lowerBoundBranchScore,int numNodes,SymmetricTwoDimenMatrix matrix){
         this.root = new BranchAndBoundGraphNode(new PathFromDepot(numNodes),lowerBoundBranchScore,matrix);
         this.minQueueOfNonTraversedNodes = new PriorityQueue<>();
     }

    public BranchAndBoundGraphNode getRootNode(){
        return root;
    }


    public BranchAndBoundGraphNode addTraversedChildNode(BranchAndBoundGraphNode currentNode, double branchScoreOfChildNode, PathBtwTwoCustomerLocations newPath,SymmetricTwoDimenMatrix matrix){

        logger.info("Adding Traversed path: " +  newPath.toString() + " to graph");
        PathFromDepot clonedPathFromDepot = addPathAndClone(currentNode,newPath);
        BranchAndBoundGraphNode traversedChildNode = new BranchAndBoundGraphNode(clonedPathFromDepot,branchScoreOfChildNode,matrix);
        currentNode.setNextTraversedChildNode(traversedChildNode);
        return traversedChildNode;
    }

     public BranchAndBoundGraphNode addNonTraversedChildNode(BranchAndBoundGraphNode currentNode, double branchScoreOfChildNode, PathBtwTwoCustomerLocations newPath, SymmetricTwoDimenMatrix matrix){

         logger.info("Adding Non Traversed path: " + newPath.toString() + " to graph");

         PathFromDepot clonedPathFromDepot = addPathAndClone(currentNode,newPath);

         BranchAndBoundGraphNode nonTraversedChildNode = new BranchAndBoundGraphNode(clonedPathFromDepot,branchScoreOfChildNode,matrix);

         logger.debug("Adding Non traversed node with score : " + nonTraversedChildNode.getBranchScore() + " to priority queue");
         minQueueOfNonTraversedNodes.add(nonTraversedChildNode);
         currentNode.setNextNonTraversedChildNode(nonTraversedChildNode);
         return nonTraversedChildNode;

     }

    private PathFromDepot addPathAndClone(BranchAndBoundGraphNode currentNode,PathBtwTwoCustomerLocations newPath) {

        PathFromDepot pathFromDepot = currentNode.getPathFromDepot();
        PathFromDepot clonedPathFromDepot = pathFromDepot.clone();
        clonedPathFromDepot.addPath(newPath);
        return clonedPathFromDepot;
    }

    public double getMinScoreOfNonTraversedNodes() throws VehicleRoutingException {

        exitIfPriorityQueueIsEmpty();
        double minScore = minQueueOfNonTraversedNodes.peek().getBranchScore();
        logger.debug("Retrieving min score from graph: " + minScore );
        return minScore;
    }

    private void exitIfPriorityQueueIsEmpty() throws VehicleRoutingException {

        if(minQueueOfNonTraversedNodes.isEmpty()){
            throw new VehicleRoutingException("Cannot get min score from an empty priority queue");
        }
    }

    public BranchAndBoundGraphNode popNonTraversedNodeWithMinScore() throws VehicleRoutingException {

        exitIfPriorityQueueIsEmpty();
        return minQueueOfNonTraversedNodes.poll();
    }

}
