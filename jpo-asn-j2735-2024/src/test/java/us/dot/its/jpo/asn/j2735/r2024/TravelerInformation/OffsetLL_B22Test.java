package us.dot.its.jpo.asn.j2735.r2024.TravelerInformation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the OffsetLL_B22 class.
 */
public class OffsetLL_B22Test {
  
  @Test
  public void constructorTest() {
    var offsetLL_B22 = new OffsetLL_B22(5L);
    assertThat(offsetLL_B22, notNullValue());
    assertThat(offsetLL_B22.getValue(), equalTo(5L));
  }
}
