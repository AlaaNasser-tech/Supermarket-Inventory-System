package model;

public class Client {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;

    public Client() {
    }

    public Client(int id, String name, String email, String phone, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String toFileString() {
        return id + "," + clean(name) + "," + clean(email) + "," + clean(phone) + "," + clean(address);
    }

    public static Client fromFileString(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length < 5) {
            throw new IllegalArgumentException("Invalid client record: " + line);
        }
        return new Client(
                Integer.parseInt(parts[0].trim()),
                parts[1].trim(),
                parts[2].trim(),
                parts[3].trim(),
                parts[4].trim()
        );
    }

    private String clean(String value) {
        return value == null ? "" : value.replace(",", " ");
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
