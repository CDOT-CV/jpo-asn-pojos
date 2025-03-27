package us.dot.its.jpo.asn.j2735.r2024.BasicSafetyMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class ObstacleDirectionTest {

  @Test
  public void constructorTest() {
    var od = new ObstacleDirection(10L);
    assertThat(od.getValue(), equalTo(10L));
  }

}
