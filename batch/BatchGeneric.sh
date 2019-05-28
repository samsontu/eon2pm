#!/bin/sh

MAXIMUM_MEMORY=-Xmx512M
OPTIONS=$MAXIMUM_MEMORY
EONHOME=$1
GUIDELINEFILE=$2
TestEnvironmentINI=$EONHOME/$3
LIB=$EONHOME/lib

JRE=$JAVA_HOME
PCADIR=$LIB/plugins/edu.stanford.smi.eon.pca
LOG4JCONFIG=-Dlog4j.configuration=$TestEnvironmentINI
INI=-DTestEnvironmentINI=$TestEnvironmentIN
JAVACLASS=gov.va.test.opioidtesttool.pca.RegressionBatchClient

PATH=$PATH:JRE/bin

PROTEGEJARS=$LIB/protege.jar:$LIB/looks.jar:$LIB/unicode_panel.jar
GUIDELINEJARS=$PCADIR/eonpm.jar:$PCADIR/AthenaPerformanceMetrics.jar:$PCADIR/eonpal.jar:$PCADIR/antlr.jar:$LIB/jcchart.jar:$LIB/jctable.jar:$PCADIR/log4j.jar
TESTJARS=$LIB/jcalendar.jar
CLASSPATH=$PROTEGEJARS:$GUIDELINEJARS:$TESTJARS

$JRE/bin/java $OPTIONS  -Dprotege.dir=$LIB -Dlog4j.debug  -cp $CLASSPATH -DEON_HOME=$EONHOME $LOG4JCONFIG $INI $JAVACLASS $GUIDELINEFILE
