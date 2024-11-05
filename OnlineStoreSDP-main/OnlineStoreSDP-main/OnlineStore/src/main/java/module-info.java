module assegnamento2.assegnamento2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;


    opens assegnamento2.assegnamento2.communication.product to javafx.fxml;
    exports assegnamento2.assegnamento2.communication.product;
    opens assegnamento2.assegnamento2.communication.user to javafx.fxml;
    exports assegnamento2.assegnamento2.communication.user;
    opens assegnamento2.assegnamento2 to javafx.fxml;
    exports assegnamento2.assegnamento2;
    exports assegnamento2.assegnamento2.controller;
    opens assegnamento2.assegnamento2.controller to javafx.fxml;


}