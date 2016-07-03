#!/bin/bash

KHA_VERSION="0.0.5"
if [ -z "$KHA_PREFIX" ]; then KHA_PREFIX="/usr/local"; fi

rm -rf ${KHA_PREFIX}/kh-annex-${KHA_VERSION} && \
mkdir -p ${KHA_PREFIX}/kh-annex-${KHA_VERSION} && \
cp target/kh-annex-${KHA_VERSION}.jar ${KHA_PREFIX}/kh-annex-${KHA_VERSION} && \
cp src/main/resources/bin/kh-annex.sh ${KHA_PREFIX}/kh-annex-${KHA_VERSION} && \
chmod 644 ${KHA_PREFIX}/kh-annex-${KHA_VERSION}/kh-annex-${KHA_VERSION}.jar && \
chmod 755 ${KHA_PREFIX}/kh-annex-${KHA_VERSION}/kh-annex.sh && \
rm -f ${KHA_PREFIX}/kh-annex && \
ln -s ${KHA_PREFIX}/kh-annex-${KHA_VERSION} ${KHA_PREFIX}/kh-annex
