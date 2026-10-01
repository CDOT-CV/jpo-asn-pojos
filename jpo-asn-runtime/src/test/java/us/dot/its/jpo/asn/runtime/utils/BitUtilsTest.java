package us.dot.its.jpo.asn.runtime.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class BitUtilsTest {

  @Test
  public void appendBitsToListFromEmptyBitSet() {
    List<Boolean> bits = new ArrayList<>();
    BitUtils.appendBits(bits, new BitSet());
    assertThat(bits, empty());
  }

  @Test
  public void appendBitsToList() {
    List<Boolean> bits = new ArrayList<>(List.of(true));
    BitUtils.appendBits(bits, bitSet("101"));
    assertThat(bits, contains(true, true, false, true));
  }

  @Test
  public void appendBitSetToEmptyBitSet() {
    BitSet second = bitSet("11");
    assertThat(BitUtils.appendBits(new BitSet(), second), sameInstance(second));
  }

  @Test
  public void appendEmptyBitSet() {
    BitSet first = bitSet("11");
    assertThat(BitUtils.appendBits(first, new BitSet()), sameInstance(first));
  }

  @Test
  public void appendBitSets() {
    assertThat(BitUtils.appendBits(bitSet("1"), bitSet("01")), equalTo(bitSet("101")));
  }

  @Test
  public void getBitSet() {
    assertThat(BitUtils.getBitSet(List.of(true, false, true)), equalTo(bitSet("101")));
  }

  @Test
  public void popBits() {
    BitSetPair pair = BitUtils.popBits(bitSet("1101"), 2);
    assertThat(pair.value(), equalTo(bitSet("11")));
    assertThat(pair.remainder(), equalTo(bitSet("01")));
  }

  @ParameterizedTest
  @CsvSource({
      "1, -128",
      "15, -16",
      "-128, 1",
      "0, 0"
  })
  public void reverseBits(byte input, byte expected) {
    assertThat(BitUtils.reverseBits(input), equalTo(expected));
  }

  @Test
  public void reverseBitsArray() {
    assertThat(BitUtils.reverseBits(new byte[] {1, 15}), equalTo(new byte[] {-128, -16}));
  }

  @ParameterizedTest
  @CsvSource({
      "255, -1",
      "256, 0",
      "127, 127"
  })
  public void unsignedByte(int input, byte expected) {
    assertThat(BitUtils.unsignedByte(input), equalTo(expected));
  }

  // BitSet with bit i set where character i of the string is '1'
  private static BitSet bitSet(String binary) {
    BitSet bits = new BitSet();
    for (int i = 0; i < binary.length(); i++) {
      bits.set(i, binary.charAt(i) == '1');
    }
    return bits;
  }
}
