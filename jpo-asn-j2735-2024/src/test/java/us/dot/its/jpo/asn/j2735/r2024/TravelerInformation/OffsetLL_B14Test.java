package us.dot.its.jpo.asn.j2735.r2024.TravelerInformation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the OffsetLL_B14 class.
 */
public class OffsetLL_B14Test {
  
  @Test
  public void constructorTest() {
    var offsetLL_B14 = new OffsetLL_B14(5L);
    assertThat(offsetLL_B14, notNullValue());
    assertThat(offsetLL_B14.getValue(), equalTo(5L));
  }
}
