#!/bin/bash

if [ -z "$KHA_BOOTSTRAP" ]; then KHA_BOOTSTRAP="/usr/local/kh-annex/kh-annex.sh"; fi

mkdir -p /etc/opt/chrome/native-messaging-hosts/ && \
cat > /etc/opt/chrome/native-messaging-hosts/com.khb.auth.plugin.chrome.json <<- __EOF__
{
  "name": "com.khb.auth.plugin.chrome",
  "description": "K&H Chrome NMApp",
  "path": "$KHA_BOOTSTRAP",
  "type": "stdio",
  "allowed_origins": [
    "chrome-extension://glagoleiomgpoinafhokfapdjbogdeko/",
    "chrome-extension://bohjgijjdhjamfonbbbdgpfcajaokdcg/"
  ]
}
__EOF__
