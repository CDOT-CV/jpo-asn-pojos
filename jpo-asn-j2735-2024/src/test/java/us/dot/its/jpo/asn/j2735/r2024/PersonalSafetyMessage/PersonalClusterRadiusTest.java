package us.dot.its.jpo.asn.j2735.r2024.PersonalSafetyMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class PersonalClusterRadiusTest {

  @Test
  public void constructorTest() {
    var cr = new PersonalClusterRadius(2000L);
    assertThat(cr.getValue(), equalTo(2000L));
  }

}
