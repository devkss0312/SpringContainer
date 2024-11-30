package org.example.services;

import org.example.annotations.Autowired;
import org.example.annotations.Component;

@Component
public class ServiceB {
    private final ServiceC serviceC;

    @Autowired
    public ServiceB(ServiceC serviceC) {
        this.serviceC = serviceC;
    }

    public String execute() {
        return "ServiceB executed via " + serviceC.execute();
    }
}
