/*
 * An XML document type.
 * Localname: getreport
 * Namespace: urn:SY_SoapServer
 * Java type: sy_soapserver.GetreportDocument
 *
 * Automatically generated - do not modify.
 */
package sy_soapserver.impl;
/**
 * A document containing one getreport(@urn:SY_SoapServer) element.
 *
 * This is a complex type.
 */
public class GetreportDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.GetreportDocument
{
    private static final long serialVersionUID = 1L;
    
    public GetreportDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName GETREPORT$0 = 
        new javax.xml.namespace.QName("urn:SY_SoapServer", "getreport");
    
    
    /**
     * Gets the "getreport" element
     */
    public sy_soapserver.GetreportDocument.Getreport getGetreport()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.GetreportDocument.Getreport target = null;
            target = (sy_soapserver.GetreportDocument.Getreport)get_store().find_element_user(GETREPORT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "getreport" element
     */
    public void setGetreport(sy_soapserver.GetreportDocument.Getreport getreport)
    {
        generatedSetterHelperImpl(getreport, GETREPORT$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "getreport" element
     */
    public sy_soapserver.GetreportDocument.Getreport addNewGetreport()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.GetreportDocument.Getreport target = null;
            target = (sy_soapserver.GetreportDocument.Getreport)get_store().add_element_user(GETREPORT$0);
            return target;
        }
    }
    /**
     * An XML getreport(@urn:SY_SoapServer).
     *
     * This is a complex type.
     */
    public static class GetreportImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.GetreportDocument.Getreport
    {
        private static final long serialVersionUID = 1L;
        
        public GetreportImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName USER$0 = 
            new javax.xml.namespace.QName("", "user");
        private static final javax.xml.namespace.QName PASSWD$2 = 
            new javax.xml.namespace.QName("", "passwd");
        
        
        /**
         * Gets the "user" element
         */
        public java.lang.String getUser()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USER$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "user" element
         */
        public org.apache.xmlbeans.XmlString xgetUser()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USER$0, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "user" element
         */
        public boolean isNilUser()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USER$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "user" element
         */
        public void setUser(java.lang.String user)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USER$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(USER$0);
                }
                target.setStringValue(user);
            }
        }
        
        /**
         * Sets (as xml) the "user" element
         */
        public void xsetUser(org.apache.xmlbeans.XmlString user)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USER$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USER$0);
                }
                target.set(user);
            }
        }
        
        /**
         * Nils the "user" element
         */
        public void setNilUser()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(USER$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(USER$0);
                }
                target.setNil();
            }
        }
        
        /**
         * Gets the "passwd" element
         */
        public java.lang.String getPasswd()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PASSWD$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "passwd" element
         */
        public org.apache.xmlbeans.XmlString xgetPasswd()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PASSWD$2, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "passwd" element
         */
        public boolean isNilPasswd()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PASSWD$2, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "passwd" element
         */
        public void setPasswd(java.lang.String passwd)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PASSWD$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PASSWD$2);
                }
                target.setStringValue(passwd);
            }
        }
        
        /**
         * Sets (as xml) the "passwd" element
         */
        public void xsetPasswd(org.apache.xmlbeans.XmlString passwd)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PASSWD$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PASSWD$2);
                }
                target.set(passwd);
            }
        }
        
        /**
         * Nils the "passwd" element
         */
        public void setNilPasswd()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PASSWD$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PASSWD$2);
                }
                target.setNil();
            }
        }
    }
}
