module deliverer {
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires service;
    requires java.desktop;

    opens org.example.deliverer;
    exports org.example.deliverer;
}