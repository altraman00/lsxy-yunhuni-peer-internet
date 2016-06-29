/*
 * An XML document type.
 * Localname: __setResponse
 * Namespace: urn:SY_SoapServer
 * Java type: sy_soapserver.SetResponseDocument
 *
 * Automatically generated - do not modify.
 */
package sy_soapserver.impl;
/**
 * A document containing one __setResponse(@urn:SY_SoapServer) element.
 *
 * This is a complex type.
 */
public class SetResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.SetResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public SetResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SETRESPONSE$0 = 
        new javax.xml.namespace.QName("urn:SY_SoapServer", "__setResponse");
    
    
    /**
     * Gets the "__setResponse" element
     */
    public sy_soapserver.SetResponseDocument.SetResponse getSetResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.SetResponseDocument.SetResponse target = null;
            target = (sy_soapserver.SetResponseDocument.SetResponse)get_store().find_element_user(SETRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "__setResponse" element
     */
    public void setSetResponse(sy_soapserver.SetResponseDocument.SetResponse setResponse)
    {
        generatedSetterHelperImpl(setResponse, SETRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "__setResponse" element
     */
    public sy_soapserver.SetResponseDocument.SetResponse addNewSetResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.SetResponseDocument.SetResponse target = null;
            target = (sy_soapserver.SetResponseDocument.SetResponse)get_store().add_element_user(SETRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML __setResponse(@urn:SY_SoapServer).
     *
     * This is a complex type.
     */
    public static class SetResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.SetResponseDocument.SetResponse
    {
        private static final long serialVersionUID = 1L;
        
        public SetResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName SET$0 = 
            new javax.xml.namespace.QName("", "__set");
        
        
        /**
         * Gets the "__set" element
         */
        public String getSet()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SET$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "__set" element
         */
        public org.apache.xmlbeans.XmlString xgetSet()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SET$0, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "__set" element
         */
        public boolean isNilSet()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SET$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "__set" element
         */
        public void setSet(String set)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SET$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SET$0);
                }
                target.setStringValue(set);
            }
        }
        
        /**
         * Sets (as xml) the "__set" element
         */
        public void xsetSet(org.apache.xmlbeans.XmlString set)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SET$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SET$0);
                }
                target.set(set);
            }
        }
        
        /**
         * Nils the "__set" element
         */
        public void setNilSet()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SET$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SET$0);
                }
                target.setNil();
            }
        }
    }
}
