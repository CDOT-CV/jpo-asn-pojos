#!/bin/bash

# Create output directory if it doesn't exist
mkdir -p schemas

# List of PDUs to generate schemas for
PDUS=(
    "MessageFrame"
    "BasicSafetyMessage"
    "PersonalSafetyMessage"
    "SignalRequestMessage"
    "SignalStatusMessage"
    "SPAT"
    "MapData"
    "SensorDataSharingMessage"
    "RTCMcorrections"
    "RoadSafetyMessage"
)

# Generate schema for each PDU
for pdu in "${PDUS[@]}"; do
    echo "Generating schema for $pdu..."
    java -jar build/libs/schemagen-cli.jar -m "$pdu" -p "$pdu" -o "schemas/${pdu}.schema.json"
done

echo "Schema generation complete!"
