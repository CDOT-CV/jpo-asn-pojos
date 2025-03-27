package us.dot.its.jpo.asn.j2735.r2024.TravelerInformation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Radius_B12 class.
 */
public class Radius_B12Test {
  
  @Test
  public void constructorTest() {
    var radius_B12 = new Radius_B12(5L);
    assertThat(radius_B12, notNullValue());
    assertThat(radius_B12.getValue(), equalTo(5L));
  }
}
