#README

## Beta - werkende functionaliteit

### Functionaliteit voor versturen

- Bericht kan worden omgezet van string naar binair
- Bericht kan gemoduleerd worden met binary frequency-shift keying (BFSK)
- Het gemoduleerde signaal kan verstuurd worden via de luidspreker

### Functionaliteit voor ontvangen

- Geluid kan opgevangen worden door de microfoon
- Geluid kan gedemoduleerd worden met het algoritme van Goertzel (efficiente DFT)
- Het gedemoduleerde signaal kan als bericht worden herkent, wordt omgezet naar string en op UI getoont

### Persistentie

- Berichten worden opgeslaan

### Werking van de app

- Een bericht wordt ingegeven en bij versturen wordt dit omgezet naar een geluidsignaal
- Het ontvangen gebeurt voorlopig na het indrukken van de "Rec" knop 
- Versturen gaat voorlopig slechts aan **1 bps**!

### Problemen

- Er worden vaak bits teveel gezien tijdens het demoduleren: letter 'a' (110 0001)  wordt gezien als (1100 0001), een bit teveel
- patronen als 010, 101, worden vaak gezien als 000.


### Een geslaagde demodulatie van de letter "t"

![Soundchat](http://f.cl.ly/items/3921343b2m271j1X0V01/Schermafbeelding%202014-04-25%20om%2013.29.59.png)
