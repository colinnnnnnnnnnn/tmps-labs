#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROD_DIR="$SCRIPT_DIR/out/production/lab-2"
TEST_DIR="$SCRIPT_DIR/out/test/lab-2"

mkdir -p "$PROD_DIR" "$TEST_DIR"
javac -d "$PROD_DIR" $(find "$SCRIPT_DIR/src" -name "*.java")
javac -cp "$PROD_DIR" -d "$TEST_DIR" $(find "$SCRIPT_DIR/test" -name "*.java")
java -cp "$PROD_DIR:$TEST_DIR" com.tmps.lab2.test.TestRunner "$@"
