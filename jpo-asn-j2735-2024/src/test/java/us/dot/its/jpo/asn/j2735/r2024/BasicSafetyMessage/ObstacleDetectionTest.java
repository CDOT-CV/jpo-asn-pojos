package us.dot.its.jpo.asn.j2735.r2024.BasicSafetyMessage;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import us.dot.its.jpo.asn.j2735.r2024.BaseSerializeTest;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class ObstacleDetectionTest extends BaseSerializeTest<ObstacleDetection> {

  public ObstacleDetectionTest() {
    super(ObstacleDetection.class);
  }

  @Test
  public void xmlRoundTripTest() throws IOException {
    ObstacleDetection od = fromXml(XER);
    assertThat(od, notNullValue());
    String xmlRoundTrip = toXml(od);
    assertThat(xmlRoundTrip, isIdenticalTo(XER).ignoreWhitespace().ignoreElementContentWhitespace());
  }

  @Test
  public void jsonRoundTripTest() throws IOException {
    ObstacleDetection od = fromJson(JER);
    assertThat(od, notNullValue());
    String jsonRoundTrip = toJson(od);
    assertThat(jsonRoundTrip, jsonEquals(JER));
  }

  @Test
  public void xmlToJsonTest() throws IOException {
    ObstacleDetection od = fromXml(XER);
    assertThat(od, notNullValue());
    String json = toJson(od);
    assertThat(json, jsonEquals(JER));
  }

  public static final String XER = """
      <ObstacleDetection>
          <obDist>256</obDist>
          <obDirect>257</obDirect>
          <description>523</description>
          <locationDetails><airport/></locationDetails>
          <dateTime>
              <year>2000</year>
              <month>10</month>
              <day>20</day>
              <hour>5</hour>
              <minute>32</minute>
              <second>7</second>
              <offset>-7</offset>
          </dateTime>
          <vertEvent>
            10111
          </vertEvent>
      </ObstacleDetection>
      """;

  public static final String JER = """
      {
          "obDist": 256,
          "obDirect": 257,
          "description": 523,
          "locationDetails": "airport",
          "dateTime": {
              "year": 2000,
              "month": 10,
              "day": 20,
              "hour": 5,
              "minute": 32,
              "second": 7,
              "offset": -7
          },
          "vertEvent": "B8"
      }
      """;
}
