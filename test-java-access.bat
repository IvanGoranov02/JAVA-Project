@echo off
set JAVA_HOME=C:\Java\jdk-25.0.1
cd /d C:\src
"%JAVA_HOME%\bin\java.exe" -cp ".mvn\wrapper" -Dmaven.multiModuleProjectDirectory=C:\src org.apache.maven.wrapper.MavenWrapperMain --version
