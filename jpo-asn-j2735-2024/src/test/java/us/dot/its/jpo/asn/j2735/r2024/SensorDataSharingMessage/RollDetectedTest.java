package us.dot.its.jpo.asn.j2735.r2024.SensorDataSharingMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the RollDetected class.
 */
public class RollDetectedTest {
  
  @Test
  public void constructorTest() {
    var rollDetected = new RollDetected(10L);
    assertThat(rollDetected, notNullValue());
    assertThat(rollDetected.getValue(), equalTo(10L));
  }
}
