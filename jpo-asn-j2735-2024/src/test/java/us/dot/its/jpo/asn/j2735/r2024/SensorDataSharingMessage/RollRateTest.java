package us.dot.its.jpo.asn.j2735.r2024.SensorDataSharingMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the RollRate class.
 */
public class RollRateTest {
  
  @Test
  public void constructorTest() {
    var rollRate = new RollRate(40L);
    assertThat(rollRate, notNullValue());
    assertThat(rollRate.getValue(), equalTo(40L));
  }
}
