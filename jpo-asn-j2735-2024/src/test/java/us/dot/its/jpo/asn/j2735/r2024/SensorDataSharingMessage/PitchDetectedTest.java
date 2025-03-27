package us.dot.its.jpo.asn.j2735.r2024.SensorDataSharingMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the PitchDetected class.
 */
public class PitchDetectedTest {
  
  @Test
  public void constructorTest() {
    var pitchDetected = new PitchDetected(70L);
    assertThat(pitchDetected, notNullValue());
    assertThat(pitchDetected.getValue(), equalTo(70L));
  }
}
