set EONHOME=C:\apps\ProtegeTestEnvironmentWin
set INIFILE=BatchCKDDashboard.ini
set KBPATH=C:\apps\athenakbs\ATHENA_CKD_Dashboard.pprj
set LOGFILE=BatchCKDDash.log

BatchGeneric.bat %EONHOME% %KBPATH% ini\%INIFILE% > ..\testresult\%LOGFILE%
