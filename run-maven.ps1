# Simple script to run Maven commands
# This sets JAVA_HOME and runs Maven Wrapper

$env:JAVA_HOME = "C:\Java\jdk-25.0.1"

# Check if JAVA_HOME is valid
if (-not (Test-Path "$env:JAVA_HOME\bin\java.exe")) {
    Write-Host "ERROR: Java not found at $env:JAVA_HOME" -ForegroundColor Red
    Write-Host "Please check your Java installation." -ForegroundColor Red
    exit 1
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Maven Wrapper Build Script" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "JAVA_HOME: $env:JAVA_HOME" -ForegroundColor Green
Write-Host ""

# Get the command arguments (everything after the script name)
$mavenArgs = $args

if ($mavenArgs.Count -eq 0) {
    Write-Host "No Maven command specified. Running 'clean install'..." -ForegroundColor Yellow
    $mavenArgs = @("clean", "install")
}

Write-Host "Running: mvnw.cmd $($mavenArgs -join ' ')" -ForegroundColor Yellow
Write-Host ""

# Run Maven Wrapper
& ".\mvnw.cmd" $mavenArgs

# Check exit code
if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "Build failed with exit code: $LASTEXITCODE" -ForegroundColor Red
    exit $LASTEXITCODE
} else {
    Write-Host ""
    Write-Host "Build completed successfully!" -ForegroundColor Green
}
