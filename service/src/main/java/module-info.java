module service {
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires shop;
    requires java.rmi;

    exports org.example.service;
    exports org.example.service.model;
    exports org.example.service.model.enums;
    exports org.example.service.model.tables;
    opens org.example.service;
    opens org.example.service.model;
    opens org.example.service.model.enums;
    opens org.example.service.model.tables;
    exports org.example.service.RMI;
}