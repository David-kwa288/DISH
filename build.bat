@echo off
echo Building Celebrity Catalog Project...

:: Set project directory variables
set SRC_DIR=src
set OUT_DIR=out\production\CelebrityCatalog
set MAIN_CLASS=main.java.Run

:: Create output directory if it doesn’t exist
if not exist %OUT_DIR% mkdir %OUT_DIR%

:: Compile all Java files
javac -d %OUT_DIR% %SRC_DIR%\view\*.java %SRC_DIR%\controller\*.java %SRC_DIR%\model\*.java

:: Check if compilation was successful
if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    exit /b %ERRORLEVEL%
)

echo Compilation successful!

:: main.java.Run the program
echo Running the program...
java -cp %OUT_DIR% %MAIN_CLASS%


