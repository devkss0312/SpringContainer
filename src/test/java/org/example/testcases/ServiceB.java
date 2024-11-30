package org.example.testcases;

import org.example.annotations.Autowired;
import org.example.annotations.Component;

@Component
public class ServiceB {
    private ServiceC serviceC;

    @Autowired
    public void setServiceC(ServiceC serviceC) {
        this.serviceC = serviceC;
    }

    public ServiceC getServiceC() {
        return serviceC;
    }

    public String performAction() {
        return "ServiceB executed via " + serviceC.performAction();
    }
}
