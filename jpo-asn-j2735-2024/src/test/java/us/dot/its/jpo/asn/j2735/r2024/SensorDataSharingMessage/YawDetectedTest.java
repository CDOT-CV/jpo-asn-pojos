package us.dot.its.jpo.asn.j2735.r2024.SensorDataSharingMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the YawDetected class.
 */
public class YawDetectedTest {
  
  @Test
  public void constructorTest() {
    var yawDetected = new YawDetected(25L);
    assertThat(yawDetected, notNullValue());
    assertThat(yawDetected.getValue(), equalTo(25L));
  }
}
