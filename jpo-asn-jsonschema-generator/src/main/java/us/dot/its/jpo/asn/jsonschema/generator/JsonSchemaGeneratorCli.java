package us.dot.its.jpo.asn.jsonschema.generator;

import static us.dot.its.jpo.asn.jsonschema.generator.Utils.getClassFromName;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

@Command(name = "java -jar schemagen-cli.jar")
public class JsonSchemaGeneratorCli implements Runnable {

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
          "REQUIRED. Protocol Data Unit (PDU).  Name of the class to generate a JSON Schema for."
              + " For example: MapData, BSMcoreData.")
  String pdu;

  @Option(names = {"-o", "--outfile"},
  description = "Output file")
  File outfile;

  public static void main(String[] args) {
    int exitCode = new CommandLine(new JsonSchemaGeneratorCli()).execute(args);
    System.exit(exitCode);
  }

  @Override
  public void run() {
    cmd().getOut().println("ASN.1 JSON Schema Generator");
    cmd().getOut().println("Module: " + module);
    cmd().getOut().println("PDU: " + pdu);

    final String fullPdu = fullyQualified(module, pdu);
    cmd().getOut().printf("Fully qualified class name = %s%n", fullPdu);
    var clazz = getClassFromName(fullPdu);
    cmd().getOut().printf("Class: %s%n", clazz.getName());

    var generator = new JsonSchemaGenerator(clazz);
    try {
      String schema = generator.generate();
      if (outfile != null) {
        FileUtils.writeStringToFile(outfile, schema, StandardCharsets.UTF_8);
        cmd().getOut().printf("Saved json schema to file: %s%n", outfile.getAbsolutePath());
      } else {
        cmd().getOut().println(schema);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  static String fullyQualified(final String module, final String pdu) {
    return String.format("us.dot.its.jpo.asn.j2735.r2024.%s.%s", module, pdu);
  }

  static String fullyQualified(final String modulePdu) {
    return String.format("us.dot.its.jpo.asn.j2735.r2024.%s", modulePdu);
  }

//  @SuppressWarnings({"rawtypes"})
//  static Class getClass(final String fullyQualifiedName) {
//    Class clazz;
//    try {
//      clazz = Class.forName(fullyQualifiedName);
//    } catch (ClassNotFoundException e) {
//      throw new RuntimeException(e);
//    }
//    return clazz;
//  }
}
