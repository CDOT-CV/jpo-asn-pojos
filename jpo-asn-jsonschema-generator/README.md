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

## Batch Schema Generation

To generate schemas for multiple PDUs at once, you can use the [provided script](./batch_gen_schemas.sh):

```bash
./batch_gen_schemas.sh
```

This script will generate JSON schemas for the following messages:

- Generic `MessageFrame`
- BasicSafetyMessage
- PersonalSafetyMessage
- SignalRequestMessage
- SignalStatusMessage
- SignalPhaseAndTimingMessage
- MapData
- SensorDataSharingMessage
- RTCMCorrections
- RoadSafetyMessage

The generated schemas will be placed in the `schemas` directory. To create specific `MessageFrame` type schemas, copy the specific schema from the [MessageFrame](./schemas/MessageFrame/MessageFrame.schema.json) schema. However, only the various MessageFrame schemas have been committed to version control due to reducing redundancy. These are located in the [resources](src/main/resources/schemas/) folder of the project.
