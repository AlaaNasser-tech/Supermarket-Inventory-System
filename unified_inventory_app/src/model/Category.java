package model;

public class Category {
    private int id;
    private String name;
    private String description;

    public Category() {
    }

    public Category(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toFileString() {
        return id + "," + escape(name) + "," + escape(description);
    }

    public static Category fromFileString(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid category record: " + line);
        }
        return new Category(
                Integer.parseInt(parts[0].trim()),
                unescape(parts[1]),
                unescape(parts[2])
        );
    }

    private String escape(String value) {
        return value == null ? "" : value.replace(",", " ");
    }

    private static String unescape(String value) {
        return value == null ? "" : value.trim();
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
