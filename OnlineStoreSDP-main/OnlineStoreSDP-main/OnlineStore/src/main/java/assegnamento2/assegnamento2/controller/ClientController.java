package assegnamento2.assegnamento2.controller;

import assegnamento2.assegnamento2.communication.product.ElettronicDevice;
import assegnamento2.assegnamento2.communication.user.Client;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public final class ClientController extends Controller implements Initializable {

    @FXML
    private TableView tableElettronicc;

    @FXML
    private TableView tableElettronic1;

    @FXML
    private TextField productNamec;

    @FXML
    private TextField productProducerc;

    @FXML
    private TextField minimumPrice;

    @FXML
    private TextField maximumPrice;

    @FXML
    private TextField prodID;

    @FXML
    private TextField fieldAmount;

    private Client client = new Client();

    List<ElettronicDevice> prodElDev = new ArrayList<>();
    List<ElettronicDevice> buyElDev = new ArrayList<>();
    public void setClient(Client cli) throws ParserConfigurationException, IOException, SAXException {

        this.client = new Client(cli);
        readOrder();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            readElDev(prodElDev, "src/main/java/assegnamento2/assegnamento2/db/DB_ElDev.XML");
            readElDev(buyElDev, "src/main/java/assegnamento2/assegnamento2/db/DB_BuyElDev.XML");

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        printListFX(tableElettronicc, this.prodElDev);
    }
    @FXML
    public void handleSearch() {

        List<ElettronicDevice> support = null;

        try {
            support = (List<ElettronicDevice>) client.searchProduct(prodElDev, this.productNamec.getText(), this.productProducerc.getText(), this.minimumPrice.getText(), this.maximumPrice.getText());

        } catch (IOException e) {
            e.printStackTrace();
        }

        printListFX(tableElettronicc, support);
    }

    @FXML
    public void handleView() {

        printListFX(tableElettronicc, this.prodElDev);
        this.productNamec.clear();
        this.productProducerc.clear();
        this.minimumPrice.clear();
        this.maximumPrice.clear();
    }
    @FXML
    public void handleBuy() throws IOException, XMLStreamException, ParserConfigurationException, SAXException {

        int result = client.orderProduct(prodElDev, buyElDev, this.prodID.getText(), this.fieldAmount.getText());

        if (result == 1) popUp(Alert.AlertType.INFORMATION, "Amount", "Quantity enter not available");
        if (result == 2) popUp(Alert.AlertType.INFORMATION, "ID not found", "Product ID not present");
        if (result == 0) {
            popUp(Alert.AlertType.INFORMATION, "Product order", "");
            this.prodID.clear();
            this.fieldAmount.clear();
            handleView();

            refreshList(prodElDev, "src/main/java/assegnamento2/assegnamento2/db/DB_ElDev.XML");
            refreshList(buyElDev, "src/main/java/assegnamento2/assegnamento2/db/DB_BuyElDev.XML");
            writeOrder();
        }
    }
    public void readOrder() throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("src/main/java/assegnamento2/assegnamento2/db/DB_Order.XML"));

        NodeList orderList = document.getElementsByTagName("order");

        for (int i = 0; i < orderList.getLength(); i++) {
            Node node = orderList.item(i);
            String attribute = node.getAttributes().getNamedItem("id").getNodeValue();

            if (attribute.equals(this.client.getUsername())) {

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
            }
        }
    }
    public void writeOrder() throws XMLStreamException, IOException, ParserConfigurationException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("src/main/java/assegnamento2/assegnamento2/db/DB_Order.XML"));

        XMLOutputFactory output = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = output.createXMLStreamWriter(new FileOutputStream("src/main/java/assegnamento2/assegnamento2/db/DB_Order.XML"));

        writer.writeStartDocument("1.0");
        writer.writeStartElement("database");
        NodeList orderList = document.getElementsByTagName("order");

        boolean check = false;

        for (int i = 0; i < orderList.getLength(); i++) {

            Node node = orderList.item(i);
            String attribute = node.getAttributes().getNamedItem("id").getNodeValue();
            writer.writeStartElement("order");
            writer.writeAttribute("id", attribute);

            if (attribute.equals(client.getUsername())) {

                for (ElettronicDevice e : (List<ElettronicDevice>) client.getShop()) {

                    writer.writeStartElement("elDev");

                    writerElement("name", writer, e.getName());
                    writerElement("id", writer, String.valueOf(e.getId()));
                    writerElement("producer", writer, e.getProducer());
                    writerElement("price", writer, String.valueOf(e.getPrice()));
                    writerElement("amount", writer, String.valueOf(e.getAmount()));

                    writer.writeEndElement();
                    check = true;
                }

            } else {

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element ord = (Element) node;
                    NodeList devList = ord.getElementsByTagName("elDev");

                    for (int j = 0; j < devList.getLength(); j++) {

                        Node node1 = devList.item(j);
                        writer.writeStartElement("elDev"); //<elDev>

                        if (node1.getNodeType() == Node.ELEMENT_NODE) {
                            Element el_d = (Element) node1;

                            writerElement("name", writer, el_d);
                            writerElement("id", writer, el_d);
                            writerElement("producer", writer, el_d);
                            writerElement("price", writer, el_d);
                            writerElement("amount", writer, el_d);

                        }

                        writer.writeEndElement();
                    }
                }
            }

            writer.writeEndElement();
        }

        if (!check) {

            writer.writeStartElement("order");
            writer.writeAttribute("id", client.getUsername());

            for (ElettronicDevice e : (List<ElettronicDevice>) client.getShop()) {

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
    public void viewOrder() {

        printListFX(tableElettronic1, client.getShop());
    }

}
