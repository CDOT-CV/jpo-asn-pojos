package us.dot.its.jpo.asn.j2735.r2024.TravelerInformation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the MinutesDuration class.
 */
public class MinutesDurationTest {
  
  @Test
  public void constructorTest() {
    var minutesDuration = new MinutesDuration(5L);
    assertThat(minutesDuration, notNullValue());
    assertThat(minutesDuration.getValue(), equalTo(5L));
  }
}
