package view;

import controller.WritePDF;
import dao.*;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DeliveryForm extends ReceiptForm {

    public DeliveryForm(Account account) {
        super(account);
        initComponents2();
    }

    private void initComponents2() {
        this.receiptButton.setText("Xuất hàng");
        this.supplierLabel.setVisible(false);
        this.comboBox.setVisible(false);

        this.receiptBillCreatorLabel.setBounds(30, 90,200, 40);
        this.receiptBillCreatorTxt.setBounds(170, 90, 330, 40);
        this.receiptBillCodeLabel.setText("Mã phiếu xuất");
        this.receiptBillCodeTxt.setText(createBillCode(DeliveryBillDAO.getInstance().selectAll()));

        ActionListener[] actionListeners = this.addButton.getActionListeners();
        for (ActionListener actionListener : actionListeners) {
            this.addButton.removeActionListener(actionListener);
        }

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addButtonActionPerformed2(e);
            }
        });

        actionListeners = this.editButton.getActionListeners();
        for (ActionListener actionListener : actionListeners) {
            editButton.removeActionListener(actionListener);
        }

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editButtonActionListener2(e);
            }
        });

        actionListeners = this.receiptButton.getActionListeners();
        for (ActionListener actionListener : actionListeners) {
            receiptButton.removeActionListener(actionListener);
        }

        receiptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deliveryButtonActionListener2(e);
            }
        });

    }

    public void deliveryButtonActionListener2(ActionEvent e) {
        if (receiptTable.getRowCount() > 0) {
            int option = JOptionPane.showConfirmDialog(this, "Chắc chắn xuất hàng?", "Xác nhận xuất hàng", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.NO_OPTION || option == JOptionPane.CLOSED_OPTION) return;
            try {
                processListDeliveryBill();
                processListBillDetails();
                processListComputer();

                String billCode = receiptBillCodeTxt.getText();
                receiptBillCodeTxt.setText(createBillCode(DeliveryBillDAO.getInstance().selectAll()));
                searchTxtKeyReleased(null);
                defaultTableModel2.setRowCount(0);
                totalPriceLabel.setText("0đ");
                listBillDetails = new ArrayList<>();
                quantityTxt.setText("1");

                int result = JOptionPane.showConfirmDialog(this, "Xuất hàng thành công\nBạn muốn xuất file bdf?", "", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.NO_OPTION) return;

                WritePDF writePDF = new WritePDF();
                writePDF.writeDeliveryBill(billCode);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    private void processListDeliveryBill() {
        String deliveryBillCode = receiptBillCodeTxt.getText();
        long now = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(now);
        String creator = receiptBillCreatorTxt.getText();
        Double totalPrice = Double.parseDouble(totalPriceLabel.getText().replaceAll("[,đ]", ""));

        DeliveryBill deliveryBill = new DeliveryBill(deliveryBillCode, timestamp, creator, listBillDetails,totalPrice);
        DeliveryBillDAO.getInstance().insert(deliveryBill);
    }

    private void processListBillDetails() {
        for (int i = 0; i < receiptTable.getRowCount(); i++) {
            BillDetails billDetails = new BillDetails(receiptBillCodeTxt.getText(), receiptTable.getValueAt(i, 1).toString(),
                    Integer.parseInt(receiptTable.getValueAt(i, 3).toString()), Double.parseDouble(receiptTable.getValueAt(i, 4).toString().replaceAll("[,đ]","")));

            listBillDetails.add(billDetails);
        }
        if (listBillDetails.size() == receiptTable.getRowCount()) {
            for (BillDetails billDetails : listBillDetails) {
                DeliveryBillDetailsDAO.getInstance().insert(billDetails);
            }
        }
        else {
            JOptionPane.showMessageDialog(this, "Quá trình lưu phiếu chi tiết không thành công!");
        }
    }

    private void processListComputer() {
        for (BillDetails billDetails : listBillDetails) {
            Computer computer = ComputerDAO.getInstance().selectById(billDetails.getMachineCode());
            computer.setQuantity(computer.getQuantity() - billDetails.getQuantity());
            ComputerDAO.getInstance().update(computer);
        }
    }
    public void editButtonActionListener2(ActionEvent e) {
        if (receiptTable.getSelectedRow() != -1) {
            int quantityStock = ComputerDAO.getInstance().selectById(receiptTable.getValueAt(receiptTable.getSelectedRow(), 1).toString()).getQuantity();
            String resultString = JOptionPane.showInputDialog(this, "Nhập số lượng cần thay đổi", "Thay đổi số lượng", JOptionPane.OK_CANCEL_OPTION);
            if (resultString != null) {
                try {
                    int quantity = Integer.parseInt(resultString);
                    if (quantity != 0) {
                        if (quantity <= quantityStock) {
                            receiptTable.setValueAt(quantity, receiptTable.getSelectedRow(), 3);
                            setTotalPrice();
                        } else {
                            JOptionPane.showMessageDialog(this, "Số lượng trong kho không đủ!");
                        }
                    } else throw new Exception();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập số nguyên dương!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thông tin cần chỉnh sửa số lượng!");
        }
    }

    public String createBillCode(ArrayList<DeliveryBill> listDeliveryBill) {
        int code = listDeliveryBill.size() + 1;
        while (true) {
            String billCode = "PX" + code;
            boolean isExist = false;
            for (DeliveryBill deliveryBill : listDeliveryBill) {
                if (billCode.equals(deliveryBill.getBillCode())) {
                    isExist = true;
                }
            }
            if (isExist) code++;
            else return billCode;
        }
    }
    public void addButtonActionPerformed2(ActionEvent e) {
        if (contentTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm!");
        }
        else if (quantityTxt.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng!");
        }
        else {
            try {
                String machineCode = contentTable.getValueAt(contentTable.getSelectedRow(), 0).toString();
                String machineName = contentTable.getValueAt(contentTable.getSelectedRow(), 1).toString();
                int quantity = Integer.parseInt(quantityTxt.getText());
                String price = contentTable.getValueAt(contentTable.getSelectedRow(), 3).toString();

                if (isExistInReceiptTable(machineCode) != -1) {
                    int resultRow = isExistInReceiptTable(machineCode);
                    int oldQuantity = Integer.parseInt(receiptTable.getValueAt(resultRow, 3).toString());
                    int updateQuantity = oldQuantity + quantity;
                    if (updateQuantity > Integer.parseInt(contentTable.getValueAt(contentTable.getSelectedRow(), 2).toString())) {
                        JOptionPane.showMessageDialog(this, "Số lượng trong kho không đủ!");
                    }
                    else {
                        receiptTable.setValueAt(updateQuantity, resultRow, 3);
                    }
                }
                else {
                    if (quantity > Integer.parseInt(contentTable.getValueAt(contentTable.getSelectedRow(), 2).toString())) {
                        JOptionPane.showMessageDialog(this, "Số lượng trong kho không đủ!");
                    }
                    else {
                        defaultTableModel2.addRow(new Object[]{receiptTable.getRowCount() + 1, machineCode, machineName, quantity, price});
                    }
                }
                setTotalPrice();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
