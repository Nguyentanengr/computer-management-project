package view;

import com.formdev.flatlaf.ui.FlatLineBorder;
import controller.SearchProduct;
import dao.ComputerDAO;
import dao.DesktopComputerDAO;
import dao.LaptopDAO;
import model.Computer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
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
import java.util.Properties;

import model.DesktopComputer;
import model.Laptop;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ProductForm extends JInternalFrame {

    private JPanel mainPanel;
    private JPanel searchPanel;
    private JToolBar featureToolBar;
    private JTable contentTable;
    private JButton addButton;
    private JButton deleteButton;
    private JButton editButton;
    private JButton seeDetailsButton;
    private JButton inputExcelButton;
    private JButton outputExcelButton;
    private JComboBox<String> optionComboBox;
    private JTextField inputTextField;
    private JButton refreshButton;
    private JSeparator separator;
    private JScrollPane scrollPane;

    private DefaultTableModel defaultTableModel;
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    private Font fontDefault =  new Font("SF Pro Display", 0, 12);

    public ProductForm() {
        initComponent();
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
        contentTable.setDefaultEditor(Object.class, null);
        initTable();
        loadDataToTable();
        changeTextFind();
    }

    public JTable getContentTable() {
        return this.contentTable;
    }

    public DefaultTableModel getDefaultTableModel() {
        return this.defaultTableModel;
    }

    public final void initTable() {
        defaultTableModel = new DefaultTableModel();
        String[] headerTable = new String[]{"Mã máy", "Tên máy", "Số lượng", "Đơn giá", "Bộ xử lí", "RAM", "Bộ nhớ", "Loại máy"};
        defaultTableModel.setColumnIdentifiers(headerTable);
        contentTable.setModel(defaultTableModel);
        contentTable.getColumnModel().getColumn(0).setPreferredWidth(5);
        contentTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        contentTable.getColumnModel().getColumn(2).setPreferredWidth(5);
        contentTable.getColumnModel().getColumn(5).setPreferredWidth(5);
        contentTable.getColumnModel().getColumn(6).setPreferredWidth(5);


        JTableHeader header = contentTable.getTableHeader();
        header.setFont(new Font("Arial", Font.PLAIN, 14));
        header.setBackground(Color.WHITE);
    }
    public void initComponent() {
        this.setBorder(null);

        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);

        // create Buttons on Tool bar

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

        seeDetailsButton = getButton("Chi tiết", "/icon/seeDetailsButton.png");
        seeDetailsButton.setFocusable(false);
        seeDetailsButton.setBounds(175, 15, 50, 60);
        seeDetailsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                seeDetailButtonActionPerformed(e);
            }
        });

        outputExcelButton = getButton("Xuất Excel", "/icon/outputExcelButton.png");
        outputExcelButton.setBounds(235, 15, 60, 60);
        outputExcelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outputExcelActionPerformed(e);
            }
        });

        inputExcelButton = getButton("Nhập Excel", "/icon/inputExcelButton.png");
        inputExcelButton.setFocusable(false);
        inputExcelButton.setBounds(295, 15, 70, 60);
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

        featureToolBar.add(addButton);
        featureToolBar.add(deleteButton);
        featureToolBar.add(editButton);
        featureToolBar.add(seeDetailsButton);
        featureToolBar.add(inputExcelButton);
        featureToolBar.add(outputExcelButton);

        // create components on Search panel

        searchPanel = new JPanel();
        searchPanel.setFont(fontDefault);
        searchPanel.setBackground(Color.WHITE);
        titledBorder = BorderFactory.createTitledBorder("Tìm kiếm");
        titledBorder.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY));
        searchPanel.setBorder(titledBorder);
        searchPanel.setLayout(null);

        optionComboBox = new JComboBox<>();
        optionComboBox.setBackground(Color.WHITE);
        optionComboBox.setBorder(new LineBorder(Color.WHITE));
        optionComboBox.setFont(fontDefault);
        optionComboBox.setModel(new DefaultComboBoxModel<>(new String[] { "Tất cả", "Mã máy", "Tên máy", "Số lượng", "Đơn giá", "RAM", "CPU", "Dung lượng", "Card màn hình", "Xuất xứ", "Đã xóa" }));
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
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshButtonPerformed(e);
            }
        });

        optionComboBox.setBounds(20, 30, 130, 40);
        inputTextField.setBounds(170, 30, 310, 40);
        refreshButton.setBounds(490, 30, 100, 40);

        searchPanel.add(optionComboBox);
        searchPanel.add(inputTextField);
        searchPanel.add(refreshButton);

        // create table
        contentTable = new JTable();
        contentTable.setModel(new DefaultTableModel(
                new Object [][] {

                },
                new String [] {

                }
        ));
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(contentTable);
        scrollPane.getViewport().setBackground(Color.WHITE);

        GroupLayout jPanel1Layout = new GroupLayout(mainPanel);
        mainPanel.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(featureToolBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(8, 8, 8)
                                                .addComponent(searchPanel, GroupLayout.PREFERRED_SIZE, 600, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 1168, Short.MAX_VALUE))
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(searchPanel, GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                                        .addComponent(featureToolBar, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 23, Short.MAX_VALUE)
                                .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 700, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, 1000, GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, 800, GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }


    private ImageIcon getImageIcon(String pathIcon) {
        int widthIcon = 40, heighIcon = 40;
        URL urlIcon = ProductForm.class.getResource(pathIcon);
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

    public void loadDataToTable() {
        // lay du lieu trong bang computer trong csdl
        try {
            ComputerDAO cpt = new ComputerDAO();
            ArrayList<Computer> listComputer = cpt.selectAll();
            defaultTableModel.setRowCount(0);

            for (Computer i : listComputer) {
                if (i.getStatus() == 1) {
                    String machineType;
                    if (LaptopDAO.getInstance().isLaptop(i.getMachineCode()) == true) {
                        machineType = "Laptop";
                    }
                    else machineType = "DesktopComputer";
                    defaultTableModel.addRow(new Object[] {
                            i.getMachineCode(), i.getMachineName(), i.getQuantity(), decimalFormat.format(i.getPrice()) + "đ",
                            i.getCPUName(), i.getRAM(), i.getRom(), machineType
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeTextFind() {
        inputTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (inputTextField.getText().length() == 0) loadDataToTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
    }
    public void addButtonActionPerformed(ActionEvent e) {
        AddProduct addProduct = new AddProduct(this, (JFrame) SwingUtilities.getWindowAncestor(this), rootPaneCheckingEnabled);
        addProduct.setVisible(true);
    }

    public void deleteButtonActionPerformed(ActionEvent e) {
        if (contentTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa");
        }
        else {
            deleteSelectedComputer();
        }
    }

    public void editButtonActionPerformed(ActionEvent e) {

        if (contentTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa");
        }
        else {
            EditProduct editProduct = new EditProduct(this, (JFrame) SwingUtilities.getWindowAncestor(this), rootPaneCheckingEnabled);
            editProduct.setVisible(true);
        }
    }

    public void seeDetailButtonActionPerformed(ActionEvent e) {
        if (contentTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để xem");
        }
        else {
            SeeDetailsProduct seeDetailsProduct = new SeeDetailsProduct(this, (JFrame) SwingUtilities.getWindowAncestor(this), rootPaneCheckingEnabled);
            seeDetailsProduct.setVisible(true);
        }
    }

    public void inputExcelActionPerformed(ActionEvent e) {
        ArrayList<Computer> listComputer = new ArrayList<>();
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File openFile = fileChooser.getSelectedFile();
            if (openFile != null) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(openFile);
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                    Workbook workbook = new XSSFWorkbook(bufferedInputStream);
                    Sheet sheet = workbook.getSheetAt(0);
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);
                        String machineCode = row.getCell(0).getStringCellValue();
                        String machineName = row.getCell(1).getStringCellValue();
                        String price = row.getCell(3).getStringCellValue();
                        String cpu = row.getCell(4).getStringCellValue();
                        String ram = row.getCell(5).getStringCellValue();
                        String rom = row.getCell(6).getStringCellValue();
                        String type = row.getCell(7).getStringCellValue();
                        int quantity = (int) row.getCell(2).getNumericCellValue();

                        double convertPrice = Double.parseDouble(price.replaceAll("[,đ]", ""));
                        Computer computer = new Computer(machineCode, machineName, quantity, cpu, ram, "", rom, convertPrice, "", 1);
                        listComputer.add(computer);
                    }

                    if (sheet.getLastRowNum() == listComputer.size()) {
                        loadData(listComputer);
                    }
                    else {
                        JOptionPane.showMessageDialog(this, "Quá trình đọc dữ liệu bị gián đoạn!");
                    }
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi trong quá trình nhập file");
                    ex.printStackTrace();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi trong quá trình nhập file");
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

    public void optionComboBoxActionPerformed(ActionEvent e) {
        String option = optionComboBox.getSelectedItem().toString();
        String content = inputTextField.getText();
        ArrayList<Computer> result = searchFn(option, content);
        loadDataToTableV2(result);
    }

    public void optionComboBoxProertyChange(PropertyChangeEvent e) {
        String option = optionComboBox.getSelectedItem().toString();
        String content = inputTextField.getText();
        ArrayList<Computer> result = searchFn(option, content);
        loadDataToTableV2(result);
    }

    public void inputTextFieldKeyPressed(KeyEvent e) {

    }

    public void inputTextFieldKeyReleased(KeyEvent e) {
        String option = optionComboBox.getSelectedItem().toString();
        String content = inputTextField.getText();
        ArrayList<Computer> result = searchFn(option, content);
        loadDataToTableV2(result);
    }

    public void refreshButtonPerformed(ActionEvent e) {
        optionComboBox.setSelectedItem("Tất cả");
        inputTextField.setText("");
        loadDataToTable();
    }

    public void deleteSelectedComputer() {
        int selection = JOptionPane.showConfirmDialog(this, "Chắn chắn xóa sản phẩm này?", "Xóa sản phẩm", JOptionPane.YES_NO_OPTION);
        if (selection == JOptionPane.YES_OPTION) {
            ComputerDAO.getInstance().updateStatus(contentTable.getValueAt(contentTable.getSelectedRow(), 0).toString());
        }
        this.loadDataToTable();
    }

    private void openFile(String pathFile) {
        try {
            File path = new File(pathFile);
            Desktop.getDesktop().open(path);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Mở file thất bại!");
        }
    }

    private void loadData(ArrayList<Computer> listComputer) {
        try {
            defaultTableModel.setRowCount(0);
            for (Computer computer : listComputer) {
                if (LaptopDAO.getInstance().isLaptop(computer.getMachineCode())) {
                    Laptop laptop = new Laptop(computer.getMachineCode(), computer.getMachineName(), computer.getQuantity(), computer.getCPUName(),
                            computer.getRAM(), computer.getGraphicsCard(), computer.getRom(), computer.getPrice(), computer.getOrigin(), computer.getStatus(), 0, "");
                    LaptopDAO.getInstance().insert(laptop);
                    defaultTableModel.addRow(new Object[]{computer.getMachineCode(), computer.getMachineName(), computer.getQuantity(),
                            decimalFormat.format(computer.getPrice()) + "đ", computer.getCPUName(), computer.getRAM(), computer.getRom(), "Laptop"});
                }
                else {
                    DesktopComputer desktopComputer = new DesktopComputer(computer.getMachineCode(), computer.getMachineName(), computer.getQuantity(),
                            computer.getCPUName(), computer.getRAM(), computer.getGraphicsCard(), computer.getRom(), computer.getPrice(), computer.getOrigin(), computer.getStatus(), "", 0);
                    DesktopComputerDAO.getInstance().insert(desktopComputer);
                    defaultTableModel.addRow(new Object[]{computer.getMachineCode(), computer.getMachineName(), computer.getQuantity(),
                            decimalFormat.format(computer.getPrice()) + "đ", computer.getCPUName(), computer.getRAM(), computer.getRom(), "DesktopComputer"});
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi trong quá trình tải dữ liệu lên bảng!");
            e.printStackTrace();
        }
    }
    public void loadDataToTableV2(ArrayList<Computer> result) {
        try {
            defaultTableModel.setRowCount(0);
            for (Computer i : result) {
                String type;
                if (LaptopDAO.getInstance().isLaptop(i.getMachineCode()) == true) {
                    type = "Laptop";
                } else {
                    type = "DesktopComputer";
                }
                defaultTableModel.addRow(new Object[]{
                        i.getMachineCode(), i.getMachineName(), i.getQuantity(), decimalFormat.format(i.getPrice()) + "đ", i.getCPUName(), i.getRAM(), i.getRom(), type
                });
            }
        } catch (Exception e) {
        }
    }

    private ArrayList<Computer> searchFn(String option, String content) {
        ArrayList<Computer> result = new ArrayList<>();
        SearchProduct searchProduct = new SearchProduct();

        switch (option) {
            case "Tất cả":
                result = searchProduct.searchAll(content);
                break;
            case "Mã máy":
                result = searchProduct.searchMachineCode(content);
                break;
            case "Tên máy":
                result = searchProduct.searchMachineName(content);
                break;
            case "Số lượng":
                result = searchProduct.searchQuantity(content);
                break;
            case "Đơn giá":
                result = searchProduct.searchPrice(content);
                break;
            case "RAM":
                result = searchProduct.searchRam(content);
                break;
            case "CPU":
                result = searchProduct.searchCpu(content);
                break;
            case "Dung lượng":
                result = searchProduct.searchRom(content);
                break;
            case "Card màn hình":
                result = searchProduct.searchCard(content);
                break;
            case "Xuất xứ":
                result = searchProduct.searchOrigin(content);
                break;
            case "Đã xóa":
                result = searchProduct.searchDeletedProducts(content);
        }

        return result;
    }
}
