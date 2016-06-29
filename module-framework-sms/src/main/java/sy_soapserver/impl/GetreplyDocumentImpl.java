/*
 * An XML document type.
 * Localname: getreply
 * Namespace: urn:SY_SoapServer
 * Java type: sy_soapserver.GetreplyDocument
 *
 * Automatically generated - do not modify.
 */
package sy_soapserver.impl;
/**
 * A document containing one getreply(@urn:SY_SoapServer) element.
 *
 * This is a complex type.
 */
public class GetreplyDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.GetreplyDocument
{
    private static final long serialVersionUID = 1L;
    
    public GetreplyDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName GETREPLY$0 = 
        new javax.xml.namespace.QName("urn:SY_SoapServer", "getreply");
    
    
    /**
     * Gets the "getreply" element
     */
    public sy_soapserver.GetreplyDocument.Getreply getGetreply()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.GetreplyDocument.Getreply target = null;
            target = (sy_soapserver.GetreplyDocument.Getreply)get_store().find_element_user(GETREPLY$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "getreply" element
     */
    public void setGetreply(sy_soapserver.GetreplyDocument.Getreply getreply)
    {
        generatedSetterHelperImpl(getreply, GETREPLY$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "getreply" element
     */
    public sy_soapserver.GetreplyDocument.Getreply addNewGetreply()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.GetreplyDocument.Getreply target = null;
            target = (sy_soapserver.GetreplyDocument.Getreply)get_store().add_element_user(GETREPLY$0);
            return target;
        }
    }
    /**
     * An XML getreply(@urn:SY_SoapServer).
     *
     * This is a complex type.
     */
    public static class GetreplyImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.GetreplyDocument.Getreply
    {
        private static final long serialVersionUID = 1L;
        
        public GetreplyImpl(org.apache.xmlbeans.SchemaType sType)
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
