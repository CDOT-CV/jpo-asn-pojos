package us.dot.its.jpo.asn.testgenerator;

import java.util.Set;

public record GeneratorOptions (
    String pdu,
    int limit,
    boolean regional,
    Set<Class<?>> excludePdus) {

  public GeneratorOptions withPdu(String newPdu) {
    return new GeneratorOptions(newPdu, limit, regional, excludePdus);
  }

}
