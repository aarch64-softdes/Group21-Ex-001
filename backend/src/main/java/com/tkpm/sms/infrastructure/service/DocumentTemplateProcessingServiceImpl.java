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
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.local.JodConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DocumentTemplateProcessingServiceImpl implements DocumentTemplateProcessingService {

    ResourceLoader resourceLoader;
    String configuredLibreOfficeHome;

    public DocumentTemplateProcessingServiceImpl(@Value("${app.libreoffice.home:}") String configuredLibreOfficeHome,
            ResourceLoader resourceLoader) {
        this.configuredLibreOfficeHome = configuredLibreOfficeHome;
        this.resourceLoader = resourceLoader;
    }

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
        String fileExtension = templatePath.substring(templatePath.lastIndexOf("."));
        try (InputStream is = loadTemplate(templatePath)) {
            switch (fileExtension) {
                case ".xlsx":
                    byte[] processedTemplate = processExcelTemplate(is.readAllBytes(), data);
                    return convertExcelToPdf(processedTemplate);
                case ".docx":
                case ".odt":
                    boolean isDocx = fileExtension.equals(".docx");
                    InputStream templateInputStream = loadTemplate(templatePath);
                    byte[] processedDocument = processDocumentTemplate(templateInputStream, data, isDocx);
                    return convertDocumentToPdf(processedDocument, isDocx);
                default:
                    log.error("Unsupported file extension: {}", fileExtension);
                    throw new FileProcessingException("Unsupported file format",
                            ErrorCode.FAIL_TO_EXPORT_FILE);
            }
        } catch (IOException e) {
            log.error("Failed to process {} template as PDF", fileExtension, e);
            throw new FileProcessingException("Failed to process template as PDF",
                    ErrorCode.FAIL_TO_EXPORT_FILE);
        }

    }

    @Override
    public byte[] processDocumentTemplate(InputStream templateInputStream, Map<String, Object> data, boolean isDocx) {
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
    public byte[] convertDocumentToPdf(byte[] document, boolean isDocx) {
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

    @Override
    public byte[] processExcelTemplate(byte[] excelTemplate, Map<String, Object> data) {
        log.info("Processing Excel template with JXLS 2.14.0...");
        try (InputStream templateStream = new ByteArrayInputStream(excelTemplate);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Context context = new Context();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                context.putVar(entry.getKey(), entry.getValue());
            }

            JxlsHelper.getInstance().processTemplate(templateStream, outputStream, context);
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error("Failed to process Excel template", e);
            throw new FileProcessingException("Failed to process Excel template", ErrorCode.FAIL_TO_EXPORT_FILE);
        }
    }

    @Override
    public byte[] convertExcelToPdf(byte[] excelBytes) {
        try {
            // Create Office Manager for LibreOffice
            OfficeManager officeManager = LocalOfficeManager.builder()
                    .install()
                    .officeHome(getLibreOfficeHome())
                    .build();

            try {
                officeManager.start();

                Path tempInputFile = Files.createTempFile("document_", ".xlsx");
                Path tempOutputFile = Files.createTempFile("document_", ".pdf");

                try {
                    Files.write(tempInputFile, excelBytes);
                    JodConverter.convert(tempInputFile.toFile())
                            .to(tempOutputFile.toFile())
                            .execute();

                    return Files.readAllBytes(tempOutputFile);
                } finally {
                    try {
                        Files.deleteIfExists(tempInputFile);
                        Files.deleteIfExists(tempOutputFile);
                    } catch (IOException e) {
                        log.warn("Failed to delete temporary files", e);
                    }
                }
            } finally {
                officeManager.stop();
            }
        } catch (Exception e) {
            log.error("Error converting Excel to PDF", e);
            throw new FileProcessingException("Failed to convert Excel to PDF", ErrorCode.FAIL_TO_EXPORT_FILE);
        }
    }

    public byte[] convertDocumentToPdf(byte[] documentBytes, String sourceFormat) {
        if (sourceFormat.endsWith(".xlsx")) {
            return convertExcelToPdf(documentBytes);
        } else {
            boolean isDocx = sourceFormat.endsWith(".docx");
            return convertDocumentToPdf(documentBytes, isDocx);
        }
    }

    private String getLibreOfficeHome() {
        final String DEFAULT_LIBREOFFICE_WINDOWS_PATH = "C:/Program Files/LibreOffice";
        final String DEFAULT_LIBREOFFICE_MAC_PATH = "/Applications/LibreOffice.app";
        final String DEFAULT_LIBREOFFICE_LINUX_PATH = "/usr/lib/libreoffice";

        if (configuredLibreOfficeHome != null && !configuredLibreOfficeHome.isEmpty()) {
            if (Files.exists(Paths.get(configuredLibreOfficeHome))) {
                return configuredLibreOfficeHome;
            }
            log.warn("Configured LibreOffice home does not exist: {}, using default paths", configuredLibreOfficeHome);
        }

        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            return DEFAULT_LIBREOFFICE_WINDOWS_PATH;
        } else if (os.contains("mac")) {
            return DEFAULT_LIBREOFFICE_MAC_PATH;
        } else {
            String[] possiblePaths = {
                    "/usr/lib/libreoffice",
                    "/usr/lib64/libreoffice",
                    "/opt/libreoffice",
                    "/usr/lib/openoffice",
                    "/usr/lib64/openoffice"
            };

            for (String path : possiblePaths) {
                if (Files.exists(Paths.get(path))) {
                    return path;
                }
            }

            return DEFAULT_LIBREOFFICE_LINUX_PATH;
        }
    }
}