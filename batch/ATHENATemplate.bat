rem How to set up a bat file for running Protege test environment
rem This script assumes that an ini file that points the correct xml data file has been set up
rem Sample call: ATHENAGeneric  C:\apps\eon2m C:\appData\athenakbs\ATHENA_HTN_Dashboard.pprj ini\HTNTestingEnvironment.ini


set EONHOME=[EON Installation Directory]
set KBDIR=[KB Installation Directory]
set KBPROJ=[Project file of the KB to be used]
set INIFILE=[Ini file to be used]

ATHENAGeneric  %EONHOME% %KBDIR%\%KBPROJ% ini\%INIFILE