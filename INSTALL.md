## Binary Dependencies

The [javax.smartcardio](http://docs.oracle.com/javase/8/docs/jre/api/security/smartcardio/spec/index.html?javax/smartcardio/package-summary.html)
package in the Oracle JDK wraps the SCard API (PC/SC). PCSC-Lite is a Linux
implementation of the API. In Ubuntu derivatives the library is shipped
with the libpcsclite-dev package.

[as root]
```bash
apt-get install libpcsclite-dev
```

## kh-annex

### Build

```bash
mvn clean package
```

### Distribute

Place jar and script to a place of your liking. eg:

[as root]
```bash
export KHA_VERSION="0.0.2" && \
export KHA_PREFIX="/usr/local" && \
rm -rf ${KHA_PREFIX}/kh-annex-${KHA_VERSION} && \
mkdir -p ${KHA_PREFIX}/kh-annex-${KHA_VERSION} && \
cp target/kh-annex-${KHA_VERSION}.jar ${KHA_PREFIX}/kh-annex-${KHA_VERSION} && \
cp src/main/resources/bin/kh-annex.sh ${KHA_PREFIX}/kh-annex-${KHA_VERSION} && \
chmod 755 ${KHA_PREFIX}/kh-annex-${KHA_VERSION}/kh-annex.sh && \
rm -f ${KHA_PREFIX}/kh-annex && \
ln -s ${KHA_PREFIX}/kh-annex-${KHA_VERSION} ${KHA_PREFIX}/kh-annex
```

### PIN setup

No input dialog is shown by the plugin, the pin instead is read from
a specific file (this is the netbank card's pin code by the way).

[as user]
```bash
echo -n 'my pin' > ~/.khpass && chmod 600 ~/.khpass
```

**Important:** Make sure the file contains your pin only - no new line
characters or whatever! The contents are used as is!! Just imagine the
nuisance of getting a replacement card because the current was silently
locked after some failed tries.

### Smartcardio library path

Ensure that the smartcardio library path is correct by running something
similar to the following.. Modify the **sun.security.smartcardio.library**
paramter in the startup script if necessary.

[as user]
```bash
ldconfig --print-cache | grep libpcsclite | awk '{ print $4 }' | xargs dirname
```

## Test run
Now it is safe to test the setup by hand. Plug-in the reader+card,
then run:

[as user]
```bash
/usr/local/kh-annex/kh-annex.sh --dry-run
```

This will test the basic card operations and print some info on the screen.

## Configure Google Chrome Native Messaging Host
[Official doc](https://developer.chrome.com/extensions/nativeMessaging)

[as root]
```bash
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
```

**Note:** make sure the path to the binary reflects your setup. Also, the "name"
and "allowed_origins" values has to match the netbank plugin's expectations,
so just change the path if necessary.

Chrome needs a restart if the setup was done while it was running.
