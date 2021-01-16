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
rm -rf db.success db.failure > /dev/null 2>&1

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
                if [ -f create_codeql_db.sh ]; then
                    echo "[+] running local create_codeql_db.sh ..."
                    bash create_codeql_db.sh > create_codeql_db.log 2>&1
                else
                    echo "[+] running the default create_codeql_db.sh ..."
                    bash $QL_FUN/bin/create_codeql_db.sh > create_codeql_db.log 2>&1
                fi
                if [ $? != 0 ]; then
                   echo "[!] something went wrong ..."
                   pwd >> $WS/db.failure
                else
                    echo "[+] okay"
                    pwd >> $WS/db.success
                fi
		cd ..
	    fi
	done
	cd ..
    fi
done
cd ..

