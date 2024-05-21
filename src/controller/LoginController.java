package controller;

import dao.AccountDAO;
import model.Account;
import view.AdminView;
import view.LoginView;
import view.RecoverPassword;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class LoginController {

    private LoginView view;

    public LoginController(LoginView view) {
        this.view = view;
    }

    public void handleLoginKeyPress(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_ENTER) {
            checkLogin();
        }
    }

    public void handleLoginClick() {
        checkLogin();
    }

    public void handleForgotPasswordClick() {
        RecoverPassword recoverPassword = new RecoverPassword(view, true);
        recoverPassword.setVisible(true);
    }

    private void checkLogin() {
        String username = view.getUsername();
        String password = view.getPassword();
        if (username.isEmpty() || password.isEmpty()) {
            view.showErrorMessage("Vui lòng nhập đầy đủ thông tin!");
        } else {
            try {
                Account acc = AccountDAO.getInstance().selectById(username);
                if (acc != null && BCrypt.checkpw(password, acc.getPassword())) {
                    view.clearPassword();
                    handleLoginSuccess(acc);
                } else {
                    view.showErrorMessage("Tài khoản hoặc mật khẩu không đúng!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                view.showErrorMessage("Đã xảy ra lỗi hệ thống!");
            }
        }
    }

    private void handleLoginSuccess(Account acc) {
        view.dispose();
        switch (acc.getRole()) {
            case "Admin":
                new AdminView(acc).setVisible(true);
                break;
            case "Quản lý kho":
                JOptionPane.showMessageDialog(view, "Hi quản lý kho!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "Nhân viên nhập":
                JOptionPane.showMessageDialog(view, "Hi nhân viên nhập!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "Nhân viên xuất":
                JOptionPane.showMessageDialog(view, "Hi nhân viên xuất!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                break;
            default:
                JOptionPane.showMessageDialog(view, "Vai trò không xác định!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                break;
        }
    }
}
