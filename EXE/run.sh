#!/bin/bash
# Script para ejecutar AffinityFinder JAR con argumentos JVM necesarios

JAR_FILE="AffinityFinder-1.0-all.jar"

if [ ! -f "$JAR_FILE" ]; then
    echo "Error: No se encontró $JAR_FILE"
    echo "Primero ejecuta: ./gradlew shadowJar"
    exit 1
fi

java --add-opens=java.desktop/sun.awt=ALL-UNNAMED \
     --add-opens=java.desktop/java.awt=ALL-UNNAMED \
     --add-opens=java.desktop/sun.java2d=ALL-UNNAMED \
     -jar "$JAR_FILE" "$@"
