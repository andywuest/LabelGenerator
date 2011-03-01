rem Check for JAVA_HOME
if not exist "%JAVA_HOME%" goto nojava

SET JAVA_CMD="%JAVA_HOME%\bin\javaw"

SET PATH=lib
SET CLASSPATH=labelgenerator-0.1-SNAPSHOT.jar;lib

echo %CLASSPATH%

%JAVA_CMD% -jar labelgenerator-0.1-SNAPSHOT.jar

:nojava
echo.
echo ** JAVA_HOME environment variable has not been set correctly **
echo.
goto end

:end