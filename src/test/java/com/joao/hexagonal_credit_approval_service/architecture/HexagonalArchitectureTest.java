package com.joao.hexagonal_credit_approval_service.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class HexagonalArchitectureTest {

    private static final String BASE_PACKAGE = "com.joao.hexagonal_credit_approval_service";

    private static JavaClasses classes;

    @BeforeAll
    static void importClasses() {
        classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(BASE_PACKAGE);
    }

    @Test
    void shouldRespectHexagonalLayers() {
        ArchRule rule = layeredArchitecture()
                .consideringOnlyDependenciesInLayers()

                .layer("Domain").definedBy(BASE_PACKAGE + ".application.core.domain..")
                .layer("UseCase").definedBy(BASE_PACKAGE + ".application.core.useCase..")
                .layer("PortsIn").definedBy(BASE_PACKAGE + ".application.ports.in..")
                .layer("PortsOut").definedBy(BASE_PACKAGE + ".application.ports.out..")
                .layer("AdaptersIn").definedBy(BASE_PACKAGE + ".adapter.in..")
                .layer("AdaptersOut").definedBy(BASE_PACKAGE + ".adapter.out..")
                .layer("Config").definedBy(BASE_PACKAGE + ".config..")

                .whereLayer("Domain")
                .mayOnlyBeAccessedByLayers("UseCase", "PortsIn", "PortsOut", "AdaptersIn", "AdaptersOut", "Config")

                .whereLayer("UseCase")
                .mayOnlyBeAccessedByLayers("Config")

                .whereLayer("PortsIn")
                .mayOnlyBeAccessedByLayers("UseCase", "AdaptersIn", "Config")

                .whereLayer("PortsOut")
                .mayOnlyBeAccessedByLayers("UseCase", "AdaptersOut", "Config")

                .whereLayer("AdaptersIn")
                .mayOnlyBeAccessedByLayers("Config")
                .whereLayer("AdaptersOut")
                .mayOnlyBeAccessedByLayers("Config")
                .whereLayer("Config")
                .mayNotBeAccessedByAnyLayer();

        rule.check(classes);
    }

    @Test
    void domainAndUseCasesShouldNotDependOnAdapters() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(BASE_PACKAGE + ".application.core..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        BASE_PACKAGE + ".adapter..",
                        BASE_PACKAGE + ".config..");

        rule.check(classes);
    }

    @Test
    void domainAndUseCasesShouldNotDependOnFrameworks() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(BASE_PACKAGE + ".application.core..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "org.springframework..",
                        "org.apache.kafka..",
                        "org.springframework.kafka..",
                        "jakarta.persistence..",
                        "org.springframework.data..");

        rule.check(classes);
    }

    @Test
    void portsShouldOnlyDependOnDomain() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(BASE_PACKAGE + ".application.ports..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        BASE_PACKAGE + ".adapter..",
                        BASE_PACKAGE + ".config..",
                        BASE_PACKAGE + ".application.core.useCase..");

        rule.check(classes);
    }

    @Test
    void adaptersShouldNotDependOnEachOther() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(BASE_PACKAGE + ".adapter.in..")
                .should().dependOnClassesThat().resideInAPackage(BASE_PACKAGE + ".adapter.out..");

        rule.check(classes);
    }
}
