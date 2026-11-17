module com.mycompany.assignement2semester3justinlachapelle {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.assignement2semester3justinlachapelle to javafx.fxml;
    exports com.mycompany.assignement2semester3justinlachapelle;
}
