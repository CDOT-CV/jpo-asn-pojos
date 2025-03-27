package us.dot.its.jpo.asn.j2735.r2024.PersonalSafetyMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class PublicSafetyAndRoadWorkerActivityTest {

  @Test
  public void testDefaultState() {
    var activity = new PublicSafetyAndRoadWorkerActivity();
    activity.fromBinaryString("000000");
    
    assertThat(activity.isUnavailable(), equalTo(false));
    assertThat(activity.isWorkingOnRoad(), equalTo(false));
    assertThat(activity.isSettingUpClosures(), equalTo(false));
    assertThat(activity.isRespondingToEvents(), equalTo(false));
    assertThat(activity.isDirectingTraffic(), equalTo(false));
    assertThat(activity.isOtherActivities(), equalTo(false));
  }

  @Test
  public void testAllFlagsSet() {
    var activity = new PublicSafetyAndRoadWorkerActivity();
    activity.fromBinaryString("111111");
    
    assertThat(activity.isUnavailable(), equalTo(true));
    assertThat(activity.isWorkingOnRoad(), equalTo(true));
    assertThat(activity.isSettingUpClosures(), equalTo(true));
    assertThat(activity.isRespondingToEvents(), equalTo(true));
    assertThat(activity.isDirectingTraffic(), equalTo(true));
    assertThat(activity.isOtherActivities(), equalTo(true));
  }

  @Test
  public void testMixedStates() {
    var activity = new PublicSafetyAndRoadWorkerActivity();
    activity.fromBinaryString("101101");
    
    assertThat(activity.isUnavailable(), equalTo(true));
    assertThat(activity.isWorkingOnRoad(), equalTo(false));
    assertThat(activity.isSettingUpClosures(), equalTo(true));
    assertThat(activity.isRespondingToEvents(), equalTo(true));
    assertThat(activity.isDirectingTraffic(), equalTo(false));
    assertThat(activity.isOtherActivities(), equalTo(true));
  }

  @Test
  public void testSetters() {
    var activity = new PublicSafetyAndRoadWorkerActivity();

    activity.setUnavailable(true);
    assertThat(activity.isUnavailable(), equalTo(true));
    
    activity.setWorkingOnRoad(true);
    assertThat(activity.isWorkingOnRoad(), equalTo(true));
    
    activity.setSettingUpClosures(true);
    assertThat(activity.isSettingUpClosures(), equalTo(true));
    
    activity.setRespondingToEvents(true);
    assertThat(activity.isRespondingToEvents(), equalTo(true));
    
    activity.setDirectingTraffic(true);
    assertThat(activity.isDirectingTraffic(), equalTo(true));
    
    activity.setOtherActivities(true);
    assertThat(activity.isOtherActivities(), equalTo(true));
    
    var allSet = new PublicSafetyAndRoadWorkerActivity();
    allSet.fromBinaryString("111111");
    assertThat(activity.toString(), equalTo(allSet.toString()));
  }
}
