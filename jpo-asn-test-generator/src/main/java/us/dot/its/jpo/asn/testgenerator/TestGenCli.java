package us.dot.its.jpo.asn.testgenerator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "testgen-cli", version = "testgen 1.0", mixinStandardHelpOptions = true,
  sortOptions = false, sortSynopsis = false)
public class TestGenCli implements Runnable {

  @Option(names = {"-m", "--module"}, required = true,
      description = "REQUIRED. ASN.1 Module name.  For example: MapData, Common.")
  String module;

  @Option(names = {"-p", "--pdu"}, required = true,
      description = "REQUIRED. Protocol Data Unit (PDU).  Name of the class to generate an example of"
          + ", qualified by the module. For example: MapData, BSMCoreData.")
  String pdu;

  @Option(names = {"-s", "--sequence-of-limit"}, defaultValue = "5",
  description = "Limit the number of items in SEQUENCE-OF types")
  int sequenceOfLimit;

  @Option(names = {"-x", "--xer-output-file"},
      description="Output file path for the XER file.")
  File xerOutputFile;

  @Option(names = {"-j", "--jer-output-file"},
      description="Output file path for the JER file.")
  File jerOutputFile;

  @Option(names = {"-r", "--regional"}, defaultValue="false",
    description = "Include fields named 'regional' and other regional extensions."
        + " Omitted by default if this flag is not present.")
  boolean regional;


  public static void main(String[] args) {
    int exitCode = new CommandLine(new TestGenCli()).execute(args);
    System.exit(exitCode);
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public void run() {
    System.out.println("ASN.1 POJO Test Generator");
    System.out.println("Module: " + module);
    System.out.println("PDU: " + pdu);
    System.out.println("SEQUENCE-OF limit: " + sequenceOfLimit);
    System.out.println("Include regional extensions: " + regional);
    final String fullPdu = String.format("us.dot.its.jpo.asn.j2735.r2024.%s.%s", module, pdu);
    System.out.printf("Fully qualified class name = %s%n", fullPdu);

    Class clazz;
    try {
      clazz = Class.forName(fullPdu);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    RandomGenerator<?> gen = RandomGenerator.getGeneratorForType(clazz, sequenceOfLimit, regional);
    if (gen == null) {
      throw new RuntimeException(String.format("Generator for type %s not found", fullPdu));
    }
    var seq = gen.createRandom();
    try {
      var xml = gen.toXml(seq);
      if (xerOutputFile != null) {
          FileUtils.writeStringToFile(xerOutputFile, xml, StandardCharsets.UTF_8);
          System.out.printf("Saved XER to file %s%n", xerOutputFile.getAbsolutePath());
      } else {
        System.out.println("XER:");
        System.out.println(xml);
      }
      var json = gen.toJson(seq);
      if (jerOutputFile != null) {
        FileUtils.writeStringToFile(jerOutputFile, json, StandardCharsets.UTF_8);
        System.out.printf("Saved JER to file %s%n", jerOutputFile.getAbsolutePath());
      } else {
        System.out.println("JER:");
        System.out.println(json);
      }
    } catch (IOException e) {
      System.err.println(e.getMessage());
      ExceptionUtils.printRootCauseStackTrace(e);
      throw new RuntimeException(e);
    }
  }
}
