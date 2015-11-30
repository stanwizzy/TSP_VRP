package com.adobe.interview.main;


import com.adobe.interview.branchAndBoundGraph.BranchAndBoundGraphBuilder;
import com.adobe.interview.branchAndBoundGraph.PathFromDepot;
import com.adobe.interview.exceptions.VehicleRoutingException;
import com.adobe.interview.exceptions.VehicleRoutingRuntimeException;
import com.adobe.interview.inputGraph.CartesianCustomerLocationsProcessorImpl;
import com.adobe.interview.inputGraph.CustomerLocationsProcessor;
import com.adobe.interview.matrix.SymmetricTwoDimenMatrix;
import com.adobe.interview.utils.ConfigLoader;
import com.adobe.interview.utils.CustomerSeparationMatrixAdapter;
import org.apache.log4j.Logger;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Main Class For A Branch and Bound algorithm that solves the vehicle routing problem
 * This algorithm was proposed by Little in 1963. --> http://www.ecs.umass.edu/ece/tessier/courses/669/little.pdf
 *
 * BRIEF SUMMARY OF ALGORITHM
 * It computes the optimal path by;
 * (1) Finds the lower bound score by adding the sum of the smallest element of every row(minima is further deducted from every element in the corresponding row)
 *  to the sum of  smallest element from every column that does not have a zero.
 *
 * (2) It branches from the lower bound score into 2 directions.
 *  - first branch is the non-traversed branch which adds
 *    the largest penalty from  all elements with zero value to the parent score
 *  - second branch is the traversed branch(branches from the zero valued element with max penalty.
 *    which adds the parent score with the sum of smallest elements in columns that have no zero values
 *
 * (3) Sub tours are disabled by marking conflicting coordinates to infinity
 *
 * (4) The above procedure is repeated on the traversed node
 *
 * (5) When the base condition has been met the branch score is compared with the minimum score from non-traversed branches
 *      - if smaller , the program exits with the branch score as the optimal score
 *      _if larger, the program repeats above steps on the smallest non-traversed branch with the smallest score.
 */

public class Launcher {

    private static final Logger logger  = Logger.getLogger(Launcher.class);
    private CustomerLocationsProcessor locationProcessor;

    public Launcher(){

        this.locationProcessor = new CartesianCustomerLocationsProcessorImpl();
    }
    public void findOptimalRoute() {

        try{
            ConfigLoader.initConfigLoader();
            tryRunApplication();
        }catch(VehicleRoutingException e){
            logger.error("Could not complete application due to the following error: " + e.getMessage());
        }
    }

    private void tryRunApplication() throws VehicleRoutingException {

        SymmetricTwoDimenMatrix customerSeparationMatrix = loadCustomerLocationFileAndBuildMatrix();
        PathFromDepot optimalPath = buildGraphAndGetOptimalPath(customerSeparationMatrix);
        PathFromDepot orderedOptimalPath = orderAndValidatePath(optimalPath);
        writePathToFile(orderedOptimalPath);
    }

    private SymmetricTwoDimenMatrix loadCustomerLocationFileAndBuildMatrix() {

        String inputFilePath =  ConfigLoader.getCustomerLocationsFilePath();
        logger.info("Loading customer location file...");
        locationProcessor.loadLocationsFromFile(inputFilePath);
        logger.info("Creating matrix of customer separations");
        SymmetricTwoDimenMatrix customerSeparationMatrix = CustomerSeparationMatrixAdapter.getCustomerSeparationMatrixFromInputGraph(locationProcessor);
        exitIfObjectIsNull(customerSeparationMatrix);
        return customerSeparationMatrix;
    }

    private void exitIfObjectIsNull(Object o) {
        if(o == null){
            logger.error("Null project either provided or retunred from method");
            throw new VehicleRoutingRuntimeException("Exiting program...Null object provided or returned from method");
        }
    }

    private PathFromDepot buildGraphAndGetOptimalPath(SymmetricTwoDimenMatrix customerSeparationMatrix) throws VehicleRoutingException {

        logger.info("Building Branch and Bound Graph");
        PathFromDepot optimalPath = BranchAndBoundGraphBuilder.newInstance(customerSeparationMatrix)
                .setEmployeeCount(ConfigLoader.getEmployeeNum())
                .buildGraph()
                .getPathFromDepot();
        exitIfObjectIsNull(optimalPath);
        return optimalPath;
    }


    private PathFromDepot orderAndValidatePath(PathFromDepot optimalPath) throws VehicleRoutingException {

        logger.info("Sorting and validating optimal path ");
        boolean result = optimalPath.orderAndValidate();
        if(optimalPathIsValid(result)){
            logger.info("Optimal Path: \n" + optimalPath.toString());
            return optimalPath;
        }else{
            logger.error("Could not validate optimal path");
            throw new VehicleRoutingException("Could not validate optimal path, Optimal paths must be contiguous");
        }
    }

    private boolean optimalPathIsValid(boolean validatedOptimalPath) {
        return validatedOptimalPath;
    }

    private void writePathToFile(PathFromDepot sortedOptimalPath) {

        try{
            tryWritePathToFile(sortedOptimalPath);
        }catch(IOException e){
            logger.error("Could not write file to output file path " + e.getMessage());
        }
    }

    private void tryWritePathToFile(PathFromDepot sortedOptimalPath) throws IOException {

        String outputFilePath = ConfigLoader.getOutputFilePath();
        FileOutputStream outputStream = new FileOutputStream(outputFilePath);
        byte[] customerIDLocationMapping = locationProcessor.toString().getBytes();
        outputStream.write(customerIDLocationMapping);
        byte[] sortedOptimalPathBytes = sortedOptimalPath.toString().getBytes();
        logger.info("Writing optimal path to: " + outputFilePath);
        outputStream.write(sortedOptimalPathBytes);
    }

    public static void main(String[] args) {

        Launcher launcher = new Launcher();
        launcher.findOptimalRoute();
    }
}
