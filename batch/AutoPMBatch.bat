setlocal
set MAXIMUM_MEMORY=-Xmx1256M
set OPTIONS=%MAXIMUM_MEMORY%
set EONHOME=C:\ATHENA_TestEnvironment

set LIB=%EONHOME%\lib
set GUIDELINEFILE=%EONHOME%\kbs\ATHENA_CVD.pprj
set TestEnvironmentINI=%EONHOME%\ini\BatchTestingCVD.ini


set JRE="C:\Program Files (x86)\Java\jre6"
set PCADIR=%LIB%\plugins\edu.stanford.smi.eon.pca

set PATH=%PATH%;%JRE%\bin


set PROTEGEJARS=%LIB%\protege.jar;%LIB%\looks.jar;%LIB%\unicode_panel.jar
set GUIDELINEJARS=%PCADIR%\pca-glinda.jar;%LIB%\plugins\chronusII.jar;%PCADIR%\eonpal.jar;%PCADIR%\antlr.jar;%LIB%\jcchart.jar;%LIB%\jctable.jar;%PCADIR%\log4j.jar
set TESTJARS=%LIB%\jcalendar.jar;%LIB%\gov.va.test.opioidtesttool\EONTestingEnvironment.jar;%LIB%\gov.va.test.opioidtesttool\test.jar
set CLASSPATH="%PROTEGEJARS%;%GUIDELINEJARS%;%TESTJARS%"

%JRE%\bin\java %OPTIONS%  -Dprotege.dir="%LIB%"   -cp %CLASSPATH%  gov.va.test.opioidtesttool.pca.RegressionBatchClient "%TestEnvironmentINI%" 

pause
