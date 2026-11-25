#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BUILD_DIR="$SCRIPT_DIR/out/production/lab-3"

mkdir -p "$BUILD_DIR"
javac -d "$BUILD_DIR" $(find "$SCRIPT_DIR/src" -name "*.java")
java -cp "$BUILD_DIR" com.tmps.lab3.client.Main "$@"
