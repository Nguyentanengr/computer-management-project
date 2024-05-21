package view;

import com.formdev.flatlaf.ui.FlatLineBorder;
import controller.BCrypt;
import controller.Gmailer;
import dao.AccountDAO;
import dao.OTPDataDAO;
import model.Account;
import model.OTPdata;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;

public class RecoverPassword extends JDialog {

    private JPanel titlePanel;
    private JLabel titleLabel;

    private JPanel mainPanel;
    private JPanel emailAuthPanel;
    private JPanel emailCodePanel;
    private JPanel passwordChangePanel;

    private JLabel emailAuthLabel;
    private JLabel errorEmailAuthLabel;
    private JLabel inforEmailAuthLabel;
    private JLabel emailCodeLabel;
    private JLabel errorEmailCodeLabel;
    private JLabel passwordChangeLabel;
    private JLabel errorPasswordChangLabel;

    private JTextField emailAuthTxt;
    private JTextField emailCodeTxt;
    private JTextField passwordChangeTxt;

    private JButton emailCodeButton;
    private JButton emailAuthButton;
    private JButton passwordChangeButton;

    private Font headerFont = new Font("SF Pro Display", 1, 24);
    private Font defaultFont = new Font("SF Pro Display", 0, 12);
    private Font errorFont = new Font("SF Pro Display", 0, 11);

    public RecoverPassword(Frame parent, boolean modal) {
        super(parent, modal);

        initComponents();
        setLocationRelativeTo(null);
    }

