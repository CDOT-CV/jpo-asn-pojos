package us.dot.its.jpo.asn.j2735.r2024.ManeuverSharingAndCoordinatingMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import us.dot.its.jpo.asn.j2735.r2024.BaseBitstringTest;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TemporaryIDPointerTest extends BaseBitstringTest<TemporaryIDPointer> {

  private static Stream<Arguments> getTestValues() {
    return testValues(new String[] {
       "1",
       "0",
       "11111111111111111111111111111111"
    });
  }

  @Test
  public void testBinarySize1() {
    var tip = new TemporaryIDPointer();
    tip.fromBinaryString("1");
    assertThat(tip.getLowerBound(), equalTo(1));
    assertThat(tip.get(0), equalTo(true));
  }

  @Test
  public void testBinarySize32() {
    var tip = new TemporaryIDPointer();
    tip.fromBinaryString("11111111111111111111111111111111");
    for (int i = 0; i < 32; i++) {
      assertThat(tip.get(i), equalTo(true));
    }
  }

  @Test
  public void testHexSize1() {
    var tip = new TemporaryIDPointer();
    tip.fromHexString("80");
    assertThat(tip.get(0), equalTo(true));
  }

  @Test
  public void testHexSize32() {
    var tip = new TemporaryIDPointer();
    tip.fromHexString("FFFFFFFF");
    for (int i = 0; i < 32; i++) {
      assertThat(tip.get(i), equalTo(true));
    }
  }

  @Test
  public void setBitsTest() {
    var tip = new TemporaryIDPointer();
    tip.set(0, true);
    tip.set(31, true);
    assertThat(tip.get(0), equalTo(true));
    assertThat(tip.get(31), equalTo(true));
    assertThat(tip.binaryString(), equalTo("10000000000000000000000000000001"));
    assertThat(tip.hexString(), equalTo("80000001"));
  }

}
