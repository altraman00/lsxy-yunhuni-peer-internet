/*
 * An XML document type.
 * Localname: sendmsg
 * Namespace: urn:SY_SoapServer
 * Java type: sy_soapserver.SendmsgDocument
 *
 * Automatically generated - do not modify.
 */
package sy_soapserver.impl;
/**
 * A document containing one sendmsg(@urn:SY_SoapServer) element.
 *
 * This is a complex type.
 */
public class SendmsgDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.SendmsgDocument
{
    private static final long serialVersionUID = 1L;
    
    public SendmsgDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SENDMSG$0 = 
        new javax.xml.namespace.QName("urn:SY_SoapServer", "sendmsg");
    
    
    /**
     * Gets the "sendmsg" element
     */
    public sy_soapserver.SendmsgDocument.Sendmsg getSendmsg()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.SendmsgDocument.Sendmsg target = null;
            target = (sy_soapserver.SendmsgDocument.Sendmsg)get_store().find_element_user(SENDMSG$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "sendmsg" element
     */
    public void setSendmsg(sy_soapserver.SendmsgDocument.Sendmsg sendmsg)
    {
        generatedSetterHelperImpl(sendmsg, SENDMSG$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "sendmsg" element
     */
    public sy_soapserver.SendmsgDocument.Sendmsg addNewSendmsg()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.SendmsgDocument.Sendmsg target = null;
            target = (sy_soapserver.SendmsgDocument.Sendmsg)get_store().add_element_user(SENDMSG$0);
            return target;
        }
    }
    /**
     * An XML sendmsg(@urn:SY_SoapServer).
     *
     * This is a complex type.
     */
    public static class SendmsgImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.SendmsgDocument.Sendmsg
    {
        private static final long serialVersionUID = 1L;
        
        public SendmsgImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName USER$0 = 
            new javax.xml.namespace.QName("", "user");
        private static final javax.xml.namespace.QName PASSWD$2 = 
            new javax.xml.namespace.QName("", "passwd");
        private static final javax.xml.namespace.QName MSG$4 = 
            new javax.xml.namespace.QName("", "msg");
        private static final javax.xml.namespace.QName PHONE$6 = 
            new javax.xml.namespace.QName("", "phone");
        private static final javax.xml.namespace.QName SENDTIME$8 = 
            new javax.xml.namespace.QName("", "sendtime");
        
        
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
         * Gets the "msg" element
         */
        public java.lang.String getMsg()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MSG$4, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "msg" element
         */
        public org.apache.xmlbeans.XmlString xgetMsg()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(MSG$4, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "msg" element
         */
        public boolean isNilMsg()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(MSG$4, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "msg" element
         */
        public void setMsg(java.lang.String msg)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MSG$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(MSG$4);
                }
                target.setStringValue(msg);
            }
        }
        
        /**
         * Sets (as xml) the "msg" element
         */
        public void xsetMsg(org.apache.xmlbeans.XmlString msg)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(MSG$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(MSG$4);
                }
                target.set(msg);
            }
        }
        
        /**
         * Nils the "msg" element
         */
        public void setNilMsg()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(MSG$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(MSG$4);
                }
                target.setNil();
            }
        }
        
        /**
         * Gets the "phone" element
         */
        public java.lang.String getPhone()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PHONE$6, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "phone" element
         */
        public org.apache.xmlbeans.XmlString xgetPhone()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PHONE$6, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "phone" element
         */
        public boolean isNilPhone()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PHONE$6, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "phone" element
         */
        public void setPhone(java.lang.String phone)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PHONE$6, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PHONE$6);
                }
                target.setStringValue(phone);
            }
        }
        
        /**
         * Sets (as xml) the "phone" element
         */
        public void xsetPhone(org.apache.xmlbeans.XmlString phone)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PHONE$6, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PHONE$6);
                }
                target.set(phone);
            }
        }
        
        /**
         * Nils the "phone" element
         */
        public void setNilPhone()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PHONE$6, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PHONE$6);
                }
                target.setNil();
            }
        }
        
        /**
         * Gets the "sendtime" element
         */
        public java.lang.String getSendtime()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SENDTIME$8, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "sendtime" element
         */
        public org.apache.xmlbeans.XmlString xgetSendtime()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SENDTIME$8, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "sendtime" element
         */
        public boolean isNilSendtime()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SENDTIME$8, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "sendtime" element
         */
        public void setSendtime(java.lang.String sendtime)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SENDTIME$8, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SENDTIME$8);
                }
                target.setStringValue(sendtime);
            }
        }
        
        /**
         * Sets (as xml) the "sendtime" element
         */
        public void xsetSendtime(org.apache.xmlbeans.XmlString sendtime)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SENDTIME$8, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SENDTIME$8);
                }
                target.set(sendtime);
            }
        }
        
        /**
         * Nils the "sendtime" element
         */
        public void setNilSendtime()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SENDTIME$8, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SENDTIME$8);
                }
                target.setNil();
            }
        }
    }
}
