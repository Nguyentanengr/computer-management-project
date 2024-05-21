package view;

import com.formdev.flatlaf.ui.FlatLineBorder;
import controller.SearchSupplier;
import dao.ComputerDAO;
import dao.SupplierDAO;
import model.Computer;
import model.Supplier;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class SupplierForm extends JInternalFrame {
    private JPanel contentPanel;
    private JToolBar featureToolBar;
    private JPanel searchPanel;
    private JTable contentTable;
    private JButton addButton;
    private JButton deleteButton;
    private JButton editButton;
    private JButton outputExcelButton;
    private JButton inputExcelButton;
    private JButton refreshButton;
    private JSeparator separator;
    private JComboBox<String> optionComboBox;
    private JTextField inputTextField;
    private JScrollPane scrollPane;

    private DefaultTableModel defaultTableModel;
    private Font fontDefault =  new Font("SF Pro Display", 0, 12);

    public SupplierForm() {
        initComponents();
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
        contentTable.setDefaultEditor(Object.class, null);
        initTable();
        loadDataToTable(SupplierDAO.getInstance().selectAll());

    }

    public JTable getContentTable() {
        return this.contentTable;
    }
    private void initComponents() {
        addButton = getButton("Thêm", "/icon/addButton.png");
        addButton.setFocusable(false);
        addButton.setBounds(10, 15, 50, 60);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addButtonActionPerformed(e);
            }
        });

        deleteButton = getButton("Xóa", "/icon/deleteButton.png");
        deleteButton.setBounds(65, 15, 50, 60);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteButtonActionPerformed(e);
            }
        });

        editButton = getButton("Sửa", "/icon/editButton.png");
        editButton.setFocusable(false);
        editButton.setBounds(120, 15, 50, 60);
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editButtonActionPerformed(e);
            }
        });

        outputExcelButton = getButton("Xuất Excel", "/icon/outputExcelButton.png");
        outputExcelButton.setBounds(240, 15, 60, 60);
        outputExcelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outputExcelActionPerformed(e);
            }
        });

        inputExcelButton = getButton("Nhập Excel", "/icon/inputExcelButton.png");
        inputExcelButton.setFocusable(false);
        inputExcelButton.setBounds(173, 15, 65, 60);
        inputExcelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inputExcelActionPerformed(e);
            }
        });

        featureToolBar = new JToolBar();
        featureToolBar.setBackground(Color.WHITE);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Chức năng");
        titledBorder.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY));
        featureToolBar.setBorder(titledBorder);
        featureToolBar.setFont(fontDefault);
        featureToolBar.setRollover(true);
        featureToolBar.setFloatable(false);
        featureToolBar.setLayout(null);
        featureToolBar.setBounds(10, 10, 310, 90);

        featureToolBar.add(addButton);
        featureToolBar.add(deleteButton);
        featureToolBar.add(editButton);
        featureToolBar.add(inputExcelButton);
        featureToolBar.add(outputExcelButton);

        optionComboBox = new JComboBox<>();
        optionComboBox.setBackground(Color.WHITE);
        optionComboBox.setBorder(new LineBorder(Color.WHITE));
        optionComboBox.setFont(fontDefault);
        optionComboBox.setModel(new DefaultComboBoxModel<>(new String[] { "Tất cả", "Mã nhà cung cấp", "Tên nhà cung cấp", "Số điện thoại", "Địa chỉ"}));
        optionComboBox.setBounds(30, 30, 150, 40);
        optionComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                optionComboBoxActionPerformed(e);
            }
        });
        optionComboBox.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                optionComboBoxProertyChange(e);
            }
        });


        inputTextField = new JTextField();
        inputTextField.setBounds(200, 30, 330, 40);
        inputTextField.setBorder(new FlatLineBorder(new Insets(5,5,5,5), Color.LIGHT_GRAY));
        inputTextField.setFont(fontDefault);
        inputTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                inputTextFieldKeyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                inputTextFieldKeyReleased(e);
            }
        });

        refreshButton = new JButton("Làm mới");
        refreshButton.setIcon(getImageIcon("/icon/refreshButton.png")); // NOI18N
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.setBackground(Color.WHITE);
        refreshButton.setBorder(new LineBorder(Color.WHITE));
        refreshButton.setFont(fontDefault);
        refreshButton.setBounds(540, 30, 120, 40);
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshButtonPerformed(e);
            }
        });

        searchPanel = new JPanel();
        searchPanel.setFont(fontDefault);
        searchPanel.setBackground(Color.WHITE);
        titledBorder = BorderFactory.createTitledBorder("Tìm kiếm");
        titledBorder.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY));
        searchPanel.setBorder(titledBorder);
        searchPanel.setLayout(null);
        searchPanel.setBounds(330, 10, 670, 90);

        searchPanel.add(optionComboBox);
        searchPanel.add(inputTextField);
        searchPanel.add(refreshButton);

        contentTable = new JTable();
        contentTable.setModel(new DefaultTableModel(
                new Object [][] {
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String [] {
                        "Mã NCC", "Tên nhà cung cấp", "Số điện thoại", "Địa chỉ"
                }
        ));
        JTableHeader header = contentTable.getTableHeader();
        header.setFont(new Font("Arial", Font.PLAIN, 14));
        header.setBackground(Color.WHITE);

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(contentTable);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBounds(10, 120, 970, 620);

        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(featureToolBar);
        contentPanel.add(searchPanel);
        contentPanel.add(scrollPane);

        this.setBackground(Color.WHITE);
        this.setBorder(null);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(contentPanel, GroupLayout.PREFERRED_SIZE, 1000, GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(contentPanel, GroupLayout.PREFERRED_SIZE, 800, GroupLayout.PREFERRED_SIZE)
        );
        pack();
    }
    private ImageIcon getImageIcon(String pathIcon) {
        int widthIcon = 40, heighIcon = 40;
        URL urlIcon = SupplierForm.class.getResource(pathIcon);
        Image imgUser = Toolkit.getDefaultToolkit().createImage(urlIcon).getScaledInstance(widthIcon, heighIcon, Image.SCALE_SMOOTH);
        return  new ImageIcon(imgUser);
    }

    private JButton getButton(String title, String pathIcon) {
        JButton designButton = new JButton(title, getImageIcon(pathIcon));
        designButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        designButton.setHorizontalTextPosition(SwingConstants.CENTER);
        designButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        designButton.setBackground(Color.WHITE);
        designButton.setBorder(new LineBorder(Color.WHITE));
        designButton.setFont(fontDefault);
        return designButton;
    }

    private final void initTable() {
        defaultTableModel = new DefaultTableModel();
        String[] headerTbl = new String[]{"Mã NCC", "Tên nhà cung cấp", "Số điện thoại", "Địa chỉ"};
        defaultTableModel.setColumnIdentifiers(headerTbl);
        contentTable.setModel(defaultTableModel);
        contentTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        contentTable.getColumnModel().getColumn(1).setPreferredWidth(360);
        contentTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        contentTable.getColumnModel().getColumn(3).setPreferredWidth(470);
    }

    public void loadDataToTable(ArrayList<Supplier> listSupplier) {
        try {
            defaultTableModel.setRowCount(0);
            for (Supplier supplier : listSupplier) {
                SupplierDAO.getInstance().insert(supplier);
                defaultTableModel.addRow(new Object[] {supplier.getSupplierCode(), supplier.getSupplierName(), supplier.getPhone(), supplier.getAddress()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi trong quá trình đưa dữ liệu vào bảng");
            e.printStackTrace();
        }
    }

    public void addButtonActionPerformed(ActionEvent e) {
        AddSupplier addSupplier = new AddSupplier(this, (JFrame) SwingUtilities.getWindowAncestor(this), rootPaneCheckingEnabled);
        addSupplier.setVisible(true);
    }

    public void deleteButtonActionPerformed(ActionEvent e) {
        if (contentTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp cần xóa");
        }
        else {
            int selection = JOptionPane.showConfirmDialog(this, "Bạc có muốn xóa sản phẩm này?", "Xóa sản phẩm", JOptionPane.YES_NO_OPTION);
            if (selection == JOptionPane.YES_OPTION) {
                try {
                    int row = contentTable.getSelectedRow();
                    String supplierCode = contentTable.getValueAt(row, 0).toString();
                    SupplierDAO.getInstance().delete(new Supplier(supplierCode, contentTable.getValueAt(row, 1).toString(),
                            contentTable.getValueAt(row, 2).toString(), contentTable.getValueAt(row, 3).toString()));
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    this.loadDataToTable(SupplierDAO.getInstance().selectAll());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi trong quá trình xóa");
                }
            }
        }
    }

    public void editButtonActionPerformed(ActionEvent e) {
        if (contentTable.getSelectedRow() != - 1) {
            EditSupplier editSupplier = new EditSupplier(this, (JFrame) SwingUtilities.getWindowAncestor(this), rootPaneCheckingEnabled);
            editSupplier.setVisible(true);
        }
        else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp cần sửa");
        }
    }

    public void inputExcelActionPerformed(ActionEvent e) {
        ArrayList<Supplier> listSupplier = new ArrayList<>();
        JFileChooser fileChooser = new JFileChooser();
        int selection = fileChooser.showOpenDialog(this);
        if (selection == JFileChooser.APPROVE_OPTION) {
            File openFile = fileChooser.getSelectedFile();
            if (openFile != null) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(openFile);
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                    Workbook workbook = new XSSFWorkbook(bufferedInputStream);
                    Sheet sheet = workbook.getSheetAt(0);
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);
                        String supplierCode = row.getCell(0).getStringCellValue();
                        String supplierName = row.getCell(1).getStringCellValue();
                        String phone = row.getCell(2).getStringCellValue();
                        String address = row.getCell(3).getStringCellValue();
                        Supplier supplier = new Supplier(supplierCode, supplierName, phone, address);
                        listSupplier.add(supplier);
                    }
                    if (listSupplier.size() == sheet.getLastRowNum()) {
                        loadDataToTable(listSupplier);
                        JOptionPane.showMessageDialog(this, "Nhập file thành công!");
                    }
                    else throw new Exception();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Quá trình nhập từ file thất bại!");
                    ex.printStackTrace();
                }
            }
        }
    }

    public void outputExcelActionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File saveFile = fileChooser.getSelectedFile();
            if (saveFile != null) {
                String pathFile = saveFile.getPath();
                if (!pathFile.endsWith(".xlsx")) {
                    pathFile += ".xlsx";
                    saveFile = new File(pathFile);
                }
                if (saveFile.exists()) {
                    int overwriteOption = JOptionPane.showConfirmDialog(this, "File đã tồn tại. Thực hiện ghi đè?", "Xác nhận ghi đè", JOptionPane.YES_NO_OPTION);
                    if (overwriteOption != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                try (Workbook workbook = new XSSFWorkbook()) {
                    FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
                    Sheet sheet = workbook.createSheet("Computer");
                    Row headerRow = sheet.createRow(0);
                    for (int i = 0; i < contentTable.getColumnCount(); i++) {
                        Cell cell = headerRow.createCell(i);
                        cell.setCellValue(contentTable.getColumnName(i));
                    }
                    for (int i = 0; i < contentTable.getRowCount(); i++) {
                        Row row = sheet.createRow(i + 1);
                        for (int j = 0; j < contentTable.getColumnCount(); j++) {
                            Cell cell = row.createCell(j);
                            if (contentTable.getValueAt(i, j) != null) {
                                cell.setCellValue(contentTable.getValueAt(i, j).toString());
                            }
                        }
                    }
                    workbook.write(fileOutputStream);
                    JOptionPane.showMessageDialog(this, "File được lưu thành công!");
                    this.openFile(saveFile.toString());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi trong quá trình lưu file!");
                    ex.printStackTrace();
                }
            }
        }
    }

    private void openFile(String pathFile) {
        try {
            File path = new File(pathFile);
            Desktop.getDesktop().open(path);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Mở file thất bại!");
        }
    }

    public void optionComboBoxActionPerformed(ActionEvent e) {
        String option = optionComboBox.getSelectedItem().toString();
        String content = inputTextField.getText();
        ArrayList<Supplier> result = searchFn(option, content);
        loadDataToTable(result);
    }

    private ArrayList<Supplier> searchFn(String option, String content) {
        String luachon = (String) optionComboBox.getSelectedItem();
        String searchContent = inputTextField.getText();
        ArrayList<Supplier> result = new ArrayList<>();
        switch (luachon) {
            case "Tất cả":
                result = SearchSupplier.getInstance().searchAll(searchContent);
                break;
            case "Mã nhà cung cấp":
                result = SearchSupplier.getInstance().searchSupplierCode(searchContent);
                break;
            case "Tên nhà cung cấp":
                result = SearchSupplier.getInstance().searchSupplierName(searchContent);
                break;
            case "Địa chỉ":
                result = SearchSupplier.getInstance().searchAddress(searchContent);
                break;
            case "Số điện thoại":
                result = SearchSupplier.getInstance().searchPhone(searchContent);
                break;
        }
        return result;
    }
    public void optionComboBoxProertyChange(PropertyChangeEvent e) {
        String option = optionComboBox.getSelectedItem().toString();
        String content = inputTextField.getText();
        ArrayList<Supplier> result = searchFn(option, content);
        loadDataToTable(result);
    }

    public void inputTextFieldKeyPressed(KeyEvent e) {

    }

    public void inputTextFieldKeyReleased(KeyEvent e) {
        String option = optionComboBox.getSelectedItem().toString();
        String content = inputTextField.getText();
        ArrayList<Supplier> result = searchFn(option, content);
        loadDataToTable(result);
    }

    public void refreshButtonPerformed(ActionEvent e) {
        optionComboBox.setSelectedItem("Tất cả");
        inputTextField.setText("");
        loadDataToTable(SupplierDAO.getInstance().selectAll());
    }
}
