package org.example;

import org.example.context.ApplicationContext;
import org.example.services.ServiceA;
import org.example.services.ServiceB;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationContextTest {

    @Test
    void testComponentScanAndBeanCreation() throws Exception {
        // Given
        ApplicationContext context = new ApplicationContext("org.example.services");

        // When
        ServiceA serviceA = (ServiceA) context.getBean("ServiceA");
        ServiceB serviceB = (ServiceB) context.getBean("ServiceB");

        // Then
        assertNotNull(serviceA, "ServiceA should be registered as a bean.");
        assertNotNull(serviceB, "ServiceB should be registered as a bean.");
    }
}
