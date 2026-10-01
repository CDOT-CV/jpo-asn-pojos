package us.dot.its.jpo.asn.runtime.utils;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static us.dot.its.jpo.asn.runtime.utils.XmlUtils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

public class XmlUtilsTest {

  @ParameterizedTest
  @ValueSource(strings = {
      "<Example><A/><B/></Example>",
      "<Example><A>text</A><B>text</B></Example>",
      "<Example> <A> text </A><B> text </B> </Example>",
      " <Example><A/><B/></Example> ",
      " <Example> <A/><B/> </Example> ",
      """
          <Example>
              <A/>
              <B/>
          </Example>"""
  })
  public void unwrapTest(final String xml) {
    final String unwrapped = unwrap(xml);
    assertThat(xml, isIdenticalTo(BEGIN + unwrapped + END)
        .ignoreWhitespace().ignoreElementContentWhitespace());
  }

  @ParameterizedTest
  @MethodSource("unwrapExactValues")
  public void unwrapExactTest(final String xml, final String expected) {
    assertThat(unwrap(xml), equalTo(expected));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "<A>x</B>",
      "<A>x",
      "x<A/>",
      "<?xml version=\"1.0\"<A/>",
      "<!-- comment <A/>",
      "A"
  })
  public void unwrapNotWellFormedTest(final String xml) {
    assertThrows(IllegalArgumentException.class, () -> unwrap(xml));
  }

  @Test
  public void unwrapNullTest() {
    assertThat(unwrap(null), nullValue());
  }

  private static Stream<Arguments> unwrapExactValues() {
    return Stream.of(
        Arguments.of("<Example xmlns=\"urn:x\" a=\"1\"><A/></Example>", "<A/>"),
        Arguments.of("<Example/>", ""),
        Arguments.of("<Example />", ""),
        Arguments.of("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Example><A/></Example>", "<A/>"),
        Arguments.of("<?xml version=\"1.0\"?>\n<!-- comment --><Example><A/></Example>", "<A/>"),
        Arguments.of("<Example><A/></Example >", "<A/>"),
        Arguments.of("<Example><Example>x</Example></Example>", "<Example>x</Example>"),
        Arguments.of("<Example></Example>", ""),
        Arguments.of("   ", "")
    );
  }

  final static String EXAMPLE = "Example";
  final static String BEGIN = "<" + EXAMPLE + ">";
  final static String END = "</" + EXAMPLE + ">";

}
