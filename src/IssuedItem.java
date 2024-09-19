public class IssuedItem {
    private int issuedId;
    private String itemName;
    private String studentName;
    private int quantityIssued;
    private String issuedDate;

    // Constructor
    public IssuedItem(int issuedId, String itemName, String studentName, int quantityIssued, String issuedDate) {
        this.issuedId = issuedId;
        this.itemName = itemName;
        this.studentName = studentName;
        this.quantityIssued = quantityIssued;
        this.issuedDate = issuedDate;
    }

    // Getters
    public int getIssuedId() {
        return issuedId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getQuantityIssued() {
        return quantityIssued;
    }

    public String getIssuedDate() {
        return issuedDate;
    }
}
