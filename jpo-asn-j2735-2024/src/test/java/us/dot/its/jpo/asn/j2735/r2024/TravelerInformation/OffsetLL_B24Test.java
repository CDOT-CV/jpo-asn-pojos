package us.dot.its.jpo.asn.j2735.r2024.TravelerInformation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the OffsetLL_B24 class.
 */
public class OffsetLL_B24Test {
  
  @Test
  public void constructorTest() {
    var offsetLL_B24 = new OffsetLL_B24(5L);
    assertThat(offsetLL_B24, notNullValue());
    assertThat(offsetLL_B24.getValue(), equalTo(5L));
  }
}
