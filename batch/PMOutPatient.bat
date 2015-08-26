rem @Echo off

setlocal
set MAXIMUM_MEMORY=-Xmx1256M
set OPTIONS=%MAXIMUM_MEMORY%
set EONHOME=C:\projects\eclipse\eonpm

set LIB=%EONHOME%\lib


set JRE="C:\Program Files (x86)\Java\jre6"
set PCADIR=%LIB%\edu.stanford.smi.eon.pca

set PATH=%PATH%;%JRE%\bin


set PROTEGEJARS=%LIB%\protege.jar;%LIB%\looks.jar;%LIB%\unicode_panel.jar;%LIB%\mysql-connector-java-3.0.17-ga-bin.jar
set GUIDELINEJARS=%PCADIR%\eonpm.jar;%PCADIR%\bmir-site.jar;%PCADIR%\eonpal.jar;%PCADIR%\antlr.jar;%LIB%\jcchart.jar;%LIB%\jctable.jar;%PCADIR%\log4j.jar
set CLASSPATH=%PROTEGEJARS%;%GUIDELINEJARS%

%JRE%\bin\java %OPTIONS%  -Dprotege.dir="%LIB%" -cp %CLASSPATH%  edu.stanford.smi.eon.clients.TestEONExecEngineAPIForOutpatient C:\projects\eclipse\eonpm\ini\BMIROutpatientEON.ini  2012-10-10 2011-10-29 2012-10-30

pause
