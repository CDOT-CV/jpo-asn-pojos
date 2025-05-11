package us.dot.its.jpo.asn.jsonschema.generator;

import static us.dot.its.jpo.asn.jsonschema.generator.Utils.construct;
import static us.dot.its.jpo.asn.jsonschema.generator.Utils.getClassFromName;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.victools.jsonschema.generator.CustomDefinition;
import com.github.victools.jsonschema.generator.CustomPropertyDefinition;
import com.github.victools.jsonschema.generator.MemberScope;
import com.github.victools.jsonschema.generator.Module;
import com.github.victools.jsonschema.generator.SchemaGenerationContext;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigPart;
import com.github.victools.jsonschema.generator.SchemaGeneratorGeneralConfigPart;
import com.github.victools.jsonschema.generator.TypeScope;
import java.util.Arrays;
import java.util.List;
import us.dot.its.jpo.asn.runtime.types.Asn1Bitstring;
import us.dot.its.jpo.asn.runtime.types.Asn1Boolean;
import us.dot.its.jpo.asn.runtime.types.Asn1CharacterString;
import us.dot.its.jpo.asn.runtime.types.Asn1Enumerated;
import us.dot.its.jpo.asn.runtime.types.Asn1Integer;
import us.dot.its.jpo.asn.runtime.types.Asn1ObjectIdentifier;
import us.dot.its.jpo.asn.runtime.types.Asn1RelativeOID;
import us.dot.its.jpo.asn.runtime.types.Asn1Sequence;
import us.dot.its.jpo.asn.runtime.types.IA5String;

public class Asn1Module implements Module {

  @Override
  public void applyToConfigBuilder(SchemaGeneratorConfigBuilder schemaGeneratorConfigBuilder) {
    applyToConfigPart(schemaGeneratorConfigBuilder.forFields());
    applyToConfigPart(schemaGeneratorConfigBuilder.forMethods());
    applyToTypesInGeneral(schemaGeneratorConfigBuilder.forTypesInGeneral());
  }

  private void applyToConfigPart(SchemaGeneratorConfigPart<?> configPart) {
    configPart.withCustomDefinitionProvider(this::provideCustomSchemaDefinitionForMember);
  }

  private void applyToTypesInGeneral(SchemaGeneratorGeneralConfigPart configPart) {
    configPart.withTitleResolver(this::resolveTitle);
    configPart.withDescriptionResolver(this::resolveDescription);
    configPart.withCustomDefinitionProvider(this::provideCustomSchemaDefinitionForType);
  }

  private CustomPropertyDefinition provideCustomSchemaDefinitionForMember(MemberScope<?, ?> scope,
      SchemaGenerationContext context) {
    ResolvedType declaringType = scope.getDeclaringType();
    String declaredName = scope.getDeclaredName();
    ResolvedType declaredType = scope.getDeclaredType();
    System.out.printf("declaringType: %s, declaredName: %s, declaredType: %s%n",
        declaringType, declaredName, declaredType);

    // Don't do anything, use default
    return null;
  }

  private CustomDefinition provideCustomSchemaDefinitionForType(ResolvedType resolvedType,
      SchemaGenerationContext context) {

    if (resolvedType.isInstanceOf(Asn1Integer.class)) {
      // Custom schema for Asn1Integer types
      return provideIntegerDefinition(resolvedType, context);
    } else if (resolvedType.isInstanceOf(Asn1CharacterString.class)) {
      return provideCharacterStringDefinition(resolvedType, context);
    } else if (resolvedType.isInstanceOf(Asn1Bitstring.class)) {
      return provideBitstringDefinition(resolvedType, context);
    } else if (resolvedType.isInstanceOf(Asn1Enumerated.class)) {
      return provideEnumeratedDefinition(resolvedType, context);
    } else if (resolvedType.isInstanceOf(Asn1Boolean.class)) {
      return provideBooleanDefinition(resolvedType, context);
    } else if (resolvedType.isInstanceOf(Asn1ObjectIdentifier.class)
        || resolvedType.isInstanceOf(Asn1RelativeOID.class)) {
      return provideObjectIdentifierDefinition(resolvedType, context);
    }



    // Use default
    return null;
  }

