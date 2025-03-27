package us.dot.its.jpo.asn.j2735.r2024.BasicSafetyMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

public class SpeedProfileMeasurementTest {

  @Test
  public void constructorTest() {
    var spm = new SpeedProfileMeasurement();
    assertThat(spm, notNullValue());
  }
}
