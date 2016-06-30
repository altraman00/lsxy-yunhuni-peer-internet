/*
 * An XML document type.
 * Localname: getbalance
 * Namespace: urn:SY_SoapServer
 * Java type: sy_soapserver.GetbalanceDocument
 *
 * Automatically generated - do not modify.
 */
package sy_soapserver.impl;
/**
 * A document containing one getbalance(@urn:SY_SoapServer) element.
 *
 * This is a complex type.
 */
public class GetbalanceDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.GetbalanceDocument
{
    private static final long serialVersionUID = 1L;
    
    public GetbalanceDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName GETBALANCE$0 = 
        new javax.xml.namespace.QName("urn:SY_SoapServer", "getbalance");
    
    
    /**
     * Gets the "getbalance" element
     */
    public sy_soapserver.GetbalanceDocument.Getbalance getGetbalance()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.GetbalanceDocument.Getbalance target = null;
            target = (sy_soapserver.GetbalanceDocument.Getbalance)get_store().find_element_user(GETBALANCE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "getbalance" element
     */
    public void setGetbalance(sy_soapserver.GetbalanceDocument.Getbalance getbalance)
    {
        generatedSetterHelperImpl(getbalance, GETBALANCE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "getbalance" element
     */
    public sy_soapserver.GetbalanceDocument.Getbalance addNewGetbalance()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.GetbalanceDocument.Getbalance target = null;
            target = (sy_soapserver.GetbalanceDocument.Getbalance)get_store().add_element_user(GETBALANCE$0);
            return target;
        }
    }
    /**
     * An XML getbalance(@urn:SY_SoapServer).
     *
     * This is a complex type.
     */
    public static class GetbalanceImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.GetbalanceDocument.Getbalance
    {
        private static final long serialVersionUID = 1L;
        
        public GetbalanceImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName USERNAME$0 = 
            new javax.xml.namespace.QName("", "username");
        private static final javax.xml.namespace.QName PWD$2 = 
            new javax.xml.namespace.QName("", "pwd");
        
        
        /**
         * Gets the "username" element
         */
        public java.lang.String getUsername()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USERNAME$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "username" element
         */
        public org.apache.xmlbeans.XmlString xgetUsername()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USERNAME$0, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "username" element
         */
        public boolean isNilUsername()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USERNAME$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "username" element
         */
        public void setUsername(java.lang.String username)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USERNAME$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(USERNAME$0);
                }
                target.setStringValue(username);
            }
        }
        
        /**
         * Sets (as xml) the "username" element
         */
        public void xsetUsername(org.apache.xmlbeans.XmlString username)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USERNAME$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USERNAME$0);
                }
                target.set(username);
            }
        }
        
        /**
         * Nils the "username" element
         */
        public void setNilUsername()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USERNAME$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USERNAME$0);
                }
                target.setNil();
            }
        }
        
        /**
         * Gets the "pwd" element
         */
        public java.lang.String getPwd()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PWD$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "pwd" element
         */
        public org.apache.xmlbeans.XmlString xgetPwd()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PWD$2, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "pwd" element
         */
        public boolean isNilPwd()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PWD$2, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "pwd" element
         */
        public void setPwd(java.lang.String pwd)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PWD$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PWD$2);
                }
                target.setStringValue(pwd);
            }
        }
        
        /**
         * Sets (as xml) the "pwd" element
         */
        public void xsetPwd(org.apache.xmlbeans.XmlString pwd)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PWD$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PWD$2);
                }
                target.set(pwd);
            }
        }
        
        /**
         * Nils the "pwd" element
         */
        public void setNilPwd()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PWD$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PWD$2);
                }
                target.setNil();
            }
        }
    }
}
