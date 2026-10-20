package us.dot.its.jpo.asn.runtime.types;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.Test;

public class Asn1BooleanTest {

  @Test
  public void value() {
    var bool = new Asn1Boolean();
    assertThat(bool.getValue(), equalTo(false));
    bool.setValue(true);
    assertThat(bool.getValue(), equalTo(true));
    assertThat(bool.toString(), equalTo("true"));
  }

  @Test
  public void equalsAndHashCode() {
    var yes = new Asn1Boolean(true);
    assertThat(yes, equalTo(yes));
    assertThat(yes, equalTo(new Asn1Boolean(true)));
    assertThat(yes.hashCode(), equalTo(new Asn1Boolean(true).hashCode()));
    assertThat(yes, not(equalTo(new Asn1Boolean(false))));
    assertThat(yes, not(equalTo(null)));
    assertThat(yes, not(equalTo(Boolean.TRUE)));
  }
}
