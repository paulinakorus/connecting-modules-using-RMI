module keeper {
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires service;
    requires java.desktop;
    requires shop;

    opens org.example.keeper.service;
    exports org.example.keeper.service;
}