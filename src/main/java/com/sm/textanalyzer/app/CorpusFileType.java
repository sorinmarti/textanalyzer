package com.sm.textanalyzer.app;

public enum CorpusFileType {
    ALL("All document types", "ALL"),
    GENERIC_TEXT("Generic text documents", "PLAIN"),
    WHATSAPP("Whatsapp chat protocols", "WHATSAPP");

    private final String typeName, xmlTypeName;

    CorpusFileType(String typeName, String xmlTypeName) {
        this.typeName = typeName;
        this.xmlTypeName = xmlTypeName;
    }

    public String getName() {
        return typeName;
    }

    public String getXmlTypeName() {
        return xmlTypeName;
    }

    public static CorpusFileType fromXML(String xmlType) {
       switch (xmlType) {
           case "ALL":
               return ALL;
           case "PLAIN":
               return GENERIC_TEXT;
           case "WHATSAPP":
               return WHATSAPP;
       }
        return ALL;
    }
}
