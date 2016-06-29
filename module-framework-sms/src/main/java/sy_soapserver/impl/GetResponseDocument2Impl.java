/*
 * An XML document type.
 * Localname: __getResponse
 * Namespace: urn:SY_SoapServer
 * Java type: sy_soapserver.GetResponseDocument2
 *
 * Automatically generated - do not modify.
 */
package sy_soapserver.impl;
/**
 * A document containing one __getResponse(@urn:SY_SoapServer) element.
 *
 * This is a complex type.
 */
public class GetResponseDocument2Impl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.GetResponseDocument2
{
    private static final long serialVersionUID = 1L;
    
    public GetResponseDocument2Impl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName GETRESPONSE$0 = 
        new javax.xml.namespace.QName("urn:SY_SoapServer", "__getResponse");
    
    
    /**
     * Gets the "__getResponse" element
     */
    public sy_soapserver.GetResponseDocument2.GetResponse getGetResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.GetResponseDocument2.GetResponse target = null;
            target = (sy_soapserver.GetResponseDocument2.GetResponse)get_store().find_element_user(GETRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "__getResponse" element
     */
    public void setGetResponse(sy_soapserver.GetResponseDocument2.GetResponse getResponse)
    {
        generatedSetterHelperImpl(getResponse, GETRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "__getResponse" element
     */
    public sy_soapserver.GetResponseDocument2.GetResponse addNewGetResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.GetResponseDocument2.GetResponse target = null;
            target = (sy_soapserver.GetResponseDocument2.GetResponse)get_store().add_element_user(GETRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML __getResponse(@urn:SY_SoapServer).
     *
     * This is a complex type.
     */
    public static class GetResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.GetResponseDocument2.GetResponse
    {
        private static final long serialVersionUID = 1L;
        
        public GetResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName GET$0 = 
            new javax.xml.namespace.QName("", "__get");
        
        
        /**
         * Gets the "__get" element
         */
        public java.lang.String getGet()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(GET$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "__get" element
         */
        public org.apache.xmlbeans.XmlString xgetGet()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(GET$0, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "__get" element
         */
        public boolean isNilGet()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(GET$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "__get" element
         */
        public void setGet(java.lang.String get)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(GET$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(GET$0);
                }
                target.setStringValue(get);
            }
        }
        
        /**
         * Sets (as xml) the "__get" element
         */
        public void xsetGet(org.apache.xmlbeans.XmlString get)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(GET$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(GET$0);
                }
                target.set(get);
            }
        }
        
        /**
         * Nils the "__get" element
         */
        public void setNilGet()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(GET$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(GET$0);
                }
                target.setNil();
            }
        }
    }
}
