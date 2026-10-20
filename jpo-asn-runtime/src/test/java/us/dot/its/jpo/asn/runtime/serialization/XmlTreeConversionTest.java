package us.dot.its.jpo.asn.runtime.serialization;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.dataformat.xml.XmlMapper;
import us.dot.its.jpo.asn.runtime.examples.FruitEnum;
import us.dot.its.jpo.asn.runtime.types.Asn1Bitstring;
import us.dot.its.jpo.asn.runtime.types.Asn1Boolean;
import us.dot.its.jpo.asn.runtime.types.Asn1Enumerated;

@Slf4j
class XmlTreeConversionTest {

    @Test
    void xmlTreeConversionPreservesBitString() {
        var mapper = new XmlMapper();
        String xml = "<Envelope><bits>10000000</bits></Envelope>";

        Envelope direct = mapper.readValue(xml, Envelope.class);
        assertThat(direct.bits.binaryString(), equalTo("10000000"));

        Envelope fromTree = mapper.treeToValue(mapper.readTree(xml), Envelope.class);
        assertThat(fromTree.bits.binaryString(), equalTo("10000000"));
    }

    @Test
    void xmlTreeConversionPreservesBoolean() {
        var mapper = new XmlMapper();
        String xml = "<Envelope><flag><true/></flag></Envelope>";

        Envelope direct = mapper.readValue(xml, Envelope.class);
        assertTrue(direct.flag.getValue());

        Envelope fromTree = mapper.treeToValue(mapper.readTree(xml), Envelope.class);
        assertTrue(fromTree.flag.getValue());
    }

    @Test
    void xmlTreeConversionPreservesEnum() {
        XmlMapper mapper = new XmlMapper();
        String xml = " <Container><fruit><banana/></fruit></Container>";

        Container direct = mapper.readValue(xml, Container.class);
        log.info(direct.toString());
        assertThat(direct.fruit, equalTo(FruitEnum.BANANA));

        Container fromTree = mapper.treeToValue(mapper.readTree(xml), Container.class);
        assertThat(fromTree.fruit, equalTo(FruitEnum.BANANA));
    }

    @ToString
    public static class Container {
        public FruitEnum fruit;
    }

    public static class Envelope {
        public Bits bits;
        public Asn1Boolean flag;
    }

    public static class Bits extends Asn1Bitstring {
        public Bits() {
            super(8, false, new String[0]);
        }
    }


}