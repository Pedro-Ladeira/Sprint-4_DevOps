@echo off
REM run-local.bat - convenience wrapper to run the application locally on Windows (cmd.exe)
REM Usage:
REM   1) Open cmd.exe and optionally set DB vars, or edit this file to hardcode dev values (NOT recommended for production)
REM   2) Run: run-local.bat [profile]
REM Example:
REM   set DB_PASSWORD=yourpassword
REM   run-local.bat postgres

setlocal
if "%1"=="" (
  set PROFILE=postgres
) else (
  set PROFILE=%1
)

:: If you haven't exported DB vars, provide sensible defaults (change to your local DB)
if "%DB_URL%"=="" (
  rem default example (Azure Flexible Server). Replace with your host if needed.
  set DB_URL=jdbc:postgresql://pg-flex-sprint4.postgres.database.azure.com:5432/appdb?sslmode=require
  echo Using default DB_URL: %DB_URL%
) else (
  echo Using DB_URL from environment: %DB_URL%
)
if "%DB_USERNAME%"=="" (
  set DB_USERNAME=pgadmin@pg-flex-sprint4
  echo Using default DB_USERNAME: %DB_USERNAME%
) else (
  echo Using DB_USERNAME from environment: %DB_USERNAME%
)
if "%DB_PASSWORD%"=="" (
  echo ERROR: DB_PASSWORD is not set. Please set DB_PASSWORD environment variable and re-run.
  echo Example: set DB_PASSWORD=yourpassword
  endlocal & exit /b 1
) else (
  echo Using DB_PASSWORD from environment (hidden)
)

:: Sanitize DB_URL: ensure it starts with "jdbc:"
setlocal enabledelayedexpansion
set _url=!DB_URL!
set _prefix=!_url:~0,5!
if /I "%_prefix%"=="jdbc:" (
  rem already good
) else (
  set _p13=!_url:~0,13!
  set _p2=!_url:~0,2!
  if /I "%_p13%"=="postgresql://" (
    set DB_URL=jdbc:%DB_URL%
    echo Prefixed DB_URL with "jdbc:": %DB_URL%
  ) else if "%_p2%"=="//" (
    set DB_URL=jdbc:postgresql:%DB_URL%
    echo Prefixed DB_URL with "jdbc:postgresql:": %DB_URL%
  ) else (
    rem fallback: assume host:port/db and prefix accordingly
    set DB_URL=jdbc:postgresql://%DB_URL%
    echo Prefixed DB_URL with "jdbc:postgresql://": %DB_URL%
  )
)
endlocal & set DB_URL=%DB_URL%

:: Run Maven with explicit Spring datasource properties to avoid missing/incorrect env expansion
mvn spring-boot:run -Dspring-boot.run.profiles=%PROFILE% -Dspring-boot.run.arguments="--spring.datasource.url=%DB_URL% --spring.datasource.username=%DB_USERNAME% --spring.datasource.password=%DB_PASSWORD%"

endlocal
