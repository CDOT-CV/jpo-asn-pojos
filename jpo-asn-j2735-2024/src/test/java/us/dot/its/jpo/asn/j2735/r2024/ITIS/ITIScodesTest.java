package us.dot.its.jpo.asn.j2735.r2024.ITIS;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the ITIScodes class.
 */
public class ITIScodesTest {
  
  @Test
  public void constructorTest() {
    var itisCodes = new ITIScodes(3840L);
    assertThat(itisCodes, notNullValue());
    assertThat(itisCodes.getValue(), equalTo(3840L));
  }
}
