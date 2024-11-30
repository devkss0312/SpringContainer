package org.example.services;

import org.example.annotations.Autowired;
import org.example.annotations.Component;

@Component
public class ServiceA {
    private final ServiceB serviceB;
    private final ServiceC serviceC;

    @Autowired
    public ServiceA(ServiceB serviceB, ServiceC serviceC) {
        this.serviceB = serviceB;
        this.serviceC = serviceC;
    }

    public String execute() {
        return "ServiceA executed with " + serviceB.execute() + " and " + serviceC.execute();
    }
}
