package com.sdeven.sparrow.starter.autoconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author sdeven
 * @version 1.0.0
 */
@Target({METHOD, TYPE})
@Retention(RUNTIME)
@Inherited
@Configuration
@Import(CustomAutoConfigurationImportSelector.class)
public @interface CustomImport {
    Class<?>[] value();
}