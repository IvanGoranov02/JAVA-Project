# PowerShell script to build the project with Maven Wrapper
$env:JAVA_HOME = "C:\Java\jdk-25.0.1"

if (-not (Test-Path "$env:JAVA_HOME\bin\java.exe")) {
    Write-Host "Error: JAVA_HOME is set to an invalid directory: $env:JAVA_HOME" -ForegroundColor Red
    exit 1
}

Write-Host "Using JAVA_HOME: $env:JAVA_HOME" -ForegroundColor Green
Write-Host "Running Maven Wrapper..." -ForegroundColor Green
Write-Host ""

.\mvnw.cmd $args
