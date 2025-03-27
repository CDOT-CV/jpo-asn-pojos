package us.dot.its.jpo.asn.j2735.r2024.TravelerInformation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Zoom class.
 */
public class ZoomTest {
  
  @Test
  public void constructorTest() {
    var zoom = new Zoom(5L);
    assertThat(zoom, notNullValue());
    assertThat(zoom.getValue(), equalTo(5L));
  }
}
