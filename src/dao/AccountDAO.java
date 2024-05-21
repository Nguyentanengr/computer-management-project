package dao;

import database.JDBCUtil;
import model.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO implements DAOinterface<Account>{

    public static AccountDAO getInstance() {
        return new AccountDAO();
    }

    @Override
    public int insert(Account t) {
        int affectedRows = 0;
        try {
            String insertQuery = "INSERT INTO account VALUES(?, ?, ?, ?, ?, ?)";
            Connection con = JDBCUtil.getConnection();
            PreparedStatement pst = con.prepareStatement(insertQuery);
            pst.setString(1, t.getAccountName());
            pst.setString(2, t.getUsername());
            pst.setString(3, t.getPassword());
            pst.setString(4, t.getRole());
            pst.setInt(5, t.getStatus());
            pst.setString(6, t.getEmail());

            affectedRows = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    @Override
    public int update(Account t) {
        int affectedRows = 0;
        try {
            String updateQuery = "UPDATE account SET fullName = ?, password = ?, role = ?, status = ?, email = ? WHERE userName = ?";
            Connection con = JDBCUtil.getConnection();
            PreparedStatement pst = con.prepareStatement(updateQuery);
            pst.setString(1, t.getAccountName());
            pst.setString(2, t.getPassword());
            pst.setString(3, t.getRole());
            pst.setInt(4, t.getStatus());
            pst.setString(5, t.getEmail());
            pst.setString(6, t.getUsername());
            affectedRows = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    @Override
    public int delete(Account t) {
        int affectedRows = 0;
        try (Connection con = JDBCUtil.getConnection();) {
            String insertQuery = "DELETE FROM account WHERE userName = ?";
            PreparedStatement pst = con.prepareStatement(insertQuery);
            pst.setString(1, t.getUsername());
            affectedRows = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRows;
    }
    @Override
    public ArrayList<Account> selectAll() {
        ArrayList<Account> accountList = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM account";
            ResultSet rs;
            Connection con = JDBCUtil.getConnection();
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(selectQuery);

            while (rs.next()) {
                String accountName = rs.getString("fullName");
                String username = rs.getString("userName");
                String password = rs.getString("password");
                String role = rs.getString("role");
                int status = rs.getInt("status");
                String email = rs.getString("email");

                Account ac = new Account(accountName, username, password, role, status, email);
                accountList.add(ac);
            }
            JDBCUtil.closeConnection(con);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return accountList;
    }

    @Override
    public Account selectById(String id) {
        Account ac = null;
        try {
            String selectQuery = "SELECT * FROM account WHERE userName = ?";
            Connection con = JDBCUtil.getConnection();
            PreparedStatement pst = con.prepareStatement(selectQuery);
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                ac = new Account(rs.getString("fullName"), rs.getString("userName"), rs.getString("password"),
                        rs.getString("role"), rs.getInt("status"), rs.getString("email"));
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ac;
    }

    public Account selectByEmail(String id) {
        Account ac = null;
        try {
            String selectQuery = "SELECT * FROM account WHERE email = ?";
            Connection con = JDBCUtil.getConnection();
            PreparedStatement pst = con.prepareStatement(selectQuery);
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                ac = new Account(rs.getString("fullName"), rs.getString("userName"), rs.getString("password"),
                        rs.getString("role"), rs.getInt("status"), rs.getString("email"));
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ac;
    }
    public int updatePassword(String email, String newPassword) {
        int affectedRows = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String updateQuery = "UPDATE account SET password = ? WHERE email = ?";
            PreparedStatement pst = con.prepareStatement(updateQuery);
            pst.setString(1, newPassword);
            pst.setString(2, email);

            affectedRows = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    public ArrayList<Account> filter(String option, String content) {
        String selectQuery = "SELECT * FROM account WHERE ";
        ArrayList<Account> listAccount = new ArrayList<>();
        if (option.equals("Tất cả")) {
            selectQuery += "LOWER(fullName) LIKE ? OR LOWER(userName) LIKE ? OR LOWER(role) LIKE ?";
        } else if (option.equals("Tên tài khoản")) {
            selectQuery += "LOWER(fullName) LIKE ?";
        } else if (option.equals("Tên đăng nhập")) {
            selectQuery += "LOWER(userName) LIKE ?";
        } else if (option.equals("Vai trò")) {
            selectQuery += "LOWER(role) LIKE ?";
        }

        try (Connection con = JDBCUtil.getConnection()) {
            PreparedStatement pst = con.prepareStatement(selectQuery);
            pst.setString(1, "%" + content.toLowerCase() + "%");
            if (option.equals("Tất cả")) {
                pst.setString(2, "%" + content.toLowerCase() + "%");
                pst.setString(3, "%" + content.toLowerCase() + "%");
            }

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String accountName = rs.getString("fullName");
                String username = rs.getString("userName");
                String role = rs.getString("role");
                int status = rs.getInt("status");
                String email = AccountDAO.getInstance().selectById(username).getEmail();

                Account account = new Account(accountName, username, "", role, status, email);
                listAccount.add(account);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listAccount;
    }

    public boolean isExistUsername(String username) {
        String selectQuery = "SELECT * FROM account WHERE username = ?";
        try (Connection con = JDBCUtil.getConnection()) {
            PreparedStatement pst = con.prepareStatement(selectQuery);
            pst.setString(1, username);

            System.out.println(pst.toString());
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isExistEmail(String email) {
        String selectQuery = "SELECT * FROM account WHERE email = ?";
        try (Connection con = JDBCUtil.getConnection()) {
            PreparedStatement pst = con.prepareStatement(selectQuery);
            pst.setString(1, email);

            System.out.println(pst.toString());
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
