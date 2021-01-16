#!/bin/bash

QL_FUN=${QL_FUN:-`pwd`}
WS=$1

if [ "$WS" = "" ]; then
    echo "[x] hey! give me a path!"
    exit 1
fi

if [ ! -d $WS ]; then
    echo "[x] '$WS' is not a directory"
    exit 1
fi

echo "[+] looking for projects in $WS"

cd $WS
WS=$(pwd) # absolute path

for org in `ls $WS`
do
    if [ -d $org ]; then
        echo "[+] found organization '$org'"
	cd $org
	for project in $(ls)
	do
	    if [ -d $project ]; then
                echo "[+] found project '$project'"
		cd $project
                bash $QL_FUN/bin/print_results.sh
		cd ..
	    fi
	done
	cd ..
    fi
done
cd ..

