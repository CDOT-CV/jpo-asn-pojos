package us.dot.its.jpo.asn.testgenerator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;
import tools.jackson.core.JacksonException;

@Command(
    name = "java -jar testgen-cli",
    version = "testgen 1.0",
    mixinStandardHelpOptions = true,
    sortOptions = false,
    sortSynopsis = false)
public class TestGenCli implements Runnable {

  // Use the picocli PrintWriter for output to make testing easier
  @Spec CommandSpec spec;

  private CommandLine cmd() {
    return spec.commandLine();
  }

  @Option(
      names = {"-m", "--module"},
      required = true,
      description = "REQUIRED. ASN.1 Module name.  For example: MapData, Common.")
  String module;

  @Option(
      names = {"-p", "--pdu"},
      required = true,
      description =
          "REQUIRED. Protocol Data Unit (PDU).  Name of the class to generate an example of."
              + " For example: MapData, BSMcoreData.")
  String pdu;

  @Option(
      names = {"-xo", "--exclude-optional"},
      defaultValue = "false",
      description =
          "Exclude all optional properties from SEQUENCE types. All properties except "
              + "'regional' are included if this flag is not present.")
  boolean excludeOptional;

  @Option(
      names = {"-io", "--include-optional"},
      description =
          "List of names of optional SEQUENCE properties to include.  If set implies "
              + "'-xo' and excludes other optional properties not listed.  For example, use "
              + "'-io=intersections' with MapData to generate a MAP with only an 'intersections' "
              + "element.")
  Set<String> includeOptional;

  @Option(
      names = {"-xp", "--exclude-pdus"},
      description =
          "Module.PDUs to exclude from sequences (class name eg. 'MapData.PreemptPriorityList', "
              + " 'REGION.Reg_MovementEvent')")
  Set<String> excludePdus;

  @Option(
      names = {"-s", "--sequence-of-limit"},
      defaultValue = "5",
      description =
          "Limit the number of items in SEQUENCE-OF types. Default 5. Must be at least 2.")
  int sequenceOfLimit;

  @Option(
      names = {"-x", "--xer-output-file"},
      description = "Output file path for the XER file.")
  File xerOutputFile;

  @Option(
      names = {"-j", "--jer-output-file"},
      description = "Output file path for the JER file.")
  File jerOutputFile;

  @Option(
      names = {"-r", "--regional"},
      defaultValue = "false",
      description =
          "Include fields named 'regional' and other regional extensions."
              + " Omitted by default if this flag is not present.")
  boolean regional;

  public static void main(String[] args) {
    int exitCode = new CommandLine(new TestGenCli()).execute(args);
    System.exit(exitCode);
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public void run() {
    cmd().getOut().println("ASN.1 POJO Test Generator");
    cmd().getOut().println("Module: " + module);
    cmd().getOut().println("PDU: " + pdu);
    cmd().getOut().println("SEQUENCE-OF limit: " + sequenceOfLimit);
    if (sequenceOfLimit < 2) {
      cmd().getErr().println("Sequence of limit must be greater than or equal to 2");
    }
    cmd().getOut().println("Include regional extensions: " + regional);
    cmd().getOut().println("Exclude PDUs: " + excludePdus);
    if (includeOptional == null) {
      includeOptional = new HashSet<>();
    }
    if (!includeOptional.isEmpty()) {
      excludeOptional = true;
      for (var opt : includeOptional) {
        cmd().getOut().println("Include optional: " + opt);
      }
    }
    cmd().getOut().println("Exclude optional: " + excludeOptional);
    final String fullPdu = fullyQualified(module, pdu);
    cmd().getOut().printf("Fully qualified class name = %s%n", fullPdu);
    Set<Class<?>> excludePduClasses = new HashSet<>();
    if (excludePdus != null && !excludePdus.isEmpty()) {
      for (var excludePdu : excludePdus) {
        var excludeClass = getClass(fullyQualified(excludePdu));
        excludePduClasses.add(excludeClass);
        cmd().getOut().println("Exclude: " + excludeClass.getName());
      }
    }

    RandomGenerator<?> gen =
        RandomGenerator.getGeneratorForType(
            getClass(fullPdu),
            new GeneratorOptions(
                pdu,
                sequenceOfLimit,
                regional,
                excludePduClasses,
                spec,
                excludeOptional,
                includeOptional));
    if (gen == null) {
      throw new RuntimeException(String.format("Generator for type %s not found", fullPdu));
    }
    var seq = gen.createRandom();
    try {
      var xml = gen.toXml(seq);
      if (xerOutputFile != null) {
        FileUtils.writeStringToFile(xerOutputFile, xml, StandardCharsets.UTF_8);
        cmd().getOut().printf("Saved XER to file %s%n", xerOutputFile.getAbsolutePath());
      } else {
        cmd().getOut().println("XER:");
        cmd().getOut().println(xml);
      }
      var json = gen.toJson(seq);
      if (jerOutputFile != null) {
        FileUtils.writeStringToFile(jerOutputFile, json, StandardCharsets.UTF_8);
        cmd().getOut().printf("Saved JER to file %s%n", jerOutputFile.getAbsolutePath());
      } else {
        cmd().getOut().println("JER:");
        cmd().getOut().println(json);
      }
    } catch (IOException | JacksonException e) {
      cmd().getErr().println(e.getMessage());
      ExceptionUtils.printRootCauseStackTrace(e);
      throw new RuntimeException(e);
    }
  }

  static String fullyQualified(final String module, final String pdu) {
    return String.format("us.dot.its.jpo.asn.j2735.r2024.%s.%s", module, pdu);
  }

  static String fullyQualified(final String modulePdu) {
    return String.format("us.dot.its.jpo.asn.j2735.r2024.%s", modulePdu);
  }

  @SuppressWarnings({"rawtypes"})
  static Class getClass(final String fullyQualifiedName) {
    Class clazz;
    try {
      clazz = Class.forName(fullyQualifiedName);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return clazz;
  }
}
