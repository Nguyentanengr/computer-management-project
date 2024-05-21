package view;

import model.Account;
import com.formdev.flatlaf.ui.FlatLineBorder;
import controller.SearchProduct;
import controller.WritePDF;
import dao.ComputerDAO;
import dao.ReceiptBillDAO;
import dao.ReceiptBillDetailsDAO;
import dao.SupplierDAO;
import model.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
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
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

public class UpdateReceiptBillForm extends JDialog{
    private ReceiptBillForm owner;
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel searchPanel;
    private JTextField searchTxt;
    private JTextField quantityTxt;
    private JTextField receiptBillCodeTxt;
    private JTextField receiptBillCreatorTxt;
    private JButton refreshButton;
    private JButton inputExcelButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton receiptButton;
    private JButton addButton;
    private JLabel quantityLabel;
    private JLabel receiptBillCodeLabel;
    private JLabel supplierLabel;
    private JLabel receiptBillCreatorLabel;
    private JLabel totalPriceTextLabel;
    private JLabel totalPriceLabel;
    private JComboBox <String> comboBox;
    private JTable contentTable;
    private JScrollPane contentScrollPane;
    private JTable receiptTable;
    private JScrollPane receiptScrollPane;

    private DefaultTableModel defaultTableModel;
    private DefaultTableModel defaultTableModel2;
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    private Font fontDefault =  new Font("Arial", 0, 15);

    private ArrayList<BillDetails> listBillDetails;
    private ReceiptBill receiptBillGlobal;
    private String receiptBillCodeGlobal;
    public UpdateReceiptBillForm(JInternalFrame parent, JFrame owner, boolean modal) throws UnsupportedLookAndFeelException {
        super(owner, modal);
        this.owner = (ReceiptBillForm) parent;
        receiptBillCodeGlobal = this.owner.getContentTable().getValueAt(this.owner.getContentTable().getSelectedRow(), 1).toString();
        this.receiptBillGlobal = ReceiptBillDAO.getInstance().selectById(receiptBillCodeGlobal);
        this.listBillDetails = ReceiptBillDetailsDAO.getInstance().selectBy(receiptBillCodeGlobal);

        initComponents();
        initContentTable();
        loadDataToContentTable(ComputerDAO.getInstance().selectAll());
        loadDataSupplierComboBox(SupplierDAO.getInstance().selectAll());
        loadDataToReceiptTable();

        contentTable.setDefaultEditor(Object.class, null);
        receiptTable.setDefaultEditor(Object.class, null);
    }

    private void initContentTable() {
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

        receiptTable.setModel(defaultTableModel2);
        receiptTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        receiptTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        receiptTable.getColumnModel().getColumn(2).setPreferredWidth(280);
        receiptTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        receiptTable.getColumnModel().getColumn(4).setPreferredWidth(100);
    }

    private void loadDataToContentTable(ArrayList<Computer> listComputer) {
        if (listComputer != null) {
            defaultTableModel.setRowCount(0);
            for (Computer computer : listComputer) {
                defaultTableModel.addRow(new Object[]{computer.getMachineCode(), computer.getMachineName(), computer.getQuantity(), decimalFormat.format(computer.getPrice()) + "đ"});
            }
        }
    }

    private void loadDataToReceiptTable() {
        defaultTableModel2.setRowCount(0);
        for (BillDetails billDetails : listBillDetails) {
            defaultTableModel2.addRow(new Object[] {receiptTable.getRowCount() + 1, billDetails.getMachineCode(),
            ComputerDAO.getInstance().selectById(billDetails.getMachineCode()).getMachineName(), billDetails.getQuantity(),
            decimalFormat.format(billDetails.getUnitPrice()) + "đ"});
        }
        setTotalPrice();
    }

