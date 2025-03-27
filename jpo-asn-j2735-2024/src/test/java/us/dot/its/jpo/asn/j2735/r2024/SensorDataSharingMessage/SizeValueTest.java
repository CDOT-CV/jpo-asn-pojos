package us.dot.its.jpo.asn.j2735.r2024.SensorDataSharingMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the SizeValue class.
 */
public class SizeValueTest {
  
  @Test
  public void constructorTest() {
    var sizeValue = new SizeValue(1000L);
    assertThat(sizeValue, notNullValue());
    assertThat(sizeValue.getValue(), equalTo(1000L));
  }
}
