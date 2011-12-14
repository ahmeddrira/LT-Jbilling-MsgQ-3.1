#!/bin/bash
#
# Shutdown script for jBilling under Cruise Control. This script kills the
# jBilling instance identified by the process ID from the 'jbilling.pid' file
# created by the startup script.


PID_FILE=jbilling.pid

# get the PID output from the startup script
if [ -f $PID_FILE ]; then
    JBILLING_PID=`cat $PID_FILE`
    echo "Shutting down jBilling PID $JBILLING_PID"
else
    echo "Cannot find $PID_FILE, maybe jBilling wasn't started with the startup script?"
fi

# kill the process if it's running
if [ -n "$JBILLING_PID" ] && ps -p ${JBILLING_PID} > /dev/null ; then
    kill -9 ${JBILLING_PID}
else
    echo "jBilling is not running."
fi
                
# remove the pid file
if [ -f $PID_FILE ]; then
    rm $PID_FILE
fi

exit 0;
