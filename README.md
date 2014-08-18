#README

## Alpha

### Functionality for sending

- Message converts from string to binary
- Binary message is modulated using frequency-shift keying (FSK)
- The modulated signal is transmitted through the speaker

### Functionality for receiving 

- Sound is captured by the microphone
- Captured sound can be demodulated using Goertzel algorithm (efficient DFT)
- The demodulated signal can be recognized as a message, converted to a string and put on UI

### Persistence

- Messages are stored using SQLite


![Soundchat](http://f.cl.ly/items/3921343b2m271j1X0V01/Schermafbeelding%202014-04-25%20om%2013.29.59.png)

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## Author

[Jonas Anseeuw](http://jns.me)

## License

Copyright © 2014 Jonas Anseeuw

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
