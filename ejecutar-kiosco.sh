#!/bin/bash
# Compila y abre el kiosco de autoservicio sin necesitar Maven (solo el JDK).
cd "$(dirname "$0")"
rm -rf out-kiosco && mkdir out-kiosco
javac -encoding UTF-8 -d out-kiosco $(find src/main -name '*.java') && java -cp out-kiosco vista.MainKiosco
