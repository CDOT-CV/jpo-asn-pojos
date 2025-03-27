package us.dot.its.jpo.asn.j2735.r2024.RoadSafetyMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class RSMLanePositionTest {

  @Test
  public void constructorTest() {
    var lp = new RSMLanePosition(2000L);
    assertThat(lp.getValue(), equalTo(2000L));
  }

}
