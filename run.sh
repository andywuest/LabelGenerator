#!/bin/sh

# Check for JAVA_HOME
if test ! -d $JAVA_HOME
then
 echo ""
 echo "** JAVA_HOME environment variable has not been set correctly **"
 echo ""
 exit
fi

export JAVA_CMD="$JAVA_HOME/bin/java"

export CLASSPATH="labelgenerator-0.1-SNAPSHOT.jar"

$JAVA_CMD -jar labelgenerator-0.1-SNAPSHOT.jar

