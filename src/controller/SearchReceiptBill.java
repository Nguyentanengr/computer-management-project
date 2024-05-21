package controller;

import dao.ReceiptBillDAO;
import dao.SupplierDAO;
import model.ReceiptBill;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchReceiptBill {
    public static final int ALL = 0;
    public static final int BILL_CODE = 1;
    public static final int SUPPLIER = 2;
    public static final int CREATOR = 3;
    public static final int AMOUNT_PRICE = 4;
    public static final int AMOUNT_TIME = 5;

    public List<ReceiptBill> search(int option, String content, List<ReceiptBill> listReceiptBill) {

        return listReceiptBill.stream()
                .filter(receiptBill -> {
                    switch (option) {
                        case ALL:
                            return receiptBill.getBillCode().toLowerCase().contains(content.toLowerCase()) ||
                                    SupplierDAO.getInstance().selectById(receiptBill.getSupplier()).getSupplierName().toLowerCase().contains(content.toLowerCase()) ||
                                    receiptBill.getCreator().toLowerCase().contains(content.toLowerCase());
                        case BILL_CODE:
                            return receiptBill.getBillCode().toLowerCase().contains(content.toLowerCase());
                        case SUPPLIER:
                            return SupplierDAO.getInstance().selectById(receiptBill.getSupplier()).getSupplierName().toLowerCase().contains(content.toLowerCase());
                        case CREATOR:
                            return receiptBill.getCreator().toLowerCase().contains(content.toLowerCase());
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());
    }


    public List<ReceiptBill> search(double fromPrice, double toPrice, List<ReceiptBill> listReceiptBill) {
        return listReceiptBill.stream()
                .filter(receiptBill -> {
                    return fromPrice <= receiptBill.getTotalAmount() && receiptBill.getTotalAmount() <= toPrice;
                })
                .collect(Collectors.toList());
    }


    public List<ReceiptBill> search(Timestamp fromTimestamp, Timestamp toTimestamp, List<ReceiptBill> listReceiptBill) {

        return listReceiptBill.stream()
                .filter(receiptBill -> {
                    return receiptBill.getCreationTime().after(fromTimestamp) && receiptBill.getCreationTime().before(toTimestamp);
                })
                .collect(Collectors.toList());
    }

}
