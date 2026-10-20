![GitHub Release](https://img.shields.io/github/v/release/usdot-jpo-ode/jpo-asn-pojos) [![CI](https://github.com/usdot-jpo-ode/jpo-asn-pojos/actions/workflows/ci.yml/badge.svg)](https://github.com/usdot-jpo-ode/jpo-asn-pojos/actions/workflows/ci.yml) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=usdot-jpo-ode_jpo-asn-pojos&metric=alert_status)](https://sonarcloud.io/dashboard?id=usdot-jpo-ode_jpo-asn-pojos) ![GitHub License](https://img.shields.io/github/license/usdot-jpo-ode/jpo-asn-pojos)


# jpo-asn-pojos

This repository contains generated POJOs ("Plain Old Java Objects") from ASN.1 (Abstract Syntax 
Notation) specifications.

Currently it includes the J2735 (2024) V2X specification.  In the future, later versions of
J2735, as well as additional SAE and IEEE specifications related to connected vehicles, such as
IEEE 1609.2/3, etc. may be included.

## Background

This project provides Java classes for encoding and decoding ASN.1 messages to and from standard
Canonical XER (XML Encoding Rules) and JER (JSON Encoding Rules) formats. These classes are 
generated from SAE and IEEE standards to facilitate the handling of ASN.1 data structures in Java 
applications.

The SAE J2735/2024 specification is available as a free download from:
https://www.sae.org/standards/content/j2735_202409/

The J2735 specification uses a subset of ASN.1 which is documented in the following ITU
recommendations:
* [X.680 - ASN.1 Basic Notation](https://www.itu.int/rec/T-REC-X.680/en)
* [X.681 - Information Object Classes](https://www.itu.int/rec/T-REC-X.681/en)
* [X.682 - Constraints](https://www.itu.int/rec/T-REC-X.682/en)
* [X.683 - Parameterization](https://www.itu.int/rec/T-REC-X.683/en)

The XER and JER encodings are documented here:
* [X.693 - XML Encoding Rules (XER)](https://www.itu.int/rec/T-REC-X.693/en) (and also in X.680)
* [X.697 - JSON Encoding Rules (JER)](https://www.itu.int/rec/T-REC-X.697/en)

## Project Structure

* [jpo-asn-runtime](jpo-asn-runtime/README.md) - Runtime library containing base classes and 
serializers/deserializers for ASN.1 types.
* [jpo-asn-j2735-2024](jpo-asn-j2735-2024/README.md) - POJOs for the J2735/2024 specification.
* [jpo-asn-test-generator](jpo-asn-test-generator/README.md) - A Java command-line utility for
  generating test XER and JER messages.
* [jpo-asn-jsonschema-generator](jpo-asn-jsonschema-generator/README.md) - A Java command-line utility for generating JSON schema files to validate output JSON messages.
* [generate-test-data](generate-test-data/README.md) - Various utilities for generating test data
  and examples of serialized messages.

## Getting Started

### Prerequisites

* **To use the libraries:** Java 21 or later, and Jackson 3 (`tools.jackson` packages). Version
  2.0.0 and later of the libraries in this repository are not compatible with Jackson 2
  (`com.fasterxml.jackson.databind`). See
  [Required Jackson Mapper Configuration](#required-jackson-mapper-configuration).
* **To build from source:** JDK 25. The libraries are compiled with `--release 21`, so the output
  still runs on Java 21. Use the included Gradle (`./gradlew`) and Maven (`./mvnw`) wrappers; they
  pin the tested tool versions for this checkout. If JDK 25 is not installed, Gradle downloads it
  automatically through its toolchain support.

| Release | Min Java version<br>consumers can use | Build JDK | Gradle | Maven | Jackson |
|---------|-------------------|--------------|--------|-------|---------|
| 2.0.0   | 21                | 25           | 9.8.2  | 3.9.9 | 3.x     |
| 1.2.0   | 21                | 21           | 8.10.2 | 3.9.9 | 2.x     |
| 1.1.0   | 21                | 21           | 8.10.2 | 3.9.9 | 2.x     |
| 1.0.0   | 21                | 21           | 8.10.2 | 3.9.9 | 2.x     |

### Build

To build the project using Gradle:

```bash
cd jpo-asn-j2735-2024
./gradlew clean build
```

To build the project using Maven, from the root directory:

```bash
./mvnw clean package
```

### Run Tests

To run tests using Gradle:

```bash
cd jpo-asn-runtime
./gradlew clean test
cd ../jpo-asn-j2735-2024
./gradlew clean check
```

To run tests using Maven, from the root directory:

```bash
./mvnw clean package
```

## Installation

### Use as a Maven Dependency

The library releases are deployed to Maven Central. Each organization that maintains this
repository publishes under its own groupId:

| Publisher                                                              | groupId                   |
|------------------------------------------------------------------------|---------------------------|
| [USDOT JPO ODE](https://github.com/usdot-jpo-ode/jpo-asn-pojos)        | `io.github.usdot-jpo-ode` |
| [CDOT](https://github.com/CDOT-CV/jpo-asn-pojos)                       | `io.github.cdot-cv`       |
| [Neaera Consulting](https://github.com/neaeraconsulting/jpo-asn-pojos) | `com.neaeraconsulting`    |

The examples below use `io.github.usdot-jpo-ode`; substitute the groupId of the publisher you want.

To consume the release from Maven Central, add a dependency to `pom.xml` (or configure equivalently
for Gradle):

```xml
<dependencies>
    <dependency>
        <groupId>io.github.usdot-jpo-ode</groupId>
        <artifactId>jpo-asn-runtime</artifactId>
        <version>2.0.0-beta1</version>
    </dependency>
    <dependency>
        <groupId>io.github.usdot-jpo-ode</groupId>
        <artifactId>jpo-asn-j2735-2024</artifactId>
        <version>2.0.0-beta1</version>
    </dependency>
</dependencies>
```
No further configuration is needed to consume the releases from Central.

To consume the prerelease snapshots, add the Central Portal snapshot
repository to `pom.xml`:

```xml
<repositories>
  <repository>
    <name>Central Portal Snapshots</name>
    <id>central-portal-snapshots</id>
    <url>https://central.sonatype.com/repository/maven-snapshots/</url>
    <releases>
      <enabled>false</enabled>
    </releases>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
  </repository>
</repositories>
```

or to `gradle.build`:

```groovy
repositories {
  maven {
    name = 'Central Portal Snapshots'
    url = 'https://central.sonatype.com/repository/maven-snapshots/'

    // Only search this repository for the specific dependency
    content {
      includeModule("<the snapshot's groupId>", "<the snapshot's artifactId>")
    }
  }
  mavenCentral()
}
```

and add the dependencies to the "SNAPSHOT" version:

```xml
<dependencies>
    <dependency>
        <groupId>io.github.usdot-jpo-ode</groupId>
        <artifactId>jpo-asn-runtime</artifactId>
        <version>2.0.0-beta1-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>io.github.usdot-jpo-ode</groupId>
        <artifactId>jpo-asn-j2735-2024</artifactId>
        <version>2.0.0-beta1-SNAPSHOT</version>
    </dependency>
</dependencies>
```

Refer to the [Sonatype documentation for Central Portal snapshots](https://central.sonatype.org/publish/publish-portal-snapshots/#consuming-snapshot-releases-for-your-project)

### Use as a Submodule

To add the repository as a submodule to another project that uses it as a library, follow these
steps:

1. **Add the submodule to your project:**
   Navigate to the root directory of your project and run the following command to add the
   submodule:
   ```bash
   git submodule add https://github.com/usdot-jpo-ode/jpo-asn-pojos.git
   ```

2. **Initialize and update the submodule:**
   After adding the submodule, initialize and update it with the following commands:
   ```bash
   git submodule update --init
   ```

3. **Include the submodule in your build configuration:**

- **For Gradle:**
  Add the following to your `settings.gradle` file to include the submodule:
  ```groovy
  includeBuild('path/to/jpo-asn-pojos/jpo-asn-j2735-2024')
  ```

  Then, add the dependencies in your `build.gradle` file:
  ```groovy
  dependencies {
      implementation('io.github.usdot-jpo-ode:jpo-asn-j2735-2024')
  }
  ```

- **For Maven:**
  Add the submodule as a module in your `pom.xml` file:
  ```xml
  <modules>
      <module>path/to/jpo-asn-pojos</module>
  </modules>
  ```

  Then, add the dependencies in your `pom.xml` file:
  ```xml
  <dependency>
      <groupId>io.github.usdot-jpo-ode</groupId>
      <artifactId>jpo-asn-j2735-2024</artifactId>
      <version>2.0.0-beta2</version>
  </dependency>
  ```

  When built from source, the libraries use the groupId `io.github.usdot-jpo-ode` regardless of
  which fork the source comes from (see [Publishing to Maven Central](#publishing-to-maven-central)).

Replace `path/to/jpo-asn-pojos` with the actual path to the submodule directory relative to your
project's root directory.

## Publishing to Maven Central

The `Publish JARs to the Maven Central Repository` workflows publish the `jpo-asn-runtime` and
`jpo-asn-j2735-2024` libraries when a GitHub release is published (releases) or pre-released
(snapshots). The checked-in build files are the same in every fork; each fork configures publishing
through GitHub repository settings (**Settings > Secrets and variables > Actions**):

| Name                       | Kind     | Purpose                                                                                       |
|----------------------------|----------|-----------------------------------------------------------------------------------------------|
| `MAVEN_CENTRAL_PUBLISH`    | Variable | Set to `true` to enable publishing. The workflows are skipped in forks that don't set it.     |
| `MAVEN_GROUP_ID`           | Variable | Optional. Overrides the groupId, which otherwise defaults to `io.github.<owner>` (lowercase). |
| `CENTRAL_TOKEN_USERNAME`   | Secret   | Central Portal user token username.                                                           |
| `CENTRAL_TOKEN_PASSWORD`   | Secret   | Central Portal user token password.                                                           |
| `GPG_SIGNING_KEY`          | Secret   | ASCII-armored private key used to sign the artifacts.                                         |
| `GPG_SIGNING_KEY_PASSWORD` | Secret   | Passphrase for the signing key.                                                               |

The groupId's namespace must be verified for the publishing account in the
[Central Portal](https://central.sonatype.com/publishing/namespaces). For example, the
`CDOT-CV` organization publishes as `io.github.cdot-cv` without setting `MAVEN_GROUP_ID`, while
Neaera Consulting sets `MAVEN_GROUP_ID` to `com.neaeraconsulting`.

The workflows pass the groupId and the repository to Maven as the `publish.groupId` and
`publish.githubRepository` properties. The same properties can be set for a local build, for
example:

```bash
./mvnw clean install -Dpublish.groupId=com.neaeraconsulting -Dpublish.githubRepository=neaeraconsulting/jpo-asn-pojos
```

## Usage

To use the generated POJOs in your project, include the necessary dependencies and use the
provided classes for encoding and decoding ASN.1 messages. Refer to the documentation and examples
provided in the [jpo-asn-runtime](jpo-asn-runtime/README.md) and 
[jpo-asn-j2735-2024](jpo-asn-j2735-2024/README.md) modules for detailed usage instructions.

### Required Jackson Mapper Configuration

**Important:** Any Jackson `XmlMapper`, `JsonMapper` or `ObjectMapper` that is used to serialize or 
deserialize the POJOs must have the `MapperFeature.SORT_PROPERTIES_ALPHABETICALLY` feature disabled.

In Jackson 3 the default value of this feature was changed from `false` to `true`. The POJOs rely 
on properties being written in the order in which the fields are declared in the Java classes, 
which is the order in which the components of the ASN.1 SEQUENCE types are defined in the 
specification. A mapper with the default Jackson 3 configuration writes the properties in 
alphabetical order instead, which does not produce valid Canonical XER.

This applies to every project that uses these POJOs. Jackson 3 mappers are immutable, so the 
feature must be disabled when the mapper is built:

```java
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.dataformat.xml.XmlMapper;

// Always cache and reuse mappers
final static XmlMapper xmlMapper = XmlMapper.builder()
    .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
    .build();

final static JsonMapper jsonMapper = JsonMapper.builder()
    .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
    .build();
```

Mappers that are created with `new XmlMapper()`, `new JsonMapper()` or `new ObjectMapper()`, or 
that are provided by a framework, need to be checked and updated when upgrading a project to 
version 2.0.0 or later of these libraries. The `OdeCustomJsonMapper` provided in the 
[jpo-asn-runtime](jpo-asn-runtime/README.md) module already disables the feature.

## Contributing

Contributions are welcome! Please [create an issue](https://github.com/usdot-jpo-ode/jpo-asn-pojos/issues), 
and submit a corresponding pull request for any enhancements or bug fixes.

## License

This project is licensed under the Apache 2 License - see the [LICENSE](LICENSE) file for details.


