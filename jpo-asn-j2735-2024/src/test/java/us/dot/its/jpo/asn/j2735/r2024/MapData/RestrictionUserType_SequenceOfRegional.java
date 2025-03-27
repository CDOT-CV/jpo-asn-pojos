package us.dot.its.jpo.asn.j2735.r2024.MapData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.typeCompatibleWith;

import us.dot.its.jpo.asn.j2735.r2024.REGION.Reg_RestrictionUserType;
import org.junit.jupiter.api.Test;

public class RestrictionUserType_SequenceOfRegional {

  @Test
  public void constructorTest() {
    var sor = new RestrictionUserType.SequenceOfRegional();
    assertThat(sor.getItemClass(), typeCompatibleWith(Reg_RestrictionUserType.class));
  }
}
