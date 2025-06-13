#!/bin/bash

# Create output directory if it doesn't exist
mkdir -p schemas

# List of PDUs and their corresponding modules to generate schemas for
# Format: "PDU_NAME:MODULE_NAME"
PDUS=(
    "MessageFrame:MessageFrame"
    "BasicSafetyMessage:BasicSafetyMessage"
    "BasicSafetyMessageMessageFrame:BasicSafetyMessage"
    "PersonalSafetyMessage:PersonalSafetyMessage"
    "PersonalSafetyMessageMessageFrame:PersonalSafetyMessage"
    "SignalRequestMessage:SignalRequestMessage"
    "SignalRequestMessageMessageFrame:SignalRequestMessage"
    "SignalStatusMessage:SignalStatusMessage"
    "SignalStatusMessageMessageFrame:SignalStatusMessage"
    "SPAT:SPAT"
    "SPATMessageFrame:SPAT"
    "MapData:MapData"
    "MapDataMessageFrame:MapData"
    "SensorDataSharingMessage:SensorDataSharingMessage"
    "SensorDataSharingMessageMessageFrame:SensorDataSharingMessage"
    "RTCMcorrections:RTCMcorrections"
    "RoadSafetyMessage:RoadSafetyMessage"
    "RoadSafetyMessageMessageFrame:RoadSafetyMessage"
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
