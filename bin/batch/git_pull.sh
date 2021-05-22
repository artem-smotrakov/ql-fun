#!/bin/bash

for org in $(ls)
do
    if [ -d ${org} ]; then
        echo "found organization '${org}'"
	cd ${org}
	for project in $(ls)
	do
	    if [ -d ${project} ]; then
                echo "found project '${project}'"
		cd ${project}
		git pull
		cd ..
	    fi
	done
	cd ..
    fi
done

