# A note on the card terminal

I believe, majority of the customers were provided with an
[OMNIKEY AG CardMan 3021](https://www.hidglobal.com/products/readers/omnikey/3021-usb).
This terminal is [supported](http://pcsclite.alioth.debian.org/ccid/shouldwork.html#0x076B0x3021)
by the Linux SmartCard tools. (Even though it's in the "should
work" section).

Nevertheless, compatibility can be checked at http://pcsclite.alioth.debian.org/select_readers

In case your terminal is not supported, you will need to manually
install a driver. Unfortunately, can't cover those instructions
here as it is some highly specific scenario. If you are determined
to use the kh-annex tool, I would say don't worry about it for now.
Below install has to be done anyway, just proceed with the steps
and shake out the system with a dry-run at the end.

# Installation

This will basically involve three steps:
- install binary dependencies
- deploy middleware
- install K&H Google Chrome Extension and set up a bridge between the two

## Install binary dependencies

The [javax.smartcardio](http://docs.oracle.com/javase/8/docs/jre/api/security/smartcardio/spec/index.html?javax/smartcardio/package-summary.html)
JDK package wraps the SCard API (PC/SC). You will need to install a
PC/SC implementation - PCSC-Lite, along with some smart card tools and
utilities.

```bash
sudo apt-get install pcscd pcsc-tools opensc
```

## Deploy middleware

### Build

```bash
git clone https://github.com/lukacsd/kh-annex.git /tmp/kh-annex && \
cd /tmp/kh-annex && \
mvn clean package
```

### Distribute

Place jar and script to a place of your liking, for example:

```bash
export KHA_PREFIX="/usr/local" && sudo --preserve-env src/main/resources/bin/dist.sh
```

(installs to `/usr/local/kh-annex`)

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

Make sure the bootstrap path reflects your setup (from the distribute step) and execute:

```bash
export KHA_BOOTSTRAP="/usr/local/kh-annex/kh-annex.sh" && sudo --preserve-env src/main/resources/bin/config-chrome.sh
```

Chrome needs a restart if the setup was done while it was running.
