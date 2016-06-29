/*
 * An XML document type.
 * Localname: getbalanceResponse
 * Namespace: urn:SY_SoapServer
 * Java type: sy_soapserver.GetbalanceResponseDocument
 *
 * Automatically generated - do not modify.
 */
package sy_soapserver;


/**
 * A document containing one getbalanceResponse(@urn:SY_SoapServer) element.
 *
 * This is a complex type.
 */
public interface GetbalanceResponseDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(GetbalanceResponseDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s4262BC6C064011807FED7385999847C2").resolveHandle("getbalanceresponsef11edoctype");
    
    /**
     * Gets the "getbalanceResponse" element
     */
    sy_soapserver.GetbalanceResponseDocument.GetbalanceResponse getGetbalanceResponse();
    
    /**
     * Sets the "getbalanceResponse" element
     */
    void setGetbalanceResponse(sy_soapserver.GetbalanceResponseDocument.GetbalanceResponse getbalanceResponse);
    
    /**
     * Appends and returns a new empty "getbalanceResponse" element
     */
    sy_soapserver.GetbalanceResponseDocument.GetbalanceResponse addNewGetbalanceResponse();
    
    /**
     * An XML getbalanceResponse(@urn:SY_SoapServer).
     *
     * This is a complex type.
     */
    public interface GetbalanceResponse extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(GetbalanceResponse.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s4262BC6C064011807FED7385999847C2").resolveHandle("getbalanceresponse13fbelemtype");
        
        /**
         * Gets the "getbalance" element
         */
        java.lang.String getGetbalance();
        
        /**
         * Gets (as xml) the "getbalance" element
         */
        org.apache.xmlbeans.XmlString xgetGetbalance();
        
        /**
         * Tests for nil "getbalance" element
         */
        boolean isNilGetbalance();
        
        /**
         * Sets the "getbalance" element
         */
        void setGetbalance(java.lang.String getbalance);
        
        /**
         * Sets (as xml) the "getbalance" element
         */
        void xsetGetbalance(org.apache.xmlbeans.XmlString getbalance);
        
        /**
         * Nils the "getbalance" element
         */
        void setNilGetbalance();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static sy_soapserver.GetbalanceResponseDocument.GetbalanceResponse newInstance() {
              return (sy_soapserver.GetbalanceResponseDocument.GetbalanceResponse) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static sy_soapserver.GetbalanceResponseDocument.GetbalanceResponse newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (sy_soapserver.GetbalanceResponseDocument.GetbalanceResponse) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static sy_soapserver.GetbalanceResponseDocument newInstance() {
          return (sy_soapserver.GetbalanceResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static sy_soapserver.GetbalanceResponseDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (sy_soapserver.GetbalanceResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static sy_soapserver.GetbalanceResponseDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (sy_soapserver.GetbalanceResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static sy_soapserver.GetbalanceResponseDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (sy_soapserver.GetbalanceResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static sy_soapserver.GetbalanceResponseDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (sy_soapserver.GetbalanceResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static sy_soapserver.GetbalanceResponseDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (sy_soapserver.GetbalanceResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static sy_soapserver.GetbalanceResponseDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (sy_soapserver.GetbalanceResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static sy_soapserver.GetbalanceResponseDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (sy_soapserver.GetbalanceResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static sy_soapserver.GetbalanceResponseDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (sy_soapserver.GetbalanceResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static sy_soapserver.GetbalanceResponseDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (sy_soapserver.GetbalanceResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static sy_soapserver.GetbalanceResponseDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (sy_soapserver.GetbalanceResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static sy_soapserver.GetbalanceResponseDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (sy_soapserver.GetbalanceResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static sy_soapserver.GetbalanceResponseDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (sy_soapserver.GetbalanceResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static sy_soapserver.GetbalanceResponseDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (sy_soapserver.GetbalanceResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static sy_soapserver.GetbalanceResponseDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (sy_soapserver.GetbalanceResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static sy_soapserver.GetbalanceResponseDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (sy_soapserver.GetbalanceResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static sy_soapserver.GetbalanceResponseDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (sy_soapserver.GetbalanceResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static sy_soapserver.GetbalanceResponseDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (sy_soapserver.GetbalanceResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
