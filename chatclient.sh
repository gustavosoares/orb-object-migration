#!/bin/bash

PWD=`pwd`

export CLASSPATH=$PWD/lib/xstream-1.3.jar:xml/:$PWD/:$PWD/bin
echo "export CLASSPATH=$CLASSPATH"

java ChatClient $1
