package assegnamento2.assegnamento2.controller;

import assegnamento2.assegnamento2.communication.user.Admin;
import assegnamento2.assegnamento2.communication.user.Employee;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class AdminController extends EmployeeController implements Initializable {
    @FXML
    private TextField newEmpName;

    @FXML
    private TextField newEmpSurname;

    @FXML
    private TextField newEmpUsername;

    @FXML
    private TextField newEmpPass;

    @FXML
    private TableView employeeTableView;

    @FXML
    private TextField newProductName;

    @FXML
    private TextField newProductProducer;

    @FXML
    private TextField newProductID;

    @FXML
    private TextField newProductPrice;

    @FXML
    private TextField newProductAmount;

    @FXML
    private TextField newProductIdU;

    @FXML
    private TextField newProductAmountU;

    @FXML
    private TableView addTableView;

    @FXML
    private TableView rmvTableView;

    @FXML
    private TextField newProductIdR;

    List<Employee> empList = new ArrayList<>();

    private Admin admin = new Admin();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            readElDev(prodElDev, "src/main/java/assegnamento2/assegnamento2/db/DB_ElDev.XML");
            readElDev(buyElDev, "src/main/java/assegnamento2/assegnamento2/db/DB_BuyElDev.XML");
            readOrder();
            readUsername();
            readEmp();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        printListFX(updateTableView, Stream.concat(prodElDev.stream(), buyElDev.stream()).collect(Collectors.toList()));
        printListFX(addTableView, Stream.concat(prodElDev.stream(), buyElDev.stream()).collect(Collectors.toList()));
        printListFX(rmvTableView, Stream.concat(prodElDev.stream(), buyElDev.stream()).collect(Collectors.toList()));
        printListFX(employeeTableView, empList);
    }
    public void setAdmin(Admin a) {
        this.admin = new Admin(a);
    }

    @FXML
    public void handleAddEmployee() throws XMLStreamException, IOException, ParserConfigurationException, SAXException {
        String name = this.newEmpName.getText();
        String surname = this.newEmpSurname.getText();
        String username = this.newEmpUsername.getText();
        String password = this.newEmpPass.getText();

        if (name.isBlank() || surname.isBlank() || username.isBlank() || password.isBlank())
            popUp(Alert.AlertType.ERROR, "Blank fields!", "Add Employee failed");
        else if (loginR(username)) {
            popUp(Alert.AlertType.ERROR, "Error Username!", "Username exist");
        } else {
            this.admin.addEmployee(this.empList, name, surname, username, password);
            addUser(new Employee(name, surname, username, password));
            printListFX(employeeTableView, empList);
            popUp(Alert.AlertType.INFORMATION, "Employee Added!", "");

            this.newEmpName.clear();
            this.newEmpSurname.clear();
            this.newEmpUsername.clear();
            this.newEmpPass.clear();
        }
    }
    public void readEmp() throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("src/main/java/assegnamento2/assegnamento2/db/DB.XML"));

        NodeList nodeList1 = document.getElementsByTagName("employee");

        for (int i = 0; i < nodeList1.getLength(); i++) {
            org.w3c.dom.Node node = nodeList1.item(i);
            if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                String username = elem.getElementsByTagName("username").item(0).getChildNodes().item(0).getNodeValue();
                String password = elem.getElementsByTagName("password").item(0).getChildNodes().item(0).getNodeValue();
                String name = elem.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
                String surname = elem.getElementsByTagName("surname").item(0).getChildNodes().item(0).getNodeValue();
                this.empList.add(new Employee(name, surname, username, password));
            }
        }
    }

    @FXML
    public void handleAddProd() throws XMLStreamException, FileNotFoundException {
        String name = this.newProductName.getText();
        String producer = this.newProductProducer.getText();
        String id = this.newProductID.getText();
        String price = this.newProductPrice.getText();
        String amount = this.newProductAmount.getText();
        int i, a;
        float p;

        if (name.isBlank() || producer.isBlank() || id.isBlank() || price.isBlank() || amount.isBlank()) {
            popUp(Alert.AlertType.ERROR, "Blank fields!", "Add Product failed");
            return;
        }
        try {
            i = Integer.parseInt(id);
            a = Integer.parseInt(amount);
            p = Float.parseFloat(price);

        } catch (NumberFormatException e) {
            popUp(Alert.AlertType.ERROR, "Instruction fields!", "ID: int\nPRICE: float\nAMOUNT: int");
            return;
        }

        int result = admin.addProduct(prodElDev, name, i, producer, p, a);

        switch (result) {
            case 0 -> {
                refreshList(prodElDev, "src/main/java/assegnamento2/assegnamento2/db/DB_ElDev.XML");
                printListFX(addTableView, Stream.concat(prodElDev.stream(), buyElDev.stream()).collect(Collectors.toList()));
                popUp(Alert.AlertType.INFORMATION, "Product successfully added", "");

                this.newProductName.clear();
                this.newProductProducer.clear();
                this.newProductID.clear();
                this.newProductPrice.clear();
                this.newProductAmount.clear();
            }
            case 1 -> popUp(Alert.AlertType.ERROR, "Error Amount", "A negative quantity was entered!");
            case 2 -> popUp(Alert.AlertType.ERROR, "Error Price", "An integer was not entered!");
            case 3 -> popUp(Alert.AlertType.INFORMATION, "Product ID already exists", "Product ID already exists!");
        }
    }
    @FXML
    public void handleRmvProd() throws XMLStreamException, FileNotFoundException {
        String id = this.newProductIdR.getText();
        int i;
        if (id.isBlank()) {
            popUp(Alert.AlertType.ERROR, "Blank fields!", "Remove Product failed");
            return;
        }
        try {
            i = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            popUp(Alert.AlertType.ERROR, "Instruction fields!", "ID: int");
            return;
        }
        int result = admin.rmvProduct(prodElDev, i);
        if (result == 1) {
            result = admin.rmvProduct(buyElDev, i);
            if (result == 0) refreshList(buyElDev, "src/main/java/assegnamento2/assegnamento2/db/DB_BuyElDev.XML");
        } else refreshList(prodElDev, "src/main/java/assegnamento2/assegnamento2/db/DB_ElDev.XML");

        switch (result) {
            case 0 -> {
                printListFX(rmvTableView, Stream.concat(prodElDev.stream(), buyElDev.stream()).collect(Collectors.toList()));
                popUp(Alert.AlertType.INFORMATION, "Product successfully deleted", "");

                this.newProductIdR.clear();
            }
            case 1 -> popUp(Alert.AlertType.ERROR, "Error ID", "ID not found!");
        }
    }
    public void handleUpdate() throws XMLStreamException, FileNotFoundException {

        updateSwitch(this.admin.addAmount(prodElDev, buyElDev, this.newProductIdU.getText(), this.newProductAmountU.getText()));

        this.newProductIdU.clear();
        this.newProductAmountU.clear();
    }
    public void handleRmvEmp() throws XMLStreamException, IOException, ParserConfigurationException, SAXException {
        String username = this.newEmpUsername.getText();
        if (username.isBlank()) popUp(Alert.AlertType.ERROR, "Blank fields!", "Remove Employee failed");
        else {
            boolean value = admin.rmvEmployee(this.empList, username);

            if (value) popUp(Alert.AlertType.INFORMATION, "Username not found!", "");

            else {
                refreshEmp(this.empList);
                printListFX(employeeTableView, this.empList);
                popUp(Alert.AlertType.INFORMATION, "Operation performed successfully", "Employee account removed");

                this.newEmpUsername.clear();
            }
        }
    }
    public void handleRefreshAddTable() {
        printListFX(addTableView, Stream.concat(prodElDev.stream(), buyElDev.stream()).collect(Collectors.toList()));
    }

    public void handleRefreshRmvTable() {
        printListFX(rmvTableView, Stream.concat(prodElDev.stream(), buyElDev.stream()).collect(Collectors.toList()));
    }

    public void handleRefreshUpdateTable() {
        printListFX(updateTableView, Stream.concat(prodElDev.stream(), buyElDev.stream()).collect(Collectors.toList()));
    }

}
