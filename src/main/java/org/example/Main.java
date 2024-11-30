package org.example;

import org.example.context.ApplicationContext;
import org.example.services.ServiceA;

public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ApplicationContext("org.example.services");

        ServiceA serviceA = context.getBean(ServiceA.class);
        System.out.println(serviceA.execute());
    }
}
