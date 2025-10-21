# jpo-asn-pojos Release Notes

## Version 1.1.0, released October 2025

This release introduces JSON schema generation for J2735 messages based on POJO definitions and adds a distributionManagement tag to the repository pom.xml, enabling the jpo-ode to publish Maven packages that depend on the jpo-asn-pojos projects. A workaround has been implemented for a Jackson bug present in versions prior to 2.17.2, where the XmlReadContext nesting depth property was not incremented correctly during JSON deserialization. Additionally, this update resolves two serialization bugs caused by bitstrings with fewer named bits than the declared size and open types containing strings with special characters (e.g., &, <).

The code was reviewed in detail within the following PRs:

* [JSON Schema Generator](https://github.com/CDOT-CV/jpo-asn-pojos/pull/28) 
* [Add the distributionManagement to the pom](https://github.com/CDOT-CV/jpo-asn-pojos/pull/29)
* [Work around Jackson issue pre 2.17.2](https://github.com/CDOT-CV/jpo-asn-pojos/pull/30)
* [Fix serializing bitstrings with the custom serializer](https://github.com/CDOT-CV/jpo-asn-pojos/pull/31)
* [Fix open type xml deserialization of special characters](https://github.com/CDOT-CV/jpo-asn-pojos/pull/33)

## Version 1.0.0, released May 2025

This is the initial release of this project.  It introduces a complete set of Java classes to be used for serializing and deserializing standard XER and JER encoded ASN.1 for the SAE J2735/2024 specification, with a comprehensive set of unit tests, and a command line tool for generating example messages.

The code was reviewed in detail within the following PRs:

* [J2735-2024 Part 1: Runtime library and structure](https://github.com/CDOT-CV/jpo-asn-pojos/pull/12)
* [J2735-2024 Part 2: SPAT, SSM & SRM](https://github.com/CDOT-CV/jpo-asn-pojos/pull/13)
* [J2735-2024 Part 3: MAP, PSM, RTCM](https://github.com/CDOT-CV/jpo-asn-pojos/pull/15)
* [J2735-2024 Part 4: BSM, TIM](https://github.com/CDOT-CV/jpo-asn-pojos/pull/16)
* [J2735-2024 Part 5: RSM, SDSM, CSR, EVA, RSA, ICA, NMEA, PVD, CCM](https://github.com/CDOT-CV/jpo-asn-pojos/pull/17)
* [J2735-2024 Part 6: RWM, PDM, PDR, PDC, MSCM, RUCCM, TAM](https://github.com/CDOT-CV/jpo-asn-pojos/pull/18)
* [J2735-2024 Part 7: RUCRM, TUM, TUMack, TLSM, Test & future use messages](https://github.com/CDOT-CV/jpo-asn-pojos/pull/19)
* [J2735-2024 Part 8: MessageFrames](https://github.com/CDOT-CV/jpo-asn-pojos/pull/20)
* [J2735-2024 Part 9: Test Data Tools](https://github.com/CDOT-CV/jpo-asn-pojos/pull/21)


