package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;

public class ReceiptBill extends Bill{
    private String supplier;

    public ReceiptBill(String billCode, Timestamp creationTime, String creator, ArrayList<BillDetails> billDetails, double totalAmount, String supplier) {
        super(billCode, creationTime, creator, billDetails, totalAmount);
        this.supplier = supplier;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ReceiptBill that = (ReceiptBill) o;
        return Objects.equals(supplier, that.supplier);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.supplier);
        return hash;
    }
}

