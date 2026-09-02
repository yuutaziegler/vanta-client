@echo off
title TerentX Client Launcher v2.0
color 09

echo.
echo  ============================================
echo   TerentX Client Launcher v2.0
echo  ============================================
echo.

:: Check for Java
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Java not found!
    echo Please install Java 17 or later from:
    echo https://adoptium.net/
    echo.
    pause
    exit /b 1
)

for /f "tokens=*" %%i in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    echo [OK] %%i
)

echo.

:: Set memory
set MIN_RAM=1024M
set MAX_RAM=2048M

:: Find launcher JAR
set LAUNCHER_JAR=
if exist "build\libs\TerentX-Launcher-2.0.0.jar" (
    set LAUNCHER_JAR=build\libs\TerentX-Launcher-2.0.0.jar
) else if exist "TerentX-Launcher-2.0.0.jar" (
    set LAUNCHER_JAR=TerentX-Launcher-2.0.0.jar
) else (
    echo [BUILD] Launcher JAR not found, building...
    call gradlew jar
    if exist "build\libs\TerentX-Launcher-2.0.0.jar" (
        set LAUNCHER_JAR=build\libs\TerentX-Launcher-2.0.0.jar
    )
)

if "%LAUNCHER_JAR%"=="" (
    echo [ERROR] Could not find launcher JAR!
    echo.
    pause
    exit /b 1
)

echo [OK] Launcher found: %LAUNCHER_JAR%
echo.
echo [INFO] Memory: %MIN_RAM% - %MAX_RAM%
echo.

:: Create config directory
if not exist "%USERPROFILE%\.terentx-launcher" (
    mkdir "%USERPROFILE%\.terentx-launcher"
)

echo [INFO] Starting TerentX Launcher...
echo.

:: Launch
java -Xms%MIN_RAM% -Xmx%MAX_RAM% -Dterentx.launcher.version=2.0.0 -Dterentx.launcher.dir="%USERPROFILE%\.terentx-launcher" -jar "%LAUNCHER_JAR%"

set EXIT_CODE=%errorlevel%

echo.
if %EXIT_CODE% equ 0 (
    echo [OK] Launcher closed successfully!
) else (
    echo [ERROR] Launcher exited with code: %EXIT_CODE%
)

echo.
pause
exit /b %EXIT_CODE%
