@echo off
:: Muda a página de código do console para UTF-8 para suportar emojis
chcp 65001 >nul
setlocal enabledelayedexpansion

:: ==============================================================================
:: WHISKER CLI - PURE JAVAC RUNNER (UTF-8)
:: ==============================================================================

set "PROJECT_ROOT=%~dp0"
set "CLI_DIR=%PROJECT_ROOT%CLI"
set "SRC_DIR=%CLI_DIR%\src\main\java"
set "RES_DIR=%CLI_DIR%\src\main\resources"
set "BIN_DIR=%CLI_DIR%\bin"
set "JAR_PATH=%CLI_DIR%\target\whisker-cli.jar"
set "MAIN_CLASS=dev.whisker.cli.Main"

:: Limpa o console
cls

echo [🐱 Whisker] Starting...

:: 1. COMPILAÇÃO
echo [🔨] Compiling...

if not exist "%BIN_DIR%" mkdir "%BIN_DIR%"
del /q /s "%BIN_DIR%\*" >nul 2>&1

set "SOURCES_FILE=%TEMP%\whisker_sources.txt"
dir /s /b "%SRC_DIR%\*.java" > "%SOURCES_FILE%"

set "CP="
if exist "%JAR_PATH%" (
    set "CP=-cp "%JAR_PATH%""
)

:: Compila forçando UTF-8 nos fontes
javac -encoding UTF-8 -d "%BIN_DIR%" %CP% @"%SOURCES_FILE%"

if %errorlevel% neq 0 (
    echo.
    echo [❌] Build failed!
    del "%SOURCES_FILE%" 2>nul
    pause
    exit /b %errorlevel%
)
del "%SOURCES_FILE%" 2>nul

:: 2. RECURSOS
if exist "%RES_DIR%" (
    xcopy /s /e /y "%RES_DIR%\*" "%BIN_DIR%\" >nul 2>&1
)

echo [✅] Build ready.
echo.

:: 3. EXECUÇÃO
set "RUN_CP=%BIN_DIR%"
if exist "%JAR_PATH%" (
    set "RUN_CP=%BIN_DIR%;%JAR_PATH%"
)

:: Roda com flags de encoding para garantir emojis na saída
java -Dfile.encoding=UTF-8 -cp "%RUN_CP%" %MAIN_CLASS% %*
