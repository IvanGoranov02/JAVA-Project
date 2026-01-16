# Настройка на Java за текущата сесия
$javaPath = "C:\Program Files\Java\jdk-21"

# Задаване на JAVA_HOME за текущата сесия
$env:JAVA_HOME = $javaPath
$env:PATH = "$javaPath\bin;$env:PATH"

Write-Host "Java е настроен за текущата сесия!" -ForegroundColor Green
Write-Host "JAVA_HOME: $env:JAVA_HOME" -ForegroundColor Cyan
Write-Host ""
Write-Host "Тестване на Java:" -ForegroundColor Yellow
& "$javaPath\bin\java.exe" -version
Write-Host ""
Write-Host "За да направите това постоянно, изпълнете следните команди като Администратор:" -ForegroundColor Yellow
Write-Host '[System.Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-21", "Machine")' -ForegroundColor White
Write-Host '$currentPath = [System.Environment]::GetEnvironmentVariable("Path", "Machine")' -ForegroundColor White
Write-Host '$newPath = "C:\Program Files\Java\jdk-21\bin;$currentPath"' -ForegroundColor White
Write-Host '[System.Environment]::SetEnvironmentVariable("Path", $newPath, "Machine")' -ForegroundColor White
