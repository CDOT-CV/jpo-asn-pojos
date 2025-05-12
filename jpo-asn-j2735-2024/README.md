# J2735/2024 POJOs

This is a Java library with the 2024 POJOs.

## Scope of the J2735/2024 specification

The J2735/2024 specification can be downloaded for free from here: https://www.sae.org/standards/content/j2735_202409/. The standard is organized as a set of messages which are transmitted in message frames.  Each message is defined in an ASN.1 module, which includes a top-level message type plus "data frame" and "data element" types.  In many cases, the modules are interrelated and import data elements from other modules. This implementation includes Java classes for all the message types in the standard and all the data frames and data elements that the messages depend on. 

The download bundle for the specification includes a set of ASN.1 files and a PDF document. The ASN.1 files contain a complete specification of the machine-readable data encodings. The PDF document contains human-readable normative and informative text describing how to interpret and use the messages. The POJOs in this repository were generated directly and solely from the ASN.1 files, using a new ASN.1 compiler.  The structure of Java packages in this implementation directly reflects the module structure and naming in the ASN.1 files.  The additional human-readable text and comments in the documentation was not included in the generated POJOs in any way, as the Java classes here are solely a strict implementation of the ASN.1 data structures.  Many of the messages in the standard have additional documentation available in other related standards, and some are placeholders for future use, as noted below.  

The J2735/2024 standard includes the following message types.

