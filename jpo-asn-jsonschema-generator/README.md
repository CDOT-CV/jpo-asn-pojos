# JSON Schema Generator

Command line tool that uses a custom module for the victools json schema generator to create JSON schemas from the pojos.

## Build

```bash
./gradlew build
```

## Usage

```bash
cd build/libs
java -jar schemagen-cli.jar -m <module> -p <pdu> -o <output-fiile>
```

## To do

- Handle parameterized open types (by reading `@Asn1ParameterizedTypes` annotations)
- Use "optional" property of `@Asn1Property` annotations to populate "required".
- Handle variable-length bit strings.
- Add unit tests.