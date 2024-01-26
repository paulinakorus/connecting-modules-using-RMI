module customer {
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires service;
    requires java.desktop;
    requires java.rmi;
    requires shop;

    opens org.example.customer;
    exports org.example.customer;
}