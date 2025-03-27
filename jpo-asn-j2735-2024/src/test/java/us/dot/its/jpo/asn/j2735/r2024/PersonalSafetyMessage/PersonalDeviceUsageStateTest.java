package us.dot.its.jpo.asn.j2735.r2024.PersonalSafetyMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class PersonalDeviceUsageStateTest {

  @Test
  public void testDefaultState() {
    var state = new PersonalDeviceUsageState();
    state.fromBinaryString("000000000");
    
    assertThat(state.isUnavailable(), equalTo(false));
    assertThat(state.isOther(), equalTo(false));
    assertThat(state.isIdle(), equalTo(false));
    assertThat(state.isListeningToAudio(), equalTo(false));
    assertThat(state.isTyping(), equalTo(false));
    assertThat(state.isCalling(), equalTo(false));
    assertThat(state.isPlayingGames(), equalTo(false));
    assertThat(state.isReading(), equalTo(false));
    assertThat(state.isViewing(), equalTo(false));
  }

  @Test
  public void testAllFlagsSet() {
    var state = new PersonalDeviceUsageState();
    state.fromBinaryString("111111111");
    
    assertThat(state.isUnavailable(), equalTo(true));
    assertThat(state.isOther(), equalTo(true));
    assertThat(state.isIdle(), equalTo(true));
    assertThat(state.isListeningToAudio(), equalTo(true));
    assertThat(state.isTyping(), equalTo(true));
    assertThat(state.isCalling(), equalTo(true));
    assertThat(state.isPlayingGames(), equalTo(true));
    assertThat(state.isReading(), equalTo(true));
    assertThat(state.isViewing(), equalTo(true));
  }

  @Test
  public void testMixedStates() {
    var state = new PersonalDeviceUsageState();
    state.fromBinaryString("101010101");
    
    assertThat(state.isUnavailable(), equalTo(true));
    assertThat(state.isOther(), equalTo(false));
    assertThat(state.isIdle(), equalTo(true));
    assertThat(state.isListeningToAudio(), equalTo(false));
    assertThat(state.isTyping(), equalTo(true));
    assertThat(state.isCalling(), equalTo(false));
    assertThat(state.isPlayingGames(), equalTo(true));
    assertThat(state.isReading(), equalTo(false));
    assertThat(state.isViewing(), equalTo(true));
  }

  @Test
  public void testSetters() {
    var state = new PersonalDeviceUsageState();

    state.setUnavailable(true);
    assertThat(state.isUnavailable(), equalTo(true));

    state.setOther(true);
    assertThat(state.isOther(), equalTo(true));
    
    state.setIdle(true);
    assertThat(state.isIdle(), equalTo(true));
    
    state.setListeningToAudio(true);
    assertThat(state.isListeningToAudio(), equalTo(true));
    
    state.setTyping(true);
    assertThat(state.isTyping(), equalTo(true));
    
    state.setCalling(true);
    assertThat(state.isCalling(), equalTo(true));
    
    state.setPlayingGames(true);
    assertThat(state.isPlayingGames(), equalTo(true));
    
    state.setReading(true);
    assertThat(state.isReading(), equalTo(true));
    
    state.setViewing(true);
    assertThat(state.isViewing(), equalTo(true));
    
    // Verify final state matches all bits set
    var allSet = new PersonalDeviceUsageState();
    allSet.fromBinaryString("111111111");
    assertThat(state.toString(), equalTo(allSet.toString()));
  }
}
