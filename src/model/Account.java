package model;

import java.util.Objects;

public class Account {
    private String accountName;
    private String username;
    private String password;
    private String role;
    private int status;
    private String email;

    public Account(String accountName, String username, String password, String role, int status, String email) {
        this.accountName = accountName;
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
        this.email = email;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return status == account.status && Objects.equals(accountName, account.accountName) && Objects.equals(username, account.username) && Objects.equals(password, account.password) && Objects.equals(role, account.role) && Objects.equals(email, account.email);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.accountName);
        hash = 47 * hash + Objects.hashCode(this.username);
        hash = 47 * hash + Objects.hashCode(this.password);
        hash = 47 * hash + Objects.hashCode(this.role);
        hash = 47 * hash + this.status;
        hash = 47 * hash + Objects.hashCode(this.email);
        return hash;
    }
}
