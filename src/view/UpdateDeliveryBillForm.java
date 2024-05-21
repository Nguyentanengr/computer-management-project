package view;

import com.formdev.flatlaf.ui.FlatLineBorder;
import controller.SearchProduct;
import controller.WritePDF;
import dao.*;
import model.BillDetails;
import model.Computer;
import model.DeliveryBill;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class UpdateDeliveryBillForm extends JDialog {
    private DeliveryBillForm owner;
    protected JPanel mainPanel;
    protected JPanel leftPanel;
    protected JPanel rightPanel;
    protected JPanel searchPanel;
    protected JTextField searchTxt;
    protected JTextField quantityTxt;
    protected JTextField deliveryBillCodeTxt;
    protected JTextField deliveryBillCreatorTxt;
    protected JButton refreshButton;
    protected JButton editButton;
    protected JButton deleteButton;
    protected JButton deliveryButton;
    protected JButton addButton;
    protected JLabel quantityLabel;
    protected JLabel deliveryBillCodeLabel;
    protected JLabel deliveryBillCreatorLabel;
    protected JLabel totalPriceTextLabel;
    protected JLabel totalPriceLabel;
    protected JTable contentTable;
    protected JScrollPane contentScrollPane;
    protected JTable deliveryTable;
    protected JScrollPane deliveryScrollPane;

    protected DefaultTableModel defaultTableModel;
    protected DefaultTableModel defaultTableModel2;
    protected DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    protected Font fontDefault = new Font("Arial", 0, 15);

    private DeliveryBill deliveryBillGlobal;
    private ArrayList<BillDetails> listBillDetails;
    private String deliveryBillCodeGlobal;
    public UpdateDeliveryBillForm(DeliveryBillForm parent, JFrame owner, boolean modal)  throws UnsupportedLookAndFeelException{
        super(owner, modal);
        this.owner = parent;

        deliveryBillCodeGlobal = this.owner.getContentTable().getValueAt(this.owner.getContentTable().getSelectedRow(), 1).toString();
        deliveryBillGlobal = DeliveryBillDAO.getInstance().selectById(deliveryBillCodeGlobal);
        listBillDetails = DeliveryBillDetailsDAO.getInstance().selectBy(deliveryBillCodeGlobal);

        initComponents();
        initTable();
        loadDataToContentTable(ComputerDAO.getInstance().selectAll());
        loadDataToDeliveryTable();
    }

    private void initTable() {
        defaultTableModel = new DefaultTableModel();
        defaultTableModel2 = new DefaultTableModel();
        String[] headerTable = new String[]{"Mã máy", "Tên máy", "SL", "Đơn giá"};
        String[] headerTable2 = new String[]{"STT", "Mã SP", "Tên SP", "SL", "Đơn giá"};
        defaultTableModel.setColumnIdentifiers(headerTable);
        defaultTableModel2.setColumnIdentifiers(headerTable2);

        contentTable.setModel(defaultTableModel);
        contentTable.getColumnModel().getColumn(0).setPreferredWidth(60); // Mã máy
        contentTable.getColumnModel().getColumn(1).setPreferredWidth(270); // Tên máy
        contentTable.getColumnModel().getColumn(2).setPreferredWidth(50); // Số lượng
        contentTable.getColumnModel().getColumn(3).setPreferredWidth(100);

        deliveryTable.setModel(defaultTableModel2);
        deliveryTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        deliveryTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        deliveryTable.getColumnModel().getColumn(2).setPreferredWidth(280);
        deliveryTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        deliveryTable.getColumnModel().getColumn(4).setPreferredWidth(100);
    }

    private void loadDataToContentTable(ArrayList<Computer> listComputer) {
        if (listComputer != null) {
            defaultTableModel.setRowCount(0);
            for (Computer computer : listComputer) {
                defaultTableModel.addRow(new Object[]{computer.getMachineCode(), computer.getMachineName(), computer.getQuantity(), decimalFormat.format(computer.getPrice()) + "đ"});
            }
        }
    }

    private void loadDataToDeliveryTable() {
        defaultTableModel2.setRowCount(0);
        for (BillDetails billDetails : listBillDetails) {
            defaultTableModel2.addRow(new Object[] {deliveryTable.getRowCount() + 1, billDetails.getMachineCode(),
                    ComputerDAO.getInstance().selectById(billDetails.getMachineCode()).getMachineName(), billDetails.getQuantity(),
                    decimalFormat.format(billDetails.getUnitPrice()) + "đ"});
        }
        setTotalPrice();
    }

    private void setTotalPrice() {
        if (deliveryTable.getRowCount() > 0) {
            double totalPrice = 0;
            for (int i = 0; i < deliveryTable.getRowCount(); i++) {
                totalPrice += Double.parseDouble(deliveryTable.getValueAt(i, 4).toString().replaceAll("[,đ]", "")) * Integer.parseInt(deliveryTable.getValueAt(i, 3).toString());
            }
            totalPriceLabel.setText(decimalFormat.format(totalPrice) + "đ");
        } else {
            totalPriceLabel.setText("0đ");
        }
    }
    public void initComponents() {
        this.setSize(1000, 800);
        this.setLocationRelativeTo(null);

        searchTxt = new JTextField();
        searchTxt.setBorder(new FlatLineBorder(new Insets(5,5,5,5), Color.LIGHT_GRAY));
        searchTxt.setFont(fontDefault);
        searchTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchTxtKeyReleased(e);
            }
        });

        refreshButton = getButton("Làm mới", "/icon/refreshButton.png");
        refreshButton.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY, 1, 5));
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshButtonActionPerFormed(e);
            }
        });
        searchPanel = new JPanel();
        searchPanel.setFont(fontDefault);
        searchPanel.setBackground(Color.WHITE);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Tìm kiếm");
        titledBorder.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY));
        searchPanel.setBorder(titledBorder);

        javax.swing.GroupLayout searchLayout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(searchLayout);
        searchLayout.setHorizontalGroup(
                searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(searchLayout.createSequentialGroup()
                                .addContainerGap(17, Short.MAX_VALUE)
                                .addComponent(searchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19))
        );
        searchLayout.setVerticalGroup(
                searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, searchLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(searchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(11, 11, 11))
        );

        contentTable = new JTable();
        contentTable.setModel(new DefaultTableModel(
                new Object [][] {
                },
                new String [] {
                        "Mã máy", "Tên máy", "SL", "Đơn giá"
                }
        ));

        contentScrollPane = new JScrollPane();
        contentScrollPane.setViewportView(contentTable);
        contentScrollPane.getViewport().setBackground(Color.WHITE);
        contentScrollPane.setBackground(Color.WHITE);

        JTableHeader header = contentTable.getTableHeader();
        header.setFont(new Font("Arial", Font.PLAIN, 14));
        header.setBackground(Color.WHITE);

        // Customizing the scroll bars
        JScrollBar verticalScrollBar = new JScrollBar(JScrollBar.VERTICAL);
        verticalScrollBar.setPreferredSize(new Dimension(8, 0));
        verticalScrollBar.setBackground(Color.WHITE);

        verticalScrollBar.setUnitIncrement(60);  // Increase unit increment
        verticalScrollBar.setBlockIncrement(60);

        contentScrollPane.setVerticalScrollBar(verticalScrollBar);


        quantityLabel = new JLabel("Số lượng");
        quantityLabel.setFont(fontDefault);

        quantityTxt = new JTextField("1");
        quantityTxt.setHorizontalAlignment(JTextField.CENTER);
        quantityTxt.setFont(fontDefault);
        PlainDocument doc = (PlainDocument) quantityTxt.getDocument();
        doc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (isNumeric(string)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (isNumeric(text)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private boolean isNumeric(String text) {
                return text.matches("[0-9]*");
            }
        });


        addButton = new JButton("Thêm");
        addButton.setBackground(new Color(13, 39, 51));
        addButton.setForeground(Color.WHITE);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.setBorder(new LineBorder(Color.WHITE));
        addButton.setFont(fontDefault);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addButtonActionPerformed(e);
            }
        });

        leftPanel = new JPanel();
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBounds(0, 0, 470, 750);
        javax.swing.GroupLayout leftLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftLayout);
        leftLayout.setHorizontalGroup(
                leftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(leftLayout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(quantityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(quantityTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(189, Short.MAX_VALUE))
                        .addGroup( leftLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(leftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(contentScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        leftLayout.setVerticalGroup(
                leftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(leftLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(contentScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 523, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                                .addGroup(leftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(quantityTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(quantityLabel)
                                        .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27))
        );

        deliveryBillCodeLabel = new JLabel("Mã phiếu xuất");
        deliveryBillCodeLabel.setFont(fontDefault);
        deliveryBillCodeLabel.setBounds(30, 30, 200, 40);


        deliveryBillCreatorLabel = new JLabel("Người tạo phiếu");
        deliveryBillCreatorLabel.setFont(fontDefault);
        deliveryBillCreatorLabel.setBounds(30, 90,200, 40);

        deliveryBillCodeTxt = new JTextField();
        deliveryBillCodeTxt.setFont(fontDefault);
        deliveryBillCodeTxt.setBounds(170, 30, 330, 40);
        deliveryBillCodeTxt.setBorder(new FlatLineBorder(new Insets(5,5,5,5), Color.LIGHT_GRAY));
        deliveryBillCodeTxt.setText(deliveryBillGlobal.getBillCode());
        deliveryBillCodeTxt.setEditable(false);
        deliveryBillCodeTxt.setRequestFocusEnabled(false);


        deliveryBillCreatorTxt = new JTextField();
        deliveryBillCreatorTxt.setFont(fontDefault);
        deliveryBillCreatorTxt.setBounds(170, 90, 330, 40);
        deliveryBillCreatorTxt.setBorder(new FlatLineBorder(new Insets(5,5,5,5), Color.LIGHT_GRAY));
        deliveryBillCreatorTxt.setText(deliveryBillGlobal.getCreator());
        deliveryBillCreatorTxt.setEditable(false);
        deliveryBillCreatorTxt.setRequestFocusEnabled(false);

        deliveryTable = new JTable();
        deliveryTable.setModel(new DefaultTableModel(
                new Object [][]{

                },
                new String [] {
                        "STT", "Mã SP", "Tên SP", "Số lượng", "Đơn giá"
                }
        ));

        deliveryScrollPane = new JScrollPane();
        deliveryScrollPane.setViewportView(deliveryTable);
        deliveryScrollPane.getViewport().setBackground(Color.WHITE);
        deliveryScrollPane.setBounds(20, 190, 500, 400);
        deliveryScrollPane.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY));

        JTableHeader header2 = deliveryTable.getTableHeader();
        header2.setFont(new Font("Arial", Font.PLAIN, 14));
        header2.setBackground(Color.WHITE);

        editButton = getButton("Sửa số lượng", "/icon/editButton.png");
        editButton.setBounds(180, 610, 150, 40);
        editButton.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY, 1, 5));
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editButtonActionListener(e);
            }
        });
        deleteButton = getButton("Xóa sản phẩm", "/icon/deleteButton.png");
        deleteButton.setBounds(350, 610, 150, 40);
        deleteButton.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY, 1, 5));
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteButtonActionListener(e);
            }
        });
        deliveryButton = new JButton("Lưu thay đổi");
        deliveryButton.setForeground(Color.WHITE);
        deliveryButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deliveryButton.setBackground(new Color(13, 39, 51));
        deliveryButton.setBorder(new LineBorder(Color.WHITE));
        deliveryButton.setFont(fontDefault);
        deliveryButton.setBounds(360, 690, 123, 36);
        deliveryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deliveryButtonActionListener(e);
            }
        });
        totalPriceTextLabel = new JLabel("Tổng tiền");
        totalPriceTextLabel.setFont(new Font("SF Pro Display", 1, 18));
        totalPriceTextLabel.setBounds(80, 690, 120, 30);

        totalPriceLabel = new JLabel("0đ");
        totalPriceLabel.setFont(new Font("SF Pro Display", 1, 18));
        totalPriceLabel.setForeground(Color.RED);
        totalPriceLabel.setBounds(220, 690, 190, 30);

        rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(null);
        rightPanel.setBounds(470, 0, 620, 750);

        rightPanel.add(deliveryBillCodeLabel);
        rightPanel.add(deliveryBillCodeTxt);
        rightPanel.add(deliveryBillCreatorLabel);
        rightPanel.add(deliveryBillCreatorTxt);
        rightPanel.add(deliveryScrollPane);
        rightPanel.add(editButton);
        rightPanel.add(deleteButton);
        rightPanel.add(totalPriceLabel);
        rightPanel.add(totalPriceTextLabel);
        rightPanel.add(deliveryButton);

        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(null);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, 1000, GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, 800, GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }

    private ImageIcon getImageIcon(String pathIcon) {
        int widthIcon = 20, heighIcon = 20;
        URL urlIcon = UpdateDeliveryBillForm.class.getResource(pathIcon);
        Image imgUser = Toolkit.getDefaultToolkit().createImage(urlIcon).getScaledInstance(widthIcon, heighIcon, Image.SCALE_SMOOTH);
        return  new ImageIcon(imgUser);
    }

    private JButton getButton(String title, String pathIcon) {
        JButton designButton = new JButton(title, getImageIcon(pathIcon));
        designButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        designButton.setBackground(Color.WHITE);
        designButton.setBorder(new LineBorder(Color.WHITE));
        designButton.setFont(fontDefault);
        return designButton;
    }

    public void searchTxtKeyReleased(KeyEvent e) {
        String text = searchTxt.getText();
        ArrayList<Computer> listComputerResult = SearchProduct.getInstance().searchAll(text);
        loadDataToContentTable(listComputerResult);
    }

    public void refreshButtonActionPerFormed(ActionEvent e) {
        searchTxt.setText("");
        loadDataToContentTable(ComputerDAO.getInstance().selectAll());
    }

    public void addButtonActionPerformed(ActionEvent event) {
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

                if (isExistInDeliveryTable(machineCode) != -1) {
                    int resultRow = isExistInDeliveryTable(machineCode);
                    int oldQuantity = Integer.parseInt(deliveryTable.getValueAt(resultRow, 3).toString());
                    int updateQuantity = 0;
                    BillDetails billDetails = isExistInListBillDetails(machineCode);
                    if (billDetails == null) updateQuantity = oldQuantity + quantity;
                    else {
                        updateQuantity = oldQuantity + quantity - billDetails.getQuantity();
                    }
                    if (updateQuantity > Integer.parseInt(contentTable.getValueAt(contentTable.getSelectedRow(), 2).toString())) {
                        JOptionPane.showMessageDialog(this, "Số lượng trong kho không đủ!");
                    }
                    else {
                        deliveryTable.setValueAt(oldQuantity + quantity, resultRow, 3);
                    }
                }
                else {
                    if (quantity > Integer.parseInt(contentTable.getValueAt(contentTable.getSelectedRow(), 2).toString())) {
                        JOptionPane.showMessageDialog(this, "Số lượng trong kho không đủ!");
                    }
                    else {
                        defaultTableModel2.addRow(new Object[]{deliveryTable.getRowCount() + 1, machineCode, machineName, quantity, price});
                    }
                }
                setTotalPrice();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public BillDetails isExistInListBillDetails(String machineCode) {
        for (BillDetails billDetails : listBillDetails) {
            if (billDetails.getMachineCode().equals(machineCode)) {
                return billDetails;
            }
        }
        return null;
    }
    public int isExistInDeliveryTable(String machineCode) {
        int resultRow = -1;
        for (int i = 0; i < deliveryTable.getRowCount(); i++) {
            if (machineCode.equals(deliveryTable.getValueAt(i, 1).toString())) {
                resultRow = i; break;
            }
        }
        return resultRow;
    }

    public void editButtonActionListener(ActionEvent event) {
        if (deliveryTable.getSelectedRow() != -1) {
            String machineCode = deliveryTable.getValueAt(deliveryTable.getSelectedRow(), 1).toString();
            int quantityStock = ComputerDAO.getInstance().selectById(machineCode).getQuantity();
            BillDetails billDetails = isExistInListBillDetails(machineCode);

            String resultString = JOptionPane.showInputDialog(this, "Nhập số lượng cần thay đổi", "Thay đổi số lượng", JOptionPane.OK_CANCEL_OPTION);
            if (resultString != null) {
                try {
                    int quantity = Integer.parseInt(resultString);
                    if (quantity != 0) {
                        if (billDetails != null) {
                            int quantityBeforeUpdate = billDetails.getQuantity();
                            if (quantity - quantityBeforeUpdate <= quantityStock) {
                                deliveryTable.setValueAt(quantity, deliveryTable.getSelectedRow(), 3);
                                setTotalPrice();
                            } else {
                                JOptionPane.showMessageDialog(this, "Số lượng trong kho không đủ!");
                            }
                        }
                        else {
                            if (quantity <= quantityStock) {
                                deliveryTable.setValueAt(quantity, deliveryTable.getSelectedRow(), 3);
                                setTotalPrice();
                            }else {
                                JOptionPane.showMessageDialog(this, "Số lượng trong kho không đủ!");
                            }
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
    public void deleteButtonActionListener(ActionEvent e) {
        if (deliveryTable.getSelectedRow() != -1) {
            defaultTableModel2.removeRow(deliveryTable.getSelectedRow());
            setOrder();
        }
        else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thông tin cần xóa!");
        }
    }

    private void setOrder() {
        for (int i = 0; i < deliveryTable.getRowCount(); i++) {
            deliveryTable.setValueAt(i + 1, i, 0);
        }
    }

    public void deliveryButtonActionListener(ActionEvent e) {
        try {
            if (deliveryTable.getRowCount() > 0) {
                int returnValue = JOptionPane.showConfirmDialog(this, "Chắc chắn lưu thay đổi", "Lưu thay đổi", JOptionPane.YES_NO_OPTION);
                if (returnValue == JOptionPane.YES_OPTION) {
                    processDeliveryBill();
                    processListDeliveryBill();
                    processListBillDetails();
                    processListComputer();
                    this.owner.loadDataToTable(DeliveryBillDAO.getInstance().selectAll());
                    this.dispose();
                    returnValue = JOptionPane.showConfirmDialog(this, "Lưu thành công!\nXuất file PDF?", "Xuất file PDF", JOptionPane.YES_NO_OPTION);
                    if (returnValue == JOptionPane.NO_OPTION || returnValue == JOptionPane.CLOSED_OPTION) return;
                    WritePDF writePDF = new WritePDF();
                    writePDF.writeDeliveryBill(deliveryBillCodeGlobal);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Quá trình thay đổi bị gián đoạn!");
        }
    }
    public void processDeliveryBill() {
        for (BillDetails billDetails : listBillDetails) {
            DeliveryBillDetailsDAO.getInstance().delete(billDetails);

            Computer computer = ComputerDAO.getInstance().selectById(billDetails.getMachineCode());
            computer.setQuantity(computer.getQuantity() + billDetails.getQuantity());
            ComputerDAO.getInstance().update(computer);
        }
        listBillDetails.clear();
    }

    public void processListDeliveryBill() {
        String billCode = deliveryBillCodeTxt.getText();
        long now = System.currentTimeMillis();
        Timestamp creationTime = new Timestamp(now);
        String creator = deliveryBillCreatorTxt.getText();
        double totalAmount = Double.parseDouble(totalPriceLabel.getText().replaceAll("[,đ]", ""));

        DeliveryBill deliveryBill = new DeliveryBill(billCode, creationTime, creator, listBillDetails, totalAmount);
        DeliveryBillDAO.getInstance().update(deliveryBill);
    }

    public void processListBillDetails() {
        for (int i = 0; i < deliveryTable.getRowCount(); i++) {
            BillDetails billDetails = new BillDetails(deliveryBillCodeTxt.getText(), deliveryTable.getValueAt(i, 1).toString(),
                    Integer.parseInt(deliveryTable.getValueAt(i, 3).toString()), Double.parseDouble(deliveryTable.getValueAt(i, 4).toString().replaceAll("[,đ]","")));

            listBillDetails.add(billDetails);
        }

        if (listBillDetails.size() == deliveryTable.getRowCount()) {
            for (BillDetails billDetails : listBillDetails) {
                DeliveryBillDetailsDAO.getInstance().insert(billDetails);
            }
        }
        else {
            JOptionPane.showMessageDialog(this, "Quá trình lưu phiếu chi tiết không thành công!");
        }
    }

    public void processListComputer() {
        for (BillDetails billDetails : listBillDetails) {
            Computer computer = ComputerDAO.getInstance().selectById(billDetails.getMachineCode());
            computer.setQuantity(computer.getQuantity() - billDetails.getQuantity());
            ComputerDAO.getInstance().update(computer);
        }
    }
}

