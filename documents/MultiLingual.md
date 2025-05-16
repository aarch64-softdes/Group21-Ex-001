## MULTI-LANGUAGE SUPPORTED:

### Static text:
* Use `"i18next`, `i18next-browser-languagedetector` for auto translating to target language.

### Dynamic text:
* Configure `LocaleConfig` for getting `LanguageCode` in header of requests (ex. content-language: vi).
* Use value objects `TextContent` and `Translation` for saving and retrieving language base on `LanguageCode`(ex. en, vi, fr, ...).
* Use `TextContent` instead of `String` for name and description of some models: `Subject`, `Status`, `Program`, `Faculty`.
* Relationships between `TextContent`, `Translation` and `Models` are as follows:
  * `TextContent` has many `Translation`
  * `Translation` belongs to `TextContent`
  * `TextContent` belongs to `Subject`, `Status`, `Program`, `Faculty`
  * `Subject`, `Status`, `Program`, `Faculty` has many `TextContent`
  
![Relationship.png](assets/week05/Relationship.png)
* With error message, we define a template for message and use `org.springframework.context.MessageSource` class to get the message in the target language.
* Fallback strategy: Always return default language if translation is missing to improve UX.

### Drawback:
* Must join `TextContent` and `Translation` table whenever we want to get name and description of `Subject`, `Status`, `Program`, `Faculty`.
* Overhead in Testing and Seeding.