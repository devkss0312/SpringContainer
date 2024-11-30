package org.example.testcases;

import org.example.annotations.Autowired;
import org.example.annotations.Component;

@Component
public class ServiceA {
    private ServiceB serviceB;
    private ServiceC serviceC;

    @Autowired
    public void setServiceB(ServiceB serviceB) {
        this.serviceB = serviceB;
    }

    @Autowired
    public void setServiceC(ServiceC serviceC) {
        this.serviceC = serviceC;
    }

    public ServiceB getServiceB() {
        return serviceB;
    }

    public ServiceC getServiceC() {
        return serviceC;
    }

    public String execute() {
        return "ServiceA executed with ServiceB and ServiceC";
    }
}
