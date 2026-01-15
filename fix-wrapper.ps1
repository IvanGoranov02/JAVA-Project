# Fix maven-wrapper.jar from extracted files
$env:JAVA_HOME = "C:\Java\jdk-25.0.1"
$env:PATH = "C:\Java\jdk-25.0.1\bin;$env:PATH"

Write-Host "Поправям maven-wrapper.jar..." -ForegroundColor Yellow

# Backup old jar
if (Test-Path ".mvn\wrapper\maven-wrapper.jar") {
    Copy-Item ".mvn\wrapper\maven-wrapper.jar" ".mvn\wrapper\maven-wrapper-backup.jar" -Force
}

# Create new jar from extracted files
Push-Location ".mvn\wrapper"
jar cf maven-wrapper-new.jar org META-INF
Pop-Location

# Replace old jar
if (Test-Path ".mvn\wrapper\maven-wrapper-new.jar") {
    Remove-Item ".mvn\wrapper\maven-wrapper.jar" -Force -ErrorAction SilentlyContinue
    Rename-Item ".mvn\wrapper\maven-wrapper-new.jar" "maven-wrapper.jar"
    Write-Host "Готово! Нов .jar файл създаден." -ForegroundColor Green
} else {
    Write-Host "Грешка при създаването на .jar файла!" -ForegroundColor Red
}
