/*
 * An XML document type.
 * Localname: getbalanceResponse
 * Namespace: urn:SY_SoapServer
 * Java type: sy_soapserver.GetbalanceResponseDocument
 *
 * Automatically generated - do not modify.
 */
package sy_soapserver.impl;
/**
 * A document containing one getbalanceResponse(@urn:SY_SoapServer) element.
 *
 * This is a complex type.
 */
public class GetbalanceResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.GetbalanceResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public GetbalanceResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName GETBALANCERESPONSE$0 = 
        new javax.xml.namespace.QName("urn:SY_SoapServer", "getbalanceResponse");
    
    
    /**
     * Gets the "getbalanceResponse" element
     */
    public sy_soapserver.GetbalanceResponseDocument.GetbalanceResponse getGetbalanceResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.GetbalanceResponseDocument.GetbalanceResponse target = null;
            target = (sy_soapserver.GetbalanceResponseDocument.GetbalanceResponse)get_store().find_element_user(GETBALANCERESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "getbalanceResponse" element
     */
    public void setGetbalanceResponse(sy_soapserver.GetbalanceResponseDocument.GetbalanceResponse getbalanceResponse)
    {
        generatedSetterHelperImpl(getbalanceResponse, GETBALANCERESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "getbalanceResponse" element
     */
    public sy_soapserver.GetbalanceResponseDocument.GetbalanceResponse addNewGetbalanceResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.GetbalanceResponseDocument.GetbalanceResponse target = null;
            target = (sy_soapserver.GetbalanceResponseDocument.GetbalanceResponse)get_store().add_element_user(GETBALANCERESPONSE$0);
            return target;
        }
    }
    /**
     * An XML getbalanceResponse(@urn:SY_SoapServer).
     *
     * This is a complex type.
     */
    public static class GetbalanceResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.GetbalanceResponseDocument.GetbalanceResponse
    {
        private static final long serialVersionUID = 1L;
        
        public GetbalanceResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName GETBALANCE$0 = 
            new javax.xml.namespace.QName("", "getbalance");
        
        
        /**
         * Gets the "getbalance" element
         */
        public java.lang.String getGetbalance()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(GETBALANCE$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "getbalance" element
         */
        public org.apache.xmlbeans.XmlString xgetGetbalance()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(GETBALANCE$0, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "getbalance" element
         */
        public boolean isNilGetbalance()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(GETBALANCE$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "getbalance" element
         */
        public void setGetbalance(java.lang.String getbalance)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(GETBALANCE$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(GETBALANCE$0);
                }
                target.setStringValue(getbalance);
            }
        }
        
        /**
         * Sets (as xml) the "getbalance" element
         */
        public void xsetGetbalance(org.apache.xmlbeans.XmlString getbalance)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(GETBALANCE$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(GETBALANCE$0);
                }
                target.set(getbalance);
            }
        }
        
        /**
         * Nils the "getbalance" element
         */
        public void setNilGetbalance()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(GETBALANCE$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(GETBALANCE$0);
                }
                target.setNil();
            }
        }
    }
}
