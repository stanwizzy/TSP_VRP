package com.adobe.interview.utils;

import com.adobe.interview.exceptions.VehicleRoutingRuntimeException;
import com.adobe.interview.inputGraph.CustomerLocation;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {

    private static Properties properties ;
    private static final Logger logger=Logger.getLogger(ConfigLoader.class);

    public static void initConfigLoader(){

        properties = new Properties();
        loadProperties();
    }

    private static void loadProperties() {

        String envFilePath = System.getProperty("config.file");
        try {
            properties.load(new FileInputStream(new File(envFilePath)));
            validatePropertyFile();
        } catch (IOException e) {
            throw new VehicleRoutingRuntimeException("Could not load property file: " + System.getProperty("config.file"));
        }
    }

    private static void validatePropertyFile() {

        checkPropertyKey(Keys.EMPLOYEE_NUM.getDescription());
        checkPropertyKey(Keys.OUTPUT_FILE.getDescription());
        checkPropertyKey(Keys.INPUT_FILE.getDescription());
        checkPropertyKey(Keys.X_COORDINATE_DEPOT.getDescription());
        checkPropertyKey(Keys.Y_COORDINATE_DEPOT.getDescription());

        logger.info("Config values " +
                "employee num= " + properties.getProperty(Keys.EMPLOYEE_NUM.getDescription()) + "/n" +
                "output file= " + properties.getProperty(Keys.OUTPUT_FILE.getDescription()) + "/n" +
                "input file= " + properties.getProperty(Keys.INPUT_FILE.getDescription()) + "/n" +
                "X Coordinate of Depot " + properties.getProperty(Keys.X_COORDINATE_DEPOT.getDescription()) + "/n" +
                "Y Coordinate of Depot= " + properties.getProperty(Keys.Y_COORDINATE_DEPOT.getDescription()) + "/n" );
    }


    private static void checkPropertyKey(String key) {

        if(!properties.containsKey(key)){
            throw new VehicleRoutingRuntimeException("Property file must contain: " + key);
        }
    }

    public static Integer getEmployeeNum(){
        return Integer.valueOf(properties.getProperty(Keys.EMPLOYEE_NUM.getDescription()));
    }

    public static String getOutputFilePath(){
        return properties.getProperty(Keys.OUTPUT_FILE.getDescription());
    }

    public static CustomerLocation getSourceLocation(){

        String sourceXCoordinate = properties.getProperty(Keys.X_COORDINATE_DEPOT.getDescription());
        String sourceYCoordinate = properties.getProperty(Keys.Y_COORDINATE_DEPOT.getDescription());
        return CustomerLocation.parseTextWithSpaceDelimitedCoordinates(sourceXCoordinate + " " + sourceYCoordinate);

    }

    public static String getCustomerLocationsFilePath(){

        return properties.getProperty(Keys.INPUT_FILE.getDescription());
    }

    private enum Keys {

        EMPLOYEE_NUM  ("employee_num"),
        OUTPUT_FILE("output_file_path"),
        INPUT_FILE("customer_locations_filepath"),
        X_COORDINATE_DEPOT("source_location_X"),
        Y_COORDINATE_DEPOT("source_location_Y") ;

        private final String description;

        Keys(String description) {
            this.description = description;
        }

        public String getDescription(){
            return description;
        }
    }
}
