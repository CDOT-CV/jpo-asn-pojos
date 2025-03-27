package us.dot.its.jpo.asn.j2735.r2024.MapData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.typeCompatibleWith;

import us.dot.its.jpo.asn.j2735.r2024.REGION.Reg_GenericLane;
import org.junit.jupiter.api.Test;

public class GenericLane_SequenceOfRegionalTest {

  @Test
  public void constructorTest() {
    var sor = new GenericLane.SequenceOfRegional();
    assertThat(sor.getItemClass(), typeCompatibleWith(Reg_GenericLane.class));
  }
}
