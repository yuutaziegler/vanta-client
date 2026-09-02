@echo off
REM ============================================================
REM  Vanta Client - rebuild the mod after the full UI rewrite
REM  Requires: JDK 21 (JAVA_HOME set), internet (first run only)
REM ============================================================
setlocal
cd /d "%~dp0"

echo.
echo === [1/3] Cleaning old build...
call gradlew.bat clean --no-daemon
if errorlevel 1 (
  echo ERROR: gradle clean failed. Make sure JDK 21 is installed and JAVA_HOME points to it.
  pause
  exit /b 1
)

echo.
echo === [2/3] Building mod jar (this downloads Minecraft + Fabric on first run)...
call gradlew.bat build -x test --no-daemon
if errorlevel 1 (
  echo ERROR: gradle build failed.
  pause
  exit /b 1
)

echo.
echo === [3/3] Copying fresh jar into build\libs\ as the release jar...
copy /y "build\libs\terentx-v0.1.jar" "build\libs\vanta-client-2.1.0.jar" >nul

echo.
echo ============================================================
echo  DONE.
echo  Install these into %%appdata%%\.minecraft\mods\ :
echo    - build\libs\terentx-v0.1.jar   (or vanta-client-2.1.0.jar)
echo    - build\libs\fabric-api-0.138.0+1.21.10.jar
echo  IMPORTANT: delete the OLD terentx-v0.1.jar / vanta-client
echo  from your mods folder first so you don't load both.
echo ============================================================
pause
