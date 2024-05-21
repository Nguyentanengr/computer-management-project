package view;

import com.formdev.flatlaf.ui.FlatLineBorder;
import controller.IOExcel;
import dao.ComputerDAO;
import dao.DeliveryBillDAO;
import dao.LaptopDAO;
import database.JDBCUtil;
import model.Account;
import model.Computer;
import model.DeliveryBill;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class StockForm extends JInternalFrame {

    private Account account;

    private JPanel mainPanel;
    private JPanel searchPanel;
    private JButton addButton;
    private JButton deleteButton;
    private JButton editButton;
    private JButton seeDetailsButton;
    private JButton outputExcelButton;
    private JButton refreshButton;
    private JToolBar featureToolbar;
    private JComboBox<String> optionComboBox;
    private JTextField searchTxt;
    private JTable contentTable;
    private JScrollPane scrollPane;

    private DefaultTableModel defaultTableModel;
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    private SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/YYYY HH:mm");
    private Font fontDefault =  new Font("SF Pro Display", 0, 12);

    public StockForm(Account account) {
        this.account = account;
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
        initComponents();
        initTable();
        loadDataToTable(ComputerDAO.getInstance().selectAll());
    }

    public void initTable() {
        defaultTableModel = new DefaultTableModel();
        defaultTableModel.setColumnIdentifiers(new String[] {"Mã máy", "Tên máy", "Số lượng", "Đơn giá", "Bộ xử lí", "RAM", "Bộ nhớ", "Loại máy"});

        contentTable.setModel(defaultTableModel);
        contentTable.getColumnModel().getColumn(0).setPreferredWidth(5);
        contentTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        contentTable.getColumnModel().getColumn(2).setPreferredWidth(5);
        contentTable.getColumnModel().getColumn(5).setPreferredWidth(5);
        contentTable.getColumnModel().getColumn(6).setPreferredWidth(5);
    }

    public void loadDataToTable(ArrayList<Computer> listComputer) {
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
    }

    public void loadDataToListComputer(ArrayList<Computer> listComputer) {
        for (int i = 0; i < contentTable.getRowCount(); i++) {
            String machineCode = contentTable.getValueAt(i, 0).toString();
            String machineName = contentTable.getValueAt(i, 1).toString();
            String cpuName = contentTable.getValueAt(i, 4).toString();
            String ram = contentTable.getValueAt(i, 5).toString();
            String rom = contentTable.getValueAt(i, 6).toString();
            String type = contentTable.getValueAt(i, 7).toString();
            int quantity = Integer.parseInt(contentTable.getValueAt(i, 2).toString());
            double price = Double.parseDouble(contentTable.getValueAt(i, 3).toString().replaceAll("[,đ]", ""));

            Computer computer = new Computer(machineCode, machineName, quantity, cpuName, ram, "", rom, price, "", 1);
            listComputer.add(computer);
        }
    }
    public void initComponents() {

        this.setBorder(null);
        this.setPreferredSize(new Dimension(1000, 800));

        addButton = getButton("Thêm", "/icon/addButton.png", false);
        addButton.setBounds(20, 15, 50, 60);

        deleteButton = getButton("Xóa", "/icon/deleteButton.png", false);
        deleteButton.setBounds(80, 15, 50, 60);

        editButton = getButton("Sửa", "/icon/editButton.png", false);
        editButton.setBounds(150, 15, 50, 60);

        seeDetailsButton = getButton("Xem CT", "/icon/seeDetailsButton.png", false);
        seeDetailsButton.setBounds(210, 15, 80, 60);

        outputExcelButton = getButton("Xuất Excel", "/icon/outputExcelButton.png", true);
        outputExcelButton.setBounds(290, 15, 80, 60);
        outputExcelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputExcelActionPerformed(e);
            }
        });
        featureToolbar = new JToolBar();
        featureToolbar.setBackground(Color.WHITE);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Chức năng");
        titledBorder.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY));
        featureToolbar.setBorder(titledBorder);
        featureToolbar.setFont(fontDefault);
        featureToolbar.setRollover(true);
        featureToolbar.setFloatable(false);
        featureToolbar.setLayout(null);

        featureToolbar.add(addButton);
        featureToolbar.add(deleteButton);
        featureToolbar.add(editButton);
        featureToolbar.add(seeDetailsButton);
        featureToolbar.add(outputExcelButton);

        optionComboBox = new JComboBox<>();
        optionComboBox.setBackground(Color.WHITE);
        optionComboBox.setBorder(new LineBorder(Color.WHITE));
        optionComboBox.setFont(fontDefault);
        optionComboBox.setModel(new DefaultComboBoxModel<>(new String[] {"Tất cả", "Mã máy", "Tên máy", "Số lượng", "Đơn giá", "RAM", "CPU", "Dung lượng", "Card màn hình", "Xuất xứ" }));
        optionComboBox.setBounds(20, 30, 150, 40);
        optionComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                optionComboBoxActionPerformed(e);
            }
        });

        searchTxt = new JTextField();
        searchTxt.setBorder(new FlatLineBorder(new Insets(5,5,5,5), Color.LIGHT_GRAY));
        searchTxt.setFont(fontDefault);
        searchTxt.setBounds(190, 30, 250, 40);
        searchTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchTxtKeyReleased(e);
            }
        });

        refreshButton = new JButton("Làm mới", getImageIcon("/icon/refreshButton.png"));
        refreshButton.setBounds(450, 30, 105, 40);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.setBackground(Color.WHITE);
        refreshButton.setBorder(new LineBorder(Color.WHITE));
        refreshButton.setFont(fontDefault);
        refreshButton.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY, 1, 5));
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshButtonActionPerformed(e);
            }
        });

        searchPanel = new JPanel();
        searchPanel.setFont(fontDefault);
        searchPanel.setBackground(Color.WHITE);
        titledBorder = BorderFactory.createTitledBorder("Tìm kiếm");
        titledBorder.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY));
        searchPanel.setBorder(titledBorder);
        searchPanel.setLayout(null);

        searchPanel.add(optionComboBox);
        searchPanel.add(searchTxt);
        searchPanel.add(refreshButton);

        contentTable = new JTable();
        contentTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {

                }
        ));
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(contentTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBackground(Color.WHITE);

        JTableHeader header = contentTable.getTableHeader();
        header.setFont(new Font("Arial", Font.PLAIN, 14));
        header.setBackground(Color.WHITE);

        // Customizing the scroll bars
        JScrollBar verticalScrollBar = new JScrollBar(JScrollBar.VERTICAL);
        verticalScrollBar.setPreferredSize(new Dimension(8, 0));
        verticalScrollBar.setBackground(Color.WHITE);

        verticalScrollBar.setUnitIncrement(60);  // Increase unit increment
        verticalScrollBar.setBlockIncrement(60);

        scrollPane.setVerticalScrollBar(verticalScrollBar);

        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);

        GroupLayout mainLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainLayout);
        mainLayout.setHorizontalGroup(
                mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(mainLayout.createSequentialGroup()
                                                .addComponent(featureToolbar, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(searchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE))
                                        .addComponent(scrollPane))
                                .addContainerGap())
        );
        mainLayout.setVerticalGroup(
                mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainLayout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(searchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                                        .addComponent(featureToolbar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 626, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, 1000, 1000, 1000)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, 800, 800, 800)
        );

        pack();
    }

    private ImageIcon getImageIcon(String pathIcon) {
        int widthIcon = 40, heighIcon = 40;
        URL urlIcon = StockForm.class.getResource(pathIcon);
        Image imgUser = Toolkit.getDefaultToolkit().createImage(urlIcon).getScaledInstance(widthIcon, heighIcon, Image.SCALE_SMOOTH);
        return  new ImageIcon(imgUser);
    }

    private JButton getButton(String title, String pathIcon, boolean enable) {
        JButton designButton = new JButton(title, getImageIcon(pathIcon));
        designButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        designButton.setHorizontalTextPosition(SwingConstants.CENTER);
        designButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        designButton.setBackground(Color.WHITE);
        designButton.setBorder(new LineBorder(Color.WHITE));
        designButton.setFont(fontDefault);
        if (!enable) {
            designButton.setEnabled(false);
            designButton.setFocusable(false);
        }
        return designButton;
    }

    public void outputExcelActionPerformed(ActionEvent e) {
        IOExcel ioExcel = new IOExcel();
        ArrayList<Computer> listComputer = new ArrayList<>();
        loadDataToListComputer(listComputer);
        if (listComputer.size() == 0) return;
        ioExcel.exportComputer(listComputer);
    }

    public void optionComboBoxActionPerformed(ActionEvent event) {
        ArrayList<Computer> listComputer = filter();
        if (listComputer != null) loadDataToTable(listComputer);
    }

    public void searchTxtKeyReleased(KeyEvent e) {
        ArrayList<Computer> listComputer = filter();
        if (listComputer != null) loadDataToTable(listComputer);
    }

    public ArrayList<Computer> filter() {
        String option = optionComboBox.getSelectedItem().toString();
        String content = searchTxt.getText();

        ArrayList<Computer> listComputer = ComputerDAO.getInstance().filter(option, content);
        return listComputer;
    }

    public void refreshButtonActionPerformed(ActionEvent event) {
        optionComboBox.setSelectedItem("Tất cả");
        searchTxt.setText("");

        loadDataToTable(ComputerDAO.getInstance().selectAll());
    }
}
