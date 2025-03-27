package us.dot.its.jpo.asn.j2735.r2024.Common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class HeadingSliceTest {

  @Test
  public void testGetters() {
    var hs = new HeadingSlice();
    hs.fromBinaryString("1111111111111111");
    assertThat(hs.isFrom000_0to022_5degrees(), equalTo(true));
    assertThat(hs.isFrom000_0to022_5degrees(), equalTo(true));
    assertThat(hs.isFrom022_5to045_0degrees(), equalTo(true));
    assertThat(hs.isFrom045_0to067_5degrees(), equalTo(true));
    assertThat(hs.isFrom067_5to090_0degrees(), equalTo(true));
    assertThat(hs.isFrom090_0to112_5degrees(), equalTo(true));
    assertThat(hs.isFrom112_5to135_0degrees(), equalTo(true));
    assertThat(hs.isFrom135_0to157_5degrees(), equalTo(true));
    assertThat(hs.isFrom157_5to180_0degrees(), equalTo(true));
    assertThat(hs.isFrom180_0to202_5degrees(), equalTo(true));
    assertThat(hs.isFrom202_5to225_0degrees(), equalTo(true));
    assertThat(hs.isFrom225_0to247_5degrees(), equalTo(true));
    assertThat(hs.isFrom247_5to270_0degrees(), equalTo(true));
    assertThat(hs.isFrom270_0to292_5degrees(), equalTo(true));
    assertThat(hs.isFrom292_5to315_0degrees(), equalTo(true));
    assertThat(hs.isFrom315_0to337_5degrees(), equalTo(true));
    assertThat(hs.isFrom337_5to360_0degrees(), equalTo(true));
  }

  @Test
  public void testSetters() {
    var hs = new HeadingSlice();
    hs.setFrom000_0to022_5degrees(true);
    hs.setFrom000_0to022_5degrees(true);
    hs.setFrom022_5to045_0degrees(true);
    hs.setFrom045_0to067_5degrees(true);
    hs.setFrom067_5to090_0degrees(true);
    hs.setFrom090_0to112_5degrees(true);
    hs.setFrom112_5to135_0degrees(true);
    hs.setFrom135_0to157_5degrees(true);
    hs.setFrom157_5to180_0degrees(true);
    hs.setFrom180_0to202_5degrees(true);
    hs.setFrom202_5to225_0degrees(true);
    hs.setFrom225_0to247_5degrees(true);
    hs.setFrom247_5to270_0degrees(true);
    hs.setFrom270_0to292_5degrees(true);
    hs.setFrom292_5to315_0degrees(true);
    hs.setFrom315_0to337_5degrees(true);
    hs.setFrom337_5to360_0degrees(true);
    assertThat(hs.binaryString(), equalTo("1111111111111111"));
  }

}
