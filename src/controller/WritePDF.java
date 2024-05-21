package controller;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import dao.*;
import model.BillDetails;
import model.Computer;
import model.DeliveryBill;
import model.ReceiptBill;

import javax.print.Doc;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WritePDF {

    private DecimalFormat decimalFormat = new DecimalFormat("###, ###, ###");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY HH:mm");
    private Font fontData;
    private Font fontTitle;
    private Font fontHeader;

    public WritePDF() {
        try {
            fontData = new Font(BaseFont.createFont("lib/Roboto/Roboto-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 8, Font.NORMAL);
            fontTitle = new Font(BaseFont.createFont("lib/Roboto/Roboto-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 25, Font.NORMAL);
            fontHeader = new Font(BaseFont.createFont("lib/Roboto/Roboto-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 9, Font.NORMAL);
        } catch (IOException | DocumentException ex) {
            Logger.getLogger(WritePDF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeReceiptBill(String billCode) {
        String filePath = getFile(billCode);
        if (filePath == null) return;

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, fileOutputStream);
            document.open();
            addTitle(document, "THÔNG TIN PHIẾU NHẬP");
            addReceiptBillInfo(document, billCode);
            addBillDetails(document, billCode);

            document.close();
            openFile(filePath);
        } catch (DocumentException | FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi khi ghi file ");
        }
    }

    public void writeDeliveryBill(String billCode) {
        String filePath = getFile(billCode);
        if (filePath == null) return;

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, fileOutputStream);
            document.open();
            addTitle(document, "THÔNG TIN PHIẾU XUẤT");
            addDeliveryBillInfo(document, billCode);
            addBillDetails2(document, billCode);

            document.close();
            openFile(filePath);
        } catch (DocumentException | FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi khi ghi file ");
        }
    }

    private void addBillDetails2(Document document, String billCode) throws DocumentException {
        PdfPTable pdfPTable = createBillDetailsTable();

        for (BillDetails billDetails : DeliveryBillDetailsDAO.getInstance().selectBy(billCode)) {
            Computer computer = ComputerDAO.getInstance().selectById(billDetails.getMachineCode());
            addBillDetailsRow(pdfPTable, billDetails, computer);
        }

        document.add(pdfPTable);
        document.add(Chunk.NEWLINE);

        DeliveryBill deliveryBill = DeliveryBillDAO.getInstance().selectById(billCode);
        Paragraph para = createParagraphWithIndent(
                "Tổng thanh toán: " + decimalFormat.format(deliveryBill.getTotalAmount()) + "đ",
                300);
        document.add(para);
    }
    private void addDeliveryBillInfo(Document document, String billCode) throws DocumentException{
        DeliveryBill deliveryBill = DeliveryBillDAO.getInstance().selectById(billCode);

        Paragraph para1 = createParagraphWithIndent("Mã phiếu: " + deliveryBill.getBillCode(), 50);
        para1.add("\nThời gian tạo: " + dateFormat.format(deliveryBill.getCreationTime()));
        Paragraph para2 = createParagraphWithIndent(
                "Người tạo: " + AccountDAO.getInstance().selectById(deliveryBill.getCreator()).getAccountName(),
                50);

        document.add(para1);
        document.add(para2);
        document.add(Chunk.NEWLINE);
    }

    private void addBillDetails(Document document, String billCode) throws DocumentException {
        PdfPTable pdfPTable = createBillDetailsTable();

        for (BillDetails billDetails : ReceiptBillDetailsDAO.getInstance().selectBy(billCode)) {
            Computer computer = ComputerDAO.getInstance().selectById(billDetails.getMachineCode());
            addBillDetailsRow(pdfPTable, billDetails, computer);
        }

        document.add(pdfPTable);
        document.add(Chunk.NEWLINE);

        ReceiptBill receiptBill = ReceiptBillDAO.getInstance().selectById(billCode);
        Paragraph paraTongThanhToan = createParagraphWithIndent(
                "Tổng thanh toán: " + decimalFormat.format(receiptBill.getTotalAmount()) + "đ",
                300);
        document.add(paraTongThanhToan);

    }

    private void addBillDetailsRow(PdfPTable pdfPTable, BillDetails billDetails, Computer computer) {
        pdfPTable.addCell(new PdfPCell(new Phrase(billDetails.getMachineCode(), fontData)));
        pdfPTable.addCell(new PdfPCell(new Phrase(computer.getMachineName(), fontData)));

        PdfPCell cell3 = new PdfPCell(new Phrase(decimalFormat.format(computer.getPrice()) + "đ", fontData));
        cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPTable.addCell(cell3);

        PdfPCell cell4 = new PdfPCell(new Phrase(String.valueOf(billDetails.getQuantity()), fontData));
        cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPTable.addCell(cell4);

        PdfPCell cell5 = new PdfPCell(new Phrase(decimalFormat.format(billDetails.getQuantity() * computer.getPrice()) + "đ", fontData));
        cell5.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPTable.addCell(cell5);
    }

    private PdfPTable createBillDetailsTable() throws DocumentException {
        PdfPTable pdfPTable = new PdfPTable(5);
        pdfPTable.setWidths(new float[] {10f, 30f, 15f, 5f, 15f});

        PdfPCell cell;

        cell = new PdfPCell(new Phrase("Mã máy", fontHeader));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); // Căn lề giữa
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Tên máy", fontHeader));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); // Căn lề giữa
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Đơn giá", fontHeader));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); // Căn lề giữa
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Phrase("SL", fontHeader));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); // Căn lề giữa
        pdfPTable.addCell(cell);

        cell = new PdfPCell(new Phrase("Tổng tiền", fontHeader));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); // Căn lề giữa
        pdfPTable.addCell(cell);

        return pdfPTable;
    }
    private String getFile(String billCode) {
        FileDialog fileDialog = new FileDialog((Frame) null, "Xuất pdf", FileDialog.SAVE);
        fileDialog.setFile(billCode + ".pdf");
        fileDialog.setVisible(true);

        if (fileDialog.getFile() == null) return null;
        else return fileDialog.getDirectory() + fileDialog.getFile();
    }

    private void openFile(String filePath) {
        try {
            File file = new File(filePath);
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void addTitle(Document document, String title) throws DocumentException {
        Paragraph pdfTitle = new Paragraph(new Phrase(title, fontTitle));
        pdfTitle.setAlignment(Element.ALIGN_CENTER);
        document.add(pdfTitle);
        document.add(Chunk.NEWLINE);
    }

    private void addReceiptBillInfo(Document document, String billCode) throws DocumentException {
        ReceiptBill receiptBill = ReceiptBillDAO.getInstance().selectById(billCode);

        Paragraph para1 = createParagraphWithIndent("Mã phiếu: " + receiptBill.getBillCode(), 50);
        para1.add("\nThời gian tạo: " + dateFormat.format(receiptBill.getCreationTime()));
        Paragraph para2 = createParagraphWithIndent(
                "Người tạo: " + AccountDAO.getInstance().selectById(receiptBill.getCreator()).getAccountName() +
                        "\nNhà cung cấp: " + SupplierDAO.getInstance().selectById(receiptBill.getSupplier()).getSupplierName() +
                        "  -  " + receiptBill.getSupplier(),
                50);

        document.add(para1);
        document.add(para2);
        document.add(Chunk.NEWLINE);
    }

    private Paragraph createParagraphWithIndent(String text, float indent) {
        Paragraph paragraph = new Paragraph(text, fontData);
        paragraph.setIndentationLeft(indent);
        return paragraph;
    }
}
