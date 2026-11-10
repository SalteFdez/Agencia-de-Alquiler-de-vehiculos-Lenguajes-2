#!/bin/bash
# Compila y abre el kiosco de autoservicio sin necesitar Maven (solo el JDK).
cd "$(dirname "$0")"
rm -rf out && mkdir out
javac -encoding UTF-8 -d out $(find src/main -name '*.java') && java -cp out vista.MainKiosco
