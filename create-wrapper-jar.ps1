# Create proper maven-wrapper.jar from extracted files
$env:JAVA_HOME = "C:\Java\jdk-25.0.1"
$env:PATH = "C:\Java\jdk-25.0.1\bin;$env:PATH"

Write-Host "Създавам правилен maven-wrapper.jar с Main-Class..." -ForegroundColor Yellow

Push-Location ".mvn\wrapper"

# Create MANIFEST.MF with Main-Class
$manifestContent = @"
Manifest-Version: 1.0
Created-By: Maven JAR Plugin 3.3.0
Build-Jdk-Spec: 17
Specification-Title: Maven Wrapper Jar
Specification-Version: 3.2
Specification-Vendor: The Apache Software Foundation
Implementation-Title: Maven Wrapper Jar
Implementation-Version: 3.2.0
Implementation-Vendor: The Apache Software Foundation
Main-Class: org.apache.maven.wrapper.MavenWrapperMain

"@

$manifestContent | Out-File -FilePath "MANIFEST.MF" -Encoding ASCII -NoNewline

# Create jar file with manifest
jar cfm maven-wrapper.jar MANIFEST.MF org META-INF

# Clean up
Remove-Item "MANIFEST.MF" -Force -ErrorAction SilentlyContinue

Pop-Location

if (Test-Path ".mvn\wrapper\maven-wrapper.jar") {
    Write-Host "Готово! Правилен .jar файл създаден!" -ForegroundColor Green
} else {
    Write-Host "Грешка при създаването на .jar файла!" -ForegroundColor Red
}
