/*
 * An XML document type.
 * Localname: __call
 * Namespace: urn:SY_SoapServer
 * Java type: sy_soapserver.CallDocument
 *
 * Automatically generated - do not modify.
 */
package sy_soapserver.impl;
/**
 * A document containing one __call(@urn:SY_SoapServer) element.
 *
 * This is a complex type.
 */
public class CallDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.CallDocument
{
    private static final long serialVersionUID = 1L;
    
    public CallDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CALL$0 = 
        new javax.xml.namespace.QName("urn:SY_SoapServer", "__call");
    
    
    /**
     * Gets the "__call" element
     */
    public sy_soapserver.CallDocument.Call getCall()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.CallDocument.Call target = null;
            target = (sy_soapserver.CallDocument.Call)get_store().find_element_user(CALL$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "__call" element
     */
    public void setCall(sy_soapserver.CallDocument.Call call)
    {
        generatedSetterHelperImpl(call, CALL$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "__call" element
     */
    public sy_soapserver.CallDocument.Call addNewCall()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.CallDocument.Call target = null;
            target = (sy_soapserver.CallDocument.Call)get_store().add_element_user(CALL$0);
            return target;
        }
    }
    /**
     * An XML __call(@urn:SY_SoapServer).
     *
     * This is a complex type.
     */
    public static class CallImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.CallDocument.Call
    {
        private static final long serialVersionUID = 1L;
        
        public CallImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName METHOD$0 = 
            new javax.xml.namespace.QName("", "method");
        private static final javax.xml.namespace.QName ARGS$2 = 
            new javax.xml.namespace.QName("", "args");
        
        
        /**
         * Gets the "method" element
         */
        public java.lang.String getMethod()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(METHOD$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "method" element
         */
        public org.apache.xmlbeans.XmlString xgetMethod()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(METHOD$0, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "method" element
         */
        public boolean isNilMethod()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(METHOD$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "method" element
         */
        public void setMethod(java.lang.String method)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(METHOD$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(METHOD$0);
                }
                target.setStringValue(method);
            }
        }
        
        /**
         * Sets (as xml) the "method" element
         */
        public void xsetMethod(org.apache.xmlbeans.XmlString method)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(METHOD$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(METHOD$0);
                }
                target.set(method);
            }
        }
        
        /**
         * Nils the "method" element
         */
        public void setNilMethod()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(METHOD$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(METHOD$0);
                }
                target.setNil();
            }
        }
        
        /**
         * Gets the "args" element
         */
        public java.lang.String getArgs()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ARGS$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "args" element
         */
        public org.apache.xmlbeans.XmlString xgetArgs()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ARGS$2, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "args" element
         */
        public boolean isNilArgs()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ARGS$2, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "args" element
         */
        public void setArgs(java.lang.String args)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ARGS$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ARGS$2);
                }
                target.setStringValue(args);
            }
        }
        
        /**
         * Sets (as xml) the "args" element
         */
        public void xsetArgs(org.apache.xmlbeans.XmlString args)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ARGS$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(ARGS$2);
                }
                target.set(args);
            }
        }
        
        /**
         * Nils the "args" element
         */
        public void setNilArgs()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ARGS$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(ARGS$2);
                }
                target.setNil();
            }
        }
    }
}
