package org.example.service.model;

import org.example.service.model.enums.Role;

import java.util.UUID;

public class User {
    private String host;
    private Integer port;
    private UUID id = UUID.randomUUID();
    private static int userCounter = 0;
    private Role role;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
