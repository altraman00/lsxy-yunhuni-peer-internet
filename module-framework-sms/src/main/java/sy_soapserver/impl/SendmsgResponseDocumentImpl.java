/*
 * An XML document type.
 * Localname: sendmsgResponse
 * Namespace: urn:SY_SoapServer
 * Java type: sy_soapserver.SendmsgResponseDocument
 *
 * Automatically generated - do not modify.
 */
package sy_soapserver.impl;
/**
 * A document containing one sendmsgResponse(@urn:SY_SoapServer) element.
 *
 * This is a complex type.
 */
public class SendmsgResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.SendmsgResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public SendmsgResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SENDMSGRESPONSE$0 = 
        new javax.xml.namespace.QName("urn:SY_SoapServer", "sendmsgResponse");
    
    
    /**
     * Gets the "sendmsgResponse" element
     */
    public sy_soapserver.SendmsgResponseDocument.SendmsgResponse getSendmsgResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.SendmsgResponseDocument.SendmsgResponse target = null;
            target = (sy_soapserver.SendmsgResponseDocument.SendmsgResponse)get_store().find_element_user(SENDMSGRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "sendmsgResponse" element
     */
    public void setSendmsgResponse(sy_soapserver.SendmsgResponseDocument.SendmsgResponse sendmsgResponse)
    {
        generatedSetterHelperImpl(sendmsgResponse, SENDMSGRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "sendmsgResponse" element
     */
    public sy_soapserver.SendmsgResponseDocument.SendmsgResponse addNewSendmsgResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.SendmsgResponseDocument.SendmsgResponse target = null;
            target = (sy_soapserver.SendmsgResponseDocument.SendmsgResponse)get_store().add_element_user(SENDMSGRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML sendmsgResponse(@urn:SY_SoapServer).
     *
     * This is a complex type.
     */
    public static class SendmsgResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.SendmsgResponseDocument.SendmsgResponse
    {
        private static final long serialVersionUID = 1L;
        
        public SendmsgResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName SENDMSG$0 = 
            new javax.xml.namespace.QName("", "sendmsg");
        
        
        /**
         * Gets the "sendmsg" element
         */
        public java.lang.String getSendmsg()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SENDMSG$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "sendmsg" element
         */
        public org.apache.xmlbeans.XmlString xgetSendmsg()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SENDMSG$0, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "sendmsg" element
         */
        public boolean isNilSendmsg()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SENDMSG$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "sendmsg" element
         */
        public void setSendmsg(java.lang.String sendmsg)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SENDMSG$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SENDMSG$0);
                }
                target.setStringValue(sendmsg);
            }
        }
        
        /**
         * Sets (as xml) the "sendmsg" element
         */
        public void xsetSendmsg(org.apache.xmlbeans.XmlString sendmsg)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SENDMSG$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SENDMSG$0);
                }
                target.set(sendmsg);
            }
        }
        
        /**
         * Nils the "sendmsg" element
         */
        public void setNilSendmsg()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SENDMSG$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SENDMSG$0);
                }
                target.setNil();
            }
        }
    }
}
