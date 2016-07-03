#!/bin/bash

KHA_VERSION="0.0.5"
JVM_ARGS="-Dsun.security.smartcardio.library=/lib/x86_64-linux-gnu/libpcsclite.so.1"
JVM_ARGS="${JVM_ARGS} -Dorg.khannex.io.endianness=LITTLE_ENDIAN"
JVM_ARGS="${JVM_ARGS} -Dorg.khannex.logDir=/var/tmp/"

cd `readlink -f $0 | xargs dirname` && \
java $JVM_ARGS -jar kh-annex-${KHA_VERSION}.jar "$@"
