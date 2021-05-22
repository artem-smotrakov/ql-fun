#!/bin/bash

QUERIES=${1:-"src/main/ql/java"}

if [ "$QUERIES" = "" ]; then
    echo "[x] hey! give me a path to queries!"
    exit 1
fi

if [ ! -e $QUERIES ]; then
    echo "[x] '$QUERIES' does not exist!"
    exit 1
fi

if [ ! -d codeql-db ]; then
    echo "[x] codeql-db does not exist!"
    exit 1
fi

mkdir -p codeql-results > /dev/null 2>&1

echo "--> run queries from $QUERIES"
codeql database analyze codeql-db $QUERIES --format=sarif-latest --output=codeql-results/results.sarif

if [ $? != 0 ]; then
    echo "[x] something went wrong"
    exit 1
fi

echo "    okay, the queries have been successfully run"

n=$(cat codeql-results/results.sarif | jq '.runs[0].results | length')
if [ $n -ge 0 ]; then
    echo "[+] looks like we found something!"
fi

