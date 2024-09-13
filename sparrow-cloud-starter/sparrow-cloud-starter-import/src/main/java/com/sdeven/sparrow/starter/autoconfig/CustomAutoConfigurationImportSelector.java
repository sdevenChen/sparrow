/**
 *    Copyright 2023 sdeven.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
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
