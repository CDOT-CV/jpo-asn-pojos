package us.dot.its.jpo.asn.j2735.r2024.SensorDataSharingMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the MeasurementTimeOffset class.
 */
public class MeasurementTimeOffsetTest {
  
  @Test
  public void constructorTest() {
    var measurementTimeOffset = new MeasurementTimeOffset(500L);
    assertThat(measurementTimeOffset, notNullValue());
    assertThat(measurementTimeOffset.getValue(), equalTo(500L));
  }
}
