package us.dot.its.jpo.asn.j2735.r2024.RoadSafetyMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class ToleranceTest {

  @Test
  public void constructorTest() {
    var tol = new Tolerance(2000L);
    assertThat(tol.getValue(), equalTo(2000L));
  }

}
