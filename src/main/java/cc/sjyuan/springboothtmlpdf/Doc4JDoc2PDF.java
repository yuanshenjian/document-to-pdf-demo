package cc.sjyuan.springboothtmlpdf;

import org.docx4j.convert.out.pdf.PdfConversion;
import org.docx4j.convert.out.pdf.viaXSLFO.PdfSettings;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.*;

public class Doc4JDoc2PDF {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream in = Doc4JDoc2PDF.class.getClassLoader().getResourceAsStream("input/doc4J-input.docx");
        OutputStream out = new FileOutputStream(new File("src/main/resources/output/doc4J-output.pdf"));
        createPDF(in, out);
    }

    private static void createPDF(InputStream inputStream, OutputStream out) {
        try {
            long start = System.currentTimeMillis();
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(inputStream);

            PdfSettings pdfSettings = new PdfSettings();
            PdfConversion converter = new org.docx4j.convert.out.pdf.viaXSLFO.Conversion(
                    wordMLPackage);
            converter.output(out, pdfSettings);

            System.err.println("Generate pdf/HelloWorld.pdf with "
                    + (System.currentTimeMillis() - start) + "ms");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}