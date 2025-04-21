package com.tkpm.sms.infrastructure.service;

import com.tkpm.sms.domain.exception.ErrorCode;
import com.tkpm.sms.domain.exception.FileProcessingException;

import org.xhtmlrenderer.pdf.ITextRenderer;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
// import org.jsoup.nodes.Document;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DocumentTemplateProcessingService {

    ResourceLoader resourceLoader;
    TemplateEngine templateEngine;

    public DocumentTemplateProcessingService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.templateEngine = new TemplateEngine();
    }

    public InputStream loadTemplate(String templatePath) {
        try {
            return resourceLoader.getResource("classpath:" + templatePath).getInputStream();
        } catch (IOException e) {
            log.error("Failed to load template from path: {}", templatePath, e);
            throw new FileProcessingException("Failed to load template", ErrorCode.FAIL_TO_EXPORT_FILE);
        }
    }

    public String convertDocumentToHtml(byte[] documentBytes, String sourceFormat) {
        if (sourceFormat.endsWith(".html")) {
            try {
                return new String(documentBytes, "UTF-8");
            } catch (IOException e) {
                log.error("Failed to convert HTML bytes to string", e);
                throw new FileProcessingException("Failed to process HTML", ErrorCode.FAIL_TO_EXPORT_FILE);
            }
        } else if (sourceFormat.endsWith(".docx")) {
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(documentBytes);
                    XWPFDocument document = new XWPFDocument(inputStream);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                XHTMLOptions options = XHTMLOptions.create();
                XHTMLConverter.getInstance().convert(document, outputStream, options);

                return outputStream.toString("UTF-8");
            } catch (IOException e) {
                log.error("Failed to convert DOCX to HTML", e);
                throw new FileProcessingException("Failed to convert DOCX to HTML",
                        ErrorCode.FAIL_TO_EXPORT_FILE);
            }
        } else if (sourceFormat.endsWith(".odt")) {
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(documentBytes);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                IXDocReport report = XDocReportRegistry.getRegistry().loadReport(inputStream,
                        TemplateEngineKind.Velocity);

                Options options = Options.getTo(ConverterTypeTo.XHTML).via(ConverterTypeVia.ODFDOM);
                report.convert(report.createContext(), options, outputStream);

                return outputStream.toString("UTF-8");
            } catch (IOException | XDocReportException e) {
                log.error("Failed to convert ODT to HTML", e);
                throw new FileProcessingException("Failed to convert ODT to HTML",
                        ErrorCode.FAIL_TO_EXPORT_FILE);
            }
        } else {
            log.error("Unsupported source format for HTML conversion: {}", sourceFormat);
            throw new FileProcessingException("Unsupported source format for HTML conversion",
                    ErrorCode.FAIL_TO_EXPORT_FILE);
        }
    }

    public String processHtmlTemplate(String htmlContent, Map<String, Object> data) {
        try {
            log.info("Processing HTML template with Thymeleaf...");
            var document = Jsoup.parse(htmlContent);

            org.thymeleaf.context.Context thymeleafContext = new org.thymeleaf.context.Context();
            data.forEach(thymeleafContext::setVariable);
            return templateEngine.process(document.html(), thymeleafContext);
        } catch (Exception e) {
            log.error("Failed to process HTML template", e);
            throw new FileProcessingException("Failed to process HTML template", ErrorCode.FAIL_TO_EXPORT_FILE);
        }
    }

    public byte[] convertHtmlToPdf(String htmlContent) {
        try {
            log.info("Converting HTML to PDF using Flying Saucer");
            log.debug("HTML content to convert: {}", htmlContent.substring(0, Math.min(500, htmlContent.length())));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            org.jsoup.nodes.Document doc = Jsoup.parse(htmlContent);

            doc.outputSettings()
                    .syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml)
                    .escapeMode(org.jsoup.nodes.Entities.EscapeMode.xhtml);
            doc.select("html").first().attr("xmlns", "http://www.w3.org/1999/xhtml");

            String html = doc.outerHtml();

            ITextRenderer renderer = new ITextRenderer();

            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(baos);

            byte[] pdfBytes = baos.toByteArray();
            log.info("Successfully converted HTML to PDF, size: {} bytes",
                    pdfBytes.length);

            return pdfBytes;
        } catch (Exception e) {
            log.error("Failed to convert HTML to PDF: {}", e.getMessage());
            log.error("Error details:", e);
            throw new FileProcessingException("Failed to convert HTML to PDF", ErrorCode.FAIL_TO_EXPORT_FILE);
        }
    }

    public byte[] processTemplateAsHtmlToPdf(String templatePath, Map<String, Object> data) {
        log.info("Processing template {} as HTML to PDF", templatePath);
        String fileExtension = templatePath.substring(templatePath.lastIndexOf("."));

        try (InputStream is = loadTemplate(templatePath)) {
            byte[] documentBytes = is.readAllBytes();

            String html = convertDocumentToHtml(documentBytes, fileExtension);
            String processedHtml = processHtmlTemplate(html, data);
            return convertHtmlToPdf(processedHtml);
        } catch (IOException e) {
            log.error("Failed to process template as HTML to PDF", e);
            throw new FileProcessingException("Failed to process template as HTML to PDF",
                    ErrorCode.FAIL_TO_EXPORT_FILE);
        }
    }
}