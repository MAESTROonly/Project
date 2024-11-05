package assegnamento2.assegnamento2.controller;

import assegnamento2.assegnamento2.communication.product.ElettronicDevice;
import assegnamento2.assegnamento2.communication.user.Client;
import assegnamento2.assegnamento2.communication.user.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
import java.util.List;

public class Controller {
    public Boolean loginR(String userName) throws ParserConfigurationException, SAXException, IOException {

        String[] usr = {"administrator", "employee", "client"};

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("src/main/java/assegnamento2/assegnamento2/db/DB.XML"));

        for (String s : usr) {
            NodeList nodeList = document.getElementsByTagName(s);

            for (int i = 0; i < nodeList.getLength(); i++) {
                org.w3c.dom.Node node = nodeList.item(i);

                if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {

                    Element elem = (Element) node;
                    String username = elem.getElementsByTagName("username").item(0).getChildNodes().item(0).getNodeValue();

                    if (userName.equals(username)) return true;
                }
            }
        }
        return false;
    }

    public void addUser(Object user) throws XMLStreamException, IOException, ParserConfigurationException, SAXException {

        String[] usr = {"administrator", "employee", "client"};
        String[] n = {"username", "password", "name", "surname", "address"};

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("src/main/java/assegnamento2/assegnamento2/db/DB.XML"));

        XMLOutputFactory output = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = output.createXMLStreamWriter(new FileOutputStream("src/main/java/assegnamento2/assegnamento2/db/DB.XML"));

        writer.writeStartDocument("1.0");
        writer.writeStartElement("database");

        for (String s : usr) {
            NodeList nodeList = document.getElementsByTagName(s);

            writerNode(n, writer, s, nodeList);
        }

        if (user instanceof Employee) {
            writer.writeStartElement("employee");

            writerElement("username", writer, ((Employee) user).getUsername());
            writerElement("password", writer, ((Employee) user).getPassword());
            writerElement("name", writer, ((Employee) user).getName());
            writerElement("surname", writer, ((Employee) user).getSurname());

            writer.writeEndElement();
        }

        if (user instanceof Client) {
            writer.writeStartElement("client");

            writerElement("username", writer, ((Client) user).getUsername());
            writerElement("password", writer, ((Client) user).getPassword());
            writerElement("name", writer, ((Client) user).getName());
            writerElement("surname", writer, ((Client) user).getSurname());
            writerElement("address", writer, ((Client) user).getAddress());

            writer.writeEndElement();
        }

        writer.writeEndElement();
        writer.writeEndDocument();

        writer.flush();
        writer.close();
    }

    private void writerNode(String[] n, XMLStreamWriter writer, String s, NodeList nodeList) throws XMLStreamException {

        for (int i = 0; i < nodeList.getLength(); i++) {
            org.w3c.dom.Node node = nodeList.item(i);

            writer.writeStartElement(s);
            if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {

                Element elem = (Element) node;

                for (int j = 0; j < ((s.equals("client")) ? n.length : n.length - 1); j++) {

                    writerElement(n[j], writer, elem);
                }
            }
            writer.writeEndElement();
        }
    }
    public void refreshEmp(List<Employee> empList) throws XMLStreamException, IOException, ParserConfigurationException, SAXException {

        String[] usr = {"administrator", "employee", "client"};
        String[] n = {"username", "password", "name", "surname", "address"};

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("src/main/java/assegnamento2/assegnamento2/db/DB.XML"));

        XMLOutputFactory output = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = output.createXMLStreamWriter(new FileOutputStream("src/main/java/assegnamento2/assegnamento2/db/DB.XML"));

        writer.writeStartDocument("1.0");
        writer.writeStartElement("database");

        for (String s : usr) {
            NodeList nodeList = document.getElementsByTagName(s);

            if (s.equals("employee")) {

                for (Employee e : empList) {
                    writer.writeStartElement(s);

                    writerElement(n[0], writer, e.getUsername());
                    writerElement(n[1], writer, e.getPassword());
                    writerElement(n[2], writer, e.getName());
                    writerElement(n[3], writer, e.getSurname());

                    writer.writeEndElement();
                }
            } else writerNode(n, writer, s, nodeList);

        }

        writer.writeEndElement();// </database>
        writer.writeEndDocument();

        writer.flush();
        writer.close();
    }

    public void refreshList(List<ElettronicDevice> list, String src) throws XMLStreamException, FileNotFoundException {

        XMLOutputFactory output = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = output.createXMLStreamWriter(new FileOutputStream(src));

        writer.writeStartDocument("1.0");
        writer.writeStartElement("database");

        for (ElettronicDevice e : list) {

            writer.writeStartElement("elDev");

            writerElement("name", writer, e.getName());
            writerElement("id", writer, String.valueOf(e.getId()));
            writerElement("producer", writer, e.getProducer());
            writerElement("price", writer, String.valueOf(e.getPrice()));
            writerElement("amount", writer, String.valueOf(e.getAmount()));

            writer.writeEndElement();
        }

        writer.writeEndElement();
        writer.writeEndDocument();

        writer.flush();
        writer.close();
    }

    protected void writerElement(String string, XMLStreamWriter writer, Element element) throws XMLStreamException {

        writer.writeStartElement(string);
        writer.writeCharacters(element.getElementsByTagName(string).item(0).getChildNodes().item(0).getNodeValue());
        writer.writeEndElement();
    }
    protected void writerElement(String string, XMLStreamWriter writer, String element) throws XMLStreamException {

        writer.writeStartElement(string);
        writer.writeCharacters(element);
        writer.writeEndElement();
    }
    public void readElDev(List<ElettronicDevice> list, String src) throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(src));

        NodeList nodeList = document.getElementsByTagName("elDev");

        for (int i = 0; i < nodeList.getLength(); i++) {
            org.w3c.dom.Node node = nodeList.item(i);

            if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {

                Element elem = (Element) node;

                String name = elem.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
                String id = elem.getElementsByTagName("id").item(0).getChildNodes().item(0).getNodeValue();
                String producer = elem.getElementsByTagName("producer").item(0).getChildNodes().item(0).getNodeValue();
                String price = elem.getElementsByTagName("price").item(0).getChildNodes().item(0).getNodeValue();
                String amount = elem.getElementsByTagName("amount").item(0).getChildNodes().item(0).getNodeValue();

                list.add(new ElettronicDevice(name, Integer.parseInt(id), producer, Float.parseFloat(price), Integer.parseInt(amount)));
            }
        }
    }
    @FXML
    public void printListFX(TableView tableView, Object listProduct) {
        ObservableList<Object> oList = FXCollections.observableArrayList();

        oList.addAll((List<Object>) listProduct);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableView.setItems(oList);
    }

    @FXML
    public void popUp(Alert.AlertType alertType, String text, String header) {

        Alert alert;

        if (alertType == Alert.AlertType.INFORMATION) alert = new Alert(alertType, text, ButtonType.OK);

        else alert = new Alert(alertType, text);

        alert.setHeaderText(header);
        alert.showAndWait();
    }
    @FXML
    public void handleHomeButton(ActionEvent event) throws IOException {

        changeScene(event, "Login.fxml", "Electronic Store");
    }

    @FXML
    public void changeScene(final ActionEvent event, final String fxml, final String title) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assegnamento2/assegnamento2/" + fxml));
        Parent root = loader.load();

        Scene dashboardScene = new Scene(root); // creates a Scene for a specific root Node
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow(); // replaces the current scene with another scene in the same window
        window.setScene(dashboardScene); // specify the scene to be used on this stage
        window.setTitle(title); // set the title of the window
        window.show();
    }

}
