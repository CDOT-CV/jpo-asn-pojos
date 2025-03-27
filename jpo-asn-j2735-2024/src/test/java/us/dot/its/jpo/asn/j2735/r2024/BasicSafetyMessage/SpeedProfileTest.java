package us.dot.its.jpo.asn.j2735.r2024.BasicSafetyMessage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import us.dot.its.jpo.asn.j2735.r2024.BaseSerializeTest;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class SpeedProfileTest extends BaseSerializeTest<SpeedProfile> {

  public SpeedProfileTest() {
    super(SpeedProfile.class);
  }

  @Test
  public void deserializeXmlTest() throws IOException {
    SpeedProfile sp = fromXml(XER);
    assertThat(sp, notNullValue());
    final String xml = toXml(sp);
    assertThat(xml, isIdenticalTo(XER).ignoreWhitespace());
  }

  public static final String XER = """
      <SpeedProfile>
          <speedReports>
              <SpeedProfileMeasurement>21</SpeedProfileMeasurement>
              <SpeedProfileMeasurement>9</SpeedProfileMeasurement>
              <SpeedProfileMeasurement>24</SpeedProfileMeasurement>
              <SpeedProfileMeasurement>0</SpeedProfileMeasurement>
              <SpeedProfileMeasurement>7</SpeedProfileMeasurement>
          </speedReports>
      </SpeedProfile>
      """;
}
