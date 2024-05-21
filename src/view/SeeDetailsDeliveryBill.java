package view;

import com.formdev.flatlaf.ui.FlatLineBorder;
import controller.WritePDF;
import dao.*;
import model.BillDetails;
import model.DeliveryBill;
import model.ReceiptBill;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
public class SeeDetailsDeliveryBill extends JDialog {
    private DeliveryBillForm owner;
    private StatisticForm owner2;
    private JPanel mainPanel;
    private JPanel titlePanel;
    private JLabel titleLabel;
    private JLabel billCodeLabel;
    private JLabel inforBillCodeLabel;
    private JLabel supplierLabel;
    private JLabel inforSupplierLabel;
    private JLabel creatorLabel;
    private JLabel inforCreatorLabel;
    private JLabel creationTimeLabel;
    private JLabel inforCreationTimeLabel;
    private JLabel totalAmountTextLabel;
    private JLabel totalAmountLabel;
    private JButton outputPDFButton;
    private JTable contentTable;
    private JScrollPane scrollPane;

    private DefaultTableModel defaultTableModel = new DefaultTableModel();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY HH:mm");
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    private Font fontHeader = new Font("SF Pro Display", 1, 16);
    private Font fontDefault = new Font("SF Pro Display", 0, 13);

    private String billCode;
    private DeliveryBill deliveryBill;

    public SeeDetailsDeliveryBill(DeliveryBillForm parent, JFrame owner, boolean modal) {
        super(owner, modal);
        this.owner = (DeliveryBillForm) parent;
        this.billCode = this.owner.getContentTable().getValueAt(this.owner.getContentTable().getSelectedRow(), 1).toString();
        this.deliveryBill = DeliveryBillDAO.getInstance().selectById(this.billCode);
        initComponents();
        initTable();
        loadDataToTable(DeliveryBillDetailsDAO.getInstance().selectBy(billCode));
    }

    public SeeDetailsDeliveryBill(StatisticForm parent, JFrame owner, boolean modal) {
        super(owner, modal);
        this.owner2 = (StatisticForm) parent;
        this.billCode = this.owner2.getBillTable().getValueAt(this.owner2.getBillTable().getSelectedRow(), 1).toString();
        this.deliveryBill = DeliveryBillDAO.getInstance().selectById(this.billCode);

        initComponents();
        initTable();
        loadDataToTable( DeliveryBillDetailsDAO.getInstance().selectBy(billCode));
    }

    private void initTable() {
        defaultTableModel = new DefaultTableModel();
        defaultTableModel.setColumnIdentifiers(new Object[] {"STT", "Mã máy", "Tên máy", "Số lượng", "Đơn giá", "Thành tiền"});
        contentTable.setModel(defaultTableModel);
        contentTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        contentTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        contentTable.getColumnModel().getColumn(2).setPreferredWidth(300);
        contentTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        contentTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        contentTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        contentTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
    }

    private void loadDataToTable(ArrayList<BillDetails> listBillDetails) {
        defaultTableModel.setRowCount(0);
        for (BillDetails billDetails : listBillDetails) {
            defaultTableModel.addRow(new Object[]{contentTable.getRowCount() + 1, billDetails.getMachineCode(),
                    ComputerDAO.getInstance().selectById(billDetails.getMachineCode()).getMachineName(), billDetails.getQuantity(),
                    decimalFormat.format(billDetails.getUnitPrice()) + "đ", decimalFormat.format(billDetails.getQuantity() * billDetails.getUnitPrice()) + "đ"});
        }
    }

