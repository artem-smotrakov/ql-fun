#!/bin/bash

file=$1
if [ ! -f ${file} ]; then
    echo "[x] ${file} is not a file!"
    exit 1
fi

echo "[+] process ${file}"
for url in `cat $file`
do
    echo "[+] clone ${url}"
    git clone ${url}
done
echo "[+] done"

