package com.tkpm.sms.application.service.interfaces;

import com.tkpm.sms.domain.valueobject.TextContent;

public interface TextContentService {

    TextContent createTextContent(String text);

    TextContent updateTextContent(TextContent existingContent, String text);
}
