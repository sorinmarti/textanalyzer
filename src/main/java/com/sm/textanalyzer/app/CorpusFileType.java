package com.sm.textanalyzer.app;

public enum CorpusFileType {
    ALL("All document types", "ALL");

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
       return ALL;
    }
}
