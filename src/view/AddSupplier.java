package view;

import dao.SupplierDAO;
import model.Supplier;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class AddSupplier extends JDialog {
    protected SupplierForm owner;
    protected JPanel mainPanel;
    protected JPanel headerPanel;
    protected JLabel headerLabel;
    protected JLabel supplierCodeLabel;
    protected JLabel supplierNameLabel;
    protected JLabel phoneLabel;
    protected JLabel addressLabel;
    protected JTextField supplierCodeTxt;
    protected JTextField supplierNameTxt;
    protected JTextField phoneTxt;
    protected JTextField addressTxt;
    protected JButton addButton;
    protected JButton cancelButton;

    protected Font font = new Font("SF Pro Display", 0, 16);

    public AddSupplier(JInternalFrame parent, JFrame owner, boolean modal) {
        super(owner, modal);
        this.owner = (SupplierForm) parent;
        initComponent();
        this.setSize(400, 530);
        this.setLocationRelativeTo(null);
    }

    public void initComponent() {
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("Thêm nhà cung cấp mới");

        headerLabel = new JLabel("THÊM NHÀ CUNG CẤP");
        headerLabel.setFont(new Font("SF Pro Display", 1, 24));
        headerLabel.setForeground(Color.WHITE);

        headerPanel = new JPanel();
        headerPanel.setBackground(new Color(13, 39, 51));
        headerPanel.setBounds(0, 0, 390, 70);

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
                headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPanelLayout.createSequentialGroup()
                                .addContainerGap(65, Short.MAX_VALUE)
                                .addComponent(headerLabel)
                                .addGap(74, 74, 74))
        );
        headerPanelLayout.setVerticalGroup(
                headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPanelLayout.createSequentialGroup()
                                .addContainerGap(24, Short.MAX_VALUE)
                                .addComponent(headerLabel)
                                .addGap(17, 17, 17))
        );

        supplierCodeLabel = new JLabel("Mã nhà cung cấp");
        supplierCodeLabel.setFont(font);
        supplierCodeLabel.setBounds(40, 100, 150, 24);

        supplierCodeTxt = new JTextField();
        supplierCodeTxt.setFont(font);
        supplierCodeTxt.setBounds(40, 130, 298, 38);

        supplierNameLabel = new JLabel("Tên nhà cung cấp");
        supplierNameLabel.setFont(font);
        supplierNameLabel.setBounds(40, 180, 150, 24);

        supplierNameTxt = new JTextField();
        supplierNameTxt.setFont(font);
        supplierNameTxt.setBounds(40, 210, 298, 38);

        phoneLabel = new JLabel("Số điện thoại");
        phoneLabel.setFont(font);
        phoneLabel.setBounds(40, 260, 120, 24);

        phoneTxt = new JTextField();
        phoneTxt.setFont(font);
        PlainDocument doc = (PlainDocument) phoneTxt.getDocument();
        doc.setDocumentFilter(new DocumentFilter(){
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (isNumeric(text)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (isNumeric(string)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            public boolean isNumeric(String text) {
                return text.matches("[0-9]*");
            }
        });

        phoneTxt.setDocument(doc);
        phoneTxt.setBounds(40, 290, 298, 38);

        addressLabel = new JLabel("Địa chỉ");
        addressLabel.setFont(font);
        addressLabel.setBounds(40, 340, 120, 24);

        addressTxt = new JTextField();
        addressTxt.setFont(font);
        addressTxt.setBounds(40, 370, 298, 38);

        addButton = new JButton("Thêm");
        addButton.setBackground(new Color(13, 39, 51));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(font);
        addButton.setBorder(null);
        addButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        addButton.setBounds(40, 430, 140, 38);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addButtonActionPerformed(e);
            }
        });

        cancelButton = new JButton("Hủy");
        cancelButton.setBackground(Color.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(font);
        cancelButton.setBorder(null);
        cancelButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cancelButton.setBounds(200, 430, 140, 38);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonActionPerformed(e);
            }
        });

        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(null);
        mainPanel.add(headerPanel);
        mainPanel.add(supplierCodeLabel);
        mainPanel.add(supplierCodeTxt);
        mainPanel.add(supplierNameLabel);
        mainPanel.add(supplierNameTxt);
        mainPanel.add(phoneLabel);
        mainPanel.add(phoneTxt);
        mainPanel.add(addressLabel);
        mainPanel.add(addressTxt);
        mainPanel.add(addButton);
        mainPanel.add(cancelButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }

    public void addButtonActionPerformed(ActionEvent e) {
        String supplierCode = this.supplierCodeTxt.getText();
        String supplierName = this.supplierNameTxt.getText();
        String phone = this.phoneTxt.getText();
        String address = this.addressTxt.getText();

        if (supplierCode.equals("") || supplierName.equals("") || phone.equals("") || address.equals("")) {
            JOptionPane.showMessageDialog(this, "Không để thông tin rỗng");
        }
        else if (phone.length() < 8 || phone.charAt(0) != '0'){
            JOptionPane.showMessageDialog(this, "Số điện thoại chưa đúng định dạng");
        }
        else {
            try {
                Supplier supplier = new Supplier(supplierCode, supplierName, phone, address);
                if (SupplierDAO.getInstance().insert(supplier) > 0) {
                    JOptionPane.showMessageDialog(this, "Thêm thành công");
                    this.dispose();
                    owner.loadDataToTable(SupplierDAO.getInstance().selectAll());
                }
                else throw new Exception();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Thêm không thành công");
                ex.printStackTrace();
            }
        }
    }

    public void cancelButtonActionPerformed(ActionEvent e) {
        this.dispose();
    }
}
