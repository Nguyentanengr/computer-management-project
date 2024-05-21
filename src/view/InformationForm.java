package view;

import com.formdev.flatlaf.ui.FlatLineBorder;
import controller.BCrypt;
import dao.AccountDAO;
import model.Account;

import javax.imageio.plugins.tiff.TIFFTag;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InformationForm extends JDialog {

    private Account account;

    private JPanel mainPanel;
    private JPanel titlePanel;
    private JPanel inforPanel;
    private JPanel passwordPanel;
    private JTabbedPane tabbedPane;
    private JLabel titleLabel;
    private JLabel accountNameLabel;
    private JLabel emailLabel;
    private JLabel usernameLabel;
    private JLabel errorAccountNameLabel;
    private JLabel errorEmailLabel;
    private JLabel errorUsernameLabel;
    private JLabel oldPasswordLabel;
    private JLabel newPasswordLabel;
    private JLabel rewritePasswordLabel;
    private JLabel errorOldPasswordLabel;
    private JLabel errorNewPasswordLabel;
    private JLabel errorRewritePasswordLabel;
    private JTextField accountNameTxt;
    private JTextField emailTxt;
    private JTextField usernameTxt;
    private JPasswordField oldPasswordTxt;
    private JPasswordField newPasswordTxt;
    private JPasswordField rewritePasswordTxt;
    private JButton saveInforButton;
    private JButton savePasswordButton;

    private Font headerFont = new Font("SF Pro Display", 1, 24);
    private Font defaultFont = new Font("SF Pro Display", 0, 13);

    public InformationForm(Account account, Frame owner, boolean modal) {
        super(owner, modal);
        this.account = account;
        initComponents();
        this.setSize(415, 515);

        setLocationRelativeTo(null);
    }

    public void initComponents() {

        titleLabel = new JLabel("THAY ĐỔI THÔNG TIN");
        titleLabel.setBackground(new Color(13, 39, 51));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(headerFont);

        titlePanel = new JPanel();
        titlePanel.setBackground(new Color(13, 39, 51));

        GroupLayout titleLayout = new GroupLayout(titlePanel);
        titlePanel.setLayout(titleLayout);

        titleLayout.setHorizontalGroup(
                titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(titleLayout.createSequentialGroup()
                                .addGap(66, 66, 66)
                                .addComponent(titleLabel, 300, 300, 300)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        titleLayout.setVerticalGroup(
                titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(titleLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(titleLabel)
                                .addContainerGap(27, Short.MAX_VALUE))
        );

        accountNameLabel = new JLabel("Tên tài khoản");
        accountNameLabel.setFont(defaultFont);

        usernameLabel = new JLabel("Tên đăng nhập");
        usernameLabel.setFont(defaultFont);

        emailLabel = new JLabel("Email");
        emailLabel.setFont(defaultFont);

        accountNameTxt = new JTextField(account.getAccountName());
        accountNameTxt.setFont(defaultFont);
        accountNameTxt.setBorder(new FlatLineBorder(new Insets(0, 10, 0, 0), Color.LIGHT_GRAY));
        accountNameTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                accountNameTxtKeyReleased(e);
            }
        });
        usernameTxt = new JTextField(account.getUsername());
        usernameTxt.setFont(defaultFont);
        usernameTxt.setEditable(false);
        usernameTxt.setRequestFocusEnabled(false);
        usernameTxt.setBorder(new FlatLineBorder(new Insets(0, 10, 0, 0), Color.LIGHT_GRAY));

        emailTxt = new JTextField(account.getEmail());
        emailTxt.setFont(defaultFont);
        emailTxt.setBorder(new FlatLineBorder(new Insets(0, 10, 0, 0), Color.LIGHT_GRAY));
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

        emailTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                emailTxtKeyReleased(e);
            }
        });

        errorAccountNameLabel = new JLabel();
        errorAccountNameLabel.setFont(new Font("SF Pro Display", 0, 10));
        errorAccountNameLabel.setForeground(Color.RED);

        errorUsernameLabel = new JLabel();
        errorUsernameLabel.setFont(new Font("SF Pro Display", 0, 10));
        errorUsernameLabel.setForeground(Color.RED);

        errorEmailLabel = new JLabel();
        errorEmailLabel.setFont(new Font("SF Pro Display", 0, 10));
        errorEmailLabel.setForeground(Color.RED);

        saveInforButton = getButton("Lưu thay đổi");
        saveInforButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveInforActionPerformed(e);
            }
        });

        inforPanel = new JPanel();
        inforPanel.setBackground(Color.WHITE);

        GroupLayout inforLayout = new GroupLayout(inforPanel);
        inforPanel.setLayout(inforLayout);

        inforLayout.setHorizontalGroup(
                inforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(inforLayout.createSequentialGroup()
                                .addGroup(inforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(inforLayout.createSequentialGroup()
                                                .addGap(32, 32, 32)
                                                .addGroup(inforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(accountNameLabel, 150, 150, 150)
                                                        .addComponent(accountNameTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                                                        .addComponent(emailTxt)
                                                        .addComponent(usernameTxt)
                                                        .addComponent(errorAccountNameLabel)
                                                        .addComponent(errorUsernameLabel)
                                                        .addComponent(errorEmailLabel)
                                                        .addComponent(usernameLabel, 150, 150, 150)
                                                        .addComponent(emailLabel, 70, 70, 70)))

                                        .addGroup(inforLayout.createSequentialGroup()
                                                .addGap(105, 105, 105)
                                                .addComponent(saveInforButton, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(34, Short.MAX_VALUE))
        );
        inforLayout.setVerticalGroup(
                inforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(inforLayout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(accountNameLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(accountNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(errorAccountNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(usernameLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(usernameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(errorUsernameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(emailLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(emailTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(errorEmailLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(16, 16, 16)
                                .addComponent(saveInforButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(36, Short.MAX_VALUE))
        );

        oldPasswordLabel = new JLabel("Mật khẩu hiện tại");
        oldPasswordLabel.setFont(defaultFont);

        newPasswordLabel = new JLabel("Mật khẩu mới");
        newPasswordLabel.setFont(defaultFont);

        rewritePasswordLabel = new JLabel("Nhập lại mật khẩu mới");
        rewritePasswordLabel.setFont(defaultFont);

        oldPasswordTxt = new JPasswordField();
        oldPasswordTxt.setFont(defaultFont);
        oldPasswordTxt.setBorder(new FlatLineBorder(new Insets(0, 10, 0, 0), Color.LIGHT_GRAY));
        oldPasswordTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                oldPasswordTxtKeyReleased(e);
            }
        });
        newPasswordTxt = new JPasswordField();
        newPasswordTxt.setFont(defaultFont);
        newPasswordTxt.setBorder(new FlatLineBorder(new Insets(0, 10, 0, 0), Color.LIGHT_GRAY));
        newPasswordTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                newPasswordTxtKeyReleased(e);
            }
        });
        rewritePasswordTxt = new JPasswordField();
        rewritePasswordTxt.setFont(defaultFont);
        rewritePasswordTxt.setBorder(new FlatLineBorder(new Insets(0, 10, 0, 0), Color.LIGHT_GRAY));
        rewritePasswordTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                rewritePasswordTxtKeyReleased(e);
            }
        });
        errorOldPasswordLabel = new JLabel();
        errorOldPasswordLabel.setForeground(Color.RED);
        errorOldPasswordLabel.setFont(defaultFont);

        errorNewPasswordLabel= new JLabel();
        errorNewPasswordLabel.setForeground(Color.RED);
        errorNewPasswordLabel.setFont(defaultFont);

        errorRewritePasswordLabel = new JLabel();
        errorRewritePasswordLabel.setForeground(Color.RED);
        errorRewritePasswordLabel.setFont(defaultFont);

        savePasswordButton = getButton("Đổi mật khẩu");
        savePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePasswordActionPerformed(e);
            }
        });

        passwordPanel = new JPanel();
        passwordPanel.setBackground(Color.WHITE);

        GroupLayout passwordLayout = new GroupLayout(passwordPanel);
        passwordPanel.setLayout(passwordLayout);
        passwordLayout.setHorizontalGroup(
                passwordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(passwordLayout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addGroup(passwordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(savePasswordButton, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(passwordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(rewritePasswordLabel)
                                                .addComponent(newPasswordLabel)
                                                .addComponent(oldPasswordLabel)
                                                .addComponent(rewritePasswordTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                                                .addComponent(newPasswordTxt)
                                                .addComponent(oldPasswordTxt)
                                                .addComponent(errorOldPasswordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(errorNewPasswordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(errorRewritePasswordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addContainerGap(49, Short.MAX_VALUE))
        );
        passwordLayout.setVerticalGroup(
                passwordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(passwordLayout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(oldPasswordLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(oldPasswordTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(errorOldPasswordLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(newPasswordLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(newPasswordTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(errorNewPasswordLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rewritePasswordLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rewritePasswordTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(errorRewritePasswordLabel)
                                .addGap(29, 29, 29)
                                .addComponent(savePasswordButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(73, Short.MAX_VALUE))
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
        tabbedPane.addTab("Thông tin", inforPanel);
        tabbedPane.addTab("Mật khẩu", passwordPanel);

        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);

        GroupLayout mainLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(mainLayout);

        mainLayout.setHorizontalGroup(
                mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(titlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        mainLayout.setVerticalGroup(
                mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainLayout.createSequentialGroup()
                                .addComponent(titlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
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

        getAccessibleContext().setAccessibleName("Thông tin tài khoản");

        pack();

    }
    public JButton getButton(String title) {
        JButton designButton = new JButton(title);
        designButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        designButton.setBackground(Color.WHITE);
        designButton.setFont(defaultFont);
        designButton.setBorder(new FlatLineBorder(new Insets(0,0,0,0), Color.LIGHT_GRAY,1, 5));

        return designButton;
    }

    public void saveInforActionPerformed(ActionEvent event) {
        String accountName = accountNameTxt.getText();
        String username = usernameTxt.getText();
        String email = emailTxt.getText();

        if (accountName.isEmpty()) errorAccountNameLabel.setText("Trống thông tin!");
        else if (email.isEmpty())errorEmailLabel.setText("Trống thông tin!");
        else if ((username.equals(account.getUsername()) || !AccountDAO.getInstance().isExistUsername(username)) &&
                email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {

            account.setAccountName(accountName);
            account.setEmail(email);

            AccountDAO.getInstance().update(account);

            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        }
    }

    public void accountNameTxtKeyReleased(KeyEvent event) {
        errorAccountNameLabel.setText("");
    }


    public void emailTxtKeyReleased(KeyEvent event) {
        String text = emailTxt.getText();
        System.out.println(text);
        if (!text.endsWith("@gmail.com") && !text.isEmpty()) {
            System.out.println(1);
            errorEmailLabel.setText("Email chưa đúng định dạng!");
        } else {
            errorEmailLabel.setText("");
        }
    }

    public void oldPasswordTxtKeyReleased(KeyEvent event) {
        errorOldPasswordLabel.setText("");
    }
    public void newPasswordTxtKeyReleased(KeyEvent event) {
        errorRewritePasswordLabel.setText("");
    }
    public void rewritePasswordTxtKeyReleased(KeyEvent event) {
        errorRewritePasswordLabel.setText("");
    }

    public void savePasswordActionPerformed(ActionEvent event) {
        String newPassword = newPasswordTxt.getText();
        String rewritePassword = rewritePasswordTxt.getText();

        if (!rewritePassword.equals(newPassword)) errorRewritePasswordLabel.setText("Mật khẩu mới không khớp");
        else if (BCrypt.checkpw(oldPasswordTxt.getText(), account.getPassword()) && !newPassword.isEmpty()) {
            account.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));
            AccountDAO.getInstance().update(account);

            oldPasswordTxt.setText("");
            newPasswordTxt.setText("");
            rewritePasswordTxt.setText("");

            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công");
        } else if (!newPassword.isEmpty()) {
            errorOldPasswordLabel.setText("Mật khẩu không đúng");
        }
    }
}
