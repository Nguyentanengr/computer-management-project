package view;

import com.formdev.flatlaf.ui.FlatLineBorder;
import controller.AdminController;
import dao.AccountDAO;
import model.Account;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class AdminView extends JFrame {
    private Account account;
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JLabel homeLabel;
    private JLabel productLabel;
    private JLabel supplierLabel;
    private JLabel receiptLabel;
    private JLabel receiptBillLabel;
    private JLabel deliveryLabel;
    private JLabel deliveryBillLabel;
    private JLabel stockLabel;
    private JLabel accountLabel;
    private JLabel statisticLabel;
    private JLabel informationLabel;
    private JLabel logOutLabel;


    private final Font defaultFont = new Font("SF Pro Display", 1, 18);
    private final Color defaultColor = new Color(13, 39, 51);
    private final Color clickedColor = Color.CYAN;

    private AdminController controller;
    public AdminView(Account acc) {
        this.controller = new AdminController(this);
        this.account = acc;

        initialize();
    }

    private void initialize() {
        setupFrame();
        initComponents();
        addEventHandlers();

        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void setupFrame() {
        ImageIcon logo = new ImageIcon(getClass().getResource("/icon/logo.png"));
        this.setIconImage(logo.getImage());
        this.setTitle("My shop");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }


    public void initComponents() {

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        setupLeftPanel();
        setupRightPanel();

        GroupLayout layoutMainPanel = new GroupLayout(getContentPane());
        getContentPane().setLayout(layoutMainPanel);
        layoutMainPanel.setHorizontalGroup(layoutMainPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, 1300,  Short.MAX_VALUE)
        );

        layoutMainPanel.setVerticalGroup(layoutMainPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, 800,  Short.MAX_VALUE)
        );

        pack();
    }

    private void setupLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setBackground(this.defaultColor);
        leftPanel.setPreferredSize(new Dimension(300, 800));
        leftPanel.setLayout(null);

        homeLabel = createLabel(" HI !  " + account.getRole().toUpperCase(), "/icon/home.png", 80, 60, 250, 40);
        productLabel = createLabel(" SẢN PHẨM", "/icon/product.png", 20, 200, 250, 40);
        supplierLabel = createLabel(" NHÀ CUNG CẤP", "/icon/supplier.png", 20, 240, 250, 40);
        receiptLabel = createLabel(" NHẬP HÀNG", "/icon/receipt.png", 20, 280, 250, 40);
        receiptBillLabel = createLabel(" PHIẾU NHẬP", "/icon/receiptBill.png", 20, 320, 250, 40);
        deliveryLabel = createLabel(" XUẤT HÀNG", "/icon/delivery.png", 20, 360, 250, 40);
        deliveryBillLabel = createLabel(" PHIẾU XUẤT", "/icon/deliveryBill.png", 20, 400, 250, 40);
        stockLabel = createLabel(" TỒN KHO", "/icon/stock.png", 20, 440, 250, 40);
        accountLabel = createLabel(" TÀI KHOẢN", "/icon/account.png", 20, 480, 250, 40);
        statisticLabel = createLabel(" THỐNG KÊ", "/icon/statistic.png", 20, 520, 250, 40);
        informationLabel = createLabel(" ĐỔI THÔNG TIN", "/icon/information.png", 20, 680, 250, 40);
        logOutLabel = createLabel(" ĐĂNG XUẤT", "/icon/logOut.png", 20, 720, 250, 40);

        leftPanel.add(homeLabel);
        leftPanel.add(productLabel);
        leftPanel.add(supplierLabel);
        leftPanel.add(receiptLabel);
        leftPanel.add(receiptBillLabel);
        leftPanel.add(deliveryLabel);
        leftPanel.add(deliveryBillLabel);
        leftPanel.add(stockLabel);
        leftPanel.add(accountLabel);
        leftPanel.add(statisticLabel);
        leftPanel.add(informationLabel);
        leftPanel.add(logOutLabel);
    }

    private void setupRightPanel() {
        rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(new LineBorder(defaultColor, 2));

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        GroupLayout layoutRightPanel = new GroupLayout(rightPanel);
        rightPanel.setLayout(layoutRightPanel);
        layoutRightPanel.setHorizontalGroup(layoutRightPanel.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 1000, Short.MAX_VALUE)
        );

        layoutRightPanel.setVerticalGroup(layoutRightPanel.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 800, Short.MAX_VALUE)
        );

    }


    private void addEventHandlers() {
        productLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.handleProductClick();
            }
        });

        supplierLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.handleSupplierClick();
            }
        });

        receiptLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.handleReceiptClick();
            }
        });

        receiptBillLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.handleReceiptBillClick();
            }
        });

        deliveryLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.handleDeliveryClick();
            }
        });

        deliveryBillLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.handleDeliveryBillClick();
            }
        });

        stockLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.handleStockClick();
            }
        });

        accountLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.handleAccountClick();
            }
        });

        statisticLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.handleStatisticClick();
            }
        });

        informationLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.handleInformationClick();
            }
        });
        logOutLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.handleLogOutClick();
            }
        });
    }

    private JLabel createLabel(String title, String pathIcon, int x, int y, int width, int height) {
        int widthIcon = 20, heighIcon = 20;
        URL urlIcon = AdminView.class.getResource(pathIcon);
        Image imgUser = Toolkit.getDefaultToolkit().createImage(urlIcon).getScaledInstance(widthIcon, heighIcon, Image.SCALE_SMOOTH);

        JLabel designLabel = new JLabel(title, new ImageIcon(imgUser), JLabel.LEFT);
        designLabel.setBackground(this.defaultColor);
        designLabel.setFont(this.defaultFont);
        designLabel.setForeground(Color.WHITE);
        designLabel.setBounds(x, y, width, height);
        return designLabel;
    }

    public Account getAccount() {
        return account;
    }

    public void setHighlight(JLabel label) {
        productLabel.setBorder(null);
        supplierLabel.setBorder(null);
        receiptLabel.setBorder(null);
        receiptBillLabel.setBorder(null);
        deliveryLabel.setBorder(null);
        deliveryBillLabel.setBorder(null);
        stockLabel.setBorder(null);
        accountLabel.setBorder(null);
        statisticLabel.setBorder(null);
        informationLabel.setBorder(null);
        logOutLabel.setBorder(null);

        label.setBorder(new FlatLineBorder(new Insets(0,20,0,0), clickedColor, 1, 30));
    }

    public void setRightPanel(JInternalFrame frame) {
        rightPanel.removeAll();
        rightPanel.add(frame).setVisible(true);
    }

    public JLabel getProductLabel() {
        return productLabel;
    }

    public JLabel getSupplierLabel() {
        return supplierLabel;
    }

    public JLabel getReceiptLabel() {
        return receiptLabel;
    }

    public JLabel getReceiptBillLabel() {
        return receiptBillLabel;
    }

    public JLabel getDeliveryLabel() {
        return deliveryLabel;
    }

    public JLabel getDeliveryBillLabel() {
        return deliveryBillLabel;
    }

    public JLabel getStockLabel() {
        return stockLabel;
    }

    public JLabel getAccountLabel() {
        return accountLabel;
    }

    public JLabel getStatisticLabel() {
        return statisticLabel;
    }

    public JLabel getInformationLabel() {
        return informationLabel;
    }

    public JLabel getLogOutLabel() {
        return logOutLabel;
    }

    public static void main(String[] args) {
        AdminView ad = new AdminView(AccountDAO.getInstance().selectById("admin"));
    }

}
