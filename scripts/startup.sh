#!/usr/bin/env bash

##############################################################################
##
##  VehicleRouting start up script for UNIX
##
##############################################################################

if [ -z $INPUT_FILE_PATH ];then
    echo "ERROR: Please set environment variable - INPUT_FILE_PATH. "
    exit 1;
fi

if [ -z $JAVA_HOME ];then
    echo "ERROR: Please set environment variable - JAVA_HOME. "
fi


APP_NAME="VehicleRouting"

CUR_DIR="$PWD"
SCRIPTFILE=$0
if [ "${SCRIPTFILE}" = "-bash" ] ;
then 
        SCRIPTFILE=${BASH_ARGV[0]}
fi 
SCRIPTPATH="${CUR_DIR}/`dirname ${SCRIPTFILE}`"

APP_HOME=`dirname $SCRIPTPATH`

CLASSPATH=$APP_HOME/lib/VehicleRouting-all-1.0-SNAPSHOT.jar

echo $CLASSPATH

JAVA_OPTS="-Dconfig.file=$INPUT_FILE_PATH -Xms512m -Xmx2g"

##exec "$JAVA_HOME/bin/java" $JAVA_OPTS -classpath "$CLASSPATH" com.adobe.interview.main.Launcher
