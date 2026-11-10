#!/bin/bash
# Prueba automática de la interfaz: abre el operario y el kiosco a la vez, hace clic
# en los botones y deja capturas en pruebas-gui/capturas. No toca tu agencia.dat:
# trabaja sobre una copia de entrega/agencia-demo.dat en una carpeta temporal.
set -e
PROYECTO="$(cd "$(dirname "$0")/.." && pwd)"
TMP="$(mktemp -d)"
cp "$PROYECTO/entrega/agencia-demo.dat" "$TMP/agencia.dat"
cp -r "$PROYECTO/imagenes" "$TMP/imagenes"
mkdir "$TMP/out"
javac -encoding UTF-8 -d "$TMP/out" $(find "$PROYECTO/src/main" -name '*.java') "$PROYECTO/pruebas-gui/E2E.java"
cd "$TMP" && java -cp out E2E
rm -rf "$PROYECTO/pruebas-gui/capturas" && mv "$TMP/capturas" "$PROYECTO/pruebas-gui/capturas"
echo "Capturas en pruebas-gui/capturas"
