/*
 * An XML document type.
 * Localname: __callResponse
 * Namespace: urn:SY_SoapServer
 * Java type: sy_soapserver.CallResponseDocument
 *
 * Automatically generated - do not modify.
 */
package sy_soapserver.impl;
/**
 * A document containing one __callResponse(@urn:SY_SoapServer) element.
 *
 * This is a complex type.
 */
public class CallResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.CallResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public CallResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CALLRESPONSE$0 = 
        new javax.xml.namespace.QName("urn:SY_SoapServer", "__callResponse");
    
    
    /**
     * Gets the "__callResponse" element
     */
    public sy_soapserver.CallResponseDocument.CallResponse getCallResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.CallResponseDocument.CallResponse target = null;
            target = (sy_soapserver.CallResponseDocument.CallResponse)get_store().find_element_user(CALLRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "__callResponse" element
     */
    public void setCallResponse(sy_soapserver.CallResponseDocument.CallResponse callResponse)
    {
        generatedSetterHelperImpl(callResponse, CALLRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "__callResponse" element
     */
    public sy_soapserver.CallResponseDocument.CallResponse addNewCallResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.CallResponseDocument.CallResponse target = null;
            target = (sy_soapserver.CallResponseDocument.CallResponse)get_store().add_element_user(CALLRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML __callResponse(@urn:SY_SoapServer).
     *
     * This is a complex type.
     */
    public static class CallResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.CallResponseDocument.CallResponse
    {
        private static final long serialVersionUID = 1L;
        
        public CallResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName CALL$0 = 
            new javax.xml.namespace.QName("", "__call");
        
        
        /**
         * Gets the "__call" element
         */
        public String getCall()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CALL$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "__call" element
         */
        public org.apache.xmlbeans.XmlString xgetCall()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CALL$0, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "__call" element
         */
        public boolean isNilCall()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CALL$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "__call" element
         */
        public void setCall(String call)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CALL$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CALL$0);
                }
                target.setStringValue(call);
            }
        }
        
        /**
         * Sets (as xml) the "__call" element
         */
        public void xsetCall(org.apache.xmlbeans.XmlString call)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CALL$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CALL$0);
                }
                target.set(call);
            }
        }
        
        /**
         * Nils the "__call" element
         */
        public void setNilCall()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CALL$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CALL$0);
                }
                target.setNil();
            }
        }
    }
}
