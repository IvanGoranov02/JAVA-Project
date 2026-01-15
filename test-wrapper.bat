@echo off
set JAVA_HOME=C:\Java\jdk-25.0.1
set MAVEN_PROJECTBASEDIR=C:\src
"%JAVA_HOME%\bin\java.exe" -Dmaven.multiModuleProjectDirectory="%MAVEN_PROJECTBASEDIR%" -cp "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper" org.apache.maven.wrapper.MavenWrapperMain --version
