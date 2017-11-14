# 文档转换示例工程(Document -> PDF)

## 简介
此工程是一个示例工程，用于演示`Document`到`PDF`的转换。其中`Document`包括了`doc`、`docx`、`odt`。转换前需要将模板待替换的内容使用 [Velocity](http://velocity.apache.org/) 模板引擎进行替换。


## Spike解决方案
针对开源的库doc4xj、xdocreport、jodconverter以及非开源的库aspose进行了Spike，每种解决方案各有利弊。

### docx4j(docx -> pdf)
- 优点：
	- 依赖简单

- 缺点：
	- 转换效果不堪入目，果断不采取

### xdocreport + xwpf(docx -> pdf)
- 优点：
	- 依赖简单，可以单独运行在JVM

- 缺点：
	- 模板替换率不高。
	- 转换后中文丢失。

### xdocreport + odfdom(odt -> pdf)
- 优点：
	- 依赖简单，可以单独运行在JVM
	- 模板替换率高。

- 缺点：
	- 转换后中文丢失。

### jodconverter + openoffice(odt -> pdf)
- 优点：
	- 转换率高。
	- 可以使用Openoffice客户端可视化编辑。

- 缺点：
	- 依赖额外的OpenOffice服务，而服务又不稳定。
	- jodconverter 没有继续维护。

### [aspose](https://www.aspose.com/)(doc, docx, odt -> pdf)
- 优点：
	- 可以转换多种格式，转换率高。
	- 开发代码很少，易于集成。

- 缺点：
	- 收费昂贵，单开发者单部署的标准Support就收费 $1599
	- 黑盒转换，团队无法自己控制，依赖官方团队。


## 示例代码

### docx4j
Dependency

```groovy
dependencies {
	compile ('org.docx4j:docx4j:2.8.1')
}
```

Java Code

```java
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

// *********Take 8934 ms*********
```


### xdocreport + xwpf

Dependency

```groovy
dependencies{
    compile('fr.opensagres.xdocreport:fr.opensagres.xdocreport.template.velocity:2.0.1')
    compile('fr.opensagres.xdocreport:fr.opensagres.xdocreport.document.docx:2.0.1')
    compile('fr.opensagres.xdocreport:fr.opensagres.poi.xwpf.converter.pdf:2.0.1')
}
```

Java Code

```java
public class XWPFDoc2PDF {
    public static void main(String[] args) throws Exception {
        InputStream in = XWPFDoc2PDF.class.getClassLoader().getResourceAsStream("input/xwpf-input.docx");
        File outputFile = new File("src/main/resources/output/xwpf-output.docx");
        OutputStream out = new FileOutputStream(outputFile);

        long start = System.currentTimeMillis();
        replaceTemplate(in, out);
        out.close();

        InputStream in1 = XWPFDoc2PDF.class.getClassLoader().getResourceAsStream("output/xwpf-output.docx");
        OutputStream out1 = new FileOutputStream(new File("src/main/resources/output/xwpf-output.pdf"));
        docToPDF(in1, out1);
        out1.close();
        System.err.println("*********Take " + (System.currentTimeMillis() - start) + " ms*********");
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
// *********Take 4756 ms*********
```


### xdocreport + odfdom

Dependency

```groovy
dependencies{
	compile('fr.opensagres.xdocreport:fr.opensagres.xdocreport.document.odt:2.0.1')
	compile('fr.opensagres.xdocreport:fr.opensagres.xdocreport.template.velocity:2.0.1')
	compile('fr.opensagres.xdocreport:fr.opensagres.xdocreport.converter.odt.odfdom:2.0.1')
}
```

Java Code

```java
public class XDocReportDoc2PDF {
    public static void main(String[] args) throws Exception {
        InputStream in = XDocReportDoc2PDF.class.getClassLoader().getResourceAsStream("input/xdocreport-input.odt");
        OutputStream out = new FileOutputStream(new File("src/main/resources/output/xdocreport-output.pdf"));
        long start = System.currentTimeMillis();
        odtToPDFWithVelocity(in, out);
        out.close();
        System.err.println("*********Take " + (System.currentTimeMillis() - start) + " ms*********");
        System.exit(0);
    }
   
    private static void odtToPDFWithVelocity(InputStream in, OutputStream out) throws Exception {
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
        ctx.put("rows", rows);

        Options options = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.ODFDOM);
        report.convert(ctx, options, out);

    }
}
// *********Take 13696 ms*********
```

### jodconverter + openoffice

Dependency

```groovy
dependencies{
    compile files('libs/jodconverter-core-3.0-beta-4.jar')
}
```
> jodconverter依赖下载地址: [https://code.google.com/archive/p/jodconverter/downloads](https://code.google.com/archive/p/jodconverter/downloads)


Java Code

```java
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
```

1. 安装 [Openoffice](https://www.openoffice.org/download/)

2. 进入安装目录

	- `$ /Applications/OpenOffice.app/Contents/program`

3. 启动服务

	- `$ ./soffice -headless -accept="socket,host=127.0.0.1,port=8100;urp;" -nofirststartwizard`


### Aspose

Dependency

```groovy
dependencies{
	compile files('libs/aspose-words-17.10-jdk16.jar')
}
```

> aspose依赖下载地址: [https://www.aspose.com/community/getting-started.aspx](https://www.aspose.com/community/getting-started.aspx)


Java Code

```java
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
```

## 另辟蹊径(XHTML/HTML -> PDF)

### thymeleaf + flying-saucer-pdf?

- 优点
	- 利于开发者维护。
	- 可以并行修改文件。
	- 待挖掘。
- 缺点
	- 商业合同对尺寸要求苛刻，css样式较难调整。
	- 待挖掘。


### thymeleaf + wkhtmltopdf?
- 优点
	- 利于开发者维护。
	- 可以并行修改文件。
	- 已经有成功的生产实践。
- 缺点
	- 合同对尺寸要求苛刻，css样式较难调整。
	- 待挖掘。


## 延伸阅读

- [How to convert docx/odt to pdf/html with Java?](https://angelozerr.wordpress.com/2012/12/06/how-to-convert-docxodt-to-pdfhtml-with-java/)
- [How to Run LibreOffice in AWS Lambda for Dirty-Cheap PDFs at Scale](https://hackernoon.com/how-to-run-libreoffice-in-aws-lambda-for-dirty-cheap-pdfs-at-scale-b2c6b3d069b4)





