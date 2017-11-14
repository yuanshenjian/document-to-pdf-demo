package cc.sjyuan.springboothtmlpdf.converter;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;

import java.io.File;

public class OpenOfficeToPDF {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
        configuration.setPortNumber(8100);
        configuration.setRetryTimeout(1000);
        configuration.setOfficeHome("/Applications/OpenOffice.app/Contents");
        OfficeManager officeManager = configuration.buildOfficeManager();
        OfficeDocumentConverter documentConverter = new OfficeDocumentConverter(officeManager);
        officeManager.start();

        File sourceFile = new File("/Users/sjyuan/Personal-sjyuan/IdeaProjects/springboot-html-pdf/src/main/resources/input/openoffice-input.odt");
        File outputFile = new File("src/main/resources/output/openoffice-output.pdf");
        documentConverter.convert(sourceFile, outputFile);

        officeManager.stop();
        System.err.println("*********Take " + (System.currentTimeMillis() - start) + " ms *********");
    }
}