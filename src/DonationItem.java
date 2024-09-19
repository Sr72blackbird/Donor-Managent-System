public class DonationItem {
    private int itemId;        // Unique ID for the donation item
    private String itemName;   // Name or description of the item
    private int quantity;      // Quantity of the donated item
    private int donorId;       // ID of the donor who donated the item
    private String donationDate; // Date of the donation

    // Constructor
    public DonationItem(int itemId, String itemName, int quantity, int donorId, String donationDate) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.donorId = donorId;
        this.donationDate = donationDate;
    }

    // Getters and Setters
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getDonorId() { return donorId; }
    public void setDonorId(int donorId) { this.donorId = donorId; }

    public String getDonationDate() { return donationDate; }
    public void setDonationDate(String donationDate) { this.donationDate = donationDate; }
}
