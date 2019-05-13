#!/bin/sh

EONHOME=[EON Installation Directory]
INIFILE=[Ini file to be used]
KBPATH=[Absolute Path to KB to be used]
LOGFILE=[Log file to record console output]

./BatchGeneric.sh $EONHOME $KBPATH ini/$INIFILE  > ../testresult/$LOGFILE
