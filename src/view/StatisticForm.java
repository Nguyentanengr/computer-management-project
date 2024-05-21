package view;

import com.formdev.flatlaf.ui.FlatLineBorder;
import com.toedter.calendar.JDateChooser;
import dao.*;
import jxl.write.DateTime;
import model.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.ColorUIResource;
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
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class StatisticForm extends JInternalFrame {

    private Account account;

    private JPanel headerPanel;
    private JPanel productPanel;
    private JLabel iconProductLabel;
    private JLabel quantityProductLabel;
    private JLabel productLabel;
    private JPanel supplierPanel;
    private JLabel iconSupplierLabel;
    private JLabel quantitySupplierLabel;
    private JLabel supplierLabel;
    private JPanel accountPanel;
    private JLabel iconAccountLabel;
    private JLabel quantityAccountLabel;
    private JLabel accountLabel;

    private JTabbedPane tabbedPane;

    private JPanel productDetailsPanel;
    private JPanel searchProductPanel;
    private JTextField searchProductTxt;
    private JPanel filterProductPanel;
    private JDateChooser fromProductDate;
    private JDateChooser toProductDate;
    private JLabel fromProductLabel;
    private JLabel toProductLabel;
    private JButton refreshProductsButton;
    private JScrollPane productScrollPane;
    private JTable productTable;

    private JPanel billDetailsPanel;
    private JToolBar billToolbar;
    private JButton seeDetailsBillButton;
    private JPanel searchBillPanel;
    private JComboBox<String> billComboBox;
    private JTextField searchBillTxt;
    private JButton refreshBillButton;
    private JScrollPane billScrollPane;
    private JTable billTable;
    private JPanel filterDateBillPanel;
    private JDateChooser fromBillDate;
    private JDateChooser toBillDate;
    private JLabel fromBillLabel;
    private JLabel toBillLabel;
    private JPanel filterPriceBillPanel;
    private JTextField fromPriceBillTxt;
    private JTextField toPriceBillTxt;
    private JLabel fromPriceLabel;
    private JLabel toPriceLabel;
    private JLabel totalBillTextLabel;
    private JLabel totalBillLabel;
    private JLabel totalPriceTextLabel;
    private JLabel totalPriceLabel;

    private JPanel accountDetailsPanel;
    private JPanel searchAccountPanel;
    private JComboBox<String> accountComboBox;
    private JTextField searchAccountTxt;
    private JButton refreshAccountButton;
    private JScrollPane accountScrollPane;
    private JTable accountTable;

    private DefaultTableModel productTableModel;
    private DefaultTableModel billTableModel;
    private DefaultTableModel accountTableModel;
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    private SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/YYYY HH:mm");
    private Font fontDefault =  new Font("SF Pro Display", 0, 12);

    public StatisticForm(Account account) {
        this.account = account;
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);

        initComponents();

        initProductTable();
        initBillTable();
        initAccountTable();

        loadDataToProductTable(ComputerDAO.getInstance().selectAll());
        loadDataToBillTable(ReceiptBillDAO.getInstance().selectAll(), DeliveryBillDAO.getInstance().selectAll());
        loadDataToAccountTable(AccountDAO.getInstance().selectAll());

    }


    public JTable getBillTable() {
        return this.billTable;
    }
    public void initProductTable() {
        productTableModel = new DefaultTableModel();
        productTableModel.setColumnIdentifiers(
                new String [] {
                        "STT", "Mã máy", "Tên máy", "Số lượng nhập", "Số lượng xuất"
                }
        );
        productTable.setModel(productTableModel);
        productTable.getColumnModel().getColumn(2).setPreferredWidth(250);
    }

    public void initBillTable() {
        billTableModel = new DefaultTableModel();
        billTableModel.setColumnIdentifiers(
                new String[]{
                        "STT", "Mã phiếu", "Người tạo", "Thời gian tạo", "Tổng tiền", "Loại phiếu"
                }
        );

        billTable.setModel(billTableModel);
        billTable.getColumnModel().getColumn(0).setPreferredWidth(50);
    }

    public void initAccountTable() {
        accountTableModel = new DefaultTableModel();
        accountTableModel.setColumnIdentifiers(
                new String[]{
                        "STT", "Họ và tên", "Email", "Tên người dùng", "Vai trò", "Tình trạng"
                }
        );

        accountTable.setModel(accountTableModel);
        accountTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        accountTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        accountTable.getColumnModel().getColumn(2).setPreferredWidth(180);
    }

    public void loadDataToProductTable(ArrayList<Computer> listComputer) {
        productTableModel.setRowCount(0);
        for (int i = 0; i < listComputer.size(); i++) {
            productTableModel.addRow(new Object[]{
                    productTableModel.getRowCount() + 1,
                    listComputer.get(i).getMachineCode(),
                    listComputer.get(i).getMachineName(),
                    ReceiptBillDetailsDAO.getInstance().selectQuantityBy(listComputer.get(i).getMachineCode()),
                    DeliveryBillDetailsDAO.getInstance().selectQuantityBy(listComputer.get(i).getMachineCode())
            });
        }
    }

    public void loadDataToBillTable(ArrayList<ReceiptBill> listReceiptBill, ArrayList<DeliveryBill> listDeliveryBill) {
        ArrayList<Bill> listBill = new ArrayList<>();
        if (listReceiptBill != null) listBill.addAll(listReceiptBill);
        if (listDeliveryBill != null) listBill.addAll(listDeliveryBill);
        Collections.sort(listBill, Comparator.comparing(Bill::getCreationTime).reversed());

        double totalPrice = 0;

        billTableModel.setRowCount(0);
        for (int i = 0; i < listBill.size(); i++) {
            billTableModel.addRow(new Object[]{
                    billTableModel.getRowCount() + 1,
                    listBill.get(i).getBillCode(),
                    listBill.get(i).getCreator(),
                    formatDate.format(listBill.get(i).getCreationTime()),
                    decimalFormat.format(listBill.get(i).getTotalAmount()) + "đ",
                    listBill.get(i).getBillCode().contains("PN") ? "Phiếu nhập" : "Phiếu xuất"
            });

            totalPrice += listBill.get(i).getTotalAmount();
        }

        totalBillLabel.setText(Integer.toString(billTableModel.getRowCount()));
        totalPriceLabel.setText(decimalFormat.format(totalPrice) + "đ");
    }

    public void loadDataToAccountTable(ArrayList<Account> listAccount) {
        accountTableModel.setRowCount(0);
        for (int i = 0; i < listAccount.size(); i++) {
            accountTableModel.addRow(new Object[]{
                    accountTableModel.getRowCount() + 1,
                    listAccount.get(i).getAccountName(),
                    listAccount.get(i).getEmail(),
                    listAccount.get(i).getUsername(),
                    listAccount.get(i).getRole(),
                    listAccount.get(i).getStatus() == 1 ? "Hoạt động" : "Bị khóa"
            });
        }
    }

    public void initComponents() {
        this.setBorder(null);
        this.setPreferredSize(new Dimension(1000, 800));

        // init header
        iconProductLabel = new JLabel(getImageIcon("/icon/productLabel.png"));
        iconProductLabel.setBackground(Color.WHITE);

        quantityProductLabel = new JLabel(Integer.toString(ComputerDAO.getInstance().selectAll().size()));
        quantityProductLabel.setFont(new Font("SF Pro Display", 1, 36));
        quantityProductLabel.setBackground(Color.WHITE);
        quantityProductLabel.setForeground(new Color(30, 3, 78));

        productLabel = new JLabel("Sản phẩm trong kho");
        productLabel.setFont(new Font("SF Pro Display", 0, 18));
        productLabel.setBackground(Color.WHITE);
        productLabel.setForeground(new Color(30, 3, 78));

        productPanel = new JPanel();
        productPanel.setBackground(Color.WHITE);

        javax.swing.GroupLayout productLayout = new javax.swing.GroupLayout(productPanel);
        productPanel.setLayout(productLayout);
        productLayout.setHorizontalGroup(
                productLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, productLayout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(iconProductLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                                .addGroup(productLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(quantityProductLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(productLabel, 180, 180, 180))
                                .addGap(10, 10, 10))
        );
        productLayout.setVerticalGroup(
                productLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, productLayout.createSequentialGroup()
                                .addContainerGap(12, Short.MAX_VALUE)
                                .addGroup(productLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(iconProductLabel)
                                        .addGroup(productLayout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(quantityProductLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(productLabel)))
                                .addGap(10, 10, 10))
        );

        iconSupplierLabel = new JLabel(getImageIcon("/icon/supplierLabel.png"));
        iconSupplierLabel.setBackground(Color.WHITE);

        quantitySupplierLabel = new JLabel(Integer.toString(SupplierDAO.getInstance().selectAll().size()));
        quantitySupplierLabel.setFont(new Font("SF Pro Display", 1, 36));
        quantitySupplierLabel.setBackground(Color.WHITE);
        quantitySupplierLabel.setForeground(new Color(30, 3, 78));

        supplierLabel = new JLabel("Nhà cung cấp");
        supplierLabel.setFont(new Font("SF Pro Display", 0, 18));
        supplierLabel.setBackground(Color.WHITE);
        supplierLabel.setForeground(new Color(30, 3, 78));

        supplierPanel = new JPanel();
        supplierPanel.setBackground(Color.WHITE);

        javax.swing.GroupLayout supplierLayout = new javax.swing.GroupLayout(supplierPanel);
        supplierPanel.setLayout(supplierLayout);
        supplierLayout.setHorizontalGroup(
                supplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, supplierLayout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(iconSupplierLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                                .addGroup(supplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(quantitySupplierLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(supplierLabel))
                                .addGap(70, 70, 70))
        );
        supplierLayout.setVerticalGroup(
                supplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, supplierLayout.createSequentialGroup()
                                .addContainerGap(12, Short.MAX_VALUE)
                                .addGroup(supplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(iconSupplierLabel)
                                        .addGroup(supplierLayout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(quantitySupplierLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(supplierLabel)))
                                .addGap(10, 10, 10))
        );

        iconAccountLabel = new JLabel(getImageIcon("/icon/accountLabel.png"));
        iconAccountLabel.setBackground(Color.WHITE);

        quantityAccountLabel = new JLabel(Integer.toString(AccountDAO.getInstance().selectAll().size()));
        quantityAccountLabel.setFont(new Font("SF Pro Display", 1, 36));
        quantityAccountLabel.setBackground(Color.WHITE);
        quantityAccountLabel.setForeground(new Color(30, 3, 78));

        accountLabel = new JLabel("Tài khoản người dùng");
        accountLabel.setFont(new Font("SF Pro Display", 0, 18));
        accountLabel.setBackground(Color.WHITE);
        accountLabel.setForeground(new Color(30, 3, 78));

        accountPanel = new JPanel();
        accountPanel.setBackground(Color.WHITE);

        javax.swing.GroupLayout accountLayout = new javax.swing.GroupLayout(accountPanel);
        accountPanel.setLayout(accountLayout);
        accountLayout.setHorizontalGroup(
                accountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, accountLayout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(iconAccountLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                                .addGroup(accountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(quantityAccountLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(accountLabel, 180, 180, 180))
                                .addGap(10, 10, 10))
        );
        accountLayout.setVerticalGroup(
                accountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, accountLayout.createSequentialGroup()
                                .addContainerGap(12, Short.MAX_VALUE)
                                .addGroup(accountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(iconAccountLabel)
                                        .addGroup(accountLayout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(quantityAccountLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(accountLabel)))
                                .addGap(10, 10, 10))
        );

        headerPanel = new JPanel();
        headerPanel.setBackground(new Color(250, 250, 250));
        headerPanel.setBounds(0, 0, 1000, 130);

        GroupLayout headerLayout = new GroupLayout(headerPanel);
        headerPanel.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
                headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(headerLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(productPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(supplierPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(accountPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15))
        );
        headerLayout.setVerticalGroup(
                headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(headerLayout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(accountPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(supplierPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(productPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        // completed init header

        // init content

        searchProductTxt = new JTextField();
        searchProductTxt.setBackground(Color.WHITE);
        searchProductTxt.setFont(fontDefault);
        searchProductTxt.setBounds(20, 30, 260, 35);
        searchProductTxt.setBorder(new FlatLineBorder(new Insets(5,5,5,5), Color.LIGHT_GRAY));
        searchProductTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchProductKeyReleased(e);
            }
        });

        searchProductPanel = new JPanel();
        searchProductPanel.setFont(fontDefault);
        searchProductPanel.setBackground(Color.WHITE);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Tìm kiếm");
        titledBorder.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY));
        searchProductPanel.setBorder(titledBorder);
        searchProductPanel.setLayout(null);

        searchProductPanel.add(searchProductTxt);

        fromProductLabel = new JLabel("From");
        fromProductLabel.setFont(fontDefault);
        fromProductLabel.setBounds(30, 40, 30, 20);

        toProductLabel = new JLabel("To");
        toProductLabel.setFont(fontDefault);
        toProductLabel.setBounds(300, 40, 20, 20);

        fromProductDate = new JDateChooser();
        fromProductDate.setBackground(Color.WHITE);
        fromProductDate.getDateEditor().getUiComponent().setBorder(new FlatLineBorder(new Insets(5,5,5,5), Color.LIGHT_GRAY));
        fromProductDate.getCalendarButton().setBackground(Color.WHITE);
        fromProductDate.getJCalendar().setBackground(Color.WHITE);
        fromProductDate.setDateFormatString("dd/MM/YYYY");
        fromProductDate.setBounds(80, 40, 170, 25);
        fromProductDate.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                fromProductPropertyChange(evt);
            }
        });

        toProductDate = new JDateChooser();
        toProductDate.setBackground(Color.WHITE);
        toProductDate.getDateEditor().getUiComponent().setBorder(new FlatLineBorder(new Insets(5,5,5,5), Color.LIGHT_GRAY));
        toProductDate.getCalendarButton().setBackground(Color.WHITE);
        toProductDate.getJCalendar().setBackground(Color.WHITE);
        toProductDate.setDateFormatString("dd/MM/YYYY");
        toProductDate.setBounds(330, 40, 170, 25);
        toProductDate.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                toProductPropertyChange(evt);
            }
        });
        refreshProductsButton = new JButton("Làm mới", getImageIcon("/icon/refreshButton.png", 40, 40));
        refreshProductsButton.setBounds(520, 30, 125, 40);
        refreshProductsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshProductsButton.setBackground(Color.WHITE);
        refreshProductsButton.setBorder(new LineBorder(Color.WHITE));
        refreshProductsButton.setFont(fontDefault);
        refreshProductsButton.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY, 1, 5));
        refreshProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshProductsActionPerformed(e);
            }
        });
        filterProductPanel = new JPanel();
        filterProductPanel.setFont(fontDefault);
        filterProductPanel.setBackground(Color.WHITE);
        titledBorder = BorderFactory.createTitledBorder("Tìm kiếm");
        titledBorder.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY));
        filterProductPanel.setBorder(titledBorder);
        filterProductPanel.setLayout(null);

        filterProductPanel.add(fromProductLabel);
        filterProductPanel.add(fromProductDate);
        filterProductPanel.add(toProductLabel);
        filterProductPanel.add(toProductDate);
        filterProductPanel.add(refreshProductsButton);

        productTable = new JTable();
        productTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                }
        ));

        productScrollPane = new JScrollPane();
        productScrollPane.setViewportView(productTable);
        productScrollPane.getViewport().setBackground(Color.WHITE);
        productScrollPane.setBackground(Color.WHITE);

        JTableHeader header = productTable.getTableHeader();
        header.setFont(new Font("Arial", Font.PLAIN, 14));
        header.setBackground(Color.WHITE);

        JScrollBar verticalScrollBar = new JScrollBar(JScrollBar.VERTICAL);
        verticalScrollBar.setPreferredSize(new Dimension(8, 0));
        verticalScrollBar.setBackground(Color.WHITE);

        verticalScrollBar.setUnitIncrement(60);
        verticalScrollBar.setBlockIncrement(60);

        productScrollPane.setVerticalScrollBar(verticalScrollBar);

        productDetailsPanel = new JPanel();
        productDetailsPanel.setBackground(Color.WHITE);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(productDetailsPanel);
        productDetailsPanel.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
                jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel15Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(productScrollPane)
                                        .addGroup(jPanel15Layout.createSequentialGroup()
                                                .addComponent(searchProductPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(filterProductPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
                jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(searchProductPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(filterProductPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(productScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(53, Short.MAX_VALUE))
        );

        seeDetailsBillButton = new JButton("Xem chi tiết", getImageIcon("/icon/seeDetailsButton.png", 40, 40));
        seeDetailsBillButton.setBounds(5, 15, 85, 60);
        seeDetailsBillButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        seeDetailsBillButton.setHorizontalTextPosition(SwingConstants.CENTER);
        seeDetailsBillButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        seeDetailsBillButton.setBackground(Color.WHITE);
        seeDetailsBillButton.setBorder(new LineBorder(Color.WHITE));
        seeDetailsBillButton.setFont(fontDefault);
        seeDetailsBillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seeDetailsBillActionPerformed(e);
            }
        });

        billToolbar = new JToolBar();
        billToolbar.setBackground(Color.WHITE);
        titledBorder = BorderFactory.createTitledBorder("Chức năng");
        titledBorder.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY));
        billToolbar.setBorder(titledBorder);
        billToolbar.setFont(fontDefault);
        billToolbar.setRollover(true);
        billToolbar.setFloatable(false);
        billToolbar.setLayout(null);

        billToolbar.add(seeDetailsBillButton);

        billComboBox = new JComboBox<>();
        billComboBox.setBackground(Color.WHITE);
        billComboBox.setBorder(new LineBorder(Color.WHITE));
        billComboBox.setFont(fontDefault);
        billComboBox.setModel(new DefaultComboBoxModel<>(new String[] {"Tất cả", "Phiếu nhập", "Phiếu xuất"}));
        billComboBox.setBounds(20, 30, 150, 40);
        billComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                billComboBoxActionPerformed(e);
            }
        });
        searchBillTxt = new JTextField();
        searchBillTxt.setBorder(new FlatLineBorder(new Insets(5,5,5,5), Color.LIGHT_GRAY));
        searchBillTxt.setFont(fontDefault);
        searchBillTxt.setBounds(190, 30, 250, 40);
        searchBillTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchBillKeyReleased(e);
            }
        });
        refreshBillButton = new JButton("Làm mới", getImageIcon("/icon/refreshButton.png", 40, 40));
        refreshBillButton.setBounds(460, 30, 125, 40);
        refreshBillButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBillButton.setBackground(Color.WHITE);
        refreshBillButton.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY, 1, 5));
        refreshBillButton.setFont(fontDefault);
        refreshBillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshBillActionPerformed(e);
            }
        });

        searchBillPanel = new JPanel();

        searchBillPanel = new JPanel();
        searchBillPanel.setFont(fontDefault);
        searchBillPanel.setBackground(Color.WHITE);
        titledBorder = BorderFactory.createTitledBorder("Tìm kiếm");
        titledBorder.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY));
        searchBillPanel.setBorder(titledBorder);
        searchBillPanel.setLayout(null);

        searchBillPanel.add(billComboBox);
        searchBillPanel.add(searchBillTxt);
        searchBillPanel.add(refreshBillButton);

        fromBillLabel = new JLabel("From");
        fromBillLabel.setFont(fontDefault);
        fromBillLabel.setBounds(20, 30, 30, 25);

        fromBillDate = new JDateChooser();
        fromBillDate.setBackground(Color.WHITE);
        fromBillDate.getDateEditor().getUiComponent().setBorder(new FlatLineBorder(new Insets(5,5,5,5), Color.LIGHT_GRAY));
        fromBillDate.getCalendarButton().setBackground(Color.WHITE);
        fromBillDate.getJCalendar().setBackground(Color.WHITE);
        fromBillDate.setDateFormatString("dd/MM/YYYY");
        fromBillDate.setBounds(70, 30, 170, 25);
        fromBillDate.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                fromBillPropertyChange(evt);
            }
        });

        toBillLabel = new JLabel("To");
        toBillLabel.setFont(fontDefault);
        toBillLabel.setBounds(270, 30, 30, 25);

        toBillDate = new JDateChooser();
        toBillDate.setBackground(Color.WHITE);
        toBillDate.getDateEditor().getUiComponent().setBorder(new FlatLineBorder(new Insets(5,5,5,5), Color.LIGHT_GRAY));
        toBillDate.getCalendarButton().setBackground(Color.WHITE);
        toBillDate.getJCalendar().setBackground(Color.WHITE);
        toBillDate.setDateFormatString("dd/MM/YYYY");
        toBillDate.setBounds(300, 30, 170, 25);
        toBillDate.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                toBillPropertyChange(evt);
            }
        });
        filterDateBillPanel = new JPanel();
        filterDateBillPanel.setFont(fontDefault);
        filterDateBillPanel.setBackground(Color.WHITE);
        filterDateBillPanel.setLayout(null);
        titledBorder = BorderFactory.createTitledBorder("Lọc theo ngày");
        titledBorder.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY));
        filterDateBillPanel.setBorder(titledBorder);
        filterDateBillPanel.add(fromBillLabel);
        filterDateBillPanel.add(fromBillDate);
        filterDateBillPanel.add(toBillLabel);
        filterDateBillPanel.add(toBillDate);

        fromPriceLabel = new JLabel("From");
        fromPriceLabel.setFont(fontDefault);

        toPriceLabel = new JLabel("To");
        toPriceLabel.setFont(fontDefault);

        fromPriceBillTxt = new JTextField();
        fromPriceBillTxt.setFont(fontDefault);
        fromPriceBillTxt.setBorder(new FlatLineBorder(new Insets(5,5,5,5), Color.LIGHT_GRAY));
        fromPriceBillTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                fromPriceBillKeyReleased(e);
            }
        });
        toPriceBillTxt = new JTextField();
        toPriceBillTxt.setFont(fontDefault);
        toPriceBillTxt.setBorder(new FlatLineBorder(new Insets(5,5,5,5), Color.LIGHT_GRAY));
        toPriceBillTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                toPriceBillKeyReleased(e);
            }
        });
        filterPriceBillPanel = new JPanel();
        filterPriceBillPanel.setFont(fontDefault);
        filterPriceBillPanel.setBackground(Color.WHITE);
        titledBorder = BorderFactory.createTitledBorder("Lọc theo giá");
        titledBorder.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY));
        filterPriceBillPanel.setBorder(titledBorder);

        javax.swing.GroupLayout filterPriceLayout = new javax.swing.GroupLayout(filterPriceBillPanel);
        filterPriceBillPanel.setLayout(filterPriceLayout);
        filterPriceLayout.setHorizontalGroup(
                filterPriceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(filterPriceLayout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(fromPriceLabel, 40, 40, 40)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(fromPriceBillTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(toPriceLabel)
                                .addGap(18, 18, 18)
                                .addComponent(toPriceBillTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(84, Short.MAX_VALUE))
        );
        filterPriceLayout.setVerticalGroup(
                filterPriceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(filterPriceLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(filterPriceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(fromPriceLabel)
                                        .addComponent(fromPriceBillTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(toPriceLabel)
                                        .addComponent(toPriceBillTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(22, Short.MAX_VALUE))
        );


        billTable = new JTable();
        billTable.setModel(new DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "Mã phiếu", "Người tạo", "Thời gian tạo", "Tổng tiền", "Loại phiếu"
                }
        ));

        billScrollPane = new JScrollPane();
        billScrollPane.setViewportView(billTable);
        billScrollPane.getViewport().setBackground(Color.WHITE);
        billScrollPane.setBackground(Color.WHITE);

        header = billTable.getTableHeader();
        header.setFont(new Font("Arial", Font.PLAIN, 14));
        header.setBackground(Color.WHITE);

        // Customizing the scroll bars
        verticalScrollBar = new JScrollBar(JScrollBar.VERTICAL);
        verticalScrollBar.setPreferredSize(new Dimension(8, 0));
        verticalScrollBar.setBackground(Color.WHITE);

        verticalScrollBar.setUnitIncrement(60);  // Increase unit increment
        verticalScrollBar.setBlockIncrement(60);

        billScrollPane.setVerticalScrollBar(verticalScrollBar);

        totalBillTextLabel = new JLabel("TỔNG PHIẾU");
        totalBillTextLabel.setFont(new Font("SF Pro Display", 1, 18));
        totalBillTextLabel.setBackground(Color.WHITE);

        totalBillLabel = new JLabel("0");
        totalBillLabel.setFont(new Font("SF Pro Display", 1, 18));
        totalBillLabel.setBackground(Color.WHITE);

        totalPriceTextLabel = new JLabel("TỔNG TIỀN");
        totalPriceTextLabel.setFont(new Font("SF Pro Display", 1, 18));
        totalPriceTextLabel.setBackground(Color.WHITE);

        totalPriceLabel = new JLabel("0");
        totalPriceLabel.setFont(new Font("SF Pro Display", 1, 18));
        totalPriceLabel.setBackground(Color.WHITE);
        totalPriceLabel.setForeground(Color.red);

        billDetailsPanel = new JPanel();
        billDetailsPanel.setBackground(Color.WHITE);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(billDetailsPanel);
        billDetailsPanel.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(billScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 970, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(billToolbar, 350, 350, 350)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(searchBillPanel, 600, 600, 600))
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(filterDateBillPanel, 480, 480, 480)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(filterPriceBillPanel, 470, 470, 470))))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(37, 37, 37)
                                                .addComponent(totalBillTextLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(totalBillLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(totalPriceTextLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(totalPriceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(13, 13, 13)))
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(searchBillPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                                        .addComponent(billToolbar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(filterDateBillPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(filterPriceBillPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(billScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(totalBillTextLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(totalBillLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(totalPriceTextLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(totalPriceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(203, 203, 203))
        );

        accountComboBox = new JComboBox<>();
        accountComboBox = new JComboBox<>();
        accountComboBox.setBackground(Color.WHITE);
        accountComboBox.setBorder(new LineBorder(Color.WHITE));
        accountComboBox.setFont(fontDefault);
        accountComboBox.setModel(new DefaultComboBoxModel<>(new String[] {"Tất cả", "Tên tài khoản", "Tên đăng nhập", "Vai trò" }));
        accountComboBox.setBounds(20, 30, 210, 40);
        accountComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accountComboBoxActionPerformed(e);
            }
        });
        searchAccountTxt = new JTextField();
        searchAccountTxt.setBorder(new FlatLineBorder(new Insets(5,5,5,5), Color.LIGHT_GRAY));
        searchAccountTxt.setFont(fontDefault);
        searchAccountTxt.setBounds(260, 30, 320, 40);
        searchAccountTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchAccountKeyReleased(e);
            }
        });
        refreshAccountButton = new JButton("Làm mới", getImageIcon("/icon/refreshButton.png", 40, 40));
        refreshAccountButton.setBounds(620, 30, 160, 40);
        refreshAccountButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshAccountButton.setBackground(Color.WHITE);
        refreshAccountButton.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY, 1, 5));
        refreshAccountButton.setFont(fontDefault);
        refreshAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshAccountActionPerformed(e);
            }
        });

        searchAccountPanel = new JPanel();
        searchAccountPanel.setFont(fontDefault);
        searchAccountPanel.setBackground(Color.WHITE);
        titledBorder = BorderFactory.createTitledBorder("Tìm kiếm");
        titledBorder.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY));
        searchAccountPanel.setBorder(titledBorder);
        searchAccountPanel.setLayout(null);

        searchAccountPanel.add(accountComboBox);
        searchAccountPanel.add(searchAccountTxt);
        searchAccountPanel.add(refreshAccountButton);

        accountTable = new JTable();
        accountTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                        "Họ và tên", "Email", "Tên người dùng", "Vai trò", "Tình trạng"
                }
        ));

        accountTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        accountTable.getColumnModel().getColumn(1).setPreferredWidth(180);

        accountScrollPane = new JScrollPane();
        accountScrollPane.setViewportView(accountTable);
        accountScrollPane.getViewport().setBackground(Color.WHITE);
        accountScrollPane.setBackground(Color.WHITE);

        header = accountTable.getTableHeader();
        header.setFont(new Font("Arial", Font.PLAIN, 14));
        header.setBackground(Color.WHITE);

        // Customizing the scroll bars
        verticalScrollBar = new JScrollBar(JScrollBar.VERTICAL);
        verticalScrollBar.setPreferredSize(new Dimension(8, 0));
        verticalScrollBar.setBackground(Color.WHITE);

        verticalScrollBar.setUnitIncrement(60);  // Increase unit increment
        verticalScrollBar.setBlockIncrement(60);

        accountScrollPane.setVerticalScrollBar(verticalScrollBar);

        accountDetailsPanel = new JPanel();
        accountDetailsPanel.setBackground(Color.WHITE);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(accountDetailsPanel);
        accountDetailsPanel.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(searchAccountPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(accountScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 970, Short.MAX_VALUE))
                                .addContainerGap(11, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(searchAccountPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(accountScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 453, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(149, Short.MAX_VALUE))
        );

        ColorUIResource tabColor = new ColorUIResource(255, 255, 255); // Màu trắng
        Color borderColor = Color.WHITE; // Màu viền trắng

        // Đặt màu cho UIManager
        UIManager.put("TabbedPane.background", tabColor);
        UIManager.put("TabbedPane.selected", tabColor);
        UIManager.put("TabbedPane.borderHightlightColor", tabColor);
        UIManager.put("TabbedPane.darkShadow", tabColor);
        UIManager.put("TabbedPane.focus", tabColor);
        UIManager.put("TabbedPane.highlight", tabColor);
        UIManager.put("TabbedPane.light", tabColor);
        UIManager.put("TabbedPane.selectHighlight", tabColor);
        UIManager.put("TabbedPane.tabAreaBackground", tabColor);
        UIManager.put("TabbedPane.unselectedBackground", tabColor);
        UIManager.put("TabbedPane.shadow", tabColor);

        UIManager.put("TabbedPane.border", new LineBorder(borderColor));

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Sản phẩm", productDetailsPanel);
        tabbedPane.addTab("Phiếu", billDetailsPanel);
        tabbedPane.addTab("Tài khoản", accountDetailsPanel);

        tabbedPane.setBounds(0, 130, 1000, 800);

        // completed init content


        getContentPane().setBackground(new Color(250, 250, 250));
        getContentPane().setLayout(null);

        getContentPane().add(headerPanel);
        getContentPane().add(tabbedPane);
        pack();
    }

    private ImageIcon getImageIcon(String pathIcon) {
        int widthIcon = 80, heighIcon = 80;
        URL urlIcon = StatisticForm.class.getResource(pathIcon);
        Image imgUser = Toolkit.getDefaultToolkit().createImage(urlIcon).getScaledInstance(widthIcon, heighIcon, Image.SCALE_SMOOTH);
        return  new ImageIcon(imgUser);
    }

    private ImageIcon getImageIcon(String pathIcon, int width, int high) {
        int widthIcon = width, heighIcon = high;
        URL urlIcon = StatisticForm.class.getResource(pathIcon);
        Image imgUser = Toolkit.getDefaultToolkit().createImage(urlIcon).getScaledInstance(widthIcon, heighIcon, Image.SCALE_SMOOTH);
        return  new ImageIcon(imgUser);
    }

    public void searchProductKeyReleased(KeyEvent event) {
        ArrayList<Computer> listComputer = filterProduct();
        if (listComputer != null) loadDataToProductTable(listComputer);
    }

    public void fromProductPropertyChange(PropertyChangeEvent event) {
        if (fromProductDate.getDate() != null) {
            ArrayList<Computer> listComputer = filterProduct();
            if (listComputer != null) loadDataToProductTable(listComputer);
        }

    }

    public void toProductPropertyChange(PropertyChangeEvent event) {
        if (toProductDate.getDate() != null) {
            ArrayList<Computer> listComputer = filterProduct();
            if (listComputer != null) loadDataToProductTable(listComputer);
        }
    }

    public void refreshProductsActionPerformed(ActionEvent event) {
        searchProductTxt.setText("");
        fromProductDate.setDate(null);
        toProductDate.setDate(null);

        loadDataToProductTable(ComputerDAO.getInstance().selectAll());
    }

    public ArrayList<Computer> filterProduct() {
        String option = "Tất cả";
        String content = searchProductTxt.getText();

        ArrayList<Computer> listComputer = ComputerDAO.getInstance().filter(option, content);
        return listComputer;
    }

    public void seeDetailsBillActionPerformed(ActionEvent event) {
        if (billTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thông tin để xem chi tiết!");
        } else if (billTable.getValueAt(billTable.getSelectedRow(), 1).toString().contains("PN")) {
            SeeDetailsReceiptBill seeDetailsReceiptBill = new SeeDetailsReceiptBill(this,
                    (JFrame) SwingUtilities.getWindowAncestor(this), rootPaneCheckingEnabled);
            seeDetailsReceiptBill.setVisible(true);
        } else {
            SeeDetailsDeliveryBill seeDetailsDeliveryBill = new SeeDetailsDeliveryBill(this,
                    (JFrame) SwingUtilities.getWindowAncestor(this), rootPaneCheckingEnabled);
            seeDetailsDeliveryBill.setVisible(true);
        }
    }

    public void billComboBoxActionPerformed(ActionEvent event) {
        String option = billComboBox.getSelectedItem().toString();
        if (option.equals("Phiếu nhập")) {
            ArrayList<ReceiptBill> listReceiptBill = filterReceiptBill();
            loadDataToBillTable(listReceiptBill, null);
        } else if (option.equals("Phiếu xuất")) {
            ArrayList<DeliveryBill> listDeliveryBill = filterDeliveryBill();
            loadDataToBillTable(null, listDeliveryBill);
        } else {
            ArrayList<ReceiptBill> listReceiptBill = filterReceiptBill();
            ArrayList<DeliveryBill> listDeliveryBill = filterDeliveryBill();
            loadDataToBillTable(listReceiptBill, listDeliveryBill);
        }

    }

    public void searchBillKeyReleased(KeyEvent event) {
        String option = billComboBox.getSelectedItem().toString();
        if (option.equals("Phiếu nhập")) {
            ArrayList<ReceiptBill> listReceiptBill = filterReceiptBill();
            loadDataToBillTable(listReceiptBill, null);
        } else if (option.equals("Phiếu xuất")) {
            ArrayList<DeliveryBill> listDeliveryBill = filterDeliveryBill();
            loadDataToBillTable(null, listDeliveryBill);
        } else {
            ArrayList<ReceiptBill> listReceiptBill = filterReceiptBill();
            ArrayList<DeliveryBill> listDeliveryBill = filterDeliveryBill();
            loadDataToBillTable(listReceiptBill, listDeliveryBill);
        }
    }

    public void refreshBillActionPerformed(ActionEvent event) {
        billComboBox.setSelectedItem("Tất cả");
        searchBillTxt.setText("");
        fromBillDate.setDate(null);
        toBillDate.setDate(null);
        fromPriceBillTxt.setText("");
        toPriceBillTxt.setText("");

        loadDataToBillTable(ReceiptBillDAO.getInstance().selectAll(), DeliveryBillDAO.getInstance().selectAll());
    }

    public void fromBillPropertyChange(PropertyChangeEvent event) {
        String option = billComboBox.getSelectedItem().toString();
        if (option.equals("Phiếu nhập")) {
            ArrayList<ReceiptBill> listReceiptBill = filterReceiptBill();
            loadDataToBillTable(listReceiptBill, null);
        } else if (option.equals("Phiếu xuất")) {
            ArrayList<DeliveryBill> listDeliveryBill = filterDeliveryBill();
            loadDataToBillTable(null, listDeliveryBill);
        } else {
            ArrayList<ReceiptBill> listReceiptBill = filterReceiptBill();
            ArrayList<DeliveryBill> listDeliveryBill = filterDeliveryBill();
            loadDataToBillTable(listReceiptBill, listDeliveryBill);
        }
    }

    public void toBillPropertyChange(PropertyChangeEvent event) {
        String option = billComboBox.getSelectedItem().toString();
        if (option.equals("Phiếu nhập")) {
            ArrayList<ReceiptBill> listReceiptBill = filterReceiptBill();
            loadDataToBillTable(listReceiptBill, null);
        } else if (option.equals("Phiếu xuất")) {
            ArrayList<DeliveryBill> listDeliveryBill = filterDeliveryBill();
            loadDataToBillTable(null, listDeliveryBill);
        } else {
            ArrayList<ReceiptBill> listReceiptBill = filterReceiptBill();
            ArrayList<DeliveryBill> listDeliveryBill = filterDeliveryBill();
            loadDataToBillTable(listReceiptBill, listDeliveryBill);
        }
    }

    public void fromPriceBillKeyReleased(KeyEvent event) {
        String option = billComboBox.getSelectedItem().toString();
        if (option.equals("Phiếu nhập")) {
            ArrayList<ReceiptBill> listReceiptBill = filterReceiptBill();
            loadDataToBillTable(listReceiptBill, null);
        } else if (option.equals("Phiếu xuất")) {
            ArrayList<DeliveryBill> listDeliveryBill = filterDeliveryBill();
            loadDataToBillTable(null, listDeliveryBill);
        } else {
            ArrayList<ReceiptBill> listReceiptBill = filterReceiptBill();
            ArrayList<DeliveryBill> listDeliveryBill = filterDeliveryBill();
            loadDataToBillTable(listReceiptBill, listDeliveryBill);
        }
    }

    public void toPriceBillKeyReleased(KeyEvent event) {
        String option = billComboBox.getSelectedItem().toString();
        if (option.equals("Phiếu nhập")) {
            ArrayList<ReceiptBill> listReceiptBill = filterReceiptBill();
            loadDataToBillTable(listReceiptBill, null);
        } else if (option.equals("Phiếu xuất")) {
            ArrayList<DeliveryBill> listDeliveryBill = filterDeliveryBill();
            loadDataToBillTable(null, listDeliveryBill);
        } else {
            ArrayList<ReceiptBill> listReceiptBill = filterReceiptBill();
            ArrayList<DeliveryBill> listDeliveryBill = filterDeliveryBill();
            loadDataToBillTable(listReceiptBill, listDeliveryBill);
        }
    }

    public ArrayList<ReceiptBill> filterReceiptBill() {
        String option = "Tất cả";
        String content = searchBillTxt.getText();

        double fromPrice = fromPriceBillTxt.getText().isEmpty() ? 0 : Double.parseDouble(fromPriceBillTxt.getText());
        double toPrice = toPriceBillTxt.getText().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(toPriceBillTxt.getText());

        Timestamp fromDate = fromBillDate.getDate() != null ? getFromTime() : Timestamp.valueOf("2020-01-01 00:00:00");
        Timestamp toDate = toBillDate.getDate() != null ? getToTime() : Timestamp.valueOf("2030-01-01 00:00:00");

        ArrayList<ReceiptBill> listReceiptBill = ReceiptBillDAO.getInstance().filter(option, content, fromDate, toDate, fromPrice, toPrice);

        return listReceiptBill;
    }

    public ArrayList<DeliveryBill> filterDeliveryBill() {
        String option = "Tất cả";
        String content = searchBillTxt.getText();

        double fromPrice = fromPriceBillTxt.getText().isEmpty() ? 0 : Double.parseDouble(fromPriceBillTxt.getText());
        double toPrice = toPriceBillTxt.getText().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(toPriceBillTxt.getText());

        Timestamp fromDate = fromBillDate.getDate() != null ? getFromTime() : Timestamp.valueOf("2020-01-01 00:00:00");
        Timestamp toDate = toBillDate.getDate() != null ? getToTime() : Timestamp.valueOf("2030-01-01 00:00:00");

        ArrayList<DeliveryBill>  listDeliveryBill = DeliveryBillDAO.getInstance().filter(option, content, fromDate, toDate, fromPrice, toPrice);

        return listDeliveryBill;
    }

    private Timestamp getFromTime() {
        Date fromDate = fromBillDate.getDate();

        Calendar fromCal = Calendar.getInstance();

        fromCal.setTime(fromDate);

        fromCal.set(Calendar.HOUR_OF_DAY, 0);
        fromCal.set(Calendar.MINUTE, 0);
        fromCal.set(Calendar.SECOND, 0);
        fromCal.set(Calendar.MILLISECOND, 0);

        return new Timestamp(fromCal.getTimeInMillis());
    }

    private Timestamp getToTime() {
        Date toDate = toBillDate.getDate();

        Calendar toCal = Calendar.getInstance();

        toCal.setTime(toDate);

        toCal.set(Calendar.HOUR_OF_DAY, 0);
        toCal.set(Calendar.MINUTE, 0);
        toCal.set(Calendar.SECOND, 0);
        toCal.set(Calendar.MILLISECOND, 0);

        return new Timestamp(toCal.getTimeInMillis());
    }

    public void accountComboBoxActionPerformed(ActionEvent event) {
        ArrayList<Account> listAccount = filterAccount();
        if (listAccount != null) loadDataToAccountTable(listAccount);
    }

    public void searchAccountKeyReleased(KeyEvent event) {
        ArrayList<Account> listAccount = filterAccount();
        if (listAccount != null) loadDataToAccountTable(listAccount);
    }

    public void refreshAccountActionPerformed(ActionEvent event) {
        accountComboBox.setSelectedItem("Tất cả");
        searchAccountTxt.setText("");
        loadDataToAccountTable(AccountDAO.getInstance().selectAll());
    }

    public ArrayList<Account> filterAccount() {
        String option = accountComboBox.getSelectedItem().toString();
        String content = searchAccountTxt.getText();

        ArrayList<Account> listAccount = AccountDAO.getInstance().filter(option, content);
        return listAccount;
    }
}
