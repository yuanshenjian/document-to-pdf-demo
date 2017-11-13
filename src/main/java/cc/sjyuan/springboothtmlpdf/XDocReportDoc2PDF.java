package cc.sjyuan.springboothtmlpdf;

import cc.sjyuan.springboothtmlpdf.model.Invoice;
import cc.sjyuan.springboothtmlpdf.model.InvoiceRow;
import cc.sjyuan.springboothtmlpdf.model.User;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class XDocReportDoc2PDF {

    public static void main(String[] args) throws Exception {
        InputStream in = XDocReportDoc2PDF.class.getClassLoader().getResourceAsStream("input/xdocreport-input.odt");
        OutputStream out = new FileOutputStream(new File("src/main/resources/output/xdocreport-output.pdf"));

        odtToPDFWithVelocity(in, out);

        out.close();
        System.exit(0);
    }


    public static void odtToPDFWithVelocity(InputStream in, OutputStream out) throws Exception {
        IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity);
        IContext ctx = report.createContext();

        Invoice invoice = generateInvoice();
        User sender = generateUser();
        User to = generateUser();

        ctx.put("invoice", invoice);
        ctx.put("StringUtils", StringUtils.class);
        ctx.put("to", to);
        ctx.put("sender", sender);

        List<InvoiceRow> rows = new ArrayList<>();
        rows.add(generateInvoiceRow());
        rows.add(generateInvoiceRow());
        rows.add(generateInvoiceRow());
        rows.add(generateInvoiceRow());
        rows.add(generateInvoiceRow());
        rows.add(generateInvoiceRow());
        rows.add(generateInvoiceRow());
        rows.add(generateInvoiceRow());
        rows.add(generateInvoiceRow());
        ctx.put("rows", rows);

        // Replace template, output .pdf
        Options options = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.ODFDOM);
        report.convert(ctx, options, out);


        // Only replace template, output .odt
//        OutputStream out = new FileOutputStream(new File("test-pdf-velocity-new.odt"));
//        report.process(ctx, out);
    }

    private static InvoiceRow generateInvoiceRow() {
        InvoiceRow invoiceRow = new InvoiceRow();
        invoiceRow.setDescription("invoiceRow1");
        invoiceRow.setQuantity(2.0);
        invoiceRow.setUnit("个");
        invoiceRow.setPrice(19.0);
        return invoiceRow;
    }

    private static User generateUser() {
        User sender = new User();
        sender.setName("sendersjyuan");
        sender.setEmail("sjyuan@thoughtworks.com");
        sender.setAddress("软件园");
        sender.setPhone("18192235667");
        return sender;
    }

    private static Invoice generateInvoice() {
        Invoice invoice = new Invoice();
        invoice.setInvoiceDate("20171109");
        invoice.setInvoiceNumber("001");
        invoice.setTotal(90.0);
        return invoice;
    }
}
