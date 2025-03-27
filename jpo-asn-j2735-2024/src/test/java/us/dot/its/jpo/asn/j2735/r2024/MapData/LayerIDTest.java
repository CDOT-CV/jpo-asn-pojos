package us.dot.its.jpo.asn.j2735.r2024.MapData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class LayerIDTest {

  @Test
  public void constructorTest() {
    var layerID = new LayerID(10L);
    assertThat(layerID.getValue(), equalTo(10L));
  }
}
