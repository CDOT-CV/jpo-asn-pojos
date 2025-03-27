package us.dot.its.jpo.asn.j2735.r2024.MapData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

public class PreemptPriorityListTest {

  @Test
  public void constructorTest() {
    var ppl = new PreemptPriorityList();
    assertThat(ppl, notNullValue());
    assertThat(ppl.getSizeLowerBound(), equalTo(1L));
    assertThat(ppl.getSizeUpperBound(), equalTo(32L));
  }

}
