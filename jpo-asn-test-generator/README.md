# Test Generator

This is a Java command line utility for generating test messages in XER and JER encodings.  It is capable of generating examples of all the message types defined in the J2735/2024 specification.

It generates syntactically correct ASN.1 JER and XER messages that observe the constraints as defined in the specification. But note that the content of the messages is random and may not be semantically valid.  For example, a `MapData` message generated with this tool will be syntactically valid ASN.1, but may contain `GenericLane`s with nodes with random coordinates that don't describe geographically meaningful lanes.

## Prerequisites

See the [repository compatibility matrix](../README.md#prerequisites) for the current JDK, Gradle, and Maven versions. This module is built with Gradle from the commands below.

## Build

To build the cli tool:

```bash
cd jpo-asn-test-generator
./gradlew clean build
```

Then the executable named `testgen-cli.jar` will be available in the `build/libs` directory.

## Test

To run the unit tests:

```bash
./gradlew test
```

View the test coverage report at `build/reports/jacoco/test/html/index.html` in a browser.

## Usage

```
Usage: java -jar testgen-cli [-rhV] -m=<module> -p=<pdu> [-xo]
                             [-io=<includeOptional>]... [-xp=<excludePdus>]...
                             [-s=<sequenceOfLimit>] [-x=<xerOutputFile>]
                             [-j=<jerOutputFile>]
  -m, --module=<module>   REQUIRED. ASN.1 Module name.  For example: MapData,
                            Common.
  -p, --pdu=<pdu>         REQUIRED. Protocol Data Unit (PDU).  Name of the
                            class to generate an example of. For example:
                            MapData, BSMcoreData.
  -xo, --exclude-optional
                      Exclude all optional properties from SEQUENCE types.
                        All properties except 'regional' are included if
                        this flag is not present.
  -io, --include-optional=<includeOptional>
                      List of names of optional SEQUENCE properties to
                        include.  If set implies '-xo' and excludes other
                        optional properties not listed.  For example, use
                        '-io=intersections' with MapData to generate a MAP
                        with only an 'intersections' element.
  -xp, --exclude-pdus=<excludePdus>
                      Module.PDUs to exclude from sequences (class name eg.
                        'MapData.PreemptPriorityList',  'REGION.
                        Reg_MovementEvent')
  -s, --sequence-of-limit=<sequenceOfLimit>
                          Limit the number of items in SEQUENCE-OF types.
                            Default 5. Must be at least 2.
  -x, --xer-output-file=<xerOutputFile>
                          Output file path for the XER file.
  -j, --jer-output-file=<jerOutputFile>
                          Output file path for the JER file.
  -r, --regional          Include fields named 'regional' and other regional
                            extensions. Omitted by default if this flag is not
                            present.
  -h, --help              Show this help message and exit.
  -V, --version           Print version information and exit.
```

### Usage Examples

Generate a MovementEvent and display the output XER and JER on stdout:

```bash
java -jar testgen-cli.jar -m SPAT -p MovementEvent
```

Generate a SPAT message, and save the XER and JER to files:

```bash
java -jar testgen-cli.jar -m SPAT -p SPAT -x spat.xml -j spat.json
```

Generate a BSMcoreData type:

```bash
java -jar testgen-cli.jar -m Common -p BSMcoreData
```

Generate a BSM message with no optional elements:

```bash
java -jar testgen-cli.jar -m BasicSafetyMessage -p BasicSafetyMessage -xo
```

Generate a MAP message with only the 'intersections' and 'timeStamp' optional properties:

```bash
java -jar testgen-cli.jar -m MapData -p MapData -io timeStamp -io intersections
```

Generate a NodeSetXY with no optional elements and up to 20 nodes:

```bash
java -jar testgen-cli.jar -m Common -p NodeSetXY -xo -s 20
```



