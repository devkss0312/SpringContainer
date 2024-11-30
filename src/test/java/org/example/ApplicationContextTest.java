package org.example;

import org.example.context.ApplicationContext;
import org.example.services.ServiceA;
import org.example.services.ServiceB;
import org.example.services.ServiceC;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationContextTest {

    private static ApplicationContext context;

    @BeforeAll
    static void setup() throws Exception {
        // Spring Container 초기화
        context = new ApplicationContext("org.example.services");
    }

    @Test
    void testBeanCreation() {
        // Bean 생성 확인
        ServiceA serviceA = context.getBean(ServiceA.class);
        assertNotNull(serviceA, "ServiceA should not be null");

        ServiceB serviceB = context.getBean(ServiceB.class);
        assertNotNull(serviceB, "ServiceB should not be null");

        ServiceC serviceC = context.getBean(ServiceC.class);
        assertNotNull(serviceC, "ServiceC should not be null");
    }

    @Test
    void testDependencyInjection() {
        // 의존성 주입 확인
        ServiceA serviceA = context.getBean(ServiceA.class);
        assertNotNull(serviceA, "ServiceA should not be null");
        assertNotNull(serviceA.execute(), "ServiceA execute method should return a result");

        // ServiceB와 ServiceC 의존성 확인
        ServiceB serviceB = context.getBean(ServiceB.class);
        assertNotNull(serviceB, "ServiceB should not be null");
        assertEquals("ServiceB executed via ServiceC executed", serviceB.execute());

        ServiceC serviceC = context.getBean(ServiceC.class);
        assertEquals("ServiceC executed", serviceC.execute());
    }

    @Test
    void testSingletonBehavior() {
        // Singleton 객체 확인
        ServiceA serviceA1 = context.getBean(ServiceA.class);
        ServiceA serviceA2 = context.getBean(ServiceA.class);
        assertSame(serviceA1, serviceA2, "ServiceA should be a singleton");

        ServiceB serviceB1 = context.getBean(ServiceB.class);
        ServiceB serviceB2 = context.getBean(ServiceB.class);
        assertSame(serviceB1, serviceB2, "ServiceB should be a singleton");

        ServiceC serviceC1 = context.getBean(ServiceC.class);
        ServiceC serviceC2 = context.getBean(ServiceC.class);
        assertSame(serviceC1, serviceC2, "ServiceC should be a singleton");
    }

    @Test
    void testExecutionFlow() {
        // 실행 로직 확인
        ServiceA serviceA = context.getBean(ServiceA.class);
        String result = serviceA.execute();
        assertEquals("ServiceA executed with ServiceB executed via ServiceC executed and ServiceC executed", result,
                "ServiceA should return the correct execution flow");
    }
}
