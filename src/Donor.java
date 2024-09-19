public class Donor {
    private int id;
    private String name;
    private String email;
    private String phone;

    public Donor(int id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

// Override toString to display the donor's name in the JComboBox
@Override
public String toString() {
    return name;  // We want the combo box to display the donor's name
}
}