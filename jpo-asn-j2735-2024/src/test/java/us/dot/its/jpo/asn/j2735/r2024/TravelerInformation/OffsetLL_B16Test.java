package us.dot.its.jpo.asn.j2735.r2024.TravelerInformation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the OffsetLL_B16 class.
 */
public class OffsetLL_B16Test {
  
  @Test
  public void constructorTest() {
    var offsetLL_B16 = new OffsetLL_B16(5L);
    assertThat(offsetLL_B16, notNullValue());
    assertThat(offsetLL_B16.getValue(), equalTo(5L));
  }
}
