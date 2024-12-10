import java.io.Serializable;

public class House implements Serializable {
    private String houseId;
    private String address;
    private double rent;
    private boolean isAvailable;
    private String mobileNumber;    // New: Mobile number of the landlord
    private String specifications; // New: Specifications of the house (e.g., "2 BHK, furnished")
    private String review;         // New: Review or comments about the house

    public House(String houseId, String address, double rent, boolean isAvailable,
                 String mobileNumber, String specifications, String review) {
        this.houseId = houseId;
        this.address = address;
        this.rent = rent;
        this.isAvailable = isAvailable;
        this.mobileNumber = mobileNumber;
        this.specifications = specifications;
        this.review = review;
    }

    public String getHouseId() {
        return houseId;
    }

    public String getAddress() {
        return address;
    }

    public double getRent() {
        return rent;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public String toString() {
        return "House ID: " + houseId + ", Address: " + address + ", Rent: $" + rent +
                ", Available: " + (isAvailable ? "Yes" : "No") +
                ", Mobile: " + mobileNumber + ", Specifications: " + specifications +
                ", Review: " + review;
    }
}
