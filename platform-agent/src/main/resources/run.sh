#!/usr/bin/env bash
# 执行脚本

repo='script'
url='https://localhost:8083/'
fileName=$1
threadNum=$2
connNum=$3
elapsed=$4
url=$5
uploadUrl=$6
errorUrl=""

if [ ! -d $repo]; then
　　git clone $url:$repo
else
   cd $repo|git fetch origin master
fi
if [ ! -f "$fileName" ]; then
   curl -XPOST $errorUrl -d'{}' |exit 1
fi

wrk -c$connNum -t$threadNum -d$elapsed --latency -T8s -s $fileName $url|curl -XPOST $uploadUrl -F"file=/report/log.html"
