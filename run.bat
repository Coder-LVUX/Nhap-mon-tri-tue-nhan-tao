@echo off
chcp 65001 >nul
cd /d "%~dp0"
java -jar "%~dp0target\maze-search-1.0.0.jar"
if errorlevel 1 pause
