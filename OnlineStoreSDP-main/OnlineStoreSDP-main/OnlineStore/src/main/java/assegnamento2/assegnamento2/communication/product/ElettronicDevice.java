package assegnamento2.assegnamento2.communication.product;

public class ElettronicDevice {

    private String name;
    private int id;
    private String producer;
    private float price;
    private int amount;
    public ElettronicDevice(final String name, final int id, final String producer, final float price, final int amount) {

        this.setName(name);
        this.setId(id);
        this.setProducer(producer);
        this.setPrice(price);
        this.setAmount(amount);
    }

    public ElettronicDevice() { }
    public ElettronicDevice(ElettronicDevice i) {

        this.name = i.getName();
        this.id = i.getId();
        this.producer = i.getProducer();
        this.price = i.getPrice();
        this.amount = i.getAmount();
    }
    public String getName() {
        return this.name;
    }
    public int getId() {
        return this.id;
    }

    public String getProducer() {
        return this.producer;
    }
    public float getPrice() {
        return this.price;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public void setProducer(final String producer) {
        this.producer = producer;
    }

    public void setPrice(final float price) {
        this.price = price;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

}
