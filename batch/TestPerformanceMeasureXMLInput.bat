setlocal
set MAXIMUM_MEMORY=-Xmx856M
set OPTIONS=%MAXIMUM_MEMORY%
set EONHOME=E:\ATHENA_TestEnvironment

set LIB=%EONHOME%\lib
set INI=%EONHOME%\ini\BMIREON.ini


set JRE="C:\Program Files (x86)\Java\jre6"
set PCADIR=%LIB%\plugins\edu.stanford.smi.eon.pca

set PATH=%PATH%;%JRE%\bin


set PROTEGEJARS=%LIB%\protege.jar;%LIB%\looks.jar;%LIB%\unicode_panel.jar
set GUIDELINEJARS=%PCADIR%\pca-glinda.jar;%PCADIR%\eonpal.jar;%PCADIR%\antlr.jar;%PCADIR%\log4j.jar
set CLASSPATH="%PROTEGEJARS%;%GUIDELINEJARS%

%JRE%\bin\java %OPTIONS%  -Dprotege.dir="%LIB%"   -cp %CLASSPATH%  edu.stanford.smi.eon.clients.ComputePerformanceMeasureXML "%INI%" 1979-10-30 1978-10-29 1979-10-30

