package org.example.service.model;

import org.example.service.model.enums.Method;

public class Payload {
    private Method method;
    private String argument;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }
}
