/*
 * An XML document type.
 * Localname: getreplyResponse
 * Namespace: urn:SY_SoapServer
 * Java type: sy_soapserver.GetreplyResponseDocument
 *
 * Automatically generated - do not modify.
 */
package sy_soapserver.impl;
/**
 * A document containing one getreplyResponse(@urn:SY_SoapServer) element.
 *
 * This is a complex type.
 */
public class GetreplyResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.GetreplyResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public GetreplyResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName GETREPLYRESPONSE$0 = 
        new javax.xml.namespace.QName("urn:SY_SoapServer", "getreplyResponse");
    
    
    /**
     * Gets the "getreplyResponse" element
     */
    public sy_soapserver.GetreplyResponseDocument.GetreplyResponse getGetreplyResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.GetreplyResponseDocument.GetreplyResponse target = null;
            target = (sy_soapserver.GetreplyResponseDocument.GetreplyResponse)get_store().find_element_user(GETREPLYRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "getreplyResponse" element
     */
    public void setGetreplyResponse(sy_soapserver.GetreplyResponseDocument.GetreplyResponse getreplyResponse)
    {
        generatedSetterHelperImpl(getreplyResponse, GETREPLYRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "getreplyResponse" element
     */
    public sy_soapserver.GetreplyResponseDocument.GetreplyResponse addNewGetreplyResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.GetreplyResponseDocument.GetreplyResponse target = null;
            target = (sy_soapserver.GetreplyResponseDocument.GetreplyResponse)get_store().add_element_user(GETREPLYRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML getreplyResponse(@urn:SY_SoapServer).
     *
     * This is a complex type.
     */
    public static class GetreplyResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.GetreplyResponseDocument.GetreplyResponse
    {
        private static final long serialVersionUID = 1L;
        
        public GetreplyResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName GETREPLY$0 = 
            new javax.xml.namespace.QName("", "getreply");
        
        
        /**
         * Gets the "getreply" element
         */
        public String getGetreply()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(GETREPLY$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "getreply" element
         */
        public org.apache.xmlbeans.XmlString xgetGetreply()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(GETREPLY$0, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "getreply" element
         */
        public boolean isNilGetreply()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(GETREPLY$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "getreply" element
         */
        public void setGetreply(String getreply)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(GETREPLY$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(GETREPLY$0);
                }
                target.setStringValue(getreply);
            }
        }
        
        /**
         * Sets (as xml) the "getreply" element
         */
        public void xsetGetreply(org.apache.xmlbeans.XmlString getreply)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(GETREPLY$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(GETREPLY$0);
                }
                target.set(getreply);
            }
        }
        
        /**
         * Nils the "getreply" element
         */
        public void setNilGetreply()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(GETREPLY$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(GETREPLY$0);
                }
                target.setNil();
            }
        }
    }
}
