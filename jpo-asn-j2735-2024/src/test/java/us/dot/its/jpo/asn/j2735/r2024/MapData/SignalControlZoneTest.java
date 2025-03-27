package us.dot.its.jpo.asn.j2735.r2024.MapData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

public class SignalControlZoneTest {

  @Test
  public void constructorTest() {
    var scz = new SignalControlZone();
    assertThat(scz, notNullValue());
  }
}
