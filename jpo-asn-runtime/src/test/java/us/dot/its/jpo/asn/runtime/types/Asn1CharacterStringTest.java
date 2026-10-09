package us.dot.its.jpo.asn.runtime.types;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class Asn1CharacterStringTest {

  @Test
  public void unboundedValue() {
    var str = new IA5String("hello");
    assertThat(str.getValue(), equalTo("hello"));
    assertThat(str.toString(), equalTo("hello"));
    assertThat(str.getMinLength(), equalTo(0));
    assertThat(str.getMaxLength(), equalTo(Integer.MAX_VALUE));
  }

  @Test
  public void nullValueIsAllowed() {
    var str = new IA5String(2, 4);
    str.setValue(null);
    assertThat(str.getValue(), nullValue());
  }

  @ParameterizedTest
  @ValueSource(strings = {"ab", "abcd"})
  public void valueWithinBounds(String value) {
    var str = new IA5String(2, 4);
    str.setValue(value);
    assertThat(str.getValue(), equalTo(value));
  }

  @ParameterizedTest
  @ValueSource(strings = {"a", "abcde"})
  public void valueOutOfBounds(String value) {
    var str = new IA5String(2, 4);
    var ex = assertThrows(IllegalArgumentException.class, () -> str.setValue(value));
    assertThat(ex.getMessage(), containsString("invalid length"));
  }
}
