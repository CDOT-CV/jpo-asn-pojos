package us.dot.its.jpo.asn.runtime.types;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import us.dot.its.jpo.asn.runtime.examples.ExampleBitstring;
import us.dot.its.jpo.asn.runtime.examples.ExampleBitstringExtensible;
import us.dot.its.jpo.asn.runtime.examples.ExampleBitstringFewNamedBits;
import us.dot.its.jpo.asn.runtime.examples.ExampleBitstringNamedExtensions;
import us.dot.its.jpo.asn.runtime.examples.ExampleBitstringVariableSize;

public class Asn1BitstringTest {

  @Test
  public void fixedSizeProperties() {
    var bitstring = new ExampleBitstring();
    assertThat(bitstring.size(), equalTo(16));
    assertThat(bitstring.upperBound(), equalTo(16));
    assertThat(bitstring.noNamedValues(), equalTo(false));
    assertThat(bitstring.variableSize(), equalTo(false));
    assertThat(bitstring.hasExtensionMarker(), equalTo(false));
    assertThat(bitstring.actualSize(), equalTo(16));
  }

  @Test
  public void setByName() {
    var bitstring = new ExampleBitstring();
    bitstring.set("from022-5to045-0degrees", true);
    assertThat(bitstring.get(1), equalTo(true));
    assertThat(bitstring.binaryString(), equalTo("0100000000000000"));
    assertThat(bitstring.toString(), equalTo("0100000000000000"));
  }

  @Test
  public void hexStringIsPaddedToSize() {
    var bitstring = new ExampleBitstring();
    bitstring.set(1, true);
    assertThat(bitstring.hexString(), equalTo("4000"));
  }

  @Test
  public void setUnknownName() {
    var bitstring = new ExampleBitstring();
    var ex = assertThrows(IllegalArgumentException.class, () -> bitstring.set("unknown", true));
    assertThat(ex.getMessage(), containsString("unknown"));
  }

  @Test
  public void setUnknownNameWithFewNamedBits() {
    var bitstring = new ExampleBitstringFewNamedBits();
    var ex = assertThrows(IllegalArgumentException.class, () -> bitstring.set("unknown", true));
    assertThat(ex.getMessage(), containsString("unknown"));
  }

  @Test
  public void nameOfUnnamedBit() {
    assertThat(new ExampleBitstringFewNamedBits().name(4), nullValue());
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 16})
  public void nameOutOfRange(int index) {
    var bitstring = new ExampleBitstring();
    assertThrows(IllegalArgumentException.class, () -> bitstring.name(index));
  }

  @Test
  public void variableSizeProperties() {
    var bitstring = new ExampleBitstringVariableSize();
    assertThat(bitstring.size(), equalTo(1));
    assertThat(bitstring.upperBound(), equalTo(16));
    assertThat(bitstring.noNamedValues(), equalTo(true));
    assertThat(bitstring.variableSize(), equalTo(true));
    assertThat(bitstring.actualSize(), equalTo(0));
  }

  @Test
  public void variableSizeGrowsWhenBitsAreSet() {
    var bitstring = new ExampleBitstringVariableSize();
    bitstring.set(4, true);
    bitstring.set(2, true);
    assertThat(bitstring.actualSize(), equalTo(5));
    assertThat(bitstring.binaryString(), equalTo("00101"));
    assertThat(bitstring.hexString(), equalTo("28"));
  }

  @Test
  public void variableSizeFromBinaryString() {
    var bitstring = new ExampleBitstringVariableSize();
    bitstring.fromBinaryString(" 101 ");
    assertThat(bitstring.actualSize(), equalTo(3));
    assertThat(bitstring.binaryString(), equalTo("101"));
  }

  @Test
  public void fromBinaryStringTooShort() {
    var bitstring = new ExampleBitstring();
    var ex = assertThrows(IllegalArgumentException.class, () -> bitstring.fromBinaryString("101"));
    assertThat(ex.getMessage(), containsString("too short"));
  }

  @Test
  public void fromBinaryStringNull() {
    var bitstring = new ExampleBitstring();
    bitstring.set(0, true);
    bitstring.fromBinaryString(null);
    assertThat(bitstring.get(0), equalTo(false));
  }

  @ParameterizedTest
  @CsvSource({
      "28, 5, 00101",
      "28, , 00101000"
  })
  public void variableSizeFromHexString(String hex, Integer length, String expectedBinary) {
    var bitstring = new ExampleBitstringVariableSize();
    bitstring.fromHexString(hex, length);
    assertThat(bitstring.binaryString(), equalTo(expectedBinary));
  }

  @Test
  public void fromHexStringFixedSize() {
    var bitstring = new ExampleBitstring();
    bitstring.fromHexString("4000");
    assertThat(bitstring.binaryString(), equalTo("0100000000000000"));
    assertThat(bitstring.actualSize(), equalTo(16));
  }

  @Test
  public void fromHexStringNull() {
    var bitstring = new ExampleBitstringVariableSize();
    bitstring.fromHexString("FF");
    bitstring.fromHexString(null);
    assertThat(bitstring.get(0), equalTo(false));
  }

  @ParameterizedTest
  @CsvSource({
      "111111, 111111, FC",
      "111101, 111101, F4",
      "111100, 1111, F0",
      "11110, 1111, F0"
  })
  public void namedExtensions(String input, String expectedBinary, String expectedHex) {
    var bitstring = new ExampleBitstringNamedExtensions();
    assertThat(bitstring.variableSize(), equalTo(false));
    bitstring.fromBinaryString(input);
    assertThat(bitstring.binaryString(), equalTo(expectedBinary));
    assertThat(bitstring.hexString(), equalTo(expectedHex));
  }

  @Test
  public void extensible() {
    var bitstring = new ExampleBitstringExtensible();
    assertThat(bitstring.hasExtensionMarker(), equalTo(true));
    assertThat(bitstring.variableSize(), equalTo(true));
    bitstring.set("a", true);
    assertThat(bitstring.actualSize(), equalTo(1));
    assertThat(bitstring.binaryString(), equalTo("1000"));
    assertThat(bitstring.hexString(), equalTo("80"));
  }

  @Test
  public void equalsAndHashCode() {
    var first = new ExampleBitstring();
    first.set(3, true);
    var second = new ExampleBitstring();
    second.set(3, true);
    var different = new ExampleBitstring();
    different.set(4, true);
    assertThat(first, equalTo(second));
    assertThat(first.hashCode(), equalTo(second.hashCode()));
    assertThat(first, not(equalTo(different)));
    assertThat(first, not(equalTo(null)));
    assertThat(first, not(equalTo("0001")));
  }
}
