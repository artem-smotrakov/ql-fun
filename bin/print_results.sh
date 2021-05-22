#!/bin/bash

rows=$(cat codeql-results/results.sarif | jq -r '.runs[].results[] | @base64')
for row in $rows; do
    _for_row() {
        echo ${row} | base64 --decode | jq -r ${1}
    }
    message=$(_for_row '.message.text')
    rule_id=$(_for_row '.ruleId')
    echo "[+] $message"
    echo "    rule: $rule_id"
    locations=`echo $row | base64 --decode | jq -r '.locations[] | @base64'`
    for location in $locations; do
        _for_location() {
            echo ${location} | base64 --decode | jq -r ${1}
        }
        path=$(_for_location '.physicalLocation.artifactLocation.uri')
        project=$(git config --get remote.origin.url | cut -d ":" -f 2 | cut -d "." -f 1)
        line_number=$(_for_location '.physicalLocation.region.startLine')
        url="https://gihub.com/$project/tree/master/$path#L$line_number"
        echo "    $url"
    done
done

