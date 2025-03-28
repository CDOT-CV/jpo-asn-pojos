package us.dot.its.jpo.asn.runtime.serialization;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import us.dot.its.jpo.asn.runtime.BaseSerializeTest;
import us.dot.its.jpo.asn.runtime.examples.FruitEnum;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class EnumeratedTest extends BaseSerializeTest<FruitEnum> {

  public EnumeratedTest() {
    super(FruitEnum.class);
  }

  @Test
  public void canDeserializeXml() throws IOException {
    FruitEnum fe = fromXml(XML);
    assertThat(fe, allOf(notNullValue(), equalTo(FruitEnum.APPLE)));
  }

  @Test
  public void canDeserializeJson() throws IOException {
    FruitEnum fe = fromJson(JSON);
    assertThat(fe, allOf(notNullValue(), equalTo(FruitEnum.APPLE)));
  }

  @Test
  public void invalidXmlException() {
    MismatchedInputException mie = assertThrows(
      MismatchedInputException.class,
        () -> fromXml(INVALID_XML),
        "Invalid enum item: expect MismatchedInputException"
    );
    log.debug("got expected exception: {}", mie.getMessage());
  }

  @Test
  public void invalidJsonException() {
    MismatchedInputException mie = assertThrows(
        MismatchedInputException.class,
        () -> fromJson(INVALID_JSON),
        "Invalid enum item: expect MismatchedInputException"
    );
    log.debug("got expected exception: {}", mie.getMessage());
  }

  @Test
  public void emptyXml() throws IOException {
    FruitEnum fe = fromXml(EMPTY_XML);
    assertThat(fe, nullValue());
  }

  @Test
  public void nullJson() throws IOException {
    FruitEnum fe = fromJson(NULL_JSON);
    assertThat(fe, nullValue());
  }

  // Note the deserializer can't read an enum as unwrapped xml
  public static final String XML = "<FruitEnum><apple/></FruitEnum>";

  public static final String JSON = """
      "apple"
      """;

  public static final String INVALID_XML = "<FruitEnum><apricot/></FruitEnum>";

  public static final String INVALID_JSON = """
      "apricot"
      """;

  public static final String EMPTY_XML = "<FruitEnum></FruitEnum>";

  public static final String NULL_JSON = "null";

}