    private void initComponents() {
        this.setSize(550, 400);
        this.setLocationRelativeTo(null);

        titleLabel = new JLabel("CHI TIẾT PHIẾU XUẤT");
        titleLabel.setBackground(new Color(13, 39, 51));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(fontHeader);

        titlePanel = new JPanel();
        titlePanel.setBackground(new Color(13, 39, 51));

        GroupLayout titleLayout = new GroupLayout(titlePanel);
        titlePanel.setLayout(titleLayout);

        titleLayout.setHorizontalGroup(
                titleLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(titleLayout.createSequentialGroup()
                                .addGap(320, 320, 320)
                                .addComponent(titleLabel)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        titleLayout.setVerticalGroup(
                titleLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(titleLayout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(titleLabel)
                                .addContainerGap(15, Short.MAX_VALUE))
        );

        billCodeLabel = new JLabel("Mã Phiếu");
        billCodeLabel.setFont(fontDefault);

        supplierLabel = new JLabel("Nhà cung cấp");
        supplierLabel.setFont(fontDefault);
        supplierLabel.setVisible(false);

        creatorLabel = new JLabel("Người tạo");
        creatorLabel.setFont(fontDefault);

        creationTimeLabel = new JLabel("Thời gian tạo");
        creationTimeLabel.setFont(fontDefault);

        totalAmountTextLabel = new JLabel("TỔNG TIỀN");
        totalAmountTextLabel.setFont(new Font("SF Pro Display", 1, 16));

        totalAmountLabel = new JLabel(decimalFormat.format(deliveryBill.getTotalAmount()) + "đ");
        totalAmountLabel.setFont(new Font("SF Pro Display", 1, 16));

        inforBillCodeLabel = new JLabel(billCode);
        inforBillCodeLabel.setFont(fontDefault);

        inforSupplierLabel = new JLabel();
        inforSupplierLabel.setFont(fontDefault);
        inforSupplierLabel.setVisible(false);

        inforCreatorLabel = new JLabel(deliveryBill.getCreator());
        inforCreatorLabel.setFont(fontDefault);

        inforCreationTimeLabel = new JLabel(simpleDateFormat.format(deliveryBill.getCreationTime()));
        inforCreationTimeLabel.setFont(fontDefault);

        contentTable = new JTable();
        contentTable.setModel(new DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "STT", "Mã máy", "Tên máy", "Số lượng", "Đơn giá", "Thành tiền"
                }

        ));

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(contentTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBackground(Color.WHITE);

        JTableHeader header = contentTable.getTableHeader();
        header.setFont(fontDefault);
        header.setBackground(Color.WHITE);

        // Customizing the scroll bars
        JScrollBar verticalScrollBar = new JScrollBar(JScrollBar.VERTICAL);
        verticalScrollBar.setPreferredSize(new Dimension(8, 0));
        verticalScrollBar.setBackground(Color.WHITE);

        verticalScrollBar.setUnitIncrement(60);  // Increase unit increment
        verticalScrollBar.setBlockIncrement(60);

        scrollPane.setVerticalScrollBar(verticalScrollBar);

        outputPDFButton = new JButton("Xuất PDF", getImageIcon("/icon/inputExcelButton.png"));
        outputPDFButton.setBounds(460, 30, 105, 40);
        outputPDFButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        outputPDFButton.setBackground(Color.WHITE);
        outputPDFButton.setBorder(new LineBorder(Color.WHITE));
        outputPDFButton.setFont(fontDefault);
        outputPDFButton.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY, 1, 5));
        outputPDFButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputPDFButtonActionPerformed(e);
            }
        });

        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        javax.swing.GroupLayout mainLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainLayout);
        mainLayout.setHorizontalGroup(
                mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(titlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(mainLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainLayout.createSequentialGroup()
                                                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(creatorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(billCodeLabel, 104, 104, 104))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(inforBillCodeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(inforCreatorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(43, 43, 43)
                                                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(creationTimeLabel, 78, 78, 78)
                                                        .addComponent(supplierLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(inforSupplierLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(inforCreationTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addContainerGap(65, Short.MAX_VALUE))
                                        .addGroup(mainLayout.createSequentialGroup()
                                                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(mainLayout.createSequentialGroup()
                                                                .addComponent(totalAmountTextLabel, 120, 120, 120)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(totalAmountLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(outputPDFButton, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(scrollPane))
                                                .addGap(21, 21, 21))))
        );
        mainLayout.setVerticalGroup(
                mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainLayout.createSequentialGroup()
                                .addComponent(titlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(billCodeLabel)
                                        .addComponent(supplierLabel)
                                        .addComponent(inforBillCodeLabel)
                                        .addComponent(inforSupplierLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(creatorLabel)
                                        .addComponent(creationTimeLabel)
                                        .addComponent(inforCreationTimeLabel)
                                        .addComponent(inforCreatorLabel))
                                .addGap(31, 31, 31)
                                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                                .addGap(28, 28, 28)
                                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(totalAmountTextLabel)
                                        .addComponent(totalAmountLabel)
                                        .addComponent(outputPDFButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(26, 26, 26))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    private ImageIcon getImageIcon(String pathIcon) {
        int widthIcon = 40, heighIcon = 40;
        URL urlIcon = SeeDetailsDeliveryBill.class.getResource(pathIcon);
        Image imgUser = Toolkit.getDefaultToolkit().createImage(urlIcon).getScaledInstance(widthIcon, heighIcon, Image.SCALE_SMOOTH);
        return  new ImageIcon(imgUser);
    }


    private void outputPDFButtonActionPerformed(ActionEvent event) {
        WritePDF writePDF = new WritePDF();
        writePDF.writeDeliveryBill(billCode);
    }
}
