package com.sdeven.sparrow.starter.autoconfig;

import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Auto configurations should never be included via @ComponentScan because ordering cannot be guaranteed.
 * Auto configurations should be declared in a META-INF/spring.factories and should NOT be subject to @ComponentScan as mentioned above.
 * @Ordered does not apply to @Configuration classes since Spring Boot 1.3.
 * Use @AutoConfigureOrder, @AutoConfigureBefore, and @AutoConfigureAfter to order auto configurations for Spring Boot 1.3 or greater.
 * Avoid using @ConditionalOnX annotations outside of auto-configurations. @ConditionalOnX annotations are sensitive to ordering and ordering cannot be guaranteed with just @Configuration classes.
 * <p>
 * Solve manual@import class loading order problem
 * @author sdeven
 * @since 1.0.0
 */
public class CustomAutoConfigurationImportSelector extends AutoConfigurationImportSelector {
    @Override
    protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
        return asList(attributes, "value");
    }

    @Override
    protected Class<?> getAnnotationClass() {
        return CustomImport.class;
    }

    @Override
    protected Set<String> getExclusions(AnnotationMetadata metadata, AnnotationAttributes attributes) {
        return Collections.EMPTY_SET;
    }
}
