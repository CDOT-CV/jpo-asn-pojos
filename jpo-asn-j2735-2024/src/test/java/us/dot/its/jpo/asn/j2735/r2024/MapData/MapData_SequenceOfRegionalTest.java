package us.dot.its.jpo.asn.j2735.r2024.MapData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.typeCompatibleWith;

import us.dot.its.jpo.asn.j2735.r2024.REGION.Reg_MapData;
import org.junit.jupiter.api.Test;

public class MapData_SequenceOfRegionalTest {

  @Test
  public void constructorTest() {
    var sor = new MapData.SequenceOfRegional();
    assertThat(sor.getItemClass(), typeCompatibleWith(Reg_MapData.class));
  }
}
