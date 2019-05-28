set EONHOME=C:\apps\eon2m
set INIFILE=BatchDMGlycemicControlDashboard.ini
set KBPATH=C:\appData\athenakbs\ATHENA_DM_GlycemicControl_Dashboard.pprj
set LOGFILE=BatchDMGlyCntrlDash.log

BatchGeneric.bat %EONHOME% %KBPATH% ini\%INIFILE% > ..\testresult\%LOGFILE%
