package us.dot.its.jpo.asn.j2735.r2024.RoadSafetyMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class LongOffsetTest {

  @Test
  public void constructorTest() {
    var lo = new LongOffset(2000L);
    assertThat(lo.getValue(), equalTo(2000L));
  }

}
