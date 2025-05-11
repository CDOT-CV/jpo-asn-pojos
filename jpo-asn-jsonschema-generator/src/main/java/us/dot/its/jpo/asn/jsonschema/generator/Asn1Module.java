package us.dot.its.jpo.asn.jsonschema.generator;

import static us.dot.its.jpo.asn.jsonschema.generator.Utils.construct;
import static us.dot.its.jpo.asn.jsonschema.generator.Utils.getClassFromName;

import com.fasterxml.classmate.ResolvedType;
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
import us.dot.its.jpo.asn.runtime.types.Asn1Integer;
import us.dot.its.jpo.asn.runtime.types.Asn1Sequence;

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
    }

    // Use default
    return null;
  }

  private CustomDefinition provideIntegerDefinition(ResolvedType resolvedType,
      SchemaGenerationContext context) {

    String typeName = resolvedType.getTypeName();
    Class<?> clazz = getClassFromName(typeName);

    Asn1Integer exampleInt = (Asn1Integer)construct(clazz);
    long lowerBound = exampleInt.getLowerBound();
    long upperBound = exampleInt.getUpperBound();

    ObjectNode node = context.getGeneratorConfig().createObjectNode()
        .put("type", "integer")
        .put("minimum", lowerBound)
        .put("maximum", upperBound);

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
    }
    return null;
  }
}
