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

java -jar schemagen-cli.jar -m SensorDataSharingMessage -p SensorDataSharingMessage -o SensorDataSharingMessage.schema.json
java -jar schemagen-cli.jar -m SPAT -p SPAT -o SPAT.schema.json
```

## To do

- ~~Handle parameterized open types (by reading `@Asn1ParameterizedTypes` annotations)~~
- ~~Use "optional" property of `@Asn1Property` annotations to populate "required".~~
- ~~Handle variable-length bit strings.~~
- Use 'oneOf' to enforce choice types have one value.
- Add unit tests.