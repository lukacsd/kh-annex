# Installation

Basically involves three steps (more or less):
- install binary dependencies
- deploy middleware
- install K&H Google Chrome Extension and set up a bridge between the two

## Install binary dependencies

First make sure that you have the driver installed, e.g. from https://www.hidglobal.com/drivers.

Also make sure you installed the PC/SC daemon and tools, e.g.:

```bash
sudo apt-get install pcscd pcsc-tools opensc
```

The [javax.smartcardio](http://docs.oracle.com/javase/8/docs/jre/api/security/smartcardio/spec/index.html?javax/smartcardio/package-summary.html)
package in the Oracle JDK wraps the SCard API (PC/SC). PCSC-Lite is a Linux
implementation of the API. In Ubuntu derivatives the library is shipped
with the libpcsclite-dev package.

```bash
sudo apt-get install libpcsclite-dev
```

## Deploy middleware

### Build

```bash
git clone https://github.com/lukacsd/kh-annex.git /tmp/kh-annex && \
cd /tmp/kh-annex && \
mvn clean package
```

### Distribute

Place jar and script to a place of your liking. e.g.:

```bash
KHA_VERSION="0.0.3" && \
KHA_PREFIX="/usr/local" && \
sudo -s -- << __SUDO__
rm -rf ${KHA_PREFIX}/kh-annex-${KHA_VERSION} && \
mkdir -p ${KHA_PREFIX}/kh-annex-${KHA_VERSION} && \
cp target/kh-annex-${KHA_VERSION}.jar ${KHA_PREFIX}/kh-annex-${KHA_VERSION} && \
cp src/main/resources/bin/kh-annex.sh ${KHA_PREFIX}/kh-annex-${KHA_VERSION} && \
chmod 755 ${KHA_PREFIX}/kh-annex-${KHA_VERSION}/kh-annex.sh && \
rm -f ${KHA_PREFIX}/kh-annex && \
ln -s ${KHA_PREFIX}/kh-annex-${KHA_VERSION} ${KHA_PREFIX}/kh-annex
__SUDO__
```

Or you can just run `./src/main/resources/bin/dist.sh`.

### PIN setup

No input dialog is shown by the plugin, the pin instead is read from
a file under the executing user's home - this is the netbank card's
pin by the way.

```bash
echo -n 'my pin' > ~/.khpass && chmod 600 ~/.khpass
```

**Important:** Make sure the file contains your pin only - no new line
characters or whatever! The contents are used as is!! Just imagine the
nuisance of getting a replacement card because the current was locked.

### Smartcardio library path

Ensure that the smartcardio library path is correct. Modify the
**sun.security.smartcardio.library** paramter in the startup script if
necessary.

```bash
ldconfig --print-cache | grep libpcsclite | awk '{ print $4 }'
```

### Test run

To test the basic card operations and print some info on the screen,
plug in the reader+card and execute:

```bash
/usr/local/kh-annex/kh-annex.sh --dry-run
```

## Install K&H Google Chrome Extension

Follow the instructions on the K&H website.

## Configure Google Chrome Native Messaging Host

[Official doc](https://developer.chrome.com/extensions/nativeMessaging)

```bash
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
```

**Note:** make sure the path to the binary reflects your setup. Also, the "name"
and "allowed_origins" values has to match the netbank plugin's expectations,
so just change the path if necessary.

For defaults, you can just run `./src/main/resources/bin/config-chrome.sh`.

Chrome needs a restart if the setup was done while it was running.
