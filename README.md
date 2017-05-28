[![Build Status][travis-image]][travis-url]



## PAN Discovery

This program is used to find occurrences of PAN (ie. Credit Card numbers) in files and databases.

It is typically used in the context of PCI-DSS assessments to identify storage of card information.

## Requirements

This application requires a Java 8+ Java Runtime Environment.

The build process will generate two different executable jar artefacts:
* pan-discovery-fs-xxx.jar
* pan-discovery-db-xxx.jar



## Use to scan Filesystems

```
java -jar pan-discovery-fs-xxx.jar [--verbose] <folder> [<folder> ...]
```

Execution will create a csv file report in the execution folder like PAN_Discovery_2016-11-11_0956.csv

```
File;Matches;Content Type;Sample Match
./samples/Cards.rtf;1;application/rtf;4783853934638427
./samples/Cards.txt;1;text/plain;4783853934638427
./samples/Cards.pdf;1;application/pdf;4783853934638427
./samples/Cards.zip;1;application/zip;4783853934638427
./samples/Cards.doc;1;application/msword;4783853934638427
./samples/Cards.xls;1;application/vnd.ms-excel;4783853934638427
./samples/Cards.xlsx;1;application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;4783853934638427
./samples/Cards.ods;1;application/vnd.oasis.opendocument.spreadsheet;4783853934638427
./samples/Cards.docx;1;application/vnd.openxmlformats-officedocument.wordprocessingml.document;4783853934638427
./samples/Cards.odt;1;application/vnd.oasis.opendocument.text;4783853934638427
```


[travis-image]: https://travis-ci.org/alcibiade/pan-discovery.svg?branch=master
[travis-url]: https://travis-ci.org/alcibiade/pan-discovery
