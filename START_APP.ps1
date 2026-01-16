# Скрипт за стартиране на приложението
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
$env:PATH = "C:\Program Files\Java\jdk-21\bin;$env:PATH"

Write-Host "JAVA_HOME е настроен: $env:JAVA_HOME" -ForegroundColor Green
Write-Host ""

# Опитване да стартираме с Maven wrapper
Write-Host "Опитвам да стартирам приложението..." -ForegroundColor Yellow

# Проверка за права
$mvnPath = Join-Path $PSScriptRoot "mvnw.cmd"
if (Test-Path $mvnPath) {
    Write-Host "Maven wrapper намерен" -ForegroundColor Green
    
    # Опит с директно стартиране
    try {
        & $mvnPath spring-boot:run
    } catch {
        Write-Host "Грешка при стартиране с Maven wrapper: $_" -ForegroundColor Red
        Write-Host ""
        Write-Host "Алтернатива: Стартирайте от IDE (IntelliJ IDEA / Eclipse)" -ForegroundColor Yellow
        Write-Host "Или компилирайте и стартирайте JAR файла:" -ForegroundColor Yellow
        Write-Host "  .\mvnw.cmd clean package" -ForegroundColor Cyan
        Write-Host "  java -jar target\shoe-warehouse-1.0.0.jar" -ForegroundColor Cyan
    }
} else {
    Write-Host "Maven wrapper не е намерен" -ForegroundColor Red
}
