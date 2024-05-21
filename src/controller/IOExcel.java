package controller;

import dao.LaptopDAO;
import model.Account;
import model.Computer;
import model.DeliveryBill;
import model.ReceiptBill;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class IOExcel {

    private JFrame frame;

    public IOExcel() {
        frame = new JFrame();
        this.frame.setSize(1000, 800);
    }

    public ArrayList<Account> importAccount() {
        File openFile = getInputFile();

        if (openFile == null) return null;

        ArrayList<Account> listAccount = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(openFile);
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                try {
                    String accountName = row.getCell(0).getStringCellValue();
                    String username = row.getCell(1).getStringCellValue();
                    String role = row.getCell(2).getStringCellValue();
                    int status = (int) row.getCell(3).getNumericCellValue();

                    Account account = new Account(accountName, username, "", role, status, "");
                    listAccount.add(account);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Không thể mở file Excel: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return listAccount;
    }

    public ArrayList<DeliveryBill> importDeliveryBill() {
        File openFile = getInputFile();

        if (openFile == null) return null;

        ArrayList<DeliveryBill> listDeliveryBill = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(openFile);
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                try {
                    String billCode = row.getCell(0).getStringCellValue();
                    String creator = row.getCell(1).getStringCellValue();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    LocalDateTime dateTime = LocalDateTime.parse(row.getCell(2).getStringCellValue(), formatter);
                    Timestamp creationTime = Timestamp.valueOf(dateTime);
                    Double totalAmount = row.getCell(3).getNumericCellValue();
                    DeliveryBill deliveryBill = new DeliveryBill(billCode, creationTime, creator, null, totalAmount);
                    listDeliveryBill.add(deliveryBill);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame, "Lỗi đọc hàng" + (i + 1) + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Không thể mở file Excel: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return listDeliveryBill;
    }
    public ArrayList<ReceiptBill> importReceiptBill() {
            File openFile = getInputFile();

            if (openFile == null) return null;

            ArrayList<ReceiptBill> listReceiptBill = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(openFile);
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                try {
                    String billCode = row.getCell(0).getStringCellValue();
                    String supplierCode = row.getCell(1).getStringCellValue();
                    String creator = row.getCell(2).getStringCellValue();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    LocalDateTime dateTime = LocalDateTime.parse(row.getCell(3).getStringCellValue(), formatter);
                    Timestamp creationTime = Timestamp.valueOf(dateTime);
                    Double totalAmount = row.getCell(4).getNumericCellValue();

                    ReceiptBill receiptBill = new ReceiptBill(billCode, creationTime, creator, null, totalAmount, supplierCode);
                    listReceiptBill.add(receiptBill);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame, "Lỗi đọc hàng" + (i + 1) + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Không thể mở file Excel: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return listReceiptBill;
    }

    public void exportAccount(ArrayList<Account> listAccount) {
        File saveFile = getOutputFile();

        if (saveFile == null) return;

        try (FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
             Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet();
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Tên tài khoản");
            headerRow.createCell(1).setCellValue("Tên đăng nhập");
            headerRow.createCell(2).setCellValue("Vai trò");
            headerRow.createCell(3).setCellValue("Trạng thái");

            for (int i = 0; i < listAccount.size(); i++) {
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(listAccount.get(i).getAccountName());
                row.createCell(1).setCellValue(listAccount.get(i).getUsername());
                row.createCell(2).setCellValue(listAccount.get(i).getRole());
                row.createCell(3).setCellValue(listAccount.get(i).getStatus());
            }

            workbook.write(fileOutputStream);
            JOptionPane.showMessageDialog(frame, "Lưu file thành công!");
            openFile(saveFile.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Không thể lưu file!");
            e.printStackTrace();
        }
    }
    public void exportComputer(ArrayList<Computer> listComputer) {
        File saveFile = getOutputFile();

        if (saveFile == null) return;

        try (FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
             Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet();
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Mã máy");
            headerRow.createCell(1).setCellValue("Tên máy");
            headerRow.createCell(2).setCellValue("Số lượng");
            headerRow.createCell(3).setCellValue("Đơn giá");
            headerRow.createCell(4).setCellValue("Bộ xử lí");
            headerRow.createCell(5).setCellValue("Ram");
            headerRow.createCell(6).setCellValue("Bộ nhớ");
            headerRow.createCell(7).setCellValue("Loại máy");

            for (int i = 0; i < listComputer.size(); i++) {
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(listComputer.get(i).getMachineCode());
                row.createCell(1).setCellValue(listComputer.get(i).getMachineName());
                row.createCell(2).setCellValue(listComputer.get(i).getQuantity());
                row.createCell(3).setCellValue(listComputer.get(i).getPrice());
                row.createCell(4).setCellValue(listComputer.get(i).getCPUName());
                row.createCell(5).setCellValue(listComputer.get(i).getRAM());
                row.createCell(6).setCellValue(listComputer.get(i).getRom());

                if (LaptopDAO.getInstance().isLaptop(listComputer.get(i).getMachineCode())) {
                    row.createCell(7).setCellValue("Laptop");
                } else row.createCell(7).setCellValue("DesktopComputer");
            }

            workbook.write(fileOutputStream);
            JOptionPane.showMessageDialog(frame, "Lưu file thành công!");
            openFile(saveFile.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Không thể lưu file!");
            e.printStackTrace();
        }
    }

    public void exportDeliveryBill(ArrayList<DeliveryBill> listDeliveryBill) {
        File saveFile = getOutputFile();

        if (saveFile == null) return;

        try (FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
             Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Delivery Bill");
            Row headerRow = sheet.createRow(0);
            Cell cell;
            cell = headerRow.createCell(0); cell.setCellValue("Mã phiếu nhập");
            cell = headerRow.createCell(1); cell.setCellValue("Người tạo");
            cell = headerRow.createCell(2); cell.setCellValue("Thời gian tạo");
            cell = headerRow.createCell(3); cell.setCellValue("Tổng tiền");

            for (int i = 0; i < listDeliveryBill.size(); i++) {
                Row row = sheet.createRow(i + 1);
                cell = row.createCell(0); cell.setCellValue(listDeliveryBill.get(i).getBillCode());
                cell = row.createCell(1); cell.setCellValue(listDeliveryBill.get(i).getCreator());
                cell = row.createCell(2); cell.setCellValue(new SimpleDateFormat("dd/MM/YYYY HH:mm").format(listDeliveryBill.get(i).getCreationTime()));
                cell = row.createCell(3); cell.setCellValue(listDeliveryBill.get(i).getTotalAmount());
            }

            workbook.write(fileOutputStream);
            JOptionPane.showMessageDialog(frame, "File được lưu thành công!");
            this.openFile(saveFile.toString());

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Không thể lưu file");
            e.printStackTrace();
        }


    }
    public void exportReceiptBill(ArrayList<ReceiptBill> listReceiptBill) {
        File saveFile = getOutputFile();

        if (saveFile == null) return;

        try (FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
            Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Receipt Bill");
            Row headerRow = sheet.createRow(0);
            Cell cell;
            cell = headerRow.createCell(0); cell.setCellValue("Mã phiếu nhập");
            cell = headerRow.createCell(1); cell.setCellValue("Nhà cung cấp");
            cell = headerRow.createCell(2); cell.setCellValue("Người tạo");
            cell = headerRow.createCell(3); cell.setCellValue("Thời gian tạo");
            cell = headerRow.createCell(4); cell.setCellValue("Tổng tiền");

            System.out.println(listReceiptBill.size());

            for (int i = 0; i < listReceiptBill.size(); i++) {
                Row row = sheet.createRow(i + 1);
                cell = row.createCell(0); cell.setCellValue(listReceiptBill.get(i).getBillCode());
                cell = row.createCell(1); cell.setCellValue(listReceiptBill.get(i).getSupplier());
                cell = row.createCell(2); cell.setCellValue(listReceiptBill.get(i).getCreator());
                cell = row.createCell(3); cell.setCellValue(new SimpleDateFormat("dd/MM/YYYY HH:mm").format(listReceiptBill.get(i).getCreationTime()));
                cell = row.createCell(4); cell.setCellValue(listReceiptBill.get(i).getTotalAmount());
            }

            workbook.write(fileOutputStream);
            JOptionPane.showMessageDialog(frame, "File được lưu thành công!");
            this.openFile(saveFile.toString());

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Không thể xuất file");
            e.printStackTrace();
        }
    }

    private void openFile(String pathFile) {
        try {
            File path = new File(pathFile);
            Desktop.getDesktop().open(path);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Mở file thất bại!");
        }
    }

    private File getOutputFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File saveFile = fileChooser.getSelectedFile();
            if (saveFile != null) {
                String pathFile = saveFile.getPath();
                if (!pathFile.endsWith(".xlsx")) {
                    pathFile += ".xlsx";
                    saveFile = new File(pathFile);
                }
                if (saveFile.exists()) {
                    int option = JOptionPane.showConfirmDialog(frame, "File đã tồn tại. Thực hiện ghi đè?", "Xác nhận ghi đè", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        return saveFile;
                    }
                } else {
                    return saveFile;
                }
            }
        }
        return null;
    }
    private File getInputFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel", "xlsx"));

        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
}
