# TSP_VRP
Steps To Run This Program
(1) export an environment variable - INPUT_FILE_PATH. This environment variable should point to the location of a configuration file.
(2) Configuration file should contain these fields:
        employee_num: Number of service personels(defaults to 1)
        output_file_path: File path where the result will be printed
        source_location_X: X coordinate of depot location
        source_location_Y: Y coordinate of depot location
        customer_locations_filepath: File path of file that contains cartesian coordinates of customer locations. It assumes the first line is a header.

        Example:
                employee_num=1
                output_file_path=/Users/stanleyopara/tmp/output.txt
                source_location_X=0
                source_location_Y=0
                customer_locations_filepath=/Users/stanleyopara/tmp/input.txt
(3) Run program by starting the script: script/startup.sh
(4) Logs are printed in the logs directory


Methodology
This program implements the Branch and Bound algorithm proposed by Little et.al in 1963.
