package us.dot.its.jpo.asn.runtime.examples;

import us.dot.its.jpo.asn.runtime.serialization.EnumeratedDeserializer;
import us.dot.its.jpo.asn.runtime.serialization.EnumeratedSerializer;
import us.dot.its.jpo.asn.runtime.types.Asn1Enumerated;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

@Getter
@JsonSerialize(using = FruitEnum.FruitSerializer.class)
@JsonDeserialize(using = FruitEnum.FruitDeserializer.class)
public enum FruitEnum implements Asn1Enumerated {
  APPLE(0, "apple"),
  ORANGE(1, "orange"),
  BANANA(2, "banana");

  private final int index;
  private final String name;

  private FruitEnum(int index, String name) {
    this.index = index;
    this.name = name;
  }

  public static class FruitSerializer extends EnumeratedSerializer<FruitEnum> {

    protected FruitSerializer() {
      super(FruitEnum.class);
    }
  }

  public static class FruitDeserializer extends EnumeratedDeserializer<FruitEnum> {

    protected FruitDeserializer() {
      super(FruitEnum.class);
    }

    @Override
    protected FruitEnum[] listEnumValues() {
      return FruitEnum.values();
    }
  }
}
