## Smart Card signing utility for K&H e-bank

This project demonstrates on how to sign data with an Oberthur Authentic smart card to comply the semantics of K&H Hungary internet banking system. In essence, it can be used as a drop in replacement for the middleware provided by the institution.

Hang on, why should anyone replace the official binaries?? Ok, there is no reason to tinker with them unless you are running something other than windows.

There are some caveats though:
- Only the vital 'who-does-this-card-belong-to' and 'card-prove-yourself' commands are implemented. This basically allows a person to log in and sign pending transactions. Any other fluff coming through the pipe is just acknowledged but swallowed.
- The software is a proof-of-concept: there is no reasonable error handling for example, everything is hard-coded etc.
- It has been tested on one computer, with one type of card reader and one instance of smart card
- Works with Google Chrome only

Long story short: **No warranty of any kind. It is encouraged to see this project as a curiosity only.**

I have used the following:
- pcsclite    1.8.14
- Java        1.8.0_74
- Chrome      47.0.2526.106
- Hid Omnikey 3021
- Ebank card issued in late 2015

### Installation
For instructions refer to the [guide](INSTALL.md).

### License

Released under [Apache License v2](http://www.apache.org/licenses/LICENSE-2.0.html).
