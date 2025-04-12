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
     * Processes a template with the given data and returns the result as PDF
     * 
     * @param templateFilePath path to the template file
     * @param data             data to be used in the template
     * @return byte array of the generated PDF
     */
    byte[] processTemplateAsPdf(String templateFilePath, Map<String, Object> data);

    /**
     * Processes a `docx` or `odt` template with the given data
     * 
     * @param templateInputStream template input stream
     * @param data                data to be used in the template
     * @param isDocx              true if template is docx, false if odt
     * @return byte array of the processed document
     */
    byte[] processDocumentTemplate(InputStream templateInputStream, Map<String, Object> data, boolean isDocx);

    /**
     * Converts a `docx` or `odt` document to PDF.
     * 
     * @param document document to be converted
     * @param isDocx   true if document is docx, false if odt
     * @return byte array of the PDF
     */
    byte[] convertDocumentToPdf(byte[] document, boolean isDocx);

    /**
     * Processes an Excel template with the given data
     * 
     * @param excelTemplate Excel template as byte array
     * @param data          data to be used in the template
     * @return byte array of the processed Excel document
     */
    public byte[] processExcelTemplate(byte[] excelTemplate, Map<String, Object> data);

    /**
     * Converts an Excel document to PDF
     * 
     * @param excelBytes Excel document as byte array
     * @return byte array of the PDF
     */
    public byte[] convertExcelToPdf(byte[] excelBytes);

}