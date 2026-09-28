package model;

public class Supplier {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String address;

    public Supplier() {
    }

    public Supplier(int id, String name, String phone, String email, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String toFileString() {
        return id + "," + clean(name) + "," + clean(phone) + "," + clean(email) + "," + clean(address);
    }

    public static Supplier fromFileString(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length < 5) {
            throw new IllegalArgumentException("Invalid supplier record: " + line);
        }
        return new Supplier(
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
        return "Supplier{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
