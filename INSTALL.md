### Build

```bash
mvn clean package
```

### Distribute

Place jar and script to a place of your liking. eg:

[as root]
```bash
mkdir -p /usr/local/kh-annex-0.0.1 && \
cp target/kh-annex-0.0.1.jar /usr/local/kh-annex-0.0.1 && \
cp src/main/resources/bin/kh-annex.sh /usr/local/kh-annex-0.0.1 && \
chmod 755 /usr/local/kh-annex-0.0.1/kh-annex.sh && \
ln -s /usr/local/kh-annex-0.0.1 /usr/local/kh-annex
```

No password dialog is shown by the plugin, the pin instead is read from
a specific file (this is the netbank card's pin code by the way).

[as user]
```bash
echo -n 'my pin' > ~/.khpass && chmod 600 ~/.khpass
```

**Important:** Make sure the file contains your password only - no new line
characters or whatever! The contents are used as is!! Just imagine the
nuisance of getting a replacement card because the current was silently
locked after some failed tries.

### Test run
Now it is safe to test the setup by hand. Plug-in the reader+card,
then run:

[as user]
```bash
/usr/local/kh-annex/kh-annex.sh --dry-run
```

This will test the basic card operations and print some info on the screen.

### Configure Google Chrome Native Messaging Host
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
