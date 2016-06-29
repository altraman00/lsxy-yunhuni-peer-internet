/*
 * An XML document type.
 * Localname: __issetResponse
 * Namespace: urn:SY_SoapServer
 * Java type: sy_soapserver.IssetResponseDocument
 *
 * Automatically generated - do not modify.
 */
package sy_soapserver.impl;
/**
 * A document containing one __issetResponse(@urn:SY_SoapServer) element.
 *
 * This is a complex type.
 */
public class IssetResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.IssetResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public IssetResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ISSETRESPONSE$0 = 
        new javax.xml.namespace.QName("urn:SY_SoapServer", "__issetResponse");
    
    
    /**
     * Gets the "__issetResponse" element
     */
    public sy_soapserver.IssetResponseDocument.IssetResponse getIssetResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.IssetResponseDocument.IssetResponse target = null;
            target = (sy_soapserver.IssetResponseDocument.IssetResponse)get_store().find_element_user(ISSETRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "__issetResponse" element
     */
    public void setIssetResponse(sy_soapserver.IssetResponseDocument.IssetResponse issetResponse)
    {
        generatedSetterHelperImpl(issetResponse, ISSETRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "__issetResponse" element
     */
    public sy_soapserver.IssetResponseDocument.IssetResponse addNewIssetResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.IssetResponseDocument.IssetResponse target = null;
            target = (sy_soapserver.IssetResponseDocument.IssetResponse)get_store().add_element_user(ISSETRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML __issetResponse(@urn:SY_SoapServer).
     *
     * This is a complex type.
     */
    public static class IssetResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.IssetResponseDocument.IssetResponse
    {
        private static final long serialVersionUID = 1L;
        
        public IssetResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName ISSET$0 = 
            new javax.xml.namespace.QName("", "__isset");
        
        
        /**
         * Gets the "__isset" element
         */
        public String getIsset()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ISSET$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "__isset" element
         */
        public org.apache.xmlbeans.XmlString xgetIsset()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ISSET$0, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "__isset" element
         */
        public boolean isNilIsset()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ISSET$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "__isset" element
         */
        public void setIsset(String isset)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ISSET$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ISSET$0);
                }
                target.setStringValue(isset);
            }
        }
        
        /**
         * Sets (as xml) the "__isset" element
         */
        public void xsetIsset(org.apache.xmlbeans.XmlString isset)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ISSET$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(ISSET$0);
                }
                target.set(isset);
            }
        }
        
        /**
         * Nils the "__isset" element
         */
        public void setNilIsset()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ISSET$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(ISSET$0);
                }
                target.setNil();
            }
        }
    }
}
