/*
 * An XML document type.
 * Localname: uppasswd
 * Namespace: urn:SY_SoapServer
 * Java type: sy_soapserver.UppasswdDocument
 *
 * Automatically generated - do not modify.
 */
package sy_soapserver.impl;
/**
 * A document containing one uppasswd(@urn:SY_SoapServer) element.
 *
 * This is a complex type.
 */
public class UppasswdDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.UppasswdDocument
{
    private static final long serialVersionUID = 1L;
    
    public UppasswdDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName UPPASSWD$0 = 
        new javax.xml.namespace.QName("urn:SY_SoapServer", "uppasswd");
    
    
    /**
     * Gets the "uppasswd" element
     */
    public sy_soapserver.UppasswdDocument.Uppasswd getUppasswd()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.UppasswdDocument.Uppasswd target = null;
            target = (sy_soapserver.UppasswdDocument.Uppasswd)get_store().find_element_user(UPPASSWD$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "uppasswd" element
     */
    public void setUppasswd(sy_soapserver.UppasswdDocument.Uppasswd uppasswd)
    {
        generatedSetterHelperImpl(uppasswd, UPPASSWD$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "uppasswd" element
     */
    public sy_soapserver.UppasswdDocument.Uppasswd addNewUppasswd()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.UppasswdDocument.Uppasswd target = null;
            target = (sy_soapserver.UppasswdDocument.Uppasswd)get_store().add_element_user(UPPASSWD$0);
            return target;
        }
    }
    /**
     * An XML uppasswd(@urn:SY_SoapServer).
     *
     * This is a complex type.
     */
    public static class UppasswdImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.UppasswdDocument.Uppasswd
    {
        private static final long serialVersionUID = 1L;
        
        public UppasswdImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName USER$0 = 
            new javax.xml.namespace.QName("", "user");
        private static final javax.xml.namespace.QName PASSWD$2 = 
            new javax.xml.namespace.QName("", "passwd");
        private static final javax.xml.namespace.QName NEWPASSWD$4 = 
            new javax.xml.namespace.QName("", "newpasswd");
        
        
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
        
        /**
         * Gets the "newpasswd" element
         */
        public java.lang.String getNewpasswd()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NEWPASSWD$4, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "newpasswd" element
         */
        public org.apache.xmlbeans.XmlString xgetNewpasswd()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NEWPASSWD$4, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "newpasswd" element
         */
        public boolean isNilNewpasswd()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NEWPASSWD$4, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "newpasswd" element
         */
        public void setNewpasswd(java.lang.String newpasswd)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NEWPASSWD$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NEWPASSWD$4);
                }
                target.setStringValue(newpasswd);
            }
        }
        
        /**
         * Sets (as xml) the "newpasswd" element
         */
        public void xsetNewpasswd(org.apache.xmlbeans.XmlString newpasswd)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NEWPASSWD$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NEWPASSWD$4);
                }
                target.set(newpasswd);
            }
        }
        
        /**
         * Nils the "newpasswd" element
         */
        public void setNilNewpasswd()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NEWPASSWD$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NEWPASSWD$4);
                }
                target.setNil();
            }
        }
    }
}