  private CustomDefinition provideIntegerDefinition(ResolvedType resolvedType,
      SchemaGenerationContext context) {

    String typeName = resolvedType.getTypeName();
    Class<?> clazz = getClassFromName(typeName);

    Asn1Integer exampleInt = (Asn1Integer) construct(clazz);
    long lowerBound = exampleInt.getLowerBound();
    long upperBound = exampleInt.getUpperBound();

    ObjectNode node = context.getGeneratorConfig().createObjectNode()
        .put("type", "integer")
        .put("minimum", lowerBound)
        .put("maximum", upperBound);

    return new CustomDefinition(node);
  }

  private CustomDefinition provideCharacterStringDefinition(ResolvedType resolvedType,
      SchemaGenerationContext context) {

    String typeName = resolvedType.getTypeName();
    Class<?> clazz = getClassFromName(typeName);

    Asn1CharacterString example = (Asn1CharacterString) construct(clazz);
    int maxLength = example.getMaxLength();
    int minLength = example.getMinLength();

    ObjectNode node = context.getGeneratorConfig().createObjectNode()
        .put("type", "string")
        .put("minLength", minLength)
        .put("maxLength", maxLength);

    return new CustomDefinition(node);
  }

  private CustomDefinition provideBitstringDefinition(ResolvedType resolvedType,
      SchemaGenerationContext context) {
    String typeName = resolvedType.getTypeName();
    Class<?> clazz = getClassFromName(typeName);

    Asn1Bitstring example = (Asn1Bitstring) construct(clazz);
    final int minBits = example.size();
    final int minBytes = (minBits + 7) / 8;
    final int minChars = minBytes * 2;
    final int maxBits = example.upperBound();
    final int maxBytes = (maxBits + 7) / 8;
    final int maxChars = maxBytes * 2;

    ObjectNode node = context.getGeneratorConfig().createObjectNode()
        .put("type", "string")
        .put("pattern", // Hex string
            String.format("^[0-9a-fA-F]{%s,%s}$", minChars, maxChars));

    return new CustomDefinition(node);
  }

  private CustomDefinition provideEnumeratedDefinition(ResolvedType resolvedType,
      SchemaGenerationContext context) {
    String typeName = resolvedType.getTypeName();
    Class<?> clazz = getClassFromName(typeName);
    Object[] constants = clazz.getEnumConstants();
    List<String> names = Arrays.stream(constants).map(c -> ((Asn1Enumerated) c).getName()).toList();
    ObjectNode node = context.getGeneratorConfig().createObjectNode()
        .put("type", "string");
    ArrayNode nameArr = node.putArray("enum");
    names.forEach(nameArr::add);

    return new CustomDefinition(node);
  }

  private CustomDefinition provideBooleanDefinition(ResolvedType resolvedType, SchemaGenerationContext context) {
    ObjectNode node = context.getGeneratorConfig().createObjectNode()
        .put("type", "boolean");
    return new CustomDefinition(node);
  }

  private CustomDefinition provideObjectIdentifierDefinition(ResolvedType resolvedType, SchemaGenerationContext context) {
    ObjectNode node = context.getGeneratorConfig().createObjectNode()
        .put("type", "string");
    return new CustomDefinition(node);
  }

  private String resolveTitle(TypeScope scope) {
    var type = scope.getType();
    return type.getBriefDescription();
  }

  private String resolveDescription(TypeScope scope) {
    var type = scope.getType();
    if (type.isInstanceOf(Asn1Integer.class)) {
      return "ASN.1 INTEGER Type";
    } else if (type.isInstanceOf(Asn1Sequence.class)) {
      return "ASN.1 SEQUENCE Type";
    } else if (type.isInstanceOf(IA5String.class)) {
      return "ASN.1 IA5String Type";
    } else if (type.isInstanceOf(Asn1Bitstring.class)) {
      return "ASN.1 BIT STRING Type";
    } else if (type.isInstanceOf(Asn1Enumerated.class)) {
      return "ASN.1 ENUMERATED Type";
    } else if (type.isInstanceOf(Asn1Boolean.class)) {
      return "ASN.1 BOOLEAN Type";
    } else if (type.isInstanceOf(Asn1ObjectIdentifier.class)) {
      return "ASN.1 OBJECT IDENTIFIER Type";
    } else if (type.isInstanceOf(Asn1RelativeOID.class)) {
      return "ASN.1 RELATIVE-OID Type";
    }
    return null;
  }


}
