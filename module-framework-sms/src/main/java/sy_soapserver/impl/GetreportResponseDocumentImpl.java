/*
 * An XML document type.
 * Localname: getreportResponse
 * Namespace: urn:SY_SoapServer
 * Java type: sy_soapserver.GetreportResponseDocument
 *
 * Automatically generated - do not modify.
 */
package sy_soapserver.impl;
/**
 * A document containing one getreportResponse(@urn:SY_SoapServer) element.
 *
 * This is a complex type.
 */
public class GetreportResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.GetreportResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public GetreportResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName GETREPORTRESPONSE$0 = 
        new javax.xml.namespace.QName("urn:SY_SoapServer", "getreportResponse");
    
    
    /**
     * Gets the "getreportResponse" element
     */
    public sy_soapserver.GetreportResponseDocument.GetreportResponse getGetreportResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.GetreportResponseDocument.GetreportResponse target = null;
            target = (sy_soapserver.GetreportResponseDocument.GetreportResponse)get_store().find_element_user(GETREPORTRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "getreportResponse" element
     */
    public void setGetreportResponse(sy_soapserver.GetreportResponseDocument.GetreportResponse getreportResponse)
    {
        generatedSetterHelperImpl(getreportResponse, GETREPORTRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "getreportResponse" element
     */
    public sy_soapserver.GetreportResponseDocument.GetreportResponse addNewGetreportResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.GetreportResponseDocument.GetreportResponse target = null;
            target = (sy_soapserver.GetreportResponseDocument.GetreportResponse)get_store().add_element_user(GETREPORTRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML getreportResponse(@urn:SY_SoapServer).
     *
     * This is a complex type.
     */
    public static class GetreportResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.GetreportResponseDocument.GetreportResponse
    {
        private static final long serialVersionUID = 1L;
        
        public GetreportResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName GETREPORT$0 = 
            new javax.xml.namespace.QName("", "getreport");
        
        
        /**
         * Gets the "getreport" element
         */
        public java.lang.String getGetreport()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(GETREPORT$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "getreport" element
         */
        public org.apache.xmlbeans.XmlString xgetGetreport()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(GETREPORT$0, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "getreport" element
         */
        public boolean isNilGetreport()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(GETREPORT$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "getreport" element
         */
        public void setGetreport(java.lang.String getreport)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(GETREPORT$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(GETREPORT$0);
                }
                target.setStringValue(getreport);
            }
        }
        
        /**
         * Sets (as xml) the "getreport" element
         */
        public void xsetGetreport(org.apache.xmlbeans.XmlString getreport)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(GETREPORT$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(GETREPORT$0);
                }
                target.set(getreport);
            }
        }
        
        /**
         * Nils the "getreport" element
         */
        public void setNilGetreport()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(GETREPORT$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(GETREPORT$0);
                }
                target.setNil();
            }
        }
    }
}
