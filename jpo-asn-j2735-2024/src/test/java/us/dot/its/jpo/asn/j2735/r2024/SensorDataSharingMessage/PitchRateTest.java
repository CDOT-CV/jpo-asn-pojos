package us.dot.its.jpo.asn.j2735.r2024.SensorDataSharingMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the PitchRate class.
 */
public class PitchRateTest {
  
  @Test
  public void constructorTest() {
    var pitchRate = new PitchRate(25L);
    assertThat(pitchRate, notNullValue());
    assertThat(pitchRate.getValue(), equalTo(25L));
  }
}
