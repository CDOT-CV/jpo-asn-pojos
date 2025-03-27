package us.dot.its.jpo.asn.j2735.r2024.BasicSafetyMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import us.dot.its.jpo.asn.j2735.r2024.REGION.Reg_BasicSafetyMessage;
import org.junit.jupiter.api.Test;

public class BasicSafetyMessage_SequenceOfRegionalTest {

  @Test
  public void constructorTest() {
    var seqOfReg = new BasicSafetyMessage.SequenceOfRegional();
    assertThat(seqOfReg, notNullValue());
    assertThat(seqOfReg.getSizeLowerBound(), equalTo(1L));
    assertThat(seqOfReg.getSizeUpperBound(), equalTo(4L));
    assertThat(seqOfReg.getItemClass(), equalTo(Reg_BasicSafetyMessage.class));
  }

}
