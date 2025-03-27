package us.dot.its.jpo.asn.j2735.r2024.MapData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.typeCompatibleWith;

import us.dot.its.jpo.asn.j2735.r2024.REGION.Reg_RoadSegment;
import org.junit.jupiter.api.Test;

public class RoadSegment_SequenceOfRegionalTest {

  @Test
  public void constructorTest() {
    var sor = new RoadSegment.SequenceOfRegional();
    assertThat(sor.getItemClass(), typeCompatibleWith(Reg_RoadSegment.class));
  }
}
