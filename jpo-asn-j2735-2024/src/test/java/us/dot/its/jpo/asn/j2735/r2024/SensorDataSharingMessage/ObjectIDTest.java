package us.dot.its.jpo.asn.j2735.r2024.SensorDataSharingMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the ObjectID class.
 */
public class ObjectIDTest {
  
  @Test
  public void constructorTest() {
    var objectID = new ObjectID(253132L);
    assertThat(objectID, notNullValue());
    assertThat(objectID.getValue(), equalTo(253132L));
  }
}
