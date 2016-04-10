#!/bin/bash

WORK_DIR=`readlink -f $0`
cd `dirname "$WORK_DIR"`

JVM_ARGS="-Dsun.security.smartcardio.library=/lib/x86_64-linux-gnu/libpcsclite.so.1"
JVM_ARGS="${JVM_ARGS} -Dorg.khannex.io.endianness=LITTLE_ENDIAN"
JVM_ARGS="${JVM_ARGS} -Dorg.khannex.logDir=/var/tmp/"

java $JVM_ARGS -jar kh-annex-0.0.2.jar "$@"
