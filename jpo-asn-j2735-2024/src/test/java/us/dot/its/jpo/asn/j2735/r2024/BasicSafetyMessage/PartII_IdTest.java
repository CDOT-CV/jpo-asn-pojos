package us.dot.its.jpo.asn.j2735.r2024.BasicSafetyMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class PartII_IdTest {

  @Test
  public void constructorTest() {
    var p2id = new PartII_Id(1L);
    assertThat(p2id.getValue(), equalTo(1L));
  }

}
