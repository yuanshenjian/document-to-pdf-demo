# 文档转换示例工程(Document -> PDF)

## 简介
此工程是一个示例工程，用于演示`Document`到`PDF`的转换。其中`Document`包括了`doc`、`docx`、`odt`。转换前需要将模板待替换的内容使用 [Velocity](http://velocity.apache.org/) 模板引擎进行替换。


## 解决方案
针对开源的库Doc4j、XDocReport、Jodoconverter以及非开源的库Aspose进行了Spike，每种解决方案各有利弊。


### Doc4j
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

效果

```
1. 转换效果不堪入目，不推荐使用。
```

### XDocreport + xwpf

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

效果

```
1. 模板替换效果差，很多变量识别不了。
2. 样式绝大部分中文丢失，英文可以转换。
3. 字体样式有变化。
```


### XDocReport + odfdom

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
// *********Take 13696 ms*********
```

效果

```
1. 模板替换良好，支持循环和条件判断。
2. 样式绝大部分中文丢失，英文可以转换。
3. 字体样式有变化。
```

### Jodocconverter + openoffice




### Aspose




