package assegnamento2.assegnamento2.communication.user;

import assegnamento2.assegnamento2.communication.product.ElettronicDevice;

import java.util.List;
public class Employee extends Person {

    public Employee() { }
    public Employee(final String name, final String surname, final String username, final String password) {

        super(name, surname, username, password);
    }
    public Employee(Employee emp) {

        super(emp.getName(), emp.getSurname(), emp.getUsername(), emp.getPassword());
    }

    public int addAmount(final List<ElettronicDevice> elDev, final List<ElettronicDevice> buyElDev, String ID, String amount) {
        int id;
        try {
            id = Integer.parseInt(ID);

        } catch (NumberFormatException e) {
            return 2;
        }
        for (ElettronicDevice i : elDev) {
            if (i.getId() == id) {
                int q;
                try {
                    q = Integer.parseInt(amount);
                    if (q > 0) {
                        i.setAmount(i.getAmount() + q);
                        return 0;
                    } else return 1;
                } catch (NumberFormatException e) {
                    return 2;
                }
            }
        }
        for (ElettronicDevice j : buyElDev) {
            if (j.getId() == id) {
                int q;
                try {
                    q = Integer.parseInt(amount);

                    if (q > 0) {
                        j.setAmount(j.getAmount() + q);
                        elDev.add(buyElDev.remove(buyElDev.indexOf(j)));
                        return 0;
                    } else return 1;
                } catch (NumberFormatException e) {
                    return 2;
                }
            }
        }
        return 3;
    }

}
