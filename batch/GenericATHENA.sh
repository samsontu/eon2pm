#!/bin/sh
EONHOME=/Users/tu/workspace/eon2pm
KBHOME=EONHOME/testkbs
JRE=/usr/bin
LIB=${EONHOME}/lib
GUIDELINEFILE=${KBHOME}/$1
TestEnvironmentINI=${EONHOME}/$2
#MAXIMUM_MEMORY=-Xmx1512M
#OPTIONS=${MAXIMUM_MEMORY}
PCADIR=${LIB}/edu.stanford.smi.eon.pca
PATH={PATH}:${JRE}/bin
PROTEGEJARS=${LIB}/protege.jar:${LIB}/looks.jar:${LIB}/unicode_panel.jar:${LIB}/edu.stanford.smi.protegex.standard_extensions/standard-extensions.jar:${LIB}/edu.stanford.smi.protegex.instance_tree/instance-tree.jar
GUIDELINEJARS={PCADIR}/eonpm.jar:${PCADIR}/AthenaPerformanceMetrics.jar:${PCADIR}/eonpal.jar:${PCADIR}/antlr.jar:${PCADIR}/log4j.jar
TESTJARS=${LIB}/jcalendar.jar:${LIB}/jcchart.jar:${LIB}/jctable.jar
CLASSPATH=${PROTEGEJARS}:${GUIDELINEJARS}:${TESTJARS}
/usr/bin/java ${OPTIONS}  -Dprotege.dir=${LIB}   -cp ${CLASSPATH} -DEON_HOME=${EONHOME}   -DTestEnvironmentINI=${TestEnvironmentINI} edu.stanford.smi.protege.Application ${GUIDELINEFILE}