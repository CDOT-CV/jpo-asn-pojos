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

* JDK 21 or higher
* Gradle or Maven

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
      implementation('usdot.jpo.asn:jpo-asn-j2735-2024')
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
      <groupId>usdot.jpo.asn</groupId>
      <artifactId>jpo-asn-j2735-2024</artifactId>
      <version>1.0.2</version>
  </dependency>
  ```

Replace `path/to/jpo-asn-pojos` with the actual path to the submodule directory relative to your
project's root directory.

### Use as a Maven Dependency

TBD. The library JARs will be deployed to Github Maven, and hopefully Maven Central, in the future, 
but in the near term this repo is intended to be incorporated as a submodule into the ODE.

## Usage

To use the generated POJOs in your project, include the necessary dependencies and use the
provided classes for encoding and decoding ASN.1 messages. Refer to the documentation and examples
provided in the [jpo-asn-runtime](jpo-asn-runtime/README.md) and 
[jpo-asn-j2735-2024](jpo-asn-j2735-2024/README.md) modules for detailed usage instructions.

## Contributing

Contributions are welcome! Please [create an issue](https://github.com/usdot-jpo-ode/jpo-asn-pojos/issues), 
and submit a corresponding pull request for any enhancements or bug fixes.

## License

This project is licensed under the Apache 2 License - see the [LICENSE](LICENSE) file for details.



