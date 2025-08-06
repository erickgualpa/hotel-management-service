package org.egualpam.contexts.hotel.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ContextsArchitectureTest {

  private final JavaClasses importedClasses =
      new ClassFileImporter()
          .withImportOption(new DoNotIncludeTests())
          .importPackages("org.egualpam.contexts");

  @DisplayName("customer context should not depend on management context")
  @Test
  void customerContextShouldNotDependOnManagementContext() {
    noClasses()
        .that()
        .resideInAPackage("..contexts.hotel.customer..")
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage("..contexts.hotel.management..")
        .check(importedClasses);
  }

  @DisplayName("management context should not depend on customer context")
  @Test
  void managementContextShouldNotDependOnCustomerContext() {
    noClasses()
        .that()
        .resideInAPackage("..contexts.hotel.management..")
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage("..contexts.hotel.customer..")
        .check(importedClasses);
  }
}
