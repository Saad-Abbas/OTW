package com.example.otwAppservice.service.invoicePDFService;

import com.example.otwAppservice.InvoiceUtils;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

//import com.ceepossolution.demo.InvoiceUtils;
//import com.ceepossolution.demo.pojos.OrderInvoiceDetailPOJO;
import com.itextpdf.html2pdf.HtmlConverter;
//import org.thymeleaf.spring5.SpringTemplateEngine;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class GenerateInvoicePDF {

//    public HttpEntity<byte[]> createPdf(List<OrderInvoiceDetailPOJO> OrderDetails) throws Exception {
    public HttpEntity<byte[]> createPdf(Map<String, Object> data) throws Exception {
        // Initialize Thymeleaf template engine
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setCacheable(false);
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setForceTemplateMode(true);
        templateEngine.setTemplateResolver(templateResolver);


        LocalDate currentDate = LocalDate.now();
        // Define the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        // Format the current date
        String formattedDate = currentDate.format(formatter);


        // Prepare Thymeleaf context with dynamic values
        Context ctx = new Context();

        // Extract data from responseMap
        String customerName ="Rob & Joe Traders";
        double totalPrice = (double) data.get("totalPrice");
        String cartId = (String) data.get("cartId");
        List<Map<String, Object>> productDetails = (List<Map<String, Object>>) data.get("productDetails");
        String paymentStatus = (String) data.get("paymentStatus");
        int itemCount = (int) data.get("itemCount");

        // Set variables for the template
        ctx.setVariable("customerName", customerName);
        ctx.setVariable("invoiceIssueDate", formattedDate);
        ctx.setVariable("totalPrice", totalPrice);
        ctx.setVariable("cartId", cartId);
        ctx.setVariable("paymentStatus", paymentStatus);
        ctx.setVariable("itemCount", itemCount);
        ctx.setVariable("productDetails", productDetails);

        // Process HTML template
        String html = templateEngine.process("invoiceslip", ctx);

        // Convert HTML to PDF
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//        HtmlConverter.convertToPdf(html, buffer);
        com.itextpdf.html2pdf.ConverterProperties properties = new com.itextpdf.html2pdf.ConverterProperties();
        properties.setBaseUri("/path/to/html");
        com.itextpdf.html2pdf.HtmlConverter.convertToPdf(html, buffer, properties);

        byte[] pdfAsBytes = buffer.toByteArray();

        // Set HTTP headers and return PDF
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_PDF);
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Invoice.pdf");
        header.setContentLength(pdfAsBytes.length);

        return new HttpEntity<>(pdfAsBytes, header);
    }

    String getOrderSKUStatusName(int statusId) {
        String orderSKUStatus = "";
        switch (statusId) {
            case 1:
                orderSKUStatus = "Pending";
                break;
            case 2:
                orderSKUStatus = "In-Transit";
                break;
            case 3:
                orderSKUStatus = "Delivered";
                break;
            case 4:
                orderSKUStatus = "Not-Delivered";
                break;
            default:
                break;

        }
        return orderSKUStatus;
    }
}