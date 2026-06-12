@echo off
REM Double-click to start the Plane Finder dev server.
REM Node isn't on this machine's PATH, so we point at the portable install.
set "PATH=C:\Users\Patrick Villacojer\AppData\Local\nodejs-portable\node-v24.16.0-win-x64;%PATH%"
cd /d "%~dp0"
echo Starting Plane Finder dev server...
echo Open http://localhost:5173 on this PC, or the Network URL below on your phone.
echo Press Ctrl+C to stop.
echo.
call npm run dev
pause
