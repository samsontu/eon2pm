#!/bin/sh

EONHOME=/Users/tu/workspace/eon2pm
INIFILE=BatchCKDDashboard.ini
KBPATH=/Users/tu/Documents/Dropbox/VA/ATHENA/athenakbs/ATHENA_CKD_Dashboard.pprj
LOGFILE=BatchCKDDash.log

./BatchGeneric.sh $EONHOME $KBPATH ini/$INIFILE  > ../testresult/$LOGFILE

