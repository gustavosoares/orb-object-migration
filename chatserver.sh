#!/bin/bash

PWD=`pwd`

export CLASSPATH=$PWD/lib/xstream-1.3.jar:xml/:$PWD/
echo "export CLASSPATH=$CLASSPATH"

java ChatServer $1