* **MessageFrame (FRAME)**
* **BasicSafetyMessage (BSM)**
* **CommonSafetyRequest (CSR)**
* **CooperativeControlMessage (CCM)**
  * See also [SAE J2945/6](https://www.sae.org/standards/content/j2945/6_202310/)
* **EmergencyVehicleAlert (EVA)**
* **IntersectionCollisionAvoidance (ICA)**
* **ManeuverSharingAndCoordinatingMessage (MSCM)**
  * See also [SAE J3186](https://www.sae.org/standards/content/j3186_202303/)
* **MapData (MAP)**
* **NMEAcorrections (NMEA)**
* **PersonalSafetyMessage (PSM)**
* **PersonalSafetyMessage2 (PSM2)** 
  * Placeholder for future use
* **ProbeDataConfigMessage (PDC)**
  * See also [SAE J2945/C](https://www.sae.org/standards/content/j2945/c_202206/)
* **ProbeDataManagement (PDM)**
* **ProbeDataReportMessage (PDR)**
  * See also [SAE J2945/C](https://www.sae.org/standards/content/j2945/c_202206/)
* **ProbeVehicleData (PVD)**
* **RoadGeometryAndAttributes (RGA)** 
  * Placeholder for future use
* **RoadSafetyMessage (RSM)** 
  * See also [SAE J2945/4](https://www.sae.org/standards/content/j2945/4_202305/).  Note that the `J2735_202409.pdf` document states that RSMs are for "future use".  This appears to be a typo because the complete ASN.1 specification for them is included in the download bundle.  This repository contains a complete implementation of RSMs in Java.
* **RoadSideAlert (RSA)**
* **RoadUserChargingConfigMessage (RUCCM)**
  * See also [SAE 3217/R](https://www.sae.org/standards/content/j3217/r_202401/)
* **RoadUserChargingReportMessage (RUCRM)**
  * See also [SAE 3217/R](https://www.sae.org/standards/content/j3217/r_202401/)
* **RoadWeatherMessage (RWM)**
  * See also [SAE 2945/3](https://www.sae.org/standards/content/j2945/3_202201/)
* **RTCMcorrections (RTCM)**
* **SensorDataSharingMessage (SDSM)**
  * See also [SAE J3224](https://www.sae.org/standards/content/j3224_202208/)
* **SignalControlAndPrioritizationRequest (SCPR)**
  * Placeholder for future use
* **SignalControlAndPrioritizationStatus (SCPS)**
  * Placeholder for future use
* **SignalPhaseAndTiming (SPAT)**
* **SignalRequestMessage (SRM)**
* **SignalStatusMessage (SSM)**
* **TollAdvertisementMessage (TAM)**
  * See also [SAE J3217](https://www.sae.org/standards/content/j3217_202206/)
* **TollUsageAckMessage (TUMack)**
  * See also [SAE J3217](https://www.sae.org/standards/content/j3217_202206/)
* **TollUsageMessage (TUM)**
  * See also [SAE J3217](https://www.sae.org/standards/content/j3217_202206/)
* **TrafficLightStatusMessage (TLSM)**
* **TrafficSignalPhaseAndTiming (TSPAT)**
  * Placeholder for future use
* **TravelerInformation Message (TIM)**
* **TestMessages 00-15**


  
## Code Generation

The POJOs in this subproject were generated from the downloaded ASN.1 files using using ASN1JVM, which is a new ASN.1 compiler targeting Java developed by [Neaera Consulting](https://www.neaeraconsulting.com/)

## Design notes

### Goals

The goal of this library is to be a complete implementation of the J2735/2024 specification in Java.

The design philosophy was to try to keep it simple, but to also be complete and not to arbitrarily remove anything from the standard.

The implementation consists of a set of classes organized into a simple class hierarchy according to ASN.1 type, with Jackson and custom annotations added to facilitate serialization/deserialization.  Generics are used to implement ASN.1's parameterized extensibility mechanism. In most cases there is a one-to-one correspondence between types defined in the ASN.1 specification and the Java classes in this implementation.

### Non Goals

Despite this repository having "pojos" in its name, and documentation referring to the Java classes as "POJOS", it was not a design goal to make the classes conform to certain strict definitions of the term "POJO". For example, in some definitions, POJOs don't derive from base classes, don't have annotations, and don't have any methods other than getters and setters.   The classes in this implementation do have all of those, but a pragmatic approach was taken to try to keep the classes fairly simple. We use the term POJO here in a more colloquial sense to mean that these are reasonably simple Java objects. But "reasonably simple Java objects" is not so useful as a shorthand to say out loud as "POJO" is. Various versions of a more "strict pojo" design were considered and rejected while designing the library.

---
**Some specific notes on a few design choices whose rationale might not be obvious:**

### Parameterized Types

Examples of ASN.1 parameterized types include BSM Part II content, MessageFrames, and regional extensions in many types.

ASN.1 parameterized types are represented in this library by abstract generic Java classes.  This representation was chosen over other alternatives that were considered, such as using abstract (non-generic) classes, because of the expressiveness of being able to write, for example:

```java
MessageFrame<MapData>
MessageFrame<BasicSafetyMessage>
```

which matches the intended meaning of the parameterized types in the ASN.1 specification, which is as a set of various types that plug into a "hole" in another type in a modular and extensible fashion.

In the standard, parameterized types are defined as sets of types linked by an Information Object Class with a type identified by an integer ID.

As a specific example, the regional extensions for Position3D are defined this way in the ASN.1 standard:

```asn1
-- The Information Object class for all regional extensions
REG-EXT-ID-AND-TYPE ::= CLASS {
   &id     RegionId UNIQUE,
   &Type
   } WITH SYNTAX {&Type IDENTIFIED BY &id}

-- A parameterized type for all regional extensions
RegionalExtension {REG-EXT-ID-AND-TYPE : Set} ::= SEQUENCE {
   regionId     REG-EXT-ID-AND-TYPE.&id( {Set} ),
   regExtValue  REG-EXT-ID-AND-TYPE.&Type( {Set}{@regionId} )
   }

-- A specific set of types of the IOC for the Position3D regional extensions
Reg-Position3D            REG-EXT-ID-AND-TYPE ::= { 
   { Position3D-addGrpB IDENTIFIED BY addGrpB} |
   { Position3D-addGrpC IDENTIFIED BY addGrpC} ,
   ... 
   }   
   
```

Due to Java type erasure, the simplest way to write the code for these abstract generic classes sometimes involves the use of raw generic types, so "raw types" warnings can be found in some of the classes.  For an example, see the implementation of SEQUENCE-OF Reg-Position3D in the Position3D class:

```java
public class Position3D extends Asn1Sequence {

 ...
 
  private SequenceOfRegional regional;

  @JsonInclude(Include.NON_NULL)
  @SuppressWarnings("rawtypes")
  public static class SequenceOfRegional extends Asn1SequenceOf<Reg_Position3D> {
    public SequenceOfRegional() {
      super(Reg_Position3D.class, 1L, 4L);
    }
  }

 ...
}
```

### Custom Serializers/Deserializers

The library makes extensive use of custom Jackson serializers/deserializers and annotations. This was necessary to achieve the goal of having POJOs capable of handling both XER and JER in the same classes, while still leveraging Jackson, but does lead to some complexity.

For instance, in some cases when adding a `@JsonDeserialize(using = SomeCustomDeserializer.class)` annotation to a base class, it's necessary to add `@JsonDeserialize(using = None.class)` to the implementation classes to prevent an infinite recursion in the deserializer.  

For example, the abstract generic class `Reg_Position3D` is annotated with a custom deserializer:

```java
@JsonDeserialize(using = Reg_Position3DDeserializer.class)
public abstract class Reg_Position3D<TValue> extends RegionalExtension<TValue> {
  ...
}
```

and it's implementations require a "None" annotation:

```java
@JsonRootName("Reg_Position3D")
@JsonDeserialize(using = None.class)
public class Position3D_addGrpBReg_Position3D extends Reg_Position3D<Position3D_addGrpB> {
  ...
}
```

## To build:

From this directory:

```bash
./gradlew clean build
```

## Usage examples:

### Deserialize from XER:

```java
String XML =
    """
        <MessageFrame>
                <messageId>19</messageId>
                <value>
                    <SPAT>
                        <timeStamp>400000</timeStamp>
                        <name>Example Name</name>
                        <intersections>
                            <IntersectionState>
        ...
        """;

XmlMapper xmlMapper = new XmlMapper();
SPATMessageFrame spatMessageFrame = xmlMapper.readValue(xml, SPATMessageFrame.class);
int messageId = spatMessageFrame.getMessageId();
```

### Serialize to JER:

```java
ObjectMapper jsonMapper = new ObjectMapper();
SPATMessageFrame spat = createSpat();
String json = jsonMapper.writeValueAsString(spat);
```

Partial output:

```json
{
  "value": {
    "timeStamp": 400000,
    "name": "Example Name",
    "intersections": [
      {
        "id": {
          "region": 0,
          "id": 12111
        },
        "revision": 0,
        ...
```

## Testing

The [unit tests](src/test/java/us/dot/its/jpo/asn/j2735/r2024) include more complete usage examples.
Most of the tests involve serializing and deserializing an XER or JER example in a round trip and
comparing the result with the original using the [XMLUnit](https://www.xmlunit.org/) and
[JSONUnit](https://github.com/lukas-krecan/JsonUnit) test libraries.

The specification represented by the classes in this library consists of a large number of very
complex data structures. These were tested using generated examples of the serialized XER and JER
messages whose correctness was verified independently of the code being tested. Due to the 
complexity of the standard, it would have been a very laborious and error-prone process to create 
all the sample data needed for a comprehensive test suite by hand. Therefore, we used several 
automated tools to aid in creating and verifying the test messages. Those tools are described here: 
[generate-test-data](../generate-test-data/README.md).

### Gradle

To run the unit tests via Gradle, from this directory issue:

```bash
./gradlew clean check

```

then open the `build/reports/tests/test/index.html` file in a browser to view the test results.

The [JaCoCo](https://www.jacoco.org/jacoco/) plugin is used to measure test coverage. After running the tests, open the `build/reports/jacoco/testCodeCoverageReport/html/index.html` file in a browser to view the coverage results.

### Maven

To run the unit tests via Maven, from the **root** directory issue:

```bash
./mvnw clean package
```

The coverage reports for both this project and it's dependency, the jpo-asn-runtime project, will be 
available at `target/site/jacoco-aggregate/index.html`.

Note: if using the "koverage" plugin in the VSCode IDE, add the following to `settings.json` to be
able to view the aggregated coverage report:

```json
"koverage.coverageFilePaths": [
   "**/target/site/jacoco-aggregate"
]
```

The individual test results are available in the `target/surefire-reports` directory.

## License

This project is licensed under the Apache 2 License - see the [LICENSE](../LICENSE) file for details.


