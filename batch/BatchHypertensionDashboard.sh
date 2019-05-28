#!/bin/sh

export JAVA_HOME=`/usr/libexec/java_home -v 1.8`

EONHOME=/Users/tu/workspace/eon2pm
KB=/Users/tu/Documents/Dropbox/VA/ATHENA/athenakbs/ATHENA_HTN_Dashboard.pprj

$EONHOME/batch/BatchGeneric.sh $EONHOME $KB $EONHOME/ini/BatchHTN.ini > $EONHOME/testresult/BatchHTNDashBd.log
