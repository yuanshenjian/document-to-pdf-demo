package cc.sjyuan.springboothtmlpdf.converter;

import org.docx4j.convert.out.pdf.PdfConversion;
import org.docx4j.convert.out.pdf.viaXSLFO.PdfSettings;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Doc4JDoc2PDF {
    public static void main(String[] args) throws Exception {
        InputStream in = Doc4JDoc2PDF.class.getClassLoader().getResourceAsStream("input/doc4J-input.docx");
        OutputStream out = new FileOutputStream(new File("src/main/resources/output/doc4J-output.pdf"));
        long start = System.currentTimeMillis();
        createPDF(in, out);
        out.close();
        System.err.println("*********Take " + (System.currentTimeMillis() - start) + " ms*********");
    }

    private static void createPDF(InputStream inputStream, OutputStream out) throws Exception {

        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(inputStream);

        PdfSettings pdfSettings = new PdfSettings();
        PdfConversion converter = new org.docx4j.convert.out.pdf.viaXSLFO.Conversion(
                wordMLPackage);
        converter.output(out, pdfSettings);
    }
}