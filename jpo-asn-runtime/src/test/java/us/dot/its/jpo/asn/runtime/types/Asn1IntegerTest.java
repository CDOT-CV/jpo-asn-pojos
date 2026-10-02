package us.dot.its.jpo.asn.runtime.types;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import us.dot.its.jpo.asn.runtime.examples.AInteger;

public class Asn1IntegerTest {

  @Test
  public void bounds() {
    var unbounded = new Asn1Integer(5);
    assertThat(unbounded.getValue(), equalTo(5L));
    assertThat(unbounded.getLowerBound(), equalTo(Long.MIN_VALUE));
    assertThat(unbounded.getUpperBound(), equalTo(Long.MAX_VALUE));

    var bounded = new Asn1Integer(0, 100);
    assertThat(bounded.getLowerBound(), equalTo(0L));
    assertThat(bounded.getUpperBound(), equalTo(100L));
  }

  @Test
  public void compareTo() {
    var five = new Asn1Integer(5);
    assertThat(five.compareTo(new Asn1Integer(6)), lessThan(0));
    assertThat(five.compareTo(new Asn1Integer(4)), greaterThan(0));
    assertThat(five.compareTo(new Asn1Integer(5)), equalTo(0));
    assertThat(five.compareTo(null), lessThan(0));
  }

  @Test
  public void equalsAndHashCode() {
    var five = new Asn1Integer(5);
    assertThat(five, equalTo(five));
    assertThat(five, equalTo(new Asn1Integer(5)));
    assertThat(five.hashCode(), equalTo(new Asn1Integer(5).hashCode()));
    assertThat(five, not(equalTo(new Asn1Integer(6))));
    assertThat(five, not(equalTo(null)));
    // Different classes with the same value are not equal
    assertThat(five, not(equalTo(new AInteger(5))));
  }

  @Test
  public void stringAndName() {
    var five = new Asn1Integer(5);
    assertThat(five.toString(), equalTo("5"));
    assertThat(five.name(), equalTo(Optional.empty()));
  }
}
