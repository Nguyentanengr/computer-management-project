package view;

import dao.SupplierDAO;
import model.Supplier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditSupplier extends AddSupplier{
    public EditSupplier(JInternalFrame parent, JFrame owner, boolean modal) {
        super(parent, owner, modal);
        initComponent2();
        this.setSize(400, 530);
        this.setLocationRelativeTo(null);
    }

    public void initComponent2() {
        this.headerLabel.setText("CẬP NHẬT THÔNG TIN");
        this.addButton.setText("Lưu thay đổi!");
        this.supplierCodeTxt.setText(this.owner.getContentTable().getValueAt(this.owner.getContentTable().getSelectedRow(), 0).toString());
        this.supplierNameTxt.setText(this.owner.getContentTable().getValueAt(this.owner.getContentTable().getSelectedRow(), 1).toString());
        this.phoneTxt.setText(this.owner.getContentTable().getValueAt(this.owner.getContentTable().getSelectedRow(), 2).toString());
        this.addressTxt.setText(this.owner.getContentTable().getValueAt(this.owner.getContentTable().getSelectedRow(), 3).toString());

        this.supplierCodeTxt.setEditable(false);
        this.supplierCodeTxt.setRequestFocusEnabled(false);

        ActionListener[] listeners = addButton.getActionListeners();
        for (ActionListener listener : listeners) {
            addButton.removeActionListener(listener);
        }

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addButtonActionPerformed2(e);
            }
        });
    }

    public void addButtonActionPerformed2(ActionEvent e) {
        String supplierCode = this.supplierCodeTxt.getText();
        String supplierName = this.supplierNameTxt.getText();
        String phone = this.phoneTxt.getText();
        String address = this.addressTxt.getText();

        if (supplierCode.equals("") || supplierName.equals("") || phone.equals("") || address.equals("")) {
            JOptionPane.showMessageDialog(this, "Không để thông tin rỗng");
        }
        else if (phone.length() < 8 || phone.charAt(0) != '0'){
            JOptionPane.showMessageDialog(this, "Số điện thoại chưa đúng định dạng");
        }
        else {
            try {
                Supplier supplier = new Supplier(supplierCode, supplierName, phone, address);
                if (SupplierDAO.getInstance().update(supplier) > 0) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                    this.dispose();
                    owner.loadDataToTable(SupplierDAO.getInstance().selectAll());
                }
                else throw new Exception();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cập nhật không thành công", "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