    private void loadDataSupplierComboBox(ArrayList<Supplier> listSupplier) {
        if (listSupplier != null) {
            for (Supplier supplier : listSupplier) {
                comboBox.addItem(supplier.getSupplierName());
            }
        }
        comboBox.setSelectedItem(SupplierDAO.getInstance().selectById(receiptBillGlobal.getSupplier()).getSupplierName());
    }

    private String createBillCode(ArrayList<ReceiptBill> listReceiptBill) {
        int code = listReceiptBill.size() + 1;
        while (true) {
            String billCode = "PN" + code;
            boolean isExist = false;
            for (ReceiptBill receiptBill : listReceiptBill) {
                if (billCode.equals(receiptBill.getBillCode())) {
                    isExist = true;
                }
            }
            if (isExist) code++;
            else return billCode;
        }
    }

    private void initComponents() {

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

        receiptBillCodeLabel = new JLabel("Mã phiếu nhập");
        receiptBillCodeLabel.setFont(fontDefault);
        receiptBillCodeLabel.setBounds(30, 30, 200, 40);

        supplierLabel = new JLabel("Nhà cung cấp");
        supplierLabel.setFont(fontDefault);
        supplierLabel.setBounds(30, 80, 200, 40);

        receiptBillCreatorLabel = new JLabel("Người tạo phiếu");
        receiptBillCreatorLabel.setFont(fontDefault);
        receiptBillCreatorLabel.setBounds(30, 130, 200, 40);

        receiptBillCodeTxt = new JTextField();
        receiptBillCodeTxt.setFont(fontDefault);
        receiptBillCodeTxt.setBounds(170, 30, 330, 40);
        receiptBillCodeTxt.setBorder(new FlatLineBorder(new Insets(5,5,5,5), Color.LIGHT_GRAY));
        receiptBillCodeTxt.setText(receiptBillGlobal.getBillCode());
        receiptBillCodeTxt.setEditable(false);
        receiptBillCodeTxt.setRequestFocusEnabled(false);

        comboBox = new JComboBox<>();
        comboBox.setBounds(170, 80, 330, 40);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(new LineBorder(Color.WHITE));
        comboBox.setFont(fontDefault);

        receiptBillCreatorTxt = new JTextField();
        receiptBillCreatorTxt.setFont(fontDefault);
        receiptBillCreatorTxt.setBounds(170, 130, 330, 40);
        receiptBillCreatorTxt.setBorder(new FlatLineBorder(new Insets(5,5,5,5), Color.LIGHT_GRAY));
        receiptBillCreatorTxt.setText(receiptBillGlobal.getCreator());

        receiptTable = new JTable();
        receiptTable.setModel(new DefaultTableModel(
                new Object [][]{

                },
                new String [] {
                        "STT", "Mã SP", "Tên SP", "Số lượng", "Đơn giá"
                }
        ));

        receiptScrollPane = new JScrollPane();
        receiptScrollPane.setViewportView(receiptTable);
        receiptScrollPane.getViewport().setBackground(Color.WHITE);
        receiptScrollPane.setBounds(20, 190, 500, 400);
        receiptScrollPane.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY));

        JTableHeader header2 = receiptTable.getTableHeader();
        header2.setFont(new Font("Arial", Font.PLAIN, 14));
        header2.setBackground(Color.WHITE);

        inputExcelButton = getButton("Nhập Excel", "/icon/inputExcelButton.png");
        inputExcelButton.setBounds(30, 610, 130, 40);
        inputExcelButton.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY,1, 5));
        inputExcelButton.setVisible(false);
        inputExcelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputExcelButtonActionPerformed(e);
            }
        });
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
        receiptButton = new JButton("Lưu thay đổi");
        receiptButton.setForeground(Color.WHITE);
        receiptButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        receiptButton.setBackground(new Color(13, 39, 51));
        receiptButton.setBorder(new LineBorder(Color.WHITE));
        receiptButton.setFont(fontDefault);
        receiptButton.setBounds(360, 690, 123, 36);
        receiptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                receiptButtonActionListener(e);
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

        rightPanel.add(receiptBillCodeLabel);
        rightPanel.add(receiptBillCodeTxt);
        rightPanel.add(supplierLabel);
        rightPanel.add(comboBox);
        rightPanel.add(receiptBillCreatorLabel);
        rightPanel.add(receiptBillCreatorTxt);
        rightPanel.add(receiptScrollPane);
        rightPanel.add(inputExcelButton);
        rightPanel.add(editButton);
        rightPanel.add(deleteButton);
        rightPanel.add(totalPriceLabel);
        rightPanel.add(totalPriceTextLabel);
        rightPanel.add(receiptButton);

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

    public void addButtonActionPerformed(ActionEvent e) {
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
                    receiptTable.setValueAt(updateQuantity, resultRow, 3);
                }
                else {
                    defaultTableModel2.addRow(new Object[]{receiptTable.getRowCount() + 1, machineCode, machineName, quantity, price});
                }

                setTotalPrice();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private int isExistInReceiptTable(String machineCode) {
        int resultRow = -1;
        for (int i = 0; i < receiptTable.getRowCount(); i++) {
            if (machineCode.equals(receiptTable.getValueAt(i, 1).toString())) {
                resultRow = i; break;
            }
        }
        return resultRow;
    }

    private void setTotalPrice() {
        if (receiptTable.getRowCount() > 0) {
            double totalPrice = 0;
            for (int i = 0; i < receiptTable.getRowCount(); i++) {
                totalPrice += Double.parseDouble(receiptTable.getValueAt(i, 4).toString().replaceAll("[,đ]", "")) * Integer.parseInt(receiptTable.getValueAt(i, 3).toString());
            }
            totalPriceLabel.setText(decimalFormat.format(totalPrice) + "đ");
        } else {
            totalPriceLabel.setText("0đ");
        }
    }

    public void inputExcelButtonActionPerformed(ActionEvent e) {
        ArrayList<Computer> listComputer = new ArrayList<>();
        try {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File openFile = fileChooser.getSelectedFile();
                if (openFile != null) {
                    FileInputStream fileInputStream = new FileInputStream(openFile);
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                    Workbook workbook = new XSSFWorkbook(bufferedInputStream);
                    Sheet sheet = workbook.getSheetAt(0);
                    for (int i = 1; i < sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);
                        String machineCode = row.getCell(0).getStringCellValue();
                        String machineName = row.getCell(1).getStringCellValue();
                        int quantity = (int) row.getCell(2).getNumericCellValue();
                        double price = Double.parseDouble(row.getCell(3).getStringCellValue().replaceAll("[,đ]", ""));
                        Computer computer = new Computer(machineCode, machineName, quantity, "", "", "", "", price, "", 1);
                        listComputer.add(computer);
                    }
                    if (listComputer != null) {
                        defaultTableModel2.setRowCount(0);
                        for (int i = 0; i < listComputer.size(); i++) {
                            defaultTableModel2.addRow(new Object[]{receiptTable.getRowCount() + 1, listComputer.get(i).getMachineCode(),
                                    listComputer.get(i).getMachineName(), listComputer.get(i).getQuantity(), decimalFormat.format(listComputer.get(i).getPrice()) + "đ"});
                        }
                        setTotalPrice();
                    }
                    else throw new Exception();
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Quá trình nhập từ Excel bị gián đoạn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public void editButtonActionListener(ActionEvent e) {
        if (receiptTable.getSelectedRow() != -1) {
            String resultString = JOptionPane.showInputDialog(this, "Nhập số lượng cần thay đổi", "Thay đổi số lượng", JOptionPane.OK_CANCEL_OPTION);
            if (resultString != null) {
                try {
                    int quantity = Integer.parseInt(resultString);
                    if (quantity != 0) {
                        receiptTable.setValueAt(quantity, receiptTable.getSelectedRow(), 3);
                        setTotalPrice();
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
        if (receiptTable.getSelectedRow() != -1) {
            defaultTableModel2.removeRow(receiptTable.getSelectedRow());
            setOrder();
        }
        else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thông tin cần xóa!");
        }
    }

    private void setOrder() {
        for (int i = 0; i < receiptTable.getRowCount(); i++) {
            receiptTable.setValueAt(i + 1, i, 0);
        }
    }

    public void receiptButtonActionListener(ActionEvent e) {
        try {
            if (receiptTable.getRowCount() > 0) {
                int returnValue = JOptionPane.showConfirmDialog(this, "Chắc chắn lưu thay đổi", "Lưu thay đổi", JOptionPane.YES_NO_OPTION);
                if (returnValue == JOptionPane.NO_OPTION || returnValue == JOptionPane.CLOSED_OPTION) return;
                processReceiptBill();
                processListReceiptBill();
                processListBillDetails();
                processListComputer();
                this.defaultTableModel2.setRowCount(0);
                this.owner.loadDataToTable(ReceiptBillDAO.getInstance().selectAll());
                this.dispose();
                returnValue = JOptionPane.showConfirmDialog(this, "Lưu thành công!\nXuất file PDF?", "Xuất file PDF", JOptionPane.YES_NO_OPTION);
                if (returnValue == JOptionPane.NO_OPTION || returnValue == JOptionPane.CLOSED_OPTION) return;
                WritePDF writePDF = new WritePDF();
                writePDF.writeReceiptBill(receiptBillCodeGlobal);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Quá trình thay đổi bị gián đoạn!");
        }
    }

    private void processReceiptBill() {
        for (BillDetails billDetails : listBillDetails) {
            ReceiptBillDetailsDAO.getInstance().delete(billDetails);

            Computer computer = ComputerDAO.getInstance().selectById(billDetails.getMachineCode());
            computer.setQuantity(computer.getQuantity() - billDetails.getQuantity());
            ComputerDAO.getInstance().update(computer);
        }

        listBillDetails.clear();

    }

    private void processListReceiptBill() {
        String receiptBillCode = receiptBillCodeTxt.getText();
        long now = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(now);
        String creator = receiptBillCreatorTxt.getText();
        String supplierCode = SupplierDAO.getInstance().selectAll().get(comboBox.getSelectedIndex()).getSupplierCode();
        Double totalPrice = Double.parseDouble(totalPriceLabel.getText().replaceAll("[,đ]", ""));

        ReceiptBill receiptBill = new ReceiptBill(receiptBillCode, timestamp, creator, listBillDetails,totalPrice, supplierCode);
        ReceiptBillDAO.getInstance().update(receiptBill);
    }

    private void processListBillDetails() {
        for (int i = 0; i < receiptTable.getRowCount(); i++) {
            BillDetails billDetails = new BillDetails(receiptBillCodeTxt.getText(), receiptTable.getValueAt(i, 1).toString(),
                    Integer.parseInt(receiptTable.getValueAt(i, 3).toString()), Double.parseDouble(receiptTable.getValueAt(i, 4).toString().replaceAll("[,đ]","")));

            listBillDetails.add(billDetails);
        }
        if (listBillDetails.size() == receiptTable.getRowCount()) {
            for (BillDetails billDetails : listBillDetails) {
                ReceiptBillDetailsDAO.getInstance().insert(billDetails);
            }
        }
        else {
            JOptionPane.showMessageDialog(this, "Quá trình lưu phiếu chi tiết không thành công!");
        }
    }

    private void processListComputer() {
        for (BillDetails billDetails : listBillDetails) {
            Computer computer = ComputerDAO.getInstance().selectById(billDetails.getMachineCode());
            computer.setQuantity(computer.getQuantity() + billDetails.getQuantity());
            ComputerDAO.getInstance().update(computer);
        }
    }
}
