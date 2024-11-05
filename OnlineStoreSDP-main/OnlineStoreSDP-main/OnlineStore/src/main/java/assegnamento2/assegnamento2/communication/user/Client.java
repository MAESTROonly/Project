package assegnamento2.assegnamento2.communication.user;

import assegnamento2.assegnamento2.communication.product.ElettronicDevice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Client extends Person {

    private String address;
    private final List<ElettronicDevice> shop = new ArrayList<>();

    public Client() { }

    public Client(final String name, final String surname, final String username, final String password, final String address) {

        super(name, surname, username, password);
        this.setAddress(address);
    }

    public Client(final Client registerClient) {

        super(registerClient.getName(), registerClient.getSurname(), registerClient.getUsername(), registerClient.getPassword());
        this.address = registerClient.getAddress();
    }
    public String getAddress() {

        return this.address;
    }
    public Object getShop() {

        return this.shop;
    }
    public void setAddress(final String address) {

        this.address = address;
    }
    public Object searchProduct(final List<ElettronicDevice> list, String name, String producer, String min, String max) throws IOException {
        float minPrice = 0, maxPrice = 0;
        List<ElettronicDevice> elprint = new ArrayList<>();
        if (min.equals("")) minPrice = 0;
        else {
            try {
                minPrice = Float.parseFloat(min);

            } catch (NumberFormatException ignored) {
            }
        }

        if (max.equals("")) maxPrice = 0;
        else {
            try {
                maxPrice = Float.parseFloat(max);
            } catch (NumberFormatException ignored) {
            }
        }

        for (ElettronicDevice e : list) {
            if (name.equalsIgnoreCase(e.getName()) || (name.equals(""))) {
                elprint.add(e);
                if ((!producer.equalsIgnoreCase(e.getProducer())) && (!(producer.equals("")))) {
                    elprint.remove(e);
                }
            }
        }

        float finalMinPrice = minPrice;
        elprint.removeIf(n -> (n.getPrice() <= finalMinPrice));
        if (maxPrice != 0) {
            float finalMaxPrice = maxPrice;
            elprint.removeIf(n -> (n.getPrice() >= finalMaxPrice));
        }
        return elprint;
    }

    public int orderProduct(final List<ElettronicDevice> elDev, final List<ElettronicDevice> buyElDev, String ID, String amount) {
        int id = 0;
        try {
            id = Integer.parseInt(ID);

        } catch (NumberFormatException ignored) {
        }
        for (ElettronicDevice i : elDev) {
            if (i.getId() == id) {
                int qnt = Integer.parseInt(amount);
                if ((qnt > 0) && (qnt <= i.getAmount())) {
                    if (i.getAmount() == qnt) {
                        addOrder(new ElettronicDevice(i));
                        i.setAmount(i.getAmount() - qnt);
                        buyElDev.add(elDev.remove(elDev.indexOf(i)));
                    } else {
                        int a = i.getAmount();
                        i.setAmount(qnt);
                        addOrder(new ElettronicDevice(i));
                        i.setAmount(a - qnt);
                    }
                    return 0;
                } else return 1;
            }
        }
        return 2;
    }
    public void addOrder(final ElettronicDevice e) {
        for (ElettronicDevice i : this.shop) {
            if (i.getId() == e.getId()) {
                i.setAmount(i.getAmount() + e.getAmount());
                return;
            }
        }
        this.shop.add(e);
    }

}
