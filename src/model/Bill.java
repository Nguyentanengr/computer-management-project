package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;

public class Bill {
    private String billCode;
    private Timestamp creationTime;
    private String creator;
    private ArrayList<BillDetails> billDetails;
    private double totalAmount;

    public Bill(String billCode, Timestamp creationTime, String creator, ArrayList<BillDetails> billDetails, double totalAmount) {
        this.billCode = billCode;
        this.creationTime = creationTime;
        this.creator = creator;
        this.billDetails = billDetails;
        this.totalAmount = totalAmount;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public ArrayList<BillDetails> getBillDetails() {
        return billDetails;
    }

    public void setBillDetails(ArrayList<BillDetails> billDetails) {
        this.billDetails = billDetails;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return Double.compare(totalAmount, bill.totalAmount) == 0 && Objects.equals(billCode, bill.billCode) && Objects.equals(creationTime, bill.creationTime) && Objects.equals(creator, bill.creator) && Objects.equals(billDetails, bill.billDetails);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }
}
