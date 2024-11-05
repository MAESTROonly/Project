package assegnamento2.assegnamento2.controller;

import assegnamento2.assegnamento2.communication.product.ElettronicDevice;
import assegnamento2.assegnamento2.communication.user.Client;
import assegnamento2.assegnamento2.communication.user.Employee;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmployeeController extends Controller implements Initializable {
    @FXML
    protected TableView deliveryTableView;

    @FXML
    protected ComboBox choiceClient;

    @FXML
    protected Button deliverySendAllE;

    @FXML
    protected TableView buyFinishedTableView;

    @FXML
    protected TableView updateTableView;

    @FXML
    protected TextField IDproductE;

    @FXML
    protected TextField amountE;

    @FXML
    protected Button uploadButtonE;

    List<ElettronicDevice> prodElDev = new ArrayList<>();

    List<ElettronicDevice> buyElDev = new ArrayList<>();

    List<Client> clientList = new ArrayList<>();

    private Employee employee = new Employee();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            readElDev(prodElDev, "src/main/java/assegnamento2/assegnamento2/db/DB_ElDev.XML");
            readElDev(buyElDev, "src/main/java/assegnamento2/assegnamento2/db/DB_BuyElDev.XML");
            readOrder();
            readUsername();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        printListFX(updateTableView, Stream.concat(prodElDev.stream(), buyElDev.stream()).collect(Collectors.toList()));
    }
    public void setEmployee(Employee emp) {

        this.employee = new Employee(emp);
    }
    @FXML
    public void handleViewBuyElDev() {

        printListFX(buyFinishedTableView, buyElDev);
    }
    @FXML
    public void handleChoiceClient() {
        for (Client cli : this.clientList) {
            if (cli.getUsername().equals(choiceClient.getValue())) {
                printListFX(deliveryTableView, cli.getShop());

            }
        }
    }

    public void readOrder() throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("src/main/java/assegnamento2/assegnamento2/db/DB_Order.XML"));

        NodeList orderList = document.getElementsByTagName("order");

        for (int i = 0; i < orderList.getLength(); i++) {
            Client client = new Client();
            Node node = orderList.item(i);
            String attribute = node.getAttributes().getNamedItem("id").getNodeValue();

            client.setUsername(attribute);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element ord = (Element) node;
                NodeList devList = ord.getElementsByTagName("elDev");

                for (int j = 0; j < devList.getLength(); j++) {

                    Node node1 = devList.item(j);

                    if (node1.getNodeType() == Node.ELEMENT_NODE) {
                        Element el_d = (Element) node1;

                        String name = el_d.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
                        String id = el_d.getElementsByTagName("id").item(0).getChildNodes().item(0).getNodeValue();
                        String producer = el_d.getElementsByTagName("producer").item(0).getChildNodes().item(0).getNodeValue();
                        String price = el_d.getElementsByTagName("price").item(0).getChildNodes().item(0).getNodeValue();
                        String amount = el_d.getElementsByTagName("amount").item(0).getChildNodes().item(0).getNodeValue();

                        client.addOrder(new ElettronicDevice(name, Integer.parseInt(id), producer, Float.parseFloat(price), Integer.parseInt(amount)));
                    }
                }
            }

            this.clientList.add(client);
        }
    }
    @FXML
    public void readUsername() throws ParserConfigurationException, IOException, SAXException {

        choiceClient.getItems().clear();
        for (Client cli : this.clientList) {

            choiceClient.getItems().addAll(cli.getUsername());

        }
    }

    public void handleSendAll() throws ParserConfigurationException, IOException, SAXException, XMLStreamException {

        this.clientList.removeIf(cli -> cli.getUsername().equals(choiceClient.getValue()));

        readUsername();
        popUp(Alert.AlertType.INFORMATION, "Order confirmed", "Confirmed Order ");

        deliveryTableView.getItems().clear();
        writeOrder();
    }
    public void writeOrder() throws XMLStreamException, IOException {

        XMLOutputFactory output = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = output.createXMLStreamWriter(new FileOutputStream("src/main/java/assegnamento2/assegnamento2/db/DB_Order.XML"));

        writer.writeStartDocument("1.0");
        writer.writeStartElement("database");

        for (Client cli : this.clientList) {

            writer.writeStartElement("order");
            writer.writeAttribute("id", cli.getUsername());

            for (ElettronicDevice e : (List<ElettronicDevice>) cli.getShop()) {

                writer.writeStartElement("elDev");

                writerElement("name", writer, e.getName());
                writerElement("id", writer, String.valueOf(e.getId()));
                writerElement("producer", writer, e.getProducer());
                writerElement("price", writer, String.valueOf(e.getPrice()));
                writerElement("amount", writer, String.valueOf(e.getAmount()));

                writer.writeEndElement();
            }

            writer.writeEndElement();
        }

        writer.writeEndElement();
        writer.writeEndDocument();

        writer.flush();
        writer.close();
    }
    @FXML
    public void handleUpdate() throws XMLStreamException, FileNotFoundException {

        updateSwitch(this.employee.addAmount(prodElDev, buyElDev, this.IDproductE.getText(), this.amountE.getText()));

        this.IDproductE.clear();
        this.amountE.clear();
    }
    @FXML
    public void updateSwitch(int result) throws XMLStreamException, FileNotFoundException {

        switch (result) {
            case 0 -> {
                refreshList(prodElDev, "src/main/java/assegnamento2/assegnamento2/db/DB_ElDev.XML");
                refreshList(buyElDev, "src/main/java/assegnamento2/assegnamento2/db/DB_BuyElDev.XML");
                updateTableView.getItems().clear();
                printListFX(updateTableView, Stream.concat(prodElDev.stream(), buyElDev.stream()).collect(Collectors.toList()));
                popUp(Alert.AlertType.INFORMATION, "Successful operation", "Updated product");
            }
            case 1 -> popUp(Alert.AlertType.ERROR, "Error Amount", "A negative quantity was entered!");
            case 2 -> popUp(Alert.AlertType.ERROR, "Error", "An integer was not entered!");
            case 3 -> popUp(Alert.AlertType.INFORMATION, "ID not found", "Product ID not present!");
        }
    }

}
