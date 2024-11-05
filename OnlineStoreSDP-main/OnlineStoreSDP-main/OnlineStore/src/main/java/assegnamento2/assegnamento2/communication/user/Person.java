package assegnamento2.assegnamento2.communication.user;

public class Person {

    private String name;
    private String surname;
    private String username;
    private String password;

    public Person(final String name, final String surname, final String username, final String password) {

        this.setName(name);
        this.setSurname(surname);
        this.setUsername(username);
        this.setPassword(password);
    }

    public Person() { }
    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setSurname(final String surname) {
        this.surname = surname;
    }
    public void setUsername(final String username) {
        this.username = username;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

}
