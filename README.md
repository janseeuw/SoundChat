#README

## Alpha

### Functionality for sending

- Message converts from string to binary
- Binary message is modulated using frequency-shift keying (FSK)
- The modulated signal is transmitted through the speaker

### Functionality for receiving 

- Sound is captured by the microphone
- Captured sound can be demodulated using Goertzelalgorithm (efficient DFT)
- The demodulated signal kan be recognized as a message, converted to a string and put on UI

### Persistence

- Messages are stored using SQLite


![Soundchat](http://f.cl.ly/items/3921343b2m271j1X0V01/Schermafbeelding%202014-04-25%20om%2013.29.59.png)
