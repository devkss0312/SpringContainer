package org.example.services;

import org.example.annotations.Autowired;
import org.example.annotations.Component;

@Component
public class ServiceA {
    @Autowired
    private ServiceB serviceB;

    public void execute() {
        System.out.println("ServiceA executed");
        serviceB.execute();
    }
}