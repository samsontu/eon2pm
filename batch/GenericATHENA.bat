rem @Echo off

setlocal
set EONHOME=Z:\workspace\eon2pm
set KBHOME=Z:\workspace\eon2pm\kbs
set JRE=C:\OpioidTestEnvironment\jre7


set LIB=%EONHOME%\lib
set GUIDELINEFILE=%KBHOME%\%1
set TestEnvironmentINI=%EONHOME%\%2
set MAXIMUM_MEMORY=-Xmx512M
set OPTIONS=%MAXIMUM_MEMORY%

set PCADIR=%LIB%\edu.stanford.smi.eon.pca
set PATH=%PATH%;%JRE%\bin

set PROTEGEJARS=%LIB%\protege.jar;%LIB%\looks.jar;%LIB%\unicode_panel.jar;%LIB\edu.stanford.smi.protegex.standard_extensions\standard-extensions.jar;%LIB%\\edu.stanford.smi.protegex.instance_tree\instance-tree.jar
set GUIDELINEJARS=%PCADIR%\eonpm.jar;%PCADIR%\AthenaPerformanceMetrics.jar;%PCADIR%\eonpal.jar;%PCADIR%\antlr.jar;%PCADIR%\log4j.jar
set TESTJARS=%LIB%\jcalendar.jar;%LIB%\jcchart.jar;%LIB%\jctable.jar
set CLASSPATH="%PROTEGEJARS%;%GUIDELINEJARS%;%TESTJARS%"

%JRE%\bin\java %OPTIONS%  -Dprotege.dir="%LIB%"   -cp %CLASSPATH% -DEON_HOME="%EONHOME%"   -DTestEnvironmentINI="%TestEnvironmentINI%" edu.stanford.smi.protege.Application %GUIDELINEFILE%

pause

