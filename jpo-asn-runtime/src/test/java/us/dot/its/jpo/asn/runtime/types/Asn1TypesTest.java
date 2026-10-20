package us.dot.its.jpo.asn.runtime.types;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

import org.junit.jupiter.api.Test;

/**
 * Tests for simple ASN.1 types that hold a value but have no other behavior
 */
public class Asn1TypesTest {

  @Test
  public void objectIdentifier() {
    assertThat(new Asn1ObjectIdentifier().getValue(), nullValue());
    var oid = new Asn1ObjectIdentifier("1.2.840");
    assertThat(oid.getValue(), equalTo("1.2.840"));
    oid.setValue("1.3.6");
    assertThat(oid.getValue(), equalTo("1.3.6"));
  }

  @Test
  public void relativeOid() {
    assertThat(new Asn1RelativeOID("5.6").getValue(), equalTo("5.6"));
  }

  @Test
  public void typesWithoutExtensionMarker() {
    assertThat(new Asn1Null().hasExtensionMarker(), equalTo(false));
    assertThat(new Asn1GeneralizedTime().hasExtensionMarker(), equalTo(false));
    assertThat(new UnknownType().hasExtensionMarker(), equalTo(false));
  }

  @Test
  public void field() {
    var value = new Asn1Integer(7);
    var field = new Asn1Field("count", value, true, 2, Asn1Integer.class);
    assertThat(field.name(), equalTo("count"));
    assertThat(field.value(), sameInstance(value));
    assertThat(field.optional(), equalTo(true));
    assertThat(field.tag(), equalTo(2));
    assertThat(field.type(), equalTo(Asn1Integer.class));
  }
}
