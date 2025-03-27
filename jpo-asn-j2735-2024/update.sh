#!/usr/bin/env bash

BRANCH=j2735-2024-type-refactor-everything

echo "Checkout files from $BRANCH"

# echo on
set -x

git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/DegreesLat.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/DegreesLong.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/Elevation.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/LatitudeDMS.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/LatitudeDMS2.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/LongitudeDMS.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/LongitudeDMS2.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/MaxTimetoChange.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/MinTimetoChange.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/MinutesAngle.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/MovementEvent_addGrpB.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/MovementEvent_addGrpBReg_MovementEvent.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/MovementEvent_addGrpBReg_MovementEventValueDeserializer.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/MovementEvent_addGrpBReg_MovementEventValueSerializer.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/NodeOffsetPointXY_addGrpB.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/NodeOffsetPointXY_addGrpBReg_NodeOffsetPointXY.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/NodeOffsetPointXY_addGrpBReg_NodeOffsetPointXYValueDeserializer.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/NodeOffsetPointXY_addGrpBReg_NodeOffsetPointXYValueSerializer.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/Node_LLdms_48b.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/Node_LLdms_80b.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/Position3D_addGrpB.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/Position3D_addGrpBReg_Position3D.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/Position3D_addGrpBReg_Position3DValueDeserializer.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/Position3D_addGrpBReg_Position3DValueSerializer.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/SecondsAngle.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpB/TimeRemaining.java



git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpC/Altitude.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpC/AltitudeConfidence.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpC/AltitudeValue.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpC/ConnectionManeuverAssist_addGrpC.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpC/ConnectionManeuverAssist_addGrpCReg_ConnectionManeuverAssist.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpC/ConnectionManeuverAssist_addGrpCReg_ConnectionManeuverAssistValueDeserializer.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpC/ConnectionManeuverAssist_addGrpCReg_ConnectionManeuverAssistValueSerializer.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpC/IntersectionState_addGrpC.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpC/IntersectionState_addGrpCReg_IntersectionState.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpC/IntersectionState_addGrpCReg_IntersectionStateValueDeserializer.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpC/IntersectionState_addGrpCReg_IntersectionStateValueSerializer.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpC/Position3D_addGrpC.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpC/Position3D_addGrpCReg_Position3D.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpC/Position3D_addGrpCReg_Position3DValueDeserializer.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpC/Position3D_addGrpCReg_Position3DValueSerializer.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpC/PrioritizationResponse.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpC/PrioritizationResponseList.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpC/VehicleToLanePosition.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/AddGrpC/VehicleToLanePositionList.java



git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/Angle.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/ApproachID.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/BasicVehicleRole.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/DSecond.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/DescriptiveName.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/Elevation.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/FullRoadAuthorityID.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/IntersectionAccessPoint.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/IntersectionID.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/IntersectionReferenceID.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/Iso3833VehicleType.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/LaneConnectionID.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/LaneID.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/Latitude.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/Longitude.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/MinuteOfTheYear.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/MsgCount.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/NodeOffsetPointXY.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/Node_LLmD_64b.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/Node_XY_20b.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/Node_XY_22b.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/Node_XY_24b.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/Node_XY_26b.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/Node_XY_28b.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/Node_XY_32b.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/Offset_B09.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/Offset_B10.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/Offset_B11.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/Offset_B12.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/Offset_B13.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/Offset_B14.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/Offset_B16.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/Position3D.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/PrioritizationResponseStatus.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/RegionId.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/RegionalExtension.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/RelativeRoadAuthorityID.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/RequestID.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/RequestImportanceLevel.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/RequestSubRole.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/RequestorType.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/RestrictionClassID.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/RoadAuthorityID.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/RoadRegulatorID.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/SignalGroupID.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/SpeedConfidence.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/StationID.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/TemporaryID.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/TransmissionAndSpeed.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/TransmissionState.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/VehicleID.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/VehicleType.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/Common/Velocity.java


git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/REGION/Reg_AdvisorySpeed.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/REGION/Reg_ConnectionManeuverAssist.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/REGION/Reg_IntersectionState.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/REGION/Reg_MovementEvent.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/REGION/Reg_MovementState.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/REGION/Reg_NodeOffsetPointXY.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/REGION/Reg_Position3D.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/REGION/Reg_RequestorDescription.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/REGION/Reg_RequestorType.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/REGION/Reg_SPAT.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/REGION/Reg_SignalRequest.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/REGION/Reg_SignalRequestMessage.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/REGION/Reg_SignalRequestPackage.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/REGION/Reg_SignalStatus.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/REGION/Reg_SignalStatusMessage.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/REGION/Reg_SignalStatusPackage.java


git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SPAT/AdvisorySpeed.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SPAT/AdvisorySpeedList.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SPAT/AdvisorySpeedType.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SPAT/ConnectionManeuverAssist.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SPAT/EnabledLaneList.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SPAT/IntersectionState.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SPAT/IntersectionStateList.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SPAT/IntersectionStatusObject.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SPAT/ManeuverAssistList.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SPAT/MovementEvent.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SPAT/MovementEventList.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SPAT/MovementList.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SPAT/MovementPhaseState.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SPAT/MovementState.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SPAT/PedestrianBicycleDetect.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SPAT/PedestrianBicycleDetectDeserializer.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SPAT/SPAT.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SPAT/SpeedAdvice.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SPAT/TimeChangeDetails.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SPAT/TimeIntervalConfidence.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SPAT/TimeMark.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SPAT/WaitOnStopline.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SPAT/WaitOnStoplineDeserializer.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SPAT/ZoneLength.java


git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SignalRequestMessage/DeltaTime.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SignalRequestMessage/PriorityRequestType.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SignalRequestMessage/RequestorDescription.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SignalRequestMessage/RequestorPositionVector.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SignalRequestMessage/SignalRequest.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SignalRequestMessage/SignalRequestList.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SignalRequestMessage/SignalRequestMessage.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SignalRequestMessage/SignalRequestPackage.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SignalRequestMessage/TransitVehicleOccupancy.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SignalRequestMessage/TransitVehicleStatus.java


git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SignalStatusMessage/SignalRequesterInfo.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SignalStatusMessage/SignalStatus.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SignalStatusMessage/SignalStatusList.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SignalStatusMessage/SignalStatusMessage.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SignalStatusMessage/SignalStatusPackage.java
git checkout $BRANCH src/main/java/us/dot/its/jpo/asn/j2735/r2024/SignalStatusMessage/SignalStatusPackageList.java


