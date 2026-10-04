@echo off
echo Mengkompilasi source code Java...
javac *.java
if %errorlevel% neq 0 (
    echo Kompilasi gagal!
    pause
    exit /b %errorlevel%
)
echo Membuat file executable Calculator.jar...
jar --create --file Calculator.jar --main-class Main *.class
echo Selesai! Anda bisa menjalankan run_calculator.bat atau klik Calculator.jar.
pause
