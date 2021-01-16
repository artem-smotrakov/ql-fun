#!/bin/bash

QL_FUN=${QL_FUN:-`pwd`}
WS=$1
QUERIES=$2

if [ "$WS" = "" ]; then
    echo "[x] hey! give me a path to workspace!"
    exit 1
fi

if [ ! -d $WS ]; then
    echo "[x] '$WS' is not a directory!"
    exit 1
fi

if [ "$QUERIES" = "" ]; then
    echo "[x] hey! give me a path to queries!"
    exit 1
fi

if [ ! -f $QUERIES ]; then
    echo "[x] '$QUERIES' does not exist!"
    exit 1
fi

echo "[+] looking for projects in $WS"

cd $WS
WS=$(pwd) # absolute path

for path in $(cat db.success)
do
    echo "[+] run queries in $path"
    cd $path
    bash ${QL_FUN}/bin/analyze.sh $QUERIES
    cd ..
done
echo "[+] done"

