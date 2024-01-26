module seller {
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires service;
    requires java.desktop;

    opens org.example.seller;
    exports org.example.seller;
}