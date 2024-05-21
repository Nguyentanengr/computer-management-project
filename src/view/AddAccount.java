package view;

import controller.BCrypt;
import dao.AccountDAO;
import model.Account;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowFields;

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

public class AddAccount extends JDialog {
    private AccountForm owner;

    private JPanel mainPanel;
    private JPanel titlePanel;
    private JLabel titleLabel;
    private JLabel accountNameLabel;
    private JLabel messageAccountNameLabel;
    private JLabel usernameLabel;
    private JLabel messageUsernameLabel;
    private JLabel emailLabel;
    private JLabel messageEmailLabel;
    private JLabel passwordLabel;
    private JLabel messagePasswordLabel;
    private JLabel roleLabel;
    private JTextField accountNameTxt;
    private JTextField usernameTxt;
    private JTextField emailTxt;
    private JTextField passwordTxt;
    private JComboBox optionComboBox;
    private JButton addButton;
    private JButton cancelButton;

    private Font fontDefault = new Font("SF Pro Display", 0, 16);
    private Font fontTitle = new Font("SF Pro Display", 1, 24);

    public AddAccount(AccountForm parent, JFrame owner, boolean modal) {
        super(owner, modal);
        this.owner = parent;
        initComponents();
        this.setSize(400, 620);
        this.setLocationRelativeTo(null);
    }

    public void initComponents() {
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("Thêm tài khoản mới");

        titleLabel = new JLabel("THÊM TÀI KHOẢN");
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
                                .addGap(88, 88, 88)
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

        accountNameTxt = new JTextField();
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

        usernameTxt = new JTextField();
        usernameTxt.setFont(fontDefault);
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

        emailTxt = new JTextField();
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

        passwordLabel = new JLabel("Mật khẩu");
        passwordLabel.setFont(fontDefault);
        passwordLabel.setBounds(40, 340, 68, 24);

        passwordTxt = new JTextField();
        passwordTxt.setFont(fontDefault);
        passwordTxt.setBounds(40, 370, 298, 38);
        passwordTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                passwordTxtKeyReleased(e);
            }
        });

        doc = (PlainDocument) passwordTxt.getDocument();
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

        messagePasswordLabel = new JLabel("Trống thông tin!");
        messagePasswordLabel.setFont(new Font("SF Pro Display", 0, 10));
        messagePasswordLabel.setForeground(Color.RED);
        messagePasswordLabel.setBounds(40, 410, 298, 10);
        messagePasswordLabel.setVisible(false);

        roleLabel = new JLabel("Vai trò");
        roleLabel.setFont(fontDefault);
        roleLabel.setBounds(40, 420, 50, 24);

        optionComboBox = new JComboBox<>();
        optionComboBox.setBackground(Color.WHITE);
        optionComboBox.setBorder(new LineBorder(Color.WHITE));
        optionComboBox.setFont(fontDefault);
        optionComboBox.setModel(new DefaultComboBoxModel<>(new String[] {"Quản lý kho", "Nhân viên nhập", "Nhân viên xuất"}));
        optionComboBox.setBounds(40, 450, 298, 40);

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
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordTxt);
        mainPanel.add(messagePasswordLabel);
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
        if (isExist){
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

    public void passwordTxtKeyReleased(KeyEvent event) {
        String text = passwordTxt.getText();
        if (text.length() < 6 && !text.isEmpty()) {
            messagePasswordLabel.setText("Mật khẩu tối thiểu 6 kí tự!");
            messagePasswordLabel.setVisible(true);
        } else {
            messagePasswordLabel.setText("Trống thông tin!");
            messagePasswordLabel.setVisible(false);
        }
    }

    public void addButtonActionPerformed(ActionEvent e) {
        String accountName = accountNameTxt.getText();
        String username = usernameTxt.getText();
        String email = emailTxt.getText();
        String password = passwordTxt.getText();
        String role = optionComboBox.getSelectedItem().toString();

        if (accountName.isEmpty()) messageAccountNameLabel.setVisible(true);
        else if (username.isEmpty()) messageUsernameLabel.setVisible(true);
        else if (email.isEmpty()) messageEmailLabel.setVisible(true);
        else if (password.isEmpty()) messagePasswordLabel.setVisible(true);
        else if (!AccountDAO.getInstance().isExistUsername(username) &&
                email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$") &&
                !AccountDAO.getInstance().isExistEmail(email) &&
                password.matches("^(?=.*\\S).{6,}$")) {

            Account account = new Account(accountName, username, BCrypt.hashpw(password, BCrypt.gensalt(12)), role, 1, email);
            AccountDAO.getInstance().insert(account);

            JOptionPane.showMessageDialog(this, "Thêm thành công!");
            this.dispose();
            this.owner.loadDataToTable(AccountDAO.getInstance().selectAll());
        }
    }

    public void cancelButtonActionPerformed(ActionEvent e) {
        this.dispose();
    }
}
