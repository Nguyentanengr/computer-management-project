package view;

import com.formdev.flatlaf.ui.FlatLineBorder;
import controller.BCrypt;
import controller.IOExcel;
import dao.AccountDAO;
import model.Account;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AccountForm extends JInternalFrame {
    private Account account;

    private JPanel mainPanel;
    private JPanel searchPanel;
    private JButton addButton;
    private JButton deleteButton;
    private JButton editButton;
    private JButton resetButton;
    private JButton inputExcelButton;
    private JButton outputExcelButton;
    private JButton refreshButton;
    private JComboBox optionComboBox;
    private JTextField searchTxt;
    private JToolBar featureToolbar;
    private JTable contentTable;
    private JScrollPane scrollPane;

    private DefaultTableModel defaultTableModel;
    private DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    private SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/YYYY HH:mm");
    private Font fontDefault =  new Font("SF Pro Display", 0, 12);


    public AccountForm(Account account) {
        this.account = account;
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
        initComponents();
        initTable();
        loadDataToTable(AccountDAO.getInstance().selectAll());
    }

    public JTable getContentTable() {
        return contentTable;
    }
    public void loadDataToTable(ArrayList<Account> listAccount) {
        defaultTableModel.setRowCount(0);
        for (int i = 0; i < listAccount.size(); i++) {
            defaultTableModel.addRow(new Object[] {listAccount.get(i).getAccountName(), listAccount.get(i).getUsername(),
                    listAccount.get(i).getRole(), listAccount.get(i).getStatus()});
        }
    }

    public void loadDataToListAccount(ArrayList<Account> listAccount) {
        for (int i = 0; i < contentTable.getRowCount(); i++) {
            String username = contentTable.getValueAt(i, 1).toString();
            Account account = AccountDAO.getInstance().selectById(username);
            listAccount.add(account);
        }
    }

    public void initTable() {
        defaultTableModel = new DefaultTableModel();
        defaultTableModel.setColumnIdentifiers(new String[]{"Tên tài khoản", "Tên đăng nhập", "Vai trò", "Trạng thái"});
        contentTable.setModel(defaultTableModel);
    }

    public void initComponents() {
        this.setBorder(null);
        this.setPreferredSize(new Dimension(1000, 800));

        addButton = getButton("Thêm", "/icon/addButton.png", true);
        addButton.setBounds(20, 15, 50, 60);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addButtonActionPerformed(e);
            }
        });
        deleteButton = getButton("Xóa", "/icon/deleteButton.png", true);
        deleteButton.setBounds(80, 15, 50, 60);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteButtonActionPerformed(e);
            }
        });
        editButton = getButton("Sửa", "/icon/editButton.png", true);
        editButton.setBounds(140, 15, 50, 60);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editButtonActionPerformed(e);
            }
        });
        resetButton= getButton("Đặt lại", "/icon/resetButton.png", true);
        resetButton.setBounds(200, 15, 50, 60);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetButtonActionPerformed(e);
            }
        });
        inputExcelButton = getButton("Nhập Excel", "/icon/inputExcelButton.png", true);
        inputExcelButton.setBounds(250, 15, 80, 60) ;
        inputExcelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputExcelButtonActionPerformed(e);
            }
        });

        outputExcelButton = getButton("Xuất Excel", "/icon/outputExcelButton.png", true);
        outputExcelButton.setBounds(320, 15, 80, 60);
        outputExcelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputExcelActionPerformed(e);
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

        featureToolbar.add(addButton);
        featureToolbar.add(deleteButton);
        featureToolbar.add(editButton);
        featureToolbar.add(resetButton);
        featureToolbar.add(inputExcelButton);
        featureToolbar.add(outputExcelButton);

        optionComboBox = new JComboBox<>();
        optionComboBox.setBackground(Color.WHITE);
        optionComboBox.setBorder(new LineBorder(Color.WHITE));
        optionComboBox.setFont(fontDefault);
        optionComboBox.setModel(new DefaultComboBoxModel<>(new String[] {"Tất cả", "Tên tài khoản", "Tên đăng nhập", "Vai trò"}));
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
        refreshButton.setBounds(450, 30, 105, 40);
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

        GroupLayout mainLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainLayout);
        mainLayout.setHorizontalGroup(
                mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(mainLayout.createSequentialGroup()
                                                .addComponent(featureToolbar, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(searchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE))
                                        .addComponent(scrollPane))
                                .addContainerGap())
        );
        mainLayout.setVerticalGroup(
                mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainLayout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(searchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                                        .addComponent(featureToolbar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addGap(26, 26, 26)
                                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 626, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, 1000, 1000, 1000)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(mainPanel, 800, 800, 800)
        );

        pack();
    }

    private ImageIcon getImageIcon(String pathIcon) {
        int widthIcon = 40, heighIcon = 40;
        URL urlIcon = AccountForm.class.getResource(pathIcon);
        Image imgUser = Toolkit.getDefaultToolkit().createImage(urlIcon).getScaledInstance(widthIcon, heighIcon, Image.SCALE_SMOOTH);
        return  new ImageIcon(imgUser);
    }

    private JButton getButton(String title, String pathIcon, boolean enable) {
        JButton designButton = new JButton(title, getImageIcon(pathIcon));
        designButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        designButton.setHorizontalTextPosition(SwingConstants.CENTER);
        designButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        designButton.setBackground(Color.WHITE);
        designButton.setBorder(new LineBorder(Color.WHITE));
        designButton.setFont(fontDefault);
        if (!enable) {
            designButton.setEnabled(false);
            designButton.setFocusable(false);
        }
        return designButton;
    }

    public void addButtonActionPerformed(ActionEvent event) {
        AddAccount addAccount = new AddAccount(this, (JFrame) SwingUtilities.getWindowAncestor(this), rootPaneCheckingEnabled);
        addAccount.setVisible(true);
    }

    public void deleteButtonActionPerformed(ActionEvent event) {
        if (contentTable.getSelectedRow() != -1) {
            int returnValue = JOptionPane.showConfirmDialog(this, "Chắn chắn xóa thông tin!", "Xóa thông tin", JOptionPane.YES_NO_OPTION);
            if (returnValue == JOptionPane.YES_OPTION) {
                String username = contentTable.getValueAt(contentTable.getSelectedRow(), 1).toString();

                Account account = AccountDAO.getInstance().selectById(username);
                if (account != null) System.out.println(1);
                AccountDAO.getInstance().delete(account);

                loadDataToTable(AccountDAO.getInstance().selectAll());
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thông tin cần xóa!");
        }
    }

    public void editButtonActionPerformed(ActionEvent event) {
        if (contentTable.getSelectedRow() != -1) {
            EditAccount editAccount = new EditAccount(this, (JFrame) SwingUtilities.getWindowAncestor(this), rootPaneCheckingEnabled);
            editAccount.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thông tin cần sửa!");
        }
    }

    public void resetButtonActionPerformed(ActionEvent event) {
        if (contentTable.getSelectedRow() != -1) {
            int returnValue = JOptionPane.showConfirmDialog(this, "Đặt lại mật khẩu cho tài khoản này?", "Đặt lại mật khẩu", JOptionPane.YES_NO_OPTION);
            if (returnValue == JOptionPane.YES_OPTION) {
                String password = JOptionPane.showInputDialog(this, "Nhập mật khẩu");
                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Trống mật khẩu!");
                } else {
                    String username = contentTable.getValueAt(contentTable.getSelectedRow(), 1).toString();
                    Account account = AccountDAO.getInstance().selectById(username);
                    account.setPassword(BCrypt.hashpw(password, BCrypt.gensalt(12)));
                    AccountDAO.getInstance().update(account);

                    loadDataToTable(AccountDAO.getInstance().selectAll());
                    JOptionPane.showMessageDialog(this, "Đặt lại mật khẩu thành công!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thông tin cần đặt lại mật khẩu!");
        }
    }

    public void inputExcelButtonActionPerformed(ActionEvent event) {
        IOExcel ioExcel = new IOExcel();
        ArrayList<Account> listAccount = ioExcel.importAccount();
        if (listAccount != null) loadDataToTable(listAccount);
    }

    public void outputExcelActionPerformed(ActionEvent event) {
        ArrayList<Account> listAccount = new ArrayList<>();
        loadDataToListAccount(listAccount);
        if (listAccount.size() == 0) return;
        IOExcel ioExcel = new IOExcel();
        ioExcel.exportAccount(listAccount);
    }

    public void refreshButtonActionPerformed(ActionEvent event) {
        optionComboBox.setSelectedItem("Tất cả");
        searchTxt.setText("");
        loadDataToTable(AccountDAO.getInstance().selectAll());
    }

    public void optionComboBoxActionPerformed(ActionEvent event) {
        String option = optionComboBox.getSelectedItem().toString();
        String content = searchTxt.getText();

        ArrayList<Account> listAccount = AccountDAO.getInstance().filter(option, content);
        if (listAccount != null) {
            loadDataToTable(listAccount);
        }
    }

    public void searchTxtKeyReleased(KeyEvent event) {
        String option = optionComboBox.getSelectedItem().toString();
        String content = searchTxt.getText();

        ArrayList<Account> listAccount = AccountDAO.getInstance().filter(option, content);
        if (listAccount != null) {
            loadDataToTable(listAccount);
        }
    }
}
