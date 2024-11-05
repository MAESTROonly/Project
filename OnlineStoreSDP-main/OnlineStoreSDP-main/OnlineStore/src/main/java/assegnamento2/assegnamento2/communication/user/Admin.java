package assegnamento2.assegnamento2.communication.user;

import assegnamento2.assegnamento2.communication.product.ElettronicDevice;

import java.util.List;

public final class Admin extends Employee {

    public Admin(final String name, final String surname, final String username, final String password) {
        super(name, surname, username, password);
    }

    public Admin() {
    }
    public Admin(Admin a) {
        super(a.getName(), a.getSurname(), a.getUsername(), a.getPassword());
    }

    public boolean addEmployee(final List<Employee> list, String name, String surname, String username, String password) {

        for (Employee i : list) {

            if (i.getUsername().equals(username)) return true;

        }
        list.add(new Employee(name, surname, username, password));
        return false;
    }

    public boolean rmvEmployee(final List<Employee> list, String username) {
        for (Employee e : list) {
            if (e.getUsername().equals(username)) {
                list.remove(e);
                return false;
            }
        }

        return true;
    }
    public int addProduct(final List<ElettronicDevice> list, String name, int id, String producer, float price, int amount) {
        if (amount <= 0) return 1;
        if (price <= 0) return 2;
        for (ElettronicDevice i : list) {
            if (i.getId() == id) return 3;
        }
        list.add(new ElettronicDevice(name, id, producer, price, amount));
        return 0;
    }
    public int rmvProduct(final List<ElettronicDevice> list, int id) {
        for (ElettronicDevice e : list) {
            if (e.getId() == id) {
                list.remove(e);
                return 0;
            }
        }
        return 1;
    }

}
