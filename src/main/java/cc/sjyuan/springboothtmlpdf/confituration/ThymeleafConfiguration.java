package cc.sjyuan.springboothtmlpdf.confituration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class ThymeleafConfiguration {
    @Bean
    public ClassLoaderTemplateResolver contractTemplateResolver() {
        ClassLoaderTemplateResolver contractTemplateResolver = new ClassLoaderTemplateResolver();
        contractTemplateResolver.setPrefix("templates/");
        contractTemplateResolver.setTemplateMode("XHTML");
        contractTemplateResolver.setSuffix(".html");
        contractTemplateResolver.setCharacterEncoding("UTF-8");
        contractTemplateResolver.setOrder(1);
        return contractTemplateResolver;
    }
}