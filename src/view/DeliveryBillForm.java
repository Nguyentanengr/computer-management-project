package view;

import com.formdev.flatlaf.ui.FlatLineBorder;
import com.toedter.calendar.JDateChooser;
import controller.IOExcel;
import controller.SearchReceiptBill;
import dao.*;
import model.Account;
import model.BillDetails;
import model.DeliveryBill;
import model.ReceiptBill;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableCellRenderer;
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
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeliveryBillForm extends JInternalFrame {
    private Account account;
    private JPanel mainPanel;
    private JPanel searchPanel;
    private JPanel filterDatePanel;
    private JPanel filterPricePanel;
    private JToolBar featureToolbar;
    private JScrollPane scrollPane;
    private JTable contentTable;
    private JButton deleteButton;
    private JButton editButton;
    private JButton seeDetailsButton;
    private JButton inputExcelButton;
    private JButton outputExcelButton;
    private JButton refreshButton;
    private JComboBox <String> optionComboBox;
    private JTextField searchTxt;
    private JTextField fromPriceTxt;
    private JTextField toPriceTxt;
    private JDateChooser fromDateChooser;
    private JDateChooser toDateChooser;
    private JLabel fromDateLabel;
    private JLabel toDateLabel;
    private JLabel fromPriceLabel;
    private JLabel toPriceLabel;
    private JOptionPane jOptionPane;
    private DefaultTableModel defaultTableModel;
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    private SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/YYYY HH:mm");
    private Font fontDefault =  new Font("SF Pro Display", 0, 12);


    public DeliveryBillForm(Account account) {
        this.account = account;
        initComponents();
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
        initTable();
        loadDataToTable(DeliveryBillDAO.getInstance().selectAll());
    }

    public JTable getContentTable() {
        return this.contentTable;
    }

    public DefaultTableModel getDefaultTableModel() {
        return this.defaultTableModel;
    }

    private void initTable() {
        defaultTableModel = new DefaultTableModel();
        defaultTableModel.setColumnIdentifiers(new String[] {"STT", "Mã phiếu xuất", "Người tạo", "Thời gian tạo", "Tổng tiền"});
        contentTable.setModel(defaultTableModel);
        // thiet lap do rong cho tung cot thong quan model table
        contentTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        contentTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        contentTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        contentTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
    }

    public void loadDataToTable(ArrayList<DeliveryBill> listDeliveryBill) {
        defaultTableModel.setRowCount(0);
        for (DeliveryBill deliveryBill : listDeliveryBill) {
            defaultTableModel.addRow(new Object[]{contentTable.getRowCount() + 1, deliveryBill.getBillCode(),
                    deliveryBill.getCreator(), formatDate.format(deliveryBill.getCreationTime()), decimalFormat.format(deliveryBill.getTotalAmount()) + "đ"});
        }
    }

    private void loadDataToListDeliveryBill(ArrayList<DeliveryBill> listDeliveryBill) {
        for (int i = 0; i < contentTable.getRowCount(); i++) {
            String billCode = contentTable.getValueAt(i, 1).toString();
            String creator = contentTable.getValueAt(i, 2).toString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            Timestamp creationTime = Timestamp.valueOf(LocalDateTime.parse(contentTable.getValueAt(i, 3).toString(), formatter));
            double totalAmount = Double.parseDouble(contentTable.getValueAt(i, 4).toString().replaceAll("[,đ]", ""));

            DeliveryBill deliveryBill = new DeliveryBill(billCode, creationTime, creator, null, totalAmount);
            listDeliveryBill.add(deliveryBill);
        }
    }

    private void initComponents() {

        this.setBorder(null);
        this.setPreferredSize(new Dimension(1000, 800));

        JOptionPane jOptionPane = new JOptionPane();
        jOptionPane.setBackground(Color.WHITE);
        jOptionPane.setOpaque(true);
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);

        deleteButton = getButton("Xóa", "/icon/deleteButton.png");
        deleteButton.setBounds(20, 15, 50, 60);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteButtonActionPerformed(e);
            }
        });
        editButton = getButton("Sửa", "/icon/editButton.png");
        editButton.setBounds(80, 15, 50, 60);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editButtonActionPerformed(e);
            }
        });
        seeDetailsButton = getButton("Xem CT", "/icon/seeDetailsButton.png");
        seeDetailsButton.setBounds(150, 15, 50, 60);
        seeDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seeDetailsButtonActionPerformed(e);
            }
        });
        inputExcelButton = getButton("Nhập Excel", "/icon/inputExcelButton.png");
        inputExcelButton.setBounds(210, 15, 80, 60);
        inputExcelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputExcelButtonActionPerformed(e);
            }
        });
        outputExcelButton = getButton("Xuất Excel", "/icon/outputExcelButton.png");
        outputExcelButton.setBounds(290, 15, 80, 60);
        outputExcelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputExcelButtonActionPerformed(e);
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

        featureToolbar.add(deleteButton);
        featureToolbar.add(editButton);
        featureToolbar.add(seeDetailsButton);
        featureToolbar.add(inputExcelButton);
        featureToolbar.add(outputExcelButton);

        optionComboBox = new JComboBox<>();
        optionComboBox.setBackground(Color.WHITE);
        optionComboBox.setBorder(new LineBorder(Color.WHITE));
        optionComboBox.setFont(fontDefault);
        optionComboBox.setModel(new DefaultComboBoxModel<>(new String[] {"Tất cả", "Mã phiếu", "Người tạo"}));
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
        refreshButton.setBounds(460, 30, 105, 40);
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

        fromDateLabel = new JLabel("From");
        fromDateLabel.setFont(fontDefault);
        fromDateLabel.setBounds(20, 30, 30, 25);

        toDateLabel = new JLabel("To");
        toDateLabel.setFont(fontDefault);
        toDateLabel.setBounds(270, 30, 30, 25);

        fromDateChooser = new JDateChooser();
        fromDateChooser.setBackground(Color.WHITE);
        fromDateChooser.getDateEditor().getUiComponent().setBorder(new FlatLineBorder(new Insets(5,5,5,5), Color.LIGHT_GRAY));
        fromDateChooser.getCalendarButton().setBackground(Color.WHITE);
        fromDateChooser.getJCalendar().setBackground(Color.WHITE);
        fromDateChooser.setDateFormatString("dd/MM/YYYY");
        fromDateChooser.setBounds(70, 30, 170, 25);
        fromDateChooser.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                fromDateChooserPropertyChange(evt);
            }
        });
        fromDateChooser.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                fromDateChooserKeyReleased(e);
            }
        });

        toDateChooser = new JDateChooser();
        toDateChooser.setBackground(Color.WHITE);
        toDateChooser.getDateEditor().getUiComponent().setBorder(new FlatLineBorder(new Insets(5,5,5,5), Color.LIGHT_GRAY));
        toDateChooser.getCalendarButton().setBackground(Color.WHITE);
        toDateChooser.getJCalendar().setBackground(Color.WHITE);
        toDateChooser.setDateFormatString("dd/MM/YYYY");
        toDateChooser.setBounds(300, 30, 170, 25);
        toDateChooser.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                toDateChooserPropertyChange(evt);
            }
        });
        toDateChooser.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                toDateChooserKeyReleased(e);
            }
        });

        filterDatePanel = new JPanel();
        filterDatePanel.setFont(fontDefault);
        filterDatePanel.setBackground(Color.WHITE);
        filterDatePanel.setLayout(null);
        titledBorder = BorderFactory.createTitledBorder("Lọc theo ngày");
        titledBorder.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY));
        filterDatePanel.setBorder(titledBorder);
        filterDatePanel.add(fromDateLabel);
        filterDatePanel.add(fromDateChooser);
        filterDatePanel.add(toDateLabel);
        filterDatePanel.add(toDateChooser);

        fromPriceLabel = new JLabel("From");
        fromPriceLabel.setFont(fontDefault);

        toPriceLabel = new JLabel("To");
        toPriceLabel.setFont(fontDefault);

        fromPriceTxt = new JTextField();
        fromPriceTxt.setFont(fontDefault);
        fromPriceTxt.setBorder(new FlatLineBorder(new Insets(5,5,5,5), Color.LIGHT_GRAY));
        fromPriceTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                fromPriceTxtKeyReleased(e);
            }
        });

        toPriceTxt = new JTextField();
        toPriceTxt.setFont(fontDefault);
        toPriceTxt.setBorder(new FlatLineBorder(new Insets(5,5,5,5), Color.LIGHT_GRAY));
        toPriceTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                toPriceTxtKeyReleased(e);
            }
        });

        filterPricePanel = new JPanel();
        filterPricePanel.setFont(fontDefault);
        filterPricePanel.setBackground(Color.WHITE);
        titledBorder = BorderFactory.createTitledBorder("Lọc theo giá");
        titledBorder.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY));
        filterPricePanel.setBorder(titledBorder);

        javax.swing.GroupLayout filterPriceLayout = new javax.swing.GroupLayout(filterPricePanel);
        filterPricePanel.setLayout(filterPriceLayout);
        filterPriceLayout.setHorizontalGroup(
                filterPriceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(filterPriceLayout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(fromPriceLabel, 40, 40, 40)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(fromPriceTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(toPriceLabel)
                                .addGap(28, 28, 28)
                                .addComponent(toPriceTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(84, Short.MAX_VALUE))
        );
        filterPriceLayout.setVerticalGroup(
                filterPriceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(filterPriceLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(filterPriceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(fromPriceLabel)
                                        .addComponent(fromPriceTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(toPriceLabel)
                                        .addComponent(toPriceTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(22, Short.MAX_VALUE))
        );

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
        mainPanel.setBounds(0, 0, 1000, 800);

        GroupLayout mainLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(mainLayout);
        mainLayout.setHorizontalGroup(
                mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainLayout.createSequentialGroup()
                                                .addComponent(filterDatePanel, 480, 480, 480)
                                                .addGap(18, 18, 18)
                                                .addComponent(filterPricePanel, 480, 480, 480))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainLayout.createSequentialGroup()
                                                .addComponent(featureToolbar, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(searchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        mainLayout.setVerticalGroup(
                mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainLayout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(searchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                                        .addComponent(featureToolbar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addGap(10, 10, 10)
                                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(filterDatePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(filterPricePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(10, 10, 10)
                                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27))
        );

        getContentPane().setLayout(null);
        getContentPane().add(mainPanel);
        pack();
    }

    private ImageIcon getImageIcon(String pathIcon) {
        int widthIcon = 40, heighIcon = 40;
        URL urlIcon = DeliveryForm.class.getResource(pathIcon);
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

    private void deleteButtonActionPerformed(ActionEvent e) {
        int selectedRow = contentTable.getSelectedRow();
        if (selectedRow == -1) {
            jOptionPane.showMessageDialog(this, "Vui lòng chọn thông tin cần xóa");
        }
        else {
            int returnValue = jOptionPane.showConfirmDialog(this, "Chắc chắn xóa!", "Xóa phiếu nhập", JOptionPane.YES_NO_OPTION);
            if (returnValue == jOptionPane.NO_OPTION || returnValue == JOptionPane.CLOSED_OPTION) return;
            String deliveryBillCode = contentTable.getValueAt(selectedRow, 1).toString();
            ArrayList<BillDetails> listBillDetails = DeliveryBillDetailsDAO.getInstance().selectBy(deliveryBillCode);
            for (BillDetails billDetails : listBillDetails) {
                DeliveryBillDetailsDAO.getInstance().delete(billDetails);
            }

            DeliveryBill deliveryBill = DeliveryBillDAO.getInstance().selectById(deliveryBillCode);
            DeliveryBillDAO.getInstance().delete(deliveryBill);

            loadDataToTable(DeliveryBillDAO.getInstance().selectAll());

            jOptionPane.showMessageDialog(this, "Xóa thành công!");
        }
    }

    private void editButtonActionPerformed(ActionEvent e) {
        int selectedRow = contentTable.getSelectedRow();
        if (selectedRow == -1) {
            jOptionPane.showMessageDialog(this, "Vui lòng chọn thông tin cần sửa");
        }
        else {
            try {
                UpdateDeliveryBillForm updateDeliveryBillForm = new UpdateDeliveryBillForm(this, (JFrame) SwingUtilities.getWindowAncestor(this), rootPaneCheckingEnabled);
                updateDeliveryBillForm.setVisible(true);
            } catch (UnsupportedLookAndFeelException ex) {
                Logger.getLogger(ReceiptBillForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void seeDetailsButtonActionPerformed(ActionEvent e) {
        if (contentTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thông tin để xem chi tiết!");
        }
        else {
            SeeDetailsDeliveryBill seeDetailsDeliveryBill = new SeeDetailsDeliveryBill(this, (JFrame) SwingUtilities.getWindowAncestor(this), rootPaneCheckingEnabled);
            seeDetailsDeliveryBill.setVisible(true);
        }
    }

    private void inputExcelButtonActionPerformed(ActionEvent e) {
        IOExcel ioExcel = new IOExcel();
        ArrayList<DeliveryBill> listDeliveryBill = ioExcel.importDeliveryBill();
        if (listDeliveryBill != null) loadDataToTable(listDeliveryBill);
    }

    private void outputExcelButtonActionPerformed(ActionEvent e) {
        IOExcel ioExcel = new IOExcel();
        ArrayList<DeliveryBill> listDeliveryBill = new ArrayList<>();
        loadDataToListDeliveryBill(listDeliveryBill);
        if (listDeliveryBill.size() == 0) return;
        ioExcel.exportDeliveryBill(listDeliveryBill);
    }

    private void optionComboBoxActionPerformed(ActionEvent e) {
        ArrayList<DeliveryBill> listDeliveryBill = filter();
        if (listDeliveryBill != null) loadDataToTable(listDeliveryBill);
    }

    private ArrayList<DeliveryBill> filter() {
        String option = optionComboBox.getSelectedItem().toString();
        String content = searchTxt.getText();

        double fromPrice = fromPriceTxt.getText().isEmpty() ? 0 : Double.parseDouble(fromPriceTxt.getText());
        double toPrice = toPriceTxt.getText().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(toPriceTxt.getText());

        Timestamp fromDate = fromDateChooser.getDate() != null ? getFromTime() : Timestamp.valueOf("2020-01-01 00:00:00");
        Timestamp toDate = toDateChooser.getDate() != null ? getToTime() : Timestamp.valueOf("2030-01-01 00:00:00");

        ArrayList<DeliveryBill> listResult = DeliveryBillDAO.getInstance().filter(option, content, fromDate, toDate, fromPrice, toPrice);

        return listResult;
    }

    private Timestamp getFromTime() {
        Date fromDate = fromDateChooser.getDate();

        Calendar fromCal = Calendar.getInstance();

        fromCal.setTime(fromDate);

        fromCal.set(Calendar.HOUR_OF_DAY, 0);
        fromCal.set(Calendar.MINUTE, 0);
        fromCal.set(Calendar.SECOND, 0);
        fromCal.set(Calendar.MILLISECOND, 0);

        return new Timestamp(fromCal.getTimeInMillis());
    }

    private Timestamp getToTime() {
        Date toDate = toDateChooser.getDate();

        Calendar toCal = Calendar.getInstance();

        toCal.setTime(toDate);

        toCal.set(Calendar.HOUR_OF_DAY, 0);
        toCal.set(Calendar.MINUTE, 0);
        toCal.set(Calendar.SECOND, 0);
        toCal.set(Calendar.MILLISECOND, 0);

        return new Timestamp(toCal.getTimeInMillis());
    }

    private void searchTxtKeyReleased(KeyEvent e) {
        ArrayList<DeliveryBill> listDeliveryBill = filter();
        if (listDeliveryBill != null) loadDataToTable(listDeliveryBill);
    }

    private void refreshButtonActionPerformed(ActionEvent e) {
        optionComboBox.setSelectedItem("Tất cả");
        searchTxt.setText("");
        fromDateChooser.setDate(null);
        toDateChooser.setDate(null);
        fromPriceTxt.setText("");
        toPriceTxt.setText("");
        loadDataToTable(DeliveryBillDAO.getInstance().selectAll());
    }

    private void fromDateChooserPropertyChange(PropertyChangeEvent e) {
        if (fromDateChooser.getDate() != null) {
            ArrayList<DeliveryBill> listDeliveryBill = filter();
            if (listDeliveryBill != null) loadDataToTable(listDeliveryBill);
        }
    }

    private void fromDateChooserKeyReleased(KeyEvent e) {
        ArrayList<DeliveryBill> listDeliveryBill = filter();
        if (listDeliveryBill != null) loadDataToTable(listDeliveryBill);
    }

    private void toDateChooserPropertyChange(PropertyChangeEvent e) {
        if (toDateChooser.getDate() != null) {
            ArrayList<DeliveryBill> listDeliveryBill = filter();
            if (listDeliveryBill != null) loadDataToTable(listDeliveryBill);
        }
    }

    private void toDateChooserKeyReleased(KeyEvent e) {
        ArrayList<DeliveryBill> listDeliveryBill = filter();
        if (listDeliveryBill != null) loadDataToTable(listDeliveryBill);
    }



    private void fromPriceTxtKeyReleased(KeyEvent e) {
        ArrayList<DeliveryBill> listDeliveryBill = filter();
        if (listDeliveryBill != null) loadDataToTable(listDeliveryBill);
    }

    private void toPriceTxtKeyReleased(KeyEvent e) {
        ArrayList<DeliveryBill> listDeliveryBill = filter();
        if (listDeliveryBill != null) loadDataToTable(listDeliveryBill);

    }
}
