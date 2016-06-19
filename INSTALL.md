# Installation

Basically involves three steps (more or less):
- install binary dependencies
- deploy middleware
- install K&H Google Chrome Extension and set up a bridge between the two

## Install binary dependencies

The [javax.smartcardio](http://docs.oracle.com/javase/8/docs/jre/api/security/smartcardio/spec/index.html?javax/smartcardio/package-summary.html)
package in the Oracle JDK wraps the SCard API (PC/SC).
PCSC-Lite is a Linux implementation of the API.
In Ubuntu derivatives the library is shipped with the libpcsclite-dev package.
You also need to install the PC/SC daemon and tools, and the OpenSC smart card tools.

```bash
sudo apt-get install pcscd pcsc-tools opensc libpcsclite-dev
```

## Deploy middleware

### Build

```bash
git clone https://github.com/lukacsd/kh-annex.git /tmp/kh-annex && \
cd /tmp/kh-annex && \
mvn clean package
```

### Distribute

Place jar and script to a place of your liking,
or just use `./src/main/resources/bin/dist.sh` to install it into `/usr/local/kh-annex`.
(`sudo` might be needed.)

### PIN setup

No input dialog is shown by the plugin, the PIN instead is read from
a file under the executing user's home - this is the netbank card's
PIN by the way.

```bash
echo -n 'my pin' > ~/.khpass && chmod 600 ~/.khpass
```

**Important:** Make sure the file contains your PIN only - no new line
characters or whatever! The contents are used as is!! Just imagine the
nuisance of getting a replacement card because the current was locked.

### Smartcardio library path

Ensure that the smartcardio library path is correct. Modify the
**sun.security.smartcardio.library** parameter in the startup script if
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

For defaults, you can just run `KHA_BOOTSTRAP="/usr/local/kh-annex/kh-annex.sh" && sudo ./src/main/resources/bin/config-chrome.sh`.
Just make sure the bootstrap path reflects your setup.

Chrome needs a restart if the setup was done while it was running.
