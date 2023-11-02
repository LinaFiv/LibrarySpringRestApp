package org.example.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MyDispatcherServletInitializerTest {

    private final MyDispatcherServletInitializer initializer = new MyDispatcherServletInitializer();

    @Test
    void getRootConfigClassesShouldReturnNull() {
        Assertions.assertNull(initializer.getRootConfigClasses());
    }

    @Test
    void getServletConfigClassesShouldReturnAppConfig() {
        Class<?>[] configClasses = initializer.getServletConfigClasses();
        assertThat(configClasses).containsExactly(SpringConfig.class);
    }

    @Test
    void getServletMappingsShouldReturnRootPath() {
        String[] mappings = initializer.getServletMappings();
        assertThat(mappings).containsExactly("/");
    }
}