#!/bin/bash
sudo -s -- << __SUDO__
mkdir -p /etc/opt/chrome/native-messaging-hosts/ && \
cat > /etc/opt/chrome/native-messaging-hosts/com.khb.auth.plugin.chrome.json <<- __EOF__
{
  "name": "com.khb.auth.plugin.chrome",
  "description": "K&H Chrome NMApp",
  "path": "/usr/local/kh-annex/kh-annex.sh",
  "type": "stdio",
  "allowed_origins": [
    "chrome-extension://glagoleiomgpoinafhokfapdjbogdeko/",
    "chrome-extension://bohjgijjdhjamfonbbbdgpfcajaokdcg/"
  ]
}
__EOF__
__SUDO__
