#!/bin/sh
MAXIMUM_MEMORY=-Xmx512M
OPTIONS=$MAXIMUM_MEMORY
EONHOME=$1
GUIDELINEFILE=$2
TestEnvironmentINI=$EONHOME/$3
LIB=$EONHOME/lib

JRE=$JAVA_HOME
PCADIR=$LIB/plugins/edu.stanford.smi.eon.pca

PATH=$PATH:JRE/bin


PROTEGEJARS=$LIB/protege.jar:$LIB/looks.jar:$LIB/unicode_panel.jar
GUIDELINEJARS=$PCADIR/eonpm.jar:$PCADIR/AthenaPerformanceMetrics.jar:$LIB/plugins/chronusII.jar:$PCADIR/eonpal.jar:$PCADIR/antlr.jar:$LIB/jcchart.jar:$LIB/jctable.jar:$PCADIR/log4j.jar
TESTJARS=$LIB/jcalendar.jar:$LIB/gov.va.test.opioidtesttool/EONTestingEnvironment.jar:$LIB/gov.va.test.opioidtesttool/test.jar
CLASSPATH=$PROTEGEJARS:$GUIDELINEJARS:$TESTJARS

$JRE/bin/java $OPTIONS  -Dprotege.dir=$LIB   -cp $CLASSPATH -DEON_HOME=$EONHOME   -DTestEnvironmentINI=$TestEnvironmentINI edu.stanford.smi.protege.Application $GUIDELINEFILE



