package view;

import dao.ComputerDAO;
import dao.DesktopComputerDAO;
import dao.LaptopDAO;
import model.Computer;
import model.DesktopComputer;
import model.Laptop;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditProduct extends AddProduct{
    protected JLabel quantityLabel;
    protected JTextField quantityTxt;
    protected JButton addButton2;

    public EditProduct(JInternalFrame parent, JFrame owner, boolean modal) {
        super(parent, owner, modal);
        initComponent2();
        this.setSize(900, 500);
        this.setLocationRelativeTo(null);
    }

    private void initComponent2() {

        addButton2 = new JButton("Lưu thay đổi");
        addButton2.setBackground(new Color(13, 39, 51));
        addButton2.setForeground(new Color(255, 255, 255));
        addButton2.setBorder(null);
        addButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        addButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addButton2ActionPerformed(e);
            }
        });
        quantityLabel = new JLabel("Số lượng");
        quantityLabel.setFont(this.font);
        quantityLabel.setBackground(Color.WHITE);

        quantityTxt = new JTextField();
        quantityTxt.setFont(this.font);

        PlainDocument document = (PlainDocument) quantityTxt.getDocument();
        document.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (isNumeric(string)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
                if (isNumeric(string)) {
                    super.replace(fb, offset, length, string, attr);
                }
            }
            private boolean isNumeric(String string) {
                return string.matches("[0-9]*");
            }
        });

        this.machineCodeTxt.setEditable(false);
        this.machineCodeTxt.setRequestFocusEnabled(false);

        this.titleLabel.setText("CẬP NHẬT THÔNG TIN SẢN PHẨM");
        this.machineCodeTxt.setText(this.owner.getContentTable().getValueAt(this.owner.getContentTable().getSelectedRow(), 0).toString());
        this.machineNameTxt.setText(this.owner.getContentTable().getValueAt(this.owner.getContentTable().getSelectedRow(), 1).toString());
        this.quantityTxt.setText(this.owner.getContentTable().getValueAt(this.owner.getContentTable().getSelectedRow(), 2).toString());
        this.priceTxt.setText(this.owner.getContentTable().getValueAt(this.owner.getContentTable().getSelectedRow(), 3).toString().replaceAll("[,đ]", ""));
        this.cpuTxt.setText(this.owner.getContentTable().getValueAt(this.owner.getContentTable().getSelectedRow(), 4).toString());
        this.ramTxt.setText(this.owner.getContentTable().getValueAt(this.owner.getContentTable().getSelectedRow(), 5).toString());
        this.romTxt.setText(this.owner.getContentTable().getValueAt(this.owner.getContentTable().getSelectedRow(), 6).toString());
        this.typeComboBox.setSelectedItem(this.owner.getContentTable().getValueAt(this.owner.getContentTable().getSelectedRow(), 7).toString());

        DesktopComputer desktopComputer = DesktopComputerDAO.getInstance().selectById(this.owner.getContentTable().getValueAt(this.owner.getContentTable().getSelectedRow(), 0).toString());
        Laptop laptop = LaptopDAO.getInstance().selectById(this.owner.getContentTable().getValueAt(this.owner.getContentTable().getSelectedRow(), 0).toString());
        if (laptop != null) {
            this.screenSizeTxt.setText(Double.toString(laptop.getScreenSize()));
            this.pinTxt.setText(laptop.getBatteryCapacity());
            this.originTxt.setText(laptop.getOrigin());
            this.cardTxt.setText(laptop.getGraphicsCard());
        }
        else {
            this.mainboardTxt.setText(desktopComputer.getMainboard());
            this.powerSupplyTxt.setText(Integer.toString(desktopComputer.getPowerSupply()));
            this.originTxt.setText(desktopComputer.getOrigin());
            this.cardTxt.setText(desktopComputer.getGraphicsCard());
        }

        contentPanel.removeAll();

        javax.swing.GroupLayout contentLayout = new javax.swing.GroupLayout(contentPanel);
        contentPanel.setLayout(contentLayout);
        contentLayout.setHorizontalGroup(
                contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(contentLayout.createSequentialGroup()
                                .addGap(58, 58, 58)
                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(contentLayout.createSequentialGroup()
                                                .addComponent(priceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(198, 198, 198)
                                                .addComponent(romLabel))
                                        .addGroup(contentLayout.createSequentialGroup()
                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                .addComponent(originTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                                                                .addComponent(priceTxt))
                                                        .addComponent(originLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(91, 91, 91)
                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(cardTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(romTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(cardLabel)))
                                        .addGroup(contentLayout.createSequentialGroup()
                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(machineNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(machineNameLabel))
                                                .addGap(91, 91, 91)
                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(ramLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(ramTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(contentLayout.createSequentialGroup()
                                                .addComponent(machineCodeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(91, 91, 91)
                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(cpuLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(cpuTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(machineCodeLabel))
                                .addGap(124, 124, 124)
                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(computerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(typeComboBox, 0, 186, Short.MAX_VALUE)
                                        .addComponent(quantityTxt)
                                        .addGroup(contentLayout.createSequentialGroup()
                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(typeLabel)
                                                        .addComponent(quantityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(43, 43, 43))
                        .addGroup(contentLayout.createSequentialGroup()
                                .addGap(242, 242, 242)
                                .addComponent(addButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(85, 85, 85)
                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        contentLayout.setVerticalGroup(
                contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(contentLayout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(machineCodeLabel)
                                        .addComponent(cpuLabel)
                                        .addComponent(typeLabel))
                                .addGap(5, 5, 5)
                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(machineCodeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cpuTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(typeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(contentLayout.createSequentialGroup()
                                                .addComponent(computerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(quantityLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(quantityTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(contentLayout.createSequentialGroup()
                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(machineNameLabel)
                                                        .addComponent(ramLabel))
                                                .addGap(6, 6, 6)
                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(machineNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(ramTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(contentLayout.createSequentialGroup()
                                                                .addGap(12, 12, 12)
                                                                .addComponent(romLabel))
                                                        .addGroup(contentLayout.createSequentialGroup()
                                                                .addGap(18, 18, 18)
                                                                .addComponent(priceLabel)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(romTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(priceTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(contentLayout.createSequentialGroup()
                                                                .addGap(18, 18, 18)
                                                                .addComponent(originLabel)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(originTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(contentLayout.createSequentialGroup()
                                                                .addGap(15, 15, 15)
                                                                .addComponent(cardLabel)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cardTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(addButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(29, 29, 29))))
        );

        getContentPane().removeAll();
        getContentPane().add(contentPanel);
        getContentPane().add(headerPanel);
        pack();
    }
    public void addButton2ActionPerformed(ActionEvent event) {
        String machineCode = machineCodeTxt.getText();
        String machineName = machineNameTxt.getText();
        String cpu = cpuTxt.getText();
        String ram = ramTxt.getText();
        String rom = romTxt.getText();
        String card = cardTxt.getText();
        String type = typeComboBox.getSelectedItem().toString();
        String origin = originTxt.getText();

        int quantity = 0;
        int powerSupply = 0;
        double price = 0;
        double screenSize = 0;
        String pin;
        String mainboard;

        boolean isEmpty = false;

        try {
            price = Double.parseDouble(priceTxt.getText());
        } catch (Exception e) {
            isEmpty = true;
        }

        try {
            quantity = Integer.parseInt(quantityTxt.getText());
        } catch (Exception e) {
            isEmpty = true;
        }

        if (type.equals("Laptop")) {
            try {
                screenSize = Double.parseDouble(screenSizeTxt.getText());
            } catch (Exception e) {
                isEmpty = true;
            }
            pin = pinTxt.getText();

            if (machineCode.equals("") || machineName.equals("") || cpu.equals("") || ram.equals("") || rom.equals("") || card.equals("") || pin.equals("") || origin.equals("")) {
                isEmpty = true;
            }

            if (isEmpty) {
                JOptionPane.showMessageDialog(this, "Không để trống thông tin");
            }
            else {
                try {
                    Laptop laptop = new Laptop(machineCode, machineName, quantity, cpu, ram, card, rom, price, origin, 1, screenSize, pin);
                    LaptopDAO.getInstance().update(laptop);
                    this.dispose();
                    JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công");
                    owner.loadDataToTable();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thông tin thất bại");
                }
            }
        }

        if (type.equals("PC")) {
            try {
                powerSupply = Integer.parseInt(powerSupplyTxt.getText());
            } catch (Exception e) {
                isEmpty = true;
            }
            mainboard = mainboardTxt.getText();

            if (machineCode.equals("") || machineName.equals("") || cpu.equals("") || ram.equals("") || rom.equals("") || card.equals("") || mainboard.equals("") || origin.equals("")) {
                isEmpty = true;
            }

            if (isEmpty) {
                JOptionPane.showMessageDialog(this, "Không để trống thông tin");
            }
            else {
                try {
                    DesktopComputer desktopComputer = new DesktopComputer(machineCode, machineName, quantity, cpu, ram, card, rom, price, origin, 1, mainboard, powerSupply);
                    DesktopComputerDAO.getInstance().update(desktopComputer);
                    this.dispose();
                    JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công");
                    owner.loadDataToTable();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thông tin thất bại");
                }
            }
        }
    }
}
