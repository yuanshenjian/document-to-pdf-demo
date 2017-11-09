package cc.sjyuan.springboothtmlpdf;

import cc.sjyuan.springboothtmlpdf.model.Invoice;
import cc.sjyuan.springboothtmlpdf.model.InvoiceRow;
import cc.sjyuan.springboothtmlpdf.model.User;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PDFGenerator {

    public static void main(String[] args) throws Exception {
//        odtToPDFWithFreemarker();
//        odtToPDFWithVelocity();
        docToPDF();
//        documentToPDFWithAspose();
    }


    public static void documentToPDFWithAspose() throws Exception {
//        Document doc = new Document("/Users/sjyuan/Personal-sjyuan/IdeaProjects/springboot-html-pdf/src/main/resources/quotation.docx");
        Document doc = new Document("/Users/sjyuan/Personal-sjyuan/IdeaProjects/springboot-html-pdf/src/main/resources/quotation.odt");
        doc.save("Aspose_DocToPDF.pdf", SaveFormat.PDF); //Save the document in PDF format.
    }

    public static void docToPDF() throws IOException, XDocReportException {
        XWPFDocument document = new XWPFDocument(PDFGenerator.class.getClassLoader().getResourceAsStream("quotation.docx"));
        File outFile = new File("quotation.pdf");
        OutputStream out = new FileOutputStream(outFile);
        PdfOptions options = null;
        PdfConverter.getInstance().convert(document, out, options);
    }

    public static void odtToPDFWithVelocity() throws IOException, XDocReportException {
        InputStream in = PDFGenerator.class.getClassLoader().getResourceAsStream("tmpl-velocity-new.odt");
        IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity);

        IContext ctx = report.createContext();

        Invoice invoice = getInvoice();

        User sender = getUser();

        User to = new User();
        to.setEmail("sjyuan1@thoughtworks.com");
        to.setAddress("软件园to");
        to.setName("袁慎建");

        ctx.put("invoice", invoice);
        ctx.put("StringUtils", StringUtils.class);

        ctx.put("to", to);
        ctx.put("sender", sender);

        List<InvoiceRow> rows = new ArrayList<>();
        rows.add(getInvoiceRow());
        rows.add(getInvoiceRow());
        rows.add(getInvoiceRow());
        rows.add(getInvoiceRow());
        rows.add(getInvoiceRow());
        rows.add(getInvoiceRow());
        rows.add(getInvoiceRow());
        rows.add(getInvoiceRow());
        rows.add(getInvoiceRow());
        ctx.put("rows", rows);

        // Write the PDF file to output stream
        Options options = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.ODFDOM);
        OutputStream out = new FileOutputStream(new File("test-pdf-velocity.pdf"));
        report.convert(ctx, options, out);
        out.close();
        System.exit(0);

    }

    private static InvoiceRow getInvoiceRow() {
        InvoiceRow invoiceRow = new InvoiceRow();
        invoiceRow.setDescription("invoiceRow1");
        invoiceRow.setQuantity(2.0);
        invoiceRow.setUnit("个");
        invoiceRow.setPrice(19.0);
        return invoiceRow;
    }

    public static void odtToPDFWithFreemarker() throws IOException, XDocReportException {
        InputStream in = PDFGenerator.class.getClassLoader().getResourceAsStream("tmpl-freemarker.odt");
        IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Freemarker);
        IContext ctx = report.createContext();

        Invoice invoice = getInvoice();

        User sender = getUser();

        User to = new User();
        to.setEmail("sjyuan1@thoughtworks.com");
        to.setAddress("软件园to");
        to.setName("sjyuan");

        ctx.put("invoice", invoice);
        ctx.put("to", to);
        ctx.put("sender", sender);

        List<InvoiceRow> rows = new ArrayList<>();
        rows.add(getInvoiceRow());
        rows.add(getInvoiceRow());
        rows.add(getInvoiceRow());
        rows.add(getInvoiceRow());
        rows.add(getInvoiceRow());
        rows.add(getInvoiceRow());
        rows.add(getInvoiceRow());
        ctx.put("rows", rows);

        // Write the PDF file to output stream
        Options options = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.ODFDOM);
        OutputStream out = new FileOutputStream(new File("test-pdf-freemarker.pdf"));
        report.convert(ctx, options, out);
        out.close();
        System.exit(0);
    }

    private static User getUser() {
        User sender = new User();
        sender.setName("sendersjyuan");
        sender.setEmail("sjyuan@thoughtworks.com");
        sender.setAddress("软件园");
        sender.setPhone("18192235667");
        return sender;
    }

    private static Invoice getInvoice() {
        Invoice invoice = new Invoice();
        invoice.setInvoiceDate("20171109");
        invoice.setInvoiceNumber("001");
        invoice.setTotal(90.0);
        return invoice;
    }
}
