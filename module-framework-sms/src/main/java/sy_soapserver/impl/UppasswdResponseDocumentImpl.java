/*
 * An XML document type.
 * Localname: uppasswdResponse
 * Namespace: urn:SY_SoapServer
 * Java type: sy_soapserver.UppasswdResponseDocument
 *
 * Automatically generated - do not modify.
 */
package sy_soapserver.impl;
/**
 * A document containing one uppasswdResponse(@urn:SY_SoapServer) element.
 *
 * This is a complex type.
 */
public class UppasswdResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.UppasswdResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public UppasswdResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName UPPASSWDRESPONSE$0 = 
        new javax.xml.namespace.QName("urn:SY_SoapServer", "uppasswdResponse");
    
    
    /**
     * Gets the "uppasswdResponse" element
     */
    public sy_soapserver.UppasswdResponseDocument.UppasswdResponse getUppasswdResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.UppasswdResponseDocument.UppasswdResponse target = null;
            target = (sy_soapserver.UppasswdResponseDocument.UppasswdResponse)get_store().find_element_user(UPPASSWDRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "uppasswdResponse" element
     */
    public void setUppasswdResponse(sy_soapserver.UppasswdResponseDocument.UppasswdResponse uppasswdResponse)
    {
        generatedSetterHelperImpl(uppasswdResponse, UPPASSWDRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "uppasswdResponse" element
     */
    public sy_soapserver.UppasswdResponseDocument.UppasswdResponse addNewUppasswdResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.UppasswdResponseDocument.UppasswdResponse target = null;
            target = (sy_soapserver.UppasswdResponseDocument.UppasswdResponse)get_store().add_element_user(UPPASSWDRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML uppasswdResponse(@urn:SY_SoapServer).
     *
     * This is a complex type.
     */
    public static class UppasswdResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.UppasswdResponseDocument.UppasswdResponse
    {
        private static final long serialVersionUID = 1L;
        
        public UppasswdResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName UPPASSWD$0 = 
            new javax.xml.namespace.QName("", "uppasswd");
        
        
        /**
         * Gets the "uppasswd" element
         */
        public String getUppasswd()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(UPPASSWD$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "uppasswd" element
         */
        public org.apache.xmlbeans.XmlString xgetUppasswd()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(UPPASSWD$0, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "uppasswd" element
         */
        public boolean isNilUppasswd()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(UPPASSWD$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "uppasswd" element
         */
        public void setUppasswd(String uppasswd)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(UPPASSWD$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(UPPASSWD$0);
                }
                target.setStringValue(uppasswd);
            }
        }
        
        /**
         * Sets (as xml) the "uppasswd" element
         */
        public void xsetUppasswd(org.apache.xmlbeans.XmlString uppasswd)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(UPPASSWD$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(UPPASSWD$0);
                }
                target.set(uppasswd);
            }
        }
        
        /**
         * Nils the "uppasswd" element
         */
        public void setNilUppasswd()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(UPPASSWD$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(UPPASSWD$0);
                }
                target.setNil();
            }
        }
    }
}
