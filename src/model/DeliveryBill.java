package model;

import java.sql.Timestamp;
import java.util.ArrayList;

public class DeliveryBill extends Bill{
    public DeliveryBill(String billCode, Timestamp creationTime, String creator, ArrayList<BillDetails> billDetails, double totalAmount) {
        super(billCode, creationTime, creator, billDetails, totalAmount);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
