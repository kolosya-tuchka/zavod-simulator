module com.example.zavodsimulator {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.logging;

    opens com.kolosya.zavodsimulator to javafx.fxml;
    exports com.kolosya.zavodsimulator;
    exports com.kolosya.zavodsimulator.factory;
    exports com.kolosya.zavodsimulator.threadpool;
    opens com.kolosya.zavodsimulator.factory to javafx.fxml;
}