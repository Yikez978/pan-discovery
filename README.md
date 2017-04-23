
## PAN Discovery

This program is used to find occurrences of PAN (ie. Credit Card numbers) in files and databases.

It is typically used in the context of PCI-DSS assessments to identify storage of card information.

## Requirements

This application requires a Java 8+ Java Runtime Environment.

## Use to scan Filesystems

```
java -jar pan-discovery-xxx.jar [--verbose] <folder> [<folder> ...]
```

Execution will create a csv file report in the execution folder like PAN_Discovery_2016-11-11_0956.csv

```
samples\Cards.doc;1
samples\Cards.docx;1
samples\Cards.ods;1
samples\Cards.odt;1
samples\Cards.pdf;1
samples\Cards.rtf;1
samples\Cards.txt;1
samples\Cards.xls;1
samples\Cards.xlsx;1
samples\Cards.zip;1
```