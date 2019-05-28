#!/bin/sh

EONHOME=/Users/tu/workspace/eon2pm
INIFILE=BatchDMGlycemicControlDashboard.ini
KBPATH=/Users/tu/Documents/Dropbox/VA/ATHENA/athenakbs/ATHENA_DM_GlycemicControl_Dashboard.pprj
LOGFILE=BatchDMGlyCntrlDash.log

./BatchGeneric.sh $EONHOME $KBPATH ini/$INIFILE  > ../testresult/$LOGFILE

