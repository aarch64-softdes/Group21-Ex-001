package com.tkpm.sms.application.service.interfaces;

import java.io.InputStream;
import java.util.Map;

public interface DocumentTemplateProcessingService {
    /**
     * Loads a template file from the given path
     * 
     * @param templateFilePath path to the template file
     * @return InputStream of the template
     */
    InputStream loadTemplate(String templateFilePath);

    /**
     * Converts a document to HTML format
     *
     * @param documentBytes document to be converted
     * @param sourceFormat  the source document format extension (.docx, .odt)
     * @return HTML content as string
     */
    String convertDocumentToHtml(byte[] documentBytes, String sourceFormat);

    /**
     * Processes HTML content with template data
     *
     * @param htmlContent HTML content with template placeholders
     * @param data        data to merge into the template
     * @return processed HTML content
     */
    String processHtmlTemplate(String htmlContent, Map<String, Object> data);

    /**
     * Converts HTML to PDF
     *
     * @param htmlContent HTML content to convert
     * @return PDF as byte array
     */
    byte[] convertHtmlToPdf(String htmlContent);

    /**
     * Process template as HTML and convert to PDF
     * 
     * @param templatePath path to the template file
     * @param data         data to be used in the template
     * @return PDF as byte array
     */
    byte[] processTemplateAsHtmlToPdf(String templatePath, Map<String, Object> data);
}