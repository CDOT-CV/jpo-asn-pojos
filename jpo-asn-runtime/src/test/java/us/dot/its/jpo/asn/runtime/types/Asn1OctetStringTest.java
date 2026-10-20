package us.dot.its.jpo.asn.runtime.types;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@Slf4j
public class Asn1OctetStringTest {

  @Test
  public void testValidateUnboundedLengthOctetString() {
    Asn1OctetString aos = new Asn1OctetString();
    Exception ex = null;
    try {
      aos.setValue("FF");
    } catch (IllegalArgumentException iae) {
      log.error("Validation error", iae);
      ex = iae;
    }
    assertThat(ex, nullValue());
  }

  @Test
  public void setValue() {
    var aos = new Asn1OctetString(1, 4);
    aos.setValue(" 0aFf ");
    assertThat(aos.getOctets(), equalTo(new byte[] {0x0A, (byte) 0xFF}));
    assertThat(aos.getValue(), equalTo("0AFF"));
    assertThat(aos.toString(), equalTo("0AFF"));
    assertThat(aos.getMinLength(), equalTo(1));
    assertThat(aos.getMaxLength(), equalTo(4));
  }

  @Test
  public void emptyValue() {
    var aos = new Asn1OctetString();
    assertThat(aos.getOctets(), nullValue());
    assertThat(aos.getValue(), equalTo(""));
  }

  @Test
  public void setOctetsCopiesTheArray() {
    var aos = new Asn1OctetString();
    byte[] octets = {1, 2};
    aos.setOctets(octets);
    octets[0] = 9;
    assertThat(aos.getValue(), equalTo("0102"));
  }

  @Test
  public void setOctetsNull() {
    var aos = new Asn1OctetString();
    aos.setValue("01");
    aos.setOctets(null);
    assertThat(aos.getOctets(), nullValue());
  }

  @ParameterizedTest
  @CsvSource({
      "0, 1, 0102, out of bounds",
      "2, 3, 01, out of bounds",
      "0, 4, 012, even number"
  })
  public void setValueInvalid(int minLength, int maxLength, String value, String expectedMessage) {
    var aos = new Asn1OctetString(minLength, maxLength);
    var ex = assertThrows(IllegalArgumentException.class, () -> aos.setValue(value));
    assertThat(ex.getMessage(), containsString(expectedMessage));
  }

  @Test
  public void setValueNull() {
    var aos = new Asn1OctetString();
    var ex = assertThrows(IllegalArgumentException.class, () -> aos.setValue(null));
    assertThat(ex.getMessage(), containsString("cannot be null"));
  }

  @ParameterizedTest
  @CsvSource({
      "2, 3, 1",
      "0, 1, 2"
  })
  public void setOctetsOutOfBounds(int minLength, int maxLength, int numBytes) {
    var aos = new Asn1OctetString(minLength, maxLength);
    var ex = assertThrows(IllegalArgumentException.class, () -> aos.setOctets(new byte[numBytes]));
    assertThat(ex.getMessage(), containsString("out of bounds"));
  }

  @Test
  public void validateOctetLengthNull() {
    var aos = new Asn1OctetString();
    var ex = assertThrows(IllegalArgumentException.class, () -> aos.validateOctetLength(null));
    assertThat(ex.getMessage(), containsString("cannot be null"));
  }

}
