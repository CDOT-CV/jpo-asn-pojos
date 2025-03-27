package us.dot.its.jpo.asn.j2735.r2024.BasicSafetyMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class TrailerMassTest {

  @Test
  public void constructorTest() {
    var tm = new TrailerMass(2000L);
    assertThat(tm.getValue(), equalTo(2000L));
  }

}
