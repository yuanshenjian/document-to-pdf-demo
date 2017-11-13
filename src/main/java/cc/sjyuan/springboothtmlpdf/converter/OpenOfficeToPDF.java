package cc.sjyuan.springboothtmlpdf.converter;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;

import java.io.File;

public class OpenOfficeToPDF {

    public static void main(String[] args) throws Exception {
        DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
        configuration.setPortNumber(8100);
        configuration.setRetryTimeout(1000);
        configuration.setOfficeHome("/Applications/OpenOffice.app/Contents");
        OfficeManager officeManager = configuration.buildOfficeManager();
        OfficeDocumentConverter documentConverter = new OfficeDocumentConverter(officeManager);
        officeManager.start();

        File sourceFile = new File("/Users/sjyuan/Personal-sjyuan/IdeaProjects/springboot-html-pdf/src/main/resources/input/openoffice-input.odt");
        File outputFile = new File("src/main/resources/output/openoffice-output.pdf");
        createPDF(documentConverter, sourceFile, outputFile);

        officeManager.stop();
    }

    private static void createPDF(OfficeDocumentConverter converter, File sourceFile, File outputFile) throws Exception {
        long start = System.currentTimeMillis();
        converter.convert(sourceFile, outputFile);
        System.err.println("Generate pdf/HelloWorld.pdf with "
                + (System.currentTimeMillis() - start) + "ms");
    }
}