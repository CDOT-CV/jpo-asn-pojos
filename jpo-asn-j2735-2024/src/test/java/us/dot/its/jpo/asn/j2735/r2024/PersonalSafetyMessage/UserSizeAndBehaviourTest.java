package us.dot.its.jpo.asn.j2735.r2024.PersonalSafetyMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class UserSizeAndBehaviourTest {
  
  @Test
  public void testDefaultState() {
    var state = new UserSizeAndBehaviour();
    state.fromBinaryString("00000");
    
    assertThat(state.isUnavailable(), equalTo(false));
    assertThat(state.isSmallStature(), equalTo(false));
    assertThat(state.isLargeStature(), equalTo(false));
    assertThat(state.isErraticMoving(), equalTo(false));
    assertThat(state.isSlowMoving(), equalTo(false));
  }

  @Test
  public void testAllFlagsSet() {
    var state = new UserSizeAndBehaviour();
    state.fromBinaryString("11111");
    
    assertThat(state.isUnavailable(), equalTo(true));
    assertThat(state.isSmallStature(), equalTo(true));
    assertThat(state.isLargeStature(), equalTo(true));
    assertThat(state.isErraticMoving(), equalTo(true));
    assertThat(state.isSlowMoving(), equalTo(true));
  }

  @Test
  public void testMixedStates() {
    var state = new UserSizeAndBehaviour();
    state.fromBinaryString("10101");
    
    assertThat(state.isUnavailable(), equalTo(true));
    assertThat(state.isSmallStature(), equalTo(false));
    assertThat(state.isLargeStature(), equalTo(true));
    assertThat(state.isErraticMoving(), equalTo(false));
    assertThat(state.isSlowMoving(), equalTo(true));
  }

  @Test
  public void testSetters() {
    var state = new UserSizeAndBehaviour();

    state.setUnavailable(true);
    assertThat(state.isUnavailable(), equalTo(true));

    state.setSmallStature(true);
    assertThat(state.isSmallStature(), equalTo(true));
    
    state.setLargeStature(true);
    assertThat(state.isLargeStature(), equalTo(true));
    
    state.setErraticMoving(true);
    assertThat(state.isErraticMoving(), equalTo(true));
    
    state.setSlowMoving(true);
    assertThat(state.isSlowMoving(), equalTo(true));
    
    // Verify final state matches all bits set
    var allSet = new UserSizeAndBehaviour();
    allSet.fromBinaryString("11111");
    assertThat(state.toString(), equalTo(allSet.toString()));
  }
}
