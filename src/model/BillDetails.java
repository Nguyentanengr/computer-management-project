package model;

import java.util.Objects;

public class BillDetails {
    private String billCode;
    private String machineCode;
    private int quantity;
    private double unitPrice;

    public BillDetails(String billCode, String machineCode, int quantity, double unitPrice) {
        this.billCode = billCode;
        this.machineCode = machineCode;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillDetails that = (BillDetails) o;
        return quantity == that.quantity && Double.compare(unitPrice, that.unitPrice) == 0 && Objects.equals(billCode, that.billCode) && Objects.equals(machineCode, that.machineCode);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }
}
