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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.DoubleBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class AddProduct extends JDialog {
    protected ProductForm owner;
    protected JPanel headerPanel;
    protected JPanel contentPanel;
    protected JPanel computerPanel;
    protected JPanel desktopComputerPanel;
    protected JPanel laptopPanel;
    protected JLabel titleLabel;
    protected JLabel machineCodeLabel;
    protected JLabel machineNameLabel;
    protected JLabel priceLabel;
    protected JLabel originLabel;
    protected JLabel cpuLabel;
    protected JLabel ramLabel;
    protected JLabel romLabel;
    protected JLabel cardLabel;
    protected JLabel typeLabel;
    protected JLabel screenSizeLabel;
    protected JLabel pinLabel;
    protected JLabel mainboardLabel;
    protected JLabel powerSupplyLabel;
    protected JTextField machineCodeTxt;
    protected JTextField machineNameTxt;
    protected JTextField priceTxt;
    protected JTextField originTxt;
    protected JTextField cpuTxt;
    protected JTextField ramTxt;
    protected JTextField romTxt;
    protected JTextField cardTxt;
    protected JTextField screenSizeTxt;
    protected JTextField pinTxt;
    protected JTextField mainboardTxt;
    protected JTextField powerSupplyTxt;
    protected JComboBox typeComboBox;
    protected JButton addButton;
    protected JButton cancelButton;

    protected Font font = new Font("SF Pro Display", 0, 16);
    protected DecimalFormat decimalFormat = new DecimalFormat("###,###,###");


    public AddProduct(JInternalFrame parent, JFrame owner, boolean modal) {
        super(owner, modal);
        this.owner = (ProductForm) parent;
        initComponent();
        this.setSize(900, 500);
        this.setLocationRelativeTo(null);
        machineCodeTxt.setText(createIdLT());
    }

    private void initComponent() {

        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.getContentPane().setLayout(null);

        headerPanel = new JPanel();
        headerPanel.setBackground(new Color(13, 39, 51));
        headerPanel.setBounds(0, 0, 880, 60);

        titleLabel = new JLabel("THÊM SẢN PHẨM MỚI");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(font);

        GroupLayout layoutHeaderPanel = new GroupLayout(headerPanel);
        headerPanel.setLayout(layoutHeaderPanel);

        layoutHeaderPanel.setHorizontalGroup(
                layoutHeaderPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layoutHeaderPanel.createSequentialGroup()
                                .addContainerGap(317, Short.MAX_VALUE)
                                .addComponent(titleLabel)
                                .addGap(315, 315, 315))
        );
        layoutHeaderPanel.setVerticalGroup(
                layoutHeaderPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layoutHeaderPanel.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(titleLabel, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(15, Short.MAX_VALUE))
        );

        contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBounds(0, 60, 880, 400);

        machineCodeLabel = new JLabel("Mã sản phẩm");
        machineCodeLabel.setFont(font);
        machineCodeLabel.setBackground(Color.WHITE);

        machineNameLabel = new JLabel("Tên sản phẩm");
        machineNameLabel.setFont(font);
        machineNameLabel.setBackground(Color.WHITE);

        priceLabel = new JLabel("Đơn giá");
        priceLabel.setFont(font);
        priceLabel.setBackground(Color.WHITE);

        originLabel = new JLabel("Xuất xứ");
        originLabel.setFont(font);
        originLabel.setBackground(Color.WHITE);

        cpuLabel = new JLabel("CPU");
        cpuLabel.setFont(font);
        cpuLabel.setBackground(Color.WHITE);

        ramLabel = new JLabel("RAM");
        ramLabel.setFont(font);
        ramLabel.setBackground(Color.WHITE);

        romLabel = new JLabel("Dung lượng lưu trữ");
        romLabel.setFont(font);
        romLabel.setBackground(Color.WHITE);

        cardLabel = new JLabel("Card đồ họa");
        cardLabel.setFont(font);
        cardLabel.setBackground(Color.WHITE);

        typeLabel = new JLabel("Loại sản phẩm");
        typeLabel.setFont(font);
        typeLabel.setBackground(Color.WHITE);

        screenSizeLabel = new JLabel("Kích thước màn hình");
        screenSizeLabel.setFont(font);
        screenSizeLabel.setBackground(Color.WHITE);

        pinLabel = new JLabel("Dung lượng PIN");
        pinLabel.setFont(font);
        pinLabel.setBackground(Color.WHITE);

        mainboardLabel = new JLabel("MainBoard");
        mainboardLabel.setFont(font);
        mainboardLabel.setBackground(Color.WHITE);

        powerSupplyLabel = new JLabel("Công suất nguồn");
        powerSupplyLabel.setFont(font);
        powerSupplyLabel.setBackground(Color.WHITE);

        machineCodeTxt = new JTextField();
        machineCodeTxt.setFont(font);

        machineNameTxt = new JTextField();
        machineNameTxt.setFont(font);

        priceTxt = new JTextField();
        priceTxt.setFont(font);
        PlainDocument doc = (PlainDocument) priceTxt.getDocument();
        doc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (isValidDouble(string, fb.getDocument().getText(0, fb.getDocument().getLength()), offset)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
                if (isValidDouble(string, fb.getDocument().getText(0, fb.getDocument().getLength()), offset)) {
                    super.replace(fb, offset, length, string, attr);
                }
            }

            // Hàm kiểm tra chuỗi nhập vào có phải là số thập phân hợp lệ hay không
            private boolean isValidDouble(String string, String currentText, int offset) {
                StringBuilder sb = new StringBuilder(currentText);
                sb.insert(offset, string);
                String newText = sb.toString();
                try {
                    Double.parseDouble(newText);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        });

        originTxt = new JTextField();
        originTxt.setFont(font);

        cpuTxt = new JTextField();
        cpuTxt.setFont(font);

        ramTxt = new JTextField();
        ramTxt.setFont(font);

        romTxt = new JTextField();
        romTxt.setFont(font);

        cardTxt = new JTextField();
        cardTxt.setFont(font);

        screenSizeTxt = new JTextField();
        screenSizeTxt.setFont(font);
        PlainDocument d = (PlainDocument) screenSizeTxt.getDocument();
        d.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (isValidDouble(string, fb.getDocument().getText(0, fb.getDocument().getLength()), offset)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
                if (isValidDouble(string, fb.getDocument().getText(0, fb.getDocument().getLength()), offset)) {
                    super.replace(fb, offset, length, string, attr);
                }
            }

            // Hàm kiểm tra chuỗi nhập vào có phải là số thập phân hợp lệ hay không
            private boolean isValidDouble(String string, String currentText, int offset) {
                StringBuilder sb = new StringBuilder(currentText);
                sb.insert(offset, string);
                String newText = sb.toString();
                try {
                    Double.parseDouble(newText);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        });

        pinTxt = new JTextField();
        pinTxt.setFont(font);

        mainboardTxt = new JTextField();
        mainboardTxt.setFont(font);

        powerSupplyTxt = new JTextField();
        powerSupplyTxt.setFont(font);
        PlainDocument document = (PlainDocument) powerSupplyTxt.getDocument();
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

        typeComboBox = new JComboBox<>();
        typeComboBox.setBackground(Color.WHITE);
        typeComboBox.setFont(font);
        typeComboBox.setModel(new DefaultComboBoxModel<>(new String[] { "Laptop", "PC" }));

        typeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                typeComboBoxItemStateChanged(e);
            }
        });
        typeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                typeComboBoxActionPerformed(e);
            }
        });
        typeComboBox.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                typeComboBoxPropertyChange(e);
            }
        });

        addButton = new JButton("Add");
        addButton.setBackground(new Color(13, 39, 51));
        addButton.setForeground(new Color(255, 255, 255));
        addButton.setText("Thêm sản phẩm");
        addButton.setBorder(null);
        addButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addButtonActionPerformed(e);
            }
        });

        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(255, 0, 0));
        cancelButton.setForeground(new Color(255, 255, 255));
        cancelButton.setText("Huỷ bỏ");
        cancelButton.setBorder(null);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonActionPerformed(e);
            }
        });
        desktopComputerPanel = new JPanel();
        desktopComputerPanel.setBackground(Color.WHITE);

        GroupLayout desktopComputerLayout = new GroupLayout(desktopComputerPanel);
        desktopComputerPanel.setLayout(desktopComputerLayout);
        desktopComputerLayout.setHorizontalGroup(
                desktopComputerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(mainboardTxt)
                        .addComponent(powerSupplyTxt)
                        .addGroup(desktopComputerLayout.createSequentialGroup()
                                .addGroup(desktopComputerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(mainboardLabel)
                                        .addComponent(powerSupplyLabel, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 82, Short.MAX_VALUE))
        );
        desktopComputerLayout.setVerticalGroup(
                desktopComputerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(desktopComputerLayout.createSequentialGroup()
                                .addComponent(mainboardLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mainboardTxt, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(powerSupplyLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(powerSupplyTxt, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
        );

        laptopPanel = new JPanel();
        laptopPanel.setBackground(Color.WHITE);

        GroupLayout laptopLayout = new GroupLayout(laptopPanel);
        laptopPanel.setLayout(laptopLayout);
        laptopLayout.setHorizontalGroup(
                laptopLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(screenSizeTxt)
                        .addGroup(laptopLayout.createSequentialGroup()
                                .addGroup(laptopLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(screenSizeLabel)
                                        .addComponent(pinLabel, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 98, Short.MAX_VALUE))
                        .addComponent(pinTxt)
        );
        laptopLayout.setVerticalGroup(
                laptopLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(laptopLayout.createSequentialGroup()
                                .addComponent(screenSizeLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(screenSizeTxt, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pinLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pinTxt, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
        );


        computerPanel = new JPanel();
        computerPanel.setLayout(new CardLayout());

        computerPanel.add(laptopPanel, "card2");
        computerPanel.add(desktopComputerPanel, "card2");

        contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBounds(0, 60, 880, 400);

        GroupLayout contentLayout = new GroupLayout(contentPanel);
        contentPanel.setLayout(contentLayout);
        contentLayout.setHorizontalGroup(
                contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(contentLayout.createSequentialGroup()
                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(contentLayout.createSequentialGroup()
                                                .addGap(58, 58, 58)
                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(contentLayout.createSequentialGroup()
                                                                .addComponent(priceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(221, 221, 221)
                                                                .addComponent(romLabel))
                                                        .addGroup(contentLayout.createSequentialGroup()
                                                                .addComponent(priceTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(91, 91, 91)
                                                                .addComponent(romTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(contentLayout.createSequentialGroup()
                                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addGroup(contentLayout.createSequentialGroup()
                                                                                .addComponent(originLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(224, 224, 224))
                                                                        .addGroup(contentLayout.createSequentialGroup()
                                                                                .addComponent(originTxt)
                                                                                .addGap(91, 91, 91)))
                                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(cardLabel)
                                                                        .addComponent(cardTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addGroup(contentLayout.createSequentialGroup()
                                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(machineNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(machineNameLabel))
                                                                .addGap(91, 91, 91)
                                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(ramLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(ramTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addGroup(contentLayout.createSequentialGroup()
                                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(machineCodeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(machineCodeLabel))
                                                                .addGap(91, 91, 91)
                                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(cpuLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(cpuTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                        .addGroup(contentLayout.createSequentialGroup()
                                                .addGap(262, 262, 262)
                                                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(29, 29, 29)
                                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(57, 57, 57)
                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(contentLayout.createSequentialGroup()
                                                .addComponent(typeLabel)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(computerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(typeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(43, 43, 43))
        );
        contentLayout.setVerticalGroup(
                contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(contentLayout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(machineCodeLabel)
                                                .addComponent(cpuLabel))
                                        .addComponent(typeLabel))
                                .addGap(5, 5, 5)
                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(machineCodeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cpuTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(typeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(contentLayout.createSequentialGroup()
                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(machineNameLabel)
                                                        .addComponent(ramLabel))
                                                .addGap(6, 6, 6)
                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(machineNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(ramTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(12, 12, 12)
                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(priceLabel)
                                                        .addComponent(romLabel))
                                                .addGap(12, 12, 12)
                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(priceTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(romTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(contentLayout.createSequentialGroup()
                                                                .addGap(9, 9, 9)
                                                                .addComponent(cardLabel))
                                                        .addGroup(contentLayout.createSequentialGroup()
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(originLabel)))
                                                .addGap(4, 4, 4)
                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(cardTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(originTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(computerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(45, 45, 45)
                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(40, Short.MAX_VALUE))
        );
        getContentPane().add(headerPanel);
        getContentPane().add(contentPanel);

        pack();
    }

    public String createIdLT() {
        ArrayList<Computer> mtAll = ComputerDAO.getInstance().selectAll();
        ArrayList<Computer> lpAll = new ArrayList<Computer>();
        for (Computer computer : mtAll) {
            if (computer.getMachineCode().contains("LP")) {
                lpAll.add(computer);
            }
        }
        int i = lpAll.size();
        String check ="check";
        while(check.length()!=0){
            i++;
            for (Computer computer : lpAll) {
                if(computer.getMachineCode().equals("LP"+i)){
                    check="";
                }
            }
            if(check.length()==0){
                check ="check";
            } else {
                check = "";
            }
        }
        return "LP" + i;
    }

    public String createIdPC() {
        ArrayList<Computer> mtAll = ComputerDAO.getInstance().selectAll();
        ArrayList<Computer> pcAll = new ArrayList<>();

        for (Computer computer : mtAll) {
            if (computer.getMachineCode().contains("PC")) {
                pcAll.add(computer);
            }
        }
        int i = pcAll.size();
        while (true) {
            boolean check = false;
            i++;
            for (Computer computer : pcAll) {
                if (computer.getMachineCode().equals("PC" + i)) {
                    check = true;
                }
            }
            if (!check) {
                return "PC" + i;
            }
        }
    }


    private void typeComboBoxItemStateChanged(ItemEvent e) {
        if (typeComboBox.getSelectedItem().equals("Laptop")) {
            CardLayout typeComputer = (CardLayout) computerPanel.getLayout();
            typeComputer.first(computerPanel);
            machineCodeTxt.setText(createIdLT());
        }
        else if (typeComboBox.getSelectedItem().equals("PC")) {
            CardLayout typeComputer = (CardLayout) computerPanel.getLayout();
            typeComputer.last(computerPanel);
            machineCodeTxt.setText(createIdPC());
        }
    }

    private void typeComboBoxActionPerformed(ActionEvent e) {

    }

    private void typeComboBoxPropertyChange(PropertyChangeEvent e) {

    }

    private void addButtonActionPerformed(ActionEvent event) {
        String machineCode = machineCodeTxt.getText();
        String machineName = machineNameTxt.getText();
        String origin = originTxt.getText();
        String cpu = cpuTxt.getText();
        String ram = ramTxt.getText();
        String rom = romTxt.getText();
        String card = cardTxt.getText();
        String batteryCapacity;
        String mainboard;
        double screenSize = 0;
        int powerSupply = 0;
        double price = 0;
        boolean isEmpty = false;

        try {
            price = Double.parseDouble(priceTxt.getText());
        } catch (NumberFormatException e) {
            isEmpty = true;
        }

        if (typeComboBox.getSelectedItem().equals("Laptop")) {
            try {
                screenSize = Double.parseDouble(screenSizeTxt.getText());
            } catch (NumberFormatException e) {
                isEmpty = true;
            }
            batteryCapacity = pinTxt.getText();

            if (machineCode.equals("") || machineName.equals("") || origin.equals("") || cpu.equals("") || ram.equals("") || rom.equals("") || card.equals("")) {
                isEmpty = true;
            }

            if (isEmpty) JOptionPane.showMessageDialog(this, "Không để trống thông tin");
            else {
                try {
                    Laptop laptop = new Laptop(machineCode, machineName, 0, cpu, ram, card, rom, price, origin, 1, screenSize, batteryCapacity);
                    LaptopDAO.getInstance().insert(laptop);
                    this.dispose();
                    JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công");
                    this.owner.loadDataToTable();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Thêm sản phẩm thất bại");
                }
            }
        }

        if (typeComboBox.getSelectedItem().equals("PC")) {
            mainboard = mainboardTxt.getText();
            try {
                powerSupply = Integer.parseInt(powerSupplyTxt.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Không để trống thông tin");
            }

            if (machineCode.equals("") || machineName.equals("") || origin.equals("") || cpu.equals("") || ram.equals("") || rom.equals("") || card.equals("")) {
                JOptionPane.showMessageDialog(this, "Không để trống thông tin");
            }
            else {

                try {
                    DesktopComputer desktopComputer = new DesktopComputer(machineCode, machineName, 0, cpu, ram, card, rom, price, origin, 1, mainboard, powerSupply);
                    DesktopComputerDAO.getInstance().insert(desktopComputer);
                    this.dispose();
                    JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công");
                    this.owner.loadDataToTable();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Thêm sản phẩm thất bại");
                }
            }
        }

    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        this.dispose();
    }
}
