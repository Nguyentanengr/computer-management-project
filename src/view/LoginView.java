package view;

import controller.LoginController;


import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

public class LoginView extends JFrame {

    private LoginController controller;

    public LoginView() {
        this.controller = new LoginController(this);
        initialize();
    }

    private void initialize() {
        setupFrame();
        initComponents();
        addEventHandlers();

        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private void setupFrame() {
        ImageIcon logo = new ImageIcon(getClass().getResource("/icon/logo.png"));
        this.setIconImage(logo.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Đăng nhập vào hệ thống");
        this.setResizable(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public void initComponents(){
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 2));

        setupLeftPanel();
        setupRightPanel();

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
        );

        pack();
    }

    private void setupLeftPanel() {
        leftPanel = new JPanel(null);
        leftPanel.setBackground(new Color(0, 200, 100));

        leftLoginLabel = new JLabel("LOGIN");
        leftLoginLabel.setFont(new Font("Cantarell", Font.BOLD, 65));
        leftLoginLabel.setForeground(Color.WHITE);
        leftLoginLabel.setBounds(100, 300, 250, 80);

        iconLabel = new JLabel(new ImageIcon(getClass().getResource("/icon/user.png")));
        iconLabel.setBounds(120, 180, 150, 150);

        leftPanel.add(iconLabel);
        leftPanel.add(leftLoginLabel);

        mainPanel.add(leftPanel);
    }

    private void setupRightPanel() {
        rightPanel = new JPanel(null);
        rightPanel.setBackground(new Color(13, 39, 51));

        usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("SF Pro Display", Font.BOLD, 18));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBounds(50, 160, 200, 30);

        usernameTxt = new JTextField();
        usernameTxt.setFont(new Font("SF Pro Display", Font.BOLD, 18));
        usernameTxt.setForeground(Color.WHITE);
        usernameTxt.setBackground(new Color(13, 39, 51));
        usernameTxt.setBorder(new LineBorder(new Color(13, 39, 51), 2));
        usernameTxt.setBounds(50, 200, 250, 30);

        usernameLineLabel = new JLabel();
        usernameLineLabel.setBorder(new LineBorder(Color.WHITE, 2, true));
        usernameLineLabel.setBounds(50, 230, 250, 2);

        passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("SF Pro Display", Font.BOLD, 18));
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setBounds(50, 255, 200, 30);

        passwordTxt = new JPasswordField();
        passwordTxt.setFont(new Font("SF Pro Display", Font.BOLD, 18));
        passwordTxt.setForeground(Color.WHITE);
        passwordTxt.setBackground(new Color(13, 39, 51));
        passwordTxt.setBorder(new LineBorder(new Color(13, 39, 51), 2));
        passwordTxt.setBounds(50, 295, 250, 30);

        passwordLineLabel = new JLabel();
        passwordLineLabel.setBorder(new LineBorder(Color.WHITE, 2, true));
        passwordLineLabel.setBounds(50, 325, 250, 2);

        loginPanel = new JPanel(new BorderLayout());
        loginPanel.setBackground(new Color(0, 200, 100));
        loginPanel.setBounds(50, 365, 270, 40);

        rightLoginLabel = new JLabel("Đăng nhập");
        rightLoginLabel.setForeground(Color.WHITE);
        rightLoginLabel.setFont(new Font("SF Pro Display", Font.BOLD, 18));
        rightLoginLabel.setHorizontalAlignment(SwingConstants.CENTER);

        loginPanel.add(rightLoginLabel, BorderLayout.CENTER);

        forgotPasswordLabel = new JLabel("Quên mật khẩu?");
        forgotPasswordLabel.setFont(new Font("SF Pro Display", Font.BOLD, 18));
        forgotPasswordLabel.setForeground(Color.WHITE);
        forgotPasswordLabel.setBounds(115, 425, 200, 30);

        rightPanel.add(usernameLabel);
        rightPanel.add(usernameTxt);
        rightPanel.add(usernameLineLabel);
        rightPanel.add(passwordLabel);
        rightPanel.add(passwordTxt);
        rightPanel.add(passwordLineLabel);
        rightPanel.add(loginPanel);
        rightPanel.add(forgotPasswordLabel);

        mainPanel.add(rightPanel);
    }

    private void addEventHandlers() {
        usernameTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                controller.handleLoginKeyPress(e);
            }
        });

        passwordTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                controller.handleLoginKeyPress(e);
            }
        });

        loginPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.handleLoginClick();
            }
        });

        forgotPasswordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.handleForgotPasswordClick();
            }
        });
    }

    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel loginPanel;
    private JLabel leftLoginLabel;
    private JLabel rightLoginLabel;
    private JLabel iconLabel;
    private JLabel usernameLabel;
    private JLabel usernameLineLabel;
    private JLabel passwordLabel;
    private JLabel passwordLineLabel;
    private JLabel forgotPasswordLabel;
    private JTextField usernameTxt;
    private JPasswordField passwordTxt;

    public String getUsername() {
        return usernameTxt.getText();
    }

    public String getPassword() {
        return new String(passwordTxt.getPassword());
    }

    public void clearPassword() {
        passwordTxt.setText("");
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        LoginView login = new LoginView();
    }
}
