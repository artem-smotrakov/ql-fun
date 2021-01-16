#!/bin/bash

rm -rf codeql-db
codeql database create codeql-db --language=java

