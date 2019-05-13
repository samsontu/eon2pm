rem How to set up a bat file for running all cases in a data file
rem This script assumes that an ini file that points the correct xml data file and KB has been set up
rem It is required that a testresult directory with "working" and "archive" subdirectories have been set up

set EONHOME=[EON Installation Directory]
set INIFILE=[Ini file to be used]
BatchGeneric.bat %EONHOME ini\BatchTemplate.ini > ..\testresult\BatchTemplate.log
