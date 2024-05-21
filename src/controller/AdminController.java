package controller;

import view.*;

import javax.swing.*;

public class AdminController {

    private AdminView view;

    public AdminController(AdminView view) {
        this.view = view;
    }

    public void handleProductClick() {
        view.setHighlight(view.getProductLabel());
        ProductForm productForm = new ProductForm();
        view.setRightPanel(productForm);
    }

    public void handleSupplierClick() {
        view.setHighlight(view.getSupplierLabel());
        SupplierForm supplierForm = new SupplierForm();
        view.setRightPanel(supplierForm);
    }

    public void handleReceiptClick() {
        view.setHighlight(view.getReceiptLabel());
        ReceiptForm receiptForm = new ReceiptForm(view.getAccount());
        view.setRightPanel(receiptForm);
    }

    public void handleReceiptBillClick() {
        view.setHighlight(view.getReceiptBillLabel());
        ReceiptBillForm receiptBillForm = new ReceiptBillForm(view.getAccount());
        view.setRightPanel(receiptBillForm);
    }

    public void handleDeliveryClick() {
        view.setHighlight(view.getDeliveryLabel());
        DeliveryForm deliveryForm = new DeliveryForm(view.getAccount());
        view.setRightPanel(deliveryForm);
    }

    public void handleDeliveryBillClick() {
        view.setHighlight(view.getDeliveryBillLabel());
        DeliveryBillForm deliveryBillForm = new DeliveryBillForm(view.getAccount());
        view.setRightPanel(deliveryBillForm);
    }

    public void handleStockClick() {
        view.setHighlight(view.getStockLabel());
        StockForm stockForm = new StockForm(view.getAccount());
        view.setRightPanel(stockForm);
    }

    public void handleAccountClick() {
        view.setHighlight(view.getAccountLabel());
        AccountForm accountForm = new AccountForm(view.getAccount());
        view.setRightPanel(accountForm);
    }

    public void handleStatisticClick() {
        view.setHighlight(view.getStatisticLabel());
        StatisticForm statisticForm = new StatisticForm(view.getAccount());
        view.setRightPanel(statisticForm);
    }

    public void handleInformationClick() {
        view.setHighlight(view.getInformationLabel());
        InformationForm informationForm = new InformationForm(view.getAccount(), view, true);
        informationForm.setVisible(true);
    }

    public void handleLogOutClick() {
        view.setHighlight(view.getLogOutLabel());
        int returnValue = JOptionPane.showConfirmDialog(view, "Đăng xuất?", "Đăng xuất", JOptionPane.YES_NO_OPTION);

        if (returnValue == JOptionPane.YES_OPTION) {
            view.dispose();
            new LoginView();
        }
    }
}
