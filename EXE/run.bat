@echo off
REM Script para ejecutar AffinityFinder JAR con argumentos JVM necesarios

set JAR_FILE=./AffinityFinder-1.0-all.jar

if not exist %JAR_FILE% (
    echo Error: No se encontró %JAR_FILE%
    echo Primero ejecuta: ./gradlew shadowJar
    pause
    exit /b 1
)

java --add-opens=java.desktop/sun.awt=ALL-UNNAMED ^
     --add-opens=java.desktop/java.awt=ALL-UNNAMED ^
     --add-opens=java.desktop/sun.java2d=ALL-UNNAMED ^
     -jar %JAR_FILE% %*

pause
