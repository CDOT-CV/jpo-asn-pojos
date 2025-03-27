package us.dot.its.jpo.asn.j2735.r2024.PersonalSafetyMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class PublicSafetyDirectingTrafficSubTypeTest {

  @Test
  public void testDefaultState() {
    var state = new PublicSafetyDirectingTrafficSubType();
    state.fromBinaryString("0000000");
    
    assertThat(state.isUnavailable(), equalTo(false));
    assertThat(state.isPoliceAndTrafficOfficers(), equalTo(false));
    assertThat(state.isTrafficControlPersons(), equalTo(false));
    assertThat(state.isRailroadCrossingGuards(), equalTo(false));
    assertThat(state.isCivilDefenseNationalGuardMilitaryPolice(), equalTo(false));
    assertThat(state.isEmergencyOrganizationPersonnel(), equalTo(false));
    assertThat(state.isHighwayServiceVehiclePersonnel(), equalTo(false));
  }

  @Test
  public void testAllFlagsSet() {
    var state = new PublicSafetyDirectingTrafficSubType();
    state.fromBinaryString("1111111");
    
    assertThat(state.isUnavailable(), equalTo(true));
    assertThat(state.isPoliceAndTrafficOfficers(), equalTo(true));
    assertThat(state.isTrafficControlPersons(), equalTo(true));
    assertThat(state.isRailroadCrossingGuards(), equalTo(true));
    assertThat(state.isCivilDefenseNationalGuardMilitaryPolice(), equalTo(true));
    assertThat(state.isEmergencyOrganizationPersonnel(), equalTo(true));
    assertThat(state.isHighwayServiceVehiclePersonnel(), equalTo(true));
  }

  @Test
  public void testMixedStates() {
    var state = new PublicSafetyDirectingTrafficSubType();
    state.fromBinaryString("1010101");
    
    assertThat(state.isUnavailable(), equalTo(true));
    assertThat(state.isPoliceAndTrafficOfficers(), equalTo(false));
    assertThat(state.isTrafficControlPersons(), equalTo(true));
    assertThat(state.isRailroadCrossingGuards(), equalTo(false));
    assertThat(state.isCivilDefenseNationalGuardMilitaryPolice(), equalTo(true));
    assertThat(state.isEmergencyOrganizationPersonnel(), equalTo(false));
    assertThat(state.isHighwayServiceVehiclePersonnel(), equalTo(true));
  }

  @Test
  public void testSetters() {
    var state = new PublicSafetyDirectingTrafficSubType();

    state.setUnavailable(true);
    assertThat(state.isUnavailable(), equalTo(true));

    state.setPoliceAndTrafficOfficers(true);
    assertThat(state.isPoliceAndTrafficOfficers(), equalTo(true));
    
    state.setTrafficControlPersons(true);
    assertThat(state.isTrafficControlPersons(), equalTo(true));
    
    state.setRailroadCrossingGuards(true);
    assertThat(state.isRailroadCrossingGuards(), equalTo(true));
    
    state.setCivilDefenseNationalGuardMilitaryPolice(true);
    assertThat(state.isCivilDefenseNationalGuardMilitaryPolice(), equalTo(true));
    
    state.setEmergencyOrganizationPersonnel(true);
    assertThat(state.isEmergencyOrganizationPersonnel(), equalTo(true));
    
    state.setHighwayServiceVehiclePersonnel(true);
    assertThat(state.isHighwayServiceVehiclePersonnel(), equalTo(true));
    
    // Verify final state matches all bits set
    var allSet = new PublicSafetyDirectingTrafficSubType();
    allSet.fromBinaryString("1111111");
    assertThat(state.toString(), equalTo(allSet.toString()));
  }
}
