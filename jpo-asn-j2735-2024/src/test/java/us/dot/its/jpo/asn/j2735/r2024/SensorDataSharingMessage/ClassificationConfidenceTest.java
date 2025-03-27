package us.dot.its.jpo.asn.j2735.r2024.SensorDataSharingMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the ClassificationConfidence class.
 */
public class ClassificationConfidenceTest {
  
  @Test
  public void constructorTest() {
    var classificationConfidence = new ClassificationConfidence(5L);
    assertThat(classificationConfidence, notNullValue());
    assertThat(classificationConfidence.getValue(), equalTo(5L));
  }
}
