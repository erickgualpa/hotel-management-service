package org.egualpam.contexts.hotelmanagement.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HexagonalArchitectureTest {

  private final JavaClasses importedClasses =
      new ClassFileImporter()
          .withImportOption(new DoNotIncludeTests())
          .importPackages("org.egualpam.contexts.hotelmanagement");

  @DisplayName("domain should only depend on java or domain")
  @Test
  void domainShouldOnlyDependOnJavaOrDomain() {
    classes()
        .that()
        .resideInAPackage("..domain..")
        .should()
        .onlyDependOnClassesThat()
        .resideInAnyPackage("..java..", "..domain..")
        .check(importedClasses);
  }

  @DisplayName("application should only depend on java or application or domain")
  @Test
  void applicationShouldOnlyDependOnJavaOrApplicationOrDomain() {
    classes()
        .that()
        .resideInAPackage("..application..")
        .should()
        .onlyDependOnClassesThat()
        .resideInAnyPackage("..java..", "..application..", "..domain..")
        .check(importedClasses);
  }
}