    public void initComponents() {

        titleLabel = new JLabel("KHÔI PHỤC MẬT KHẨU");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBackground(new Color(13, 39, 51));
        titleLabel.setFont(headerFont);

        titlePanel = new JPanel();
        titlePanel.setBackground(new Color(13, 39, 51));

        GroupLayout titleLayout = new GroupLayout(titlePanel);
        titlePanel.setLayout(titleLayout);

        titleLayout.setHorizontalGroup(
                titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(titleLayout.createSequentialGroup()
                                .addGap(97, 97, 97)
                                .addComponent(titleLabel)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        titleLayout.setVerticalGroup(
                titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, titleLayout.createSequentialGroup()
                                .addContainerGap(18, Short.MAX_VALUE)
                                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(17, 17, 17))
        );

        emailAuthLabel = new JLabel("Nhập địa chỉ email khôi phục");
        emailAuthLabel.setFont(defaultFont);

        emailAuthTxt = new JTextField();
        emailAuthTxt.setFont(defaultFont);
        emailAuthTxt.setBorder(new FlatLineBorder(new Insets(0, 10, 0, 0), Color.LIGHT_GRAY));
        emailAuthTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                emailAuthKeyReleased(e);
            }
        });
        emailAuthButton = getButton("Gửi mã xác nhận");
        emailAuthButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emailAuthActionPerformed(e);
            }
        });

        errorEmailAuthLabel = new JLabel();
        errorEmailAuthLabel.setFont(errorFont);
        errorEmailAuthLabel.setForeground(Color.RED);

        emailAuthPanel = new JPanel();
        emailAuthPanel.setBackground(Color.WHITE);

        GroupLayout emailAuthLayout = new GroupLayout(emailAuthPanel);
        emailAuthPanel.setLayout(emailAuthLayout);
        emailAuthLayout.setHorizontalGroup(
                emailAuthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(emailAuthLayout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(emailAuthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(emailAuthLabel)
                                        .addComponent(errorEmailAuthLabel)
                                        .addGroup(emailAuthLayout.createSequentialGroup()
                                                .addComponent(emailAuthTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(emailAuthButton, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(23, Short.MAX_VALUE))
        );
        emailAuthLayout.setVerticalGroup(
                emailAuthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(emailAuthLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(emailAuthLabel)
                                .addGap(18, 18, 18)
                                .addGroup(emailAuthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(emailAuthButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(emailAuthTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(5, 5, 5)
                                .addComponent(errorEmailAuthLabel, 15, 15, 15)
                                .addGap(0, 23, Short.MAX_VALUE))
        );

        inforEmailAuthLabel = new JLabel("Mã xác nhận gồm 6 chữ số, có hiệu lực trong 5 phút.");
        inforEmailAuthLabel.setFont(defaultFont);

        emailCodeLabel = new JLabel("Nhập mã xác nhận");
        emailCodeLabel.setFont(defaultFont);

        errorEmailCodeLabel = new JLabel("");
        errorEmailCodeLabel.setFont(errorFont);
        errorEmailCodeLabel.setForeground(Color.RED);

        emailCodeTxt = new JTextField();
        emailCodeTxt.setFont(defaultFont);
        emailCodeTxt.setBorder(new FlatLineBorder(new Insets(0, 10, 0, 0), Color.LIGHT_GRAY));
        emailCodeTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                emailCodeKeyReleased(e);
            }
        });

        PlainDocument document = (PlainDocument) emailCodeTxt.getDocument();
        document.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (isNumeric(string)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
                if (isNumeric(string)) {
                    super.replace(fb, offset, length, string, attr);
                }
            }
            private boolean isNumeric(String string) {
                return string.matches("[0-9]*");
            }
        });

        emailCodeButton = getButton("Xác nhận");
        emailCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emailCodeAcionPerformed(e);
            }
        });

        emailCodePanel = new JPanel();
        emailCodePanel.setBackground(Color.WHITE);

        GroupLayout emailCodeLayout = new GroupLayout(emailCodePanel);
        emailCodePanel.setLayout(emailCodeLayout);

        emailCodeLayout.setHorizontalGroup(
                emailCodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(emailCodeLayout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(emailCodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(emailCodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(inforEmailAuthLabel)
                                                .addGroup(emailCodeLayout.createSequentialGroup()
                                                        .addComponent(emailCodeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(27, 27, 27)
                                                        .addComponent(emailCodeButton, 158, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addComponent(errorEmailCodeLabel)
                                        .addComponent(emailCodeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(25, Short.MAX_VALUE))
        );
        emailCodeLayout.setVerticalGroup(
                emailCodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(emailCodeLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(inforEmailAuthLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(emailCodeLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                                .addGroup(emailCodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(emailCodeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(emailCodeButton, 41, 41, 41))
                                .addGap(5, 5, 5)
                                .addComponent(errorEmailCodeLabel, 15, 15, 15)
                                .addGap(18, 18, 18))
        );

        passwordChangeLabel = new JLabel("Nhập mật khẩu mới");
        passwordChangeLabel.setFont(defaultFont);

        errorPasswordChangLabel = new JLabel("");
        errorPasswordChangLabel.setFont(errorFont);
        errorPasswordChangLabel.setForeground(Color.RED);

        passwordChangeTxt = new JTextField();
        passwordChangeTxt.setFont(defaultFont);
        passwordChangeTxt.setBorder(new FlatLineBorder(new Insets(0, 10, 0, 0), Color.LIGHT_GRAY));
        passwordChangeTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                passwordTxtKeyReleased(e);
            }
        });
        document = (PlainDocument) passwordChangeTxt.getDocument();
        document.setDocumentFilter(new DocumentFilter(){
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

        passwordChangeButton = getButton("Đổi mật khẩu");
        passwordChangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passwordChangeActionPerformed(e);
            }
        });

        passwordChangePanel = new JPanel();
        passwordChangePanel.setBackground(Color.WHITE);

        GroupLayout passwordChangeLayout = new GroupLayout(passwordChangePanel);
        passwordChangePanel.setLayout(passwordChangeLayout);

        passwordChangeLayout.setHorizontalGroup(
                passwordChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(passwordChangeLayout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addGroup(passwordChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(passwordChangeLabel)
                                        .addComponent(errorPasswordChangLabel)
                                        .addGroup(passwordChangeLayout.createSequentialGroup()
                                                .addComponent(passwordChangeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(passwordChangeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(21, Short.MAX_VALUE))
        );
        passwordChangeLayout.setVerticalGroup(
                passwordChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(passwordChangeLayout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(passwordChangeLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(passwordChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(passwordChangeButton, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                                        .addComponent(passwordChangeTxt))
                                .addGap(5, 5, 5)
                                .addComponent(errorPasswordChangLabel,15,15,15)
                                .addGap(22, 22, 22))
        );

        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);

        mainPanel.setLayout(new CardLayout());
        mainPanel.add(emailAuthPanel, "card1");
        mainPanel.add(emailCodePanel, "card2");
        mainPanel.add(passwordChangePanel, "card3");

        GroupLayout mainLayout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(mainLayout);
        mainLayout.setHorizontalGroup(
                mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(titlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        mainLayout.setVerticalGroup(
                mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(titlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(mainLayout.createSequentialGroup()
                                .addGap(75, 75, 75)
                                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }

    private void emailCodeKeyReleased(KeyEvent e) {
        errorEmailCodeLabel.setText("");
    }

    public void emailAuthKeyReleased(KeyEvent e) {
        String text = emailAuthTxt.getText();
        System.out.println(text);
        if (!text.endsWith("@gmail.com") && !text.isEmpty()) {
            System.out.println(1);
            errorEmailAuthLabel.setText("Email chưa đúng định dạng!");
        } else {
            errorEmailAuthLabel.setText("");
        }
    }


    public JButton getButton(String title) {
        JButton designButton = new JButton(title);
        designButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        designButton.setBackground(Color.WHITE);
        designButton.setFont(defaultFont);
        designButton.setBorder(new FlatLineBorder(new Insets(0,10,0,0), Color.LIGHT_GRAY,1, 5));

        return designButton;
    }

    public void emailAuthActionPerformed(ActionEvent event) {
        String emailTo = emailAuthTxt.getText();
        if (emailTo.isEmpty()) {
            errorEmailAuthLabel.setText("Trống thông tin");
        } else if (emailTo.contains("@gmail.com") && AccountDAO.getInstance().isExistEmail(emailTo)){
            String otp = getOTP();
            long now = System.currentTimeMillis();
            Timestamp creationTime = new Timestamp(now);

            CardLayout mainLayout = (CardLayout) mainPanel.getLayout();
            mainLayout.next(mainPanel);

            Gmailer.sendOTPMail(emailTo, otp);

            OTPdata otPdata = new OTPdata(emailTo, otp, creationTime);
            if (OTPDataDAO.getInstance().selectById(emailTo) == null) {
                OTPDataDAO.getInstance().insert(otPdata);
            } else {
                OTPDataDAO.getInstance().update(otPdata);
            }
        } else if (emailTo.contains("@gmail.com")) {
            errorEmailAuthLabel.setText("Email không tồn tại trong hệ thống");
        }
    }

    public static String getOTP () {
        int min = 100000;
        int max = 999999;
        return Integer.toString((int) ((Math.random() * (max - min)) + min));
    }

    public void emailCodeAcionPerformed(ActionEvent event) {
        String email = emailAuthTxt.getText();
        String otp = emailCodeTxt.getText();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        if (otp.isEmpty()) {
            errorEmailCodeLabel.setText("Trống thông tin");
        } else if (otp.length() != 6 || !isValidOTP(email, otp)) {
            errorEmailCodeLabel.setText("Mã không đúng!");
        } else if (!isValidTime(email, timestamp)) {
            errorEmailCodeLabel.setText("Mã đã hết hiệu lực!");
            Timer timer = new Timer(2000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    CardLayout mainLayout = (CardLayout) mainPanel.getLayout();
                    mainLayout.previous(mainPanel);
                }
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            CardLayout mainLayout = (CardLayout) mainPanel.getLayout();
            mainLayout.next(mainPanel);
        }
    }

    private boolean isValidOTP(String email, String otp) {

        String otpValid = OTPDataDAO.getInstance().selectById(email).getOtp();
        System.out.println("OTP valid : " + otpValid + "\nOTP typing " + otp);

        if (otpValid.equals(otp)) {
            return true;
        }
        return false;
    }

    public boolean isValidTime(String email, Timestamp endTime) {
        Timestamp startTime = OTPDataDAO.getInstance().selectById(email).getCreationTime();

        System.out.println(startTime);
        System.out.println(endTime);

        Instant startInstant = startTime.toInstant();
        Instant endInstant = endTime.toInstant();
        Duration duration = Duration.between(startInstant, endInstant);
        return duration.toMinutes() <= 5;
    }

    public void passwordTxtKeyReleased(KeyEvent event) {
        String text = passwordChangeTxt.getText();
        if (text.length() < 6 && !text.isEmpty()) {
            errorPasswordChangLabel.setText("Mật khẩu tối thiểu 6 kí tự!");
        } else {
            errorPasswordChangLabel.setText("");
        }
    }


    public void passwordChangeActionPerformed(ActionEvent event) {
        String password = passwordChangeTxt.getText();
        if (password.isEmpty()) errorPasswordChangLabel.setText("Trống thông tin");
        else if (password.matches("^(?=.*\\S).{6,}$")){
            Account account = AccountDAO.getInstance().selectByEmail(emailAuthTxt.getText());
            System.out.println(account.getAccountName() + " " + account.getEmail());
            account.setPassword(BCrypt.hashpw(password, BCrypt.gensalt(12)));
            AccountDAO.getInstance().update(account);

            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
            this.dispose();
        }
    }
}
