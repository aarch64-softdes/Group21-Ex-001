package com.tkpm.sms.infrastructure.service;

import com.tkpm.sms.application.service.interfaces.DocumentTemplateProcessingService;
import com.tkpm.sms.domain.exception.ErrorCode;
import com.tkpm.sms.domain.exception.FileProcessingException;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentTemplateProcessingServiceImpl implements DocumentTemplateProcessingService {

    private final ResourceLoader resourceLoader;

    @Override
    public InputStream loadTemplate(String templatePath) {
        try {
            return resourceLoader.getResource("classpath:" + templatePath).getInputStream();
        } catch (IOException e) {
            log.error("Failed to load template from path: {}", templatePath, e);
            throw new FileProcessingException("Failed to load template", ErrorCode.FAIL_TO_EXPORT_FILE);
        }
    }

    @Override
    public byte[] processTemplateAsPdf(String templatePath, Map<String, Object> data) {
        boolean isDocx = templatePath.endsWith(".docx");
        InputStream templateInputStream = loadTemplate(templatePath);

        byte[] processedDocument = processTemplate(templateInputStream, data, isDocx);
        return convertToPdf(processedDocument, isDocx);
    }

    @Override
    public byte[] processTemplate(InputStream templateInputStream, Map<String, Object> data, boolean isDocx) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(
                    templateInputStream,
                    isDocx ? TemplateEngineKind.Freemarker : TemplateEngineKind.Velocity);

            IContext context = report.createContext();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                context.put(entry.getKey(), entry.getValue());
            }

            report.process(context, out);
            return out.toByteArray();
        } catch (IOException | XDocReportException e) {
            log.error("Failed to process template", e);
            throw new FileProcessingException("Failed to process template", ErrorCode.FAIL_TO_EXPORT_FILE);
        }
    }

    @Override
    public byte[] convertToPdf(byte[] document, boolean isDocx) {
        if (isDocx) {
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(document);
                    XWPFDocument docxDocument = new XWPFDocument(inputStream);
                    ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream()) {

                PdfOptions options = PdfOptions.create();
                PdfConverter.getInstance().convert(docxDocument, pdfOutputStream, options);

                return pdfOutputStream.toByteArray();
            } catch (IOException e) {
                log.error("Failed to convert DOCX to PDF", e);
                throw new FileProcessingException("Failed to convert DOCX to PDF", ErrorCode.FAIL_TO_EXPORT_FILE);
            }
        } else {
            // Handle ODT conversion
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(document);
                    ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream()) {

                IXDocReport report = XDocReportRegistry.getRegistry().loadReport(inputStream,
                        TemplateEngineKind.Freemarker);

                Options options = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.ODFDOM);
                report.convert(report.createContext(), options, pdfOutputStream);

                return pdfOutputStream.toByteArray();
            } catch (IOException | XDocReportException e) {
                log.error("Failed to convert ODT to PDF", e);
                throw new FileProcessingException("Failed to convert ODT to PDF", ErrorCode.FAIL_TO_EXPORT_FILE);
            }
        }
    }
}