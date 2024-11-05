package assegnamento2.assegnamento2.controller;

import assegnamento2.assegnamento2.communication.user.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
public final class RegisterController extends Controller {

    @FXML
    private TextField newName;
    @FXML
    private TextField newSurname;
    @FXML
    private TextField newAddress;
    @FXML
    private TextField newUsername;
    @FXML
    private TextField newPass;
    @FXML
    public void handleSignIn(ActionEvent event) throws ParserConfigurationException, IOException, SAXException, XMLStreamException {

        String name = this.newName.getText();
        String surname = this.newSurname.getText();
        String address = this.newAddress.getText();
        String username = this.newUsername.getText();
        String pass = this.newPass.getText();

        if (name.isBlank() || surname.isBlank() || address.isBlank() || pass.isBlank() || username.isBlank())
            popUp(Alert.AlertType.ERROR, "Blank fields!", "Registered failed");

        else {
            Boolean value = loginR(username);

            if (value)
                popUp(Alert.AlertType.ERROR, "Error Username!", "Username exist");

            else {
                addUser(new Client(name, surname, username, pass, address));

                popUp(Alert.AlertType.INFORMATION, "Registered!", "Client registered");
                handleHomeButton(event);
            }
        }
    }
}
