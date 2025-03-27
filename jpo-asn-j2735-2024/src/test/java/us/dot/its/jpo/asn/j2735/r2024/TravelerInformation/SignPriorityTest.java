package us.dot.its.jpo.asn.j2735.r2024.TravelerInformation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the SignPriority class.
 */
public class SignPriorityTest {
  
  @Test
  public void constructorTest() {
    var signPriority = new SignPriority(5L);
    assertThat(signPriority, notNullValue());
    assertThat(signPriority.getValue(), equalTo(5L));
  }
}
