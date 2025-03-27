package us.dot.its.jpo.asn.j2735.r2024.BasicSafetyMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import us.dot.its.jpo.asn.j2735.r2024.REGION.Reg_SupplementalVehicleExtensions;
import org.junit.jupiter.api.Test;

public class SupplementalVehicleExtensions_SequenceOfRegionalTest {

  @Test
  public void constructorTest() {
    var seqReg = new SupplementalVehicleExtensions.SequenceOfRegional();
    assertThat(seqReg, notNullValue());
    assertThat(seqReg.getSizeLowerBound(), equalTo(1L));
    assertThat(seqReg.getSizeUpperBound(), equalTo(4L));
    assertThat(seqReg.getItemClass(), equalTo(Reg_SupplementalVehicleExtensions.class));
  }

}
