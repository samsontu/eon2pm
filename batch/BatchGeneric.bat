Setlocal
set MAXIMUM_MEMORY=-Xmx1256M
set OPTIONS=%MAXIMUM_MEMORY%
set EONHOME=E:\ATHENA_TestEnvironment

set LIB=%EONHOME%\lib
set TestEnvironmentINI=%EONHOME%\%1


set JRE=%EONHOME%\jre
set PCADIR=%LIB%\plugins\edu.stanford.smi.eon.pca

set PATH=%PATH%;%JRE%\bin


set PROTEGEJARS=%LIB%\protege.jar;%LIB%\looks.jar;%LIB%\unicode_panel.jar
set GUIDELINEJARS=%PCADIR%\eonpm.jar;%PCADIR%\AthenaPerformanceMetrics.jar;%PCADIR%\bmir-site.jar;%PCADIR%\eonpal.jar;%PCADIR%\antlr.jar;%LIB%\jcchart.jar;%LIB%\jctable.jar;%PCADIR%\log4j.jar
set TESTJARS=%LIB%\jcalendar.jar;%LIB%\gov.va.test.opioidtesttool\EONTestingEnvironment.jar;%LIB%\gov.va.test.opioidtesttool\test.jar
set CLASSPATH="%PROTEGEJARS%;%GUIDELINEJARS%;%TESTJARS%"

%JRE%\bin\java %OPTIONS%  -Dprotege.dir="%LIB%"   -DEON_HOME="%EONHOME%" -cp %CLASSPATH%  gov.va.test.opioidtesttool.pca.RegressionBatchClient "%TestEnvironmentINI%" 


