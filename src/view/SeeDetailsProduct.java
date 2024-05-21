package view;

import javax.swing.*;

public class SeeDetailsProduct extends EditProduct{
    public SeeDetailsProduct(JInternalFrame parent, JFrame owner, boolean modal) {
        super(parent, owner, modal);
        initComponent3();
        setLocationRelativeTo(null);
    }
    public void initComponent3() {
        this.titleLabel.setText("THÔNG TIN SẢN PHẨM");

        this.machineNameTxt.setEditable(false);
        this.machineNameTxt.setRequestFocusEnabled(true);

        this.priceTxt.setEditable(false);
        this.priceTxt.setRequestFocusEnabled(false);

        this.originTxt.setEditable(false);
        this.originTxt.setRequestFocusEnabled(false);

        this.cpuTxt.setEditable(false);
        this.cpuTxt.setRequestFocusEnabled(false);

        this.ramTxt.setEditable(false);
        this.ramTxt.setRequestFocusEnabled(false);

        this.romTxt.setEditable(false);
        this.romTxt.setRequestFocusEnabled(false);

        this.cardTxt.setEditable(false);
        this.cardTxt.setRequestFocusEnabled(false);

        this.typeComboBox.setEnabled(false);
        this.typeComboBox.setRequestFocusEnabled(false);

        this.screenSizeTxt.setEditable(false);
        this.screenSizeTxt.setEditable(false);

        this.mainboardTxt.setEditable(false);
        this.mainboardTxt.setRequestFocusEnabled(false);

        this.powerSupplyTxt.setEditable(false);
        this.powerSupplyTxt.setRequestFocusEnabled(false);

        this.pinTxt.setEditable(false);
        this.pinTxt.setRequestFocusEnabled(false);

        this.quantityTxt.setEditable(false);
        this.quantityTxt.setRequestFocusEnabled(false);

        this.addButton2.setVisible(false);
        this.cancelButton.setVisible(false);
    }
}
