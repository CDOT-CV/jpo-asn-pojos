#!/bin/bash

# Create output directory if it doesn't exist
mkdir -p schemas

# Check if the JAR file exists
JAR_FILE="build/libs/schemagen-cli.jar"
if [ ! -f "$JAR_FILE" ]; then
    echo "Error: JAR file not found at $JAR_FILE"
    echo "Please build the project first using: ./gradlew build"
    exit 1
fi

# List of PDUs and their corresponding modules to generate schemas for
# Format: "PDU_NAME:MODULE_NAME"
PDUS=(
    "MessageFrame:MessageFrame"
    "BasicSafetyMessage:BasicSafetyMessage"
    "PersonalSafetyMessage:PersonalSafetyMessage"
    "SignalRequestMessage:SignalRequestMessage"
    "SignalStatusMessage:SignalStatusMessage"
    "SPAT:SPAT"
    "MapData:MapData"
    "SensorDataSharingMessage:SensorDataSharingMessage"
    "RTCMcorrections:RTCMcorrections"
    "RoadSafetyMessage:RoadSafetyMessage"
)

# Generate schema for each PDU
for pdu_entry in "${PDUS[@]}"; do
    # Split the entry into PDU and module names
    IFS=':' read -r pdu module <<< "$pdu_entry"
    echo "Generating schema for $pdu (module: $module)..."
    mkdir -p schemas/${module}
    java -jar build/libs/schemagen-cli.jar -m "$module" -p "$pdu" -o "schemas/${module}/${pdu}.schema.json"
done

echo "Schema generation complete!"
