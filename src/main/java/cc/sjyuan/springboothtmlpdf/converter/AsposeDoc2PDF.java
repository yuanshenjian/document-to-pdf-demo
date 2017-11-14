package cc.sjyuan.springboothtmlpdf.converter;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;

public class AsposeDoc2PDF {
    public static void main(String[] args) throws Exception {
        documentToPDFWithAspose("/Users/sjyuan/Personal-sjyuan/IdeaProjects/springboot-html-pdf/src/main/resources/input/aspose-input.odt",
                "src/main/resources/output/aspose-output.pdf");
    }

    public static void documentToPDFWithAspose(String absoluteSourceFilePath, String savedFilePath) throws Exception {
        Document doc = new Document(absoluteSourceFilePath);
        doc.save(savedFilePath, SaveFormat.PDF); //Save the document in PDF format.
    }
}
