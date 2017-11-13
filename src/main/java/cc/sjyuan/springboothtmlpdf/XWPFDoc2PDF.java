package cc.sjyuan.springboothtmlpdf;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class XWPFDoc2PDF {

    public static void main(String[] args) throws Exception {
        InputStream in = XWPFDoc2PDF.class.getClassLoader().getResourceAsStream("input/xwpf-input.docx");
        File outputFile = new File("src/main/resources/output/xwpf-output.docx");
        OutputStream out = new FileOutputStream(outputFile);
        replaceTemplate(in, out);
        out.close();

        InputStream in1 = XWPFDoc2PDF.class.getClassLoader().getResourceAsStream("output/xwpf-output.docx");
        OutputStream out1 = new FileOutputStream(new File("src/main/resources/output/xwpf-output.pdf"));
        docToPDF(in1, out1);
        out1.close();

        System.exit(0);
    }

    private static void replaceTemplate(InputStream in, OutputStream out) throws Exception {
        IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity);
        IContext context = report.createContext();
        context.put("current_date", "2017-09-09");
        context.put("buyer_name", "袁慎建");
        report.process(context, out);
        out.close();
    }

    public static void docToPDF(InputStream in, OutputStream out) throws Exception {
        XWPFDocument document = new XWPFDocument(in);

        PdfOptions options = PdfOptions.create().fontEncoding("utf-8");
        PdfConverter.getInstance().convert(document, out, options);
    }
}
