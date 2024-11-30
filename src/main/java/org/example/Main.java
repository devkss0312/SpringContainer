package org.example;

import org.example.context.ApplicationContext;
import org.example.context.DependencyInjector;
import org.example.services.ServiceA;

public class Main {
    public static void main(String[] args) throws Exception {
        // Component Scan을 통해 빈 등록
        ApplicationContext context = new ApplicationContext("org.example.services");

        // 의존성 주입
        DependencyInjector injector = new DependencyInjector(context);
        injector.injectDependencies();

        // 실행
        ServiceA serviceA = (ServiceA) context.getBean("ServiceA");
        serviceA.execute();
    }
}
