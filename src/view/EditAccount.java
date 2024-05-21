package view;

import controller.BCrypt;
import dao.AccountDAO;
import model.Account;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EditAccount extends JDialog {
    private AccountForm owner;
    private Account account;

    private JPanel mainPanel;
    private JPanel titlePanel;
    private JLabel titleLabel;
    private JLabel accountNameLabel;
    private JLabel messageAccountNameLabel;
    private JLabel usernameLabel;
    private JLabel messageUsernameLabel;
    private JLabel emailLabel;
    private JLabel messageEmailLabel;
    private JLabel statusLabel;
    private JLabel roleLabel;
    private JTextField accountNameTxt;
    private JTextField usernameTxt;
    private JTextField emailTxt;
    private JComboBox optionComboBox;
    private JComboBox statusComboBox;
    private JButton addButton;
    private JButton cancelButton;

    private Font fontDefault = new Font("SF Pro Display", 0, 16);
    private Font fontTitle = new Font("SF Pro Display", 1, 24);

    public EditAccount(AccountForm parent, JFrame owner, boolean modal) {
        super(owner, modal);
        this.owner = parent;
        String username = this.owner.getContentTable().getValueAt(this.owner.getContentTable().getSelectedRow(), 1).toString();
        this.account = AccountDAO.getInstance().selectById(username);
        initComponents();
        this.setSize(400, 620);
        this.setLocationRelativeTo(null);
    }

    public void initComponents() {
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("Thêm tài khoản mới");

        titleLabel = new JLabel("CẬP NHẬT THÔNG TIN");
        titleLabel.setBackground(new Color(13, 39, 51));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(fontTitle);

        titlePanel = new JPanel();
        titlePanel.setBackground(new Color(13, 39, 51));
        titlePanel.setBounds(0, 0, 390, 70);
        GroupLayout titleLayout = new GroupLayout(titlePanel);
        titlePanel.setLayout(titleLayout);

        titleLayout.setHorizontalGroup(
                titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(titleLayout.createSequentialGroup()
                                .addGap(65, 65, 65)
                                .addComponent(titleLabel, 300, 300, 300)
                                .addContainerGap(102, Short.MAX_VALUE))
        );
        titleLayout.setVerticalGroup(
                titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, titleLayout.createSequentialGroup()
                                .addContainerGap(21, Short.MAX_VALUE)
                                .addComponent(titleLabel)
                                .addGap(20, 20, 20))
        );

        accountNameLabel = new JLabel("Tên tài khoản");
        accountNameLabel.setFont(fontDefault);
        accountNameLabel.setBounds(40, 100, 120, 24);

        accountNameTxt = new JTextField(account.getAccountName());
        accountNameTxt.setFont(fontDefault);
        accountNameTxt.setBounds(40, 130, 298, 38);
        accountNameTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                accountNameTxtKeyReleased(e);
            }
        });

        messageAccountNameLabel = new JLabel("Trống thông tin!");
        messageAccountNameLabel.setFont(new Font("SF Pro Display", 0, 10));
        messageAccountNameLabel.setForeground(Color.RED);
        messageAccountNameLabel.setBounds(40, 170, 298, 10);
        messageAccountNameLabel.setVisible(false);

        usernameLabel = new JLabel("Tên đăng nhập");
        usernameLabel.setFont(fontDefault);
        usernameLabel.setBounds(40, 180, 120, 24);

        usernameTxt = new JTextField(account.getUsername());
        usernameTxt.setFont(fontDefault);
        usernameTxt.setEditable(false);
        usernameTxt.setRequestFocusEnabled(false);
        usernameTxt.setBounds(40, 210, 298, 38);
        usernameTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                usernameTxtKeyReleased(e);
            }
        });

        PlainDocument doc = (PlainDocument) usernameTxt.getDocument();
        doc.setDocumentFilter(new DocumentFilter(){
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (isValid(text)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (isValid(string)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            public boolean isValid(String text) {
                return text.matches("\\S+");
            }
        });

        messageUsernameLabel = new JLabel("Trống thông tin!");
        messageUsernameLabel.setFont(new Font("SF Pro Display", 0, 10));
        messageUsernameLabel.setForeground(Color.RED);
        messageUsernameLabel.setBounds(40, 250, 298, 10);
        messageUsernameLabel.setVisible(false);

        emailLabel = new JLabel("Email");
        emailLabel.setFont(fontDefault);
        emailLabel.setBounds(40, 260, 50, 24);

        emailTxt = new JTextField(account.getEmail());
        emailTxt.setFont(fontDefault);
        emailTxt.setBounds(40, 290, 300, 38);
        emailTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                emailTxtKeyReleased(e);
            }
        });

        doc = (PlainDocument) emailTxt.getDocument();
        doc.setDocumentFilter(new DocumentFilter(){
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (isValid(text)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (isValid(string)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            public boolean isValid(String text) {
                return text.matches("\\S+");
            }
        });

        messageEmailLabel = new JLabel("Trống thông tin!");
        messageEmailLabel.setFont(new Font("SF Pro Display", 0, 10));
        messageEmailLabel.setForeground(Color.RED);
        messageEmailLabel.setBounds(40, 330, 298, 10);
        messageEmailLabel.setVisible(false);

        roleLabel = new JLabel("Vai trò");
        roleLabel.setFont(fontDefault);
        roleLabel.setBounds(40, 340, 68, 24);

        optionComboBox = new JComboBox<>();
        optionComboBox.setBackground(Color.WHITE);
        optionComboBox.setBorder(new LineBorder(Color.WHITE));
        optionComboBox.setFont(fontDefault);
        optionComboBox.setModel(new DefaultComboBoxModel<>(new String[] {"Quản lý kho", "Nhân viên nhập", "Nhân viên xuất"}));
        optionComboBox.setSelectedItem(account.getRole());
        optionComboBox.setBounds(40, 370, 298, 40);

        statusLabel = new JLabel("Trạng thái");
        statusLabel.setFont(fontDefault);
        statusLabel.setBounds(40, 420, 120, 24);

        String status = "";
        if (account.getStatus() == 1) status = "Hoạt động";
        else status = "Bị khóa";

        statusComboBox = new JComboBox<>();
        statusComboBox.setBackground(Color.WHITE);
        statusComboBox.setBorder(new LineBorder(Color.WHITE));
        statusComboBox.setFont(fontDefault);
        statusComboBox.setModel(new DefaultComboBoxModel<>(new String[] {"Bị khóa", "Hoạt động"}));
        statusComboBox.setSelectedItem(status);
        statusComboBox.setBounds(40, 450, 298, 40);

        addButton = new JButton("Thêm");
        addButton.setForeground(Color.WHITE);
        addButton.setFont(fontDefault);
        addButton.setBackground(new Color(13, 39, 51));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.setBounds(40, 510, 140, 38);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addButtonActionPerformed(e);
            }
        });

        cancelButton = new JButton("Hủy");
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setFont(fontDefault);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setBounds(200, 510, 140, 38);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonActionPerformed(e);
            }
        });

        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(null);

        mainPanel.add(titlePanel);
        mainPanel.add(accountNameLabel);
        mainPanel.add(accountNameTxt);
        mainPanel.add(messageAccountNameLabel);
        mainPanel.add(usernameLabel);
        mainPanel.add(usernameTxt);
        mainPanel.add(messageUsernameLabel);
        mainPanel.add(emailLabel);
        mainPanel.add(emailTxt);
        mainPanel.add(messageEmailLabel);
        mainPanel.add(statusLabel);
        mainPanel.add(statusComboBox);
        mainPanel.add(roleLabel);
        mainPanel.add(optionComboBox);
        mainPanel.add(addButton);
        mainPanel.add(cancelButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, 400, 400, 400)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, 620, 620, 620)
        );

        pack();
    }

    public void accountNameTxtKeyReleased(KeyEvent event) {
        messageAccountNameLabel.setVisible(false);
    }

    public void usernameTxtKeyReleased(KeyEvent event) {
        String username = usernameTxt.getText();
        boolean isExist = AccountDAO.getInstance().isExistUsername(username);
        if (isExist && !username.equals(account.getUsername())){
            messageUsernameLabel.setText("Tên đăng nhập đã tồn tại!");
            messageUsernameLabel.setVisible(true);
        }
        else {
            messageUsernameLabel.setText("Trống thông tin!");
            messageUsernameLabel.setVisible(false);
        }
    }

    public void emailTxtKeyReleased(KeyEvent event) {
        String text = emailTxt.getText();
        System.out.println(text);
        if (!text.endsWith("@gmail.com") && !text.isEmpty()) {
            System.out.println(1);
            messageEmailLabel.setText("Email chưa đúng định dạng!");
            messageEmailLabel.setVisible(true);
        } else if (text.isEmpty()){
            messageEmailLabel.setText("Trống thông tin!");
            messageEmailLabel.setVisible(false);
        } else if (AccountDAO.getInstance().isExistEmail(text)) {
            messageEmailLabel.setText("Email đã tồn tại trong hệ thống!");
            messageEmailLabel.setVisible(true);
        }
    }


    public void addButtonActionPerformed(ActionEvent e) {
        String accountName = accountNameTxt.getText();
        String username = usernameTxt.getText();
        String email = emailTxt.getText();
        String role = optionComboBox.getSelectedItem().toString();
        int status = statusComboBox.getSelectedItem().toString().equals("Bị khóa") ? 0 : 1;

        if (accountName.isEmpty()) messageAccountNameLabel.setVisible(true);
        else if (username.isEmpty()) messageUsernameLabel.setVisible(true);
        else if (email.isEmpty()) messageEmailLabel.setVisible(true);
        else if ((username.equals(account.getUsername()) || !AccountDAO.getInstance().isExistUsername(username)) &&
                email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$") &&
                !AccountDAO.getInstance().isExistEmail(email)) {

            account.setAccountName(accountName);
            account.setEmail(email);
            account.setRole(role);
            account.setStatus(status);

            AccountDAO.getInstance().update(account);

            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            this.dispose();
            this.owner.loadDataToTable(AccountDAO.getInstance().selectAll());
        }
    }

    public void cancelButtonActionPerformed(ActionEvent e) {
        this.dispose();
    }
}
