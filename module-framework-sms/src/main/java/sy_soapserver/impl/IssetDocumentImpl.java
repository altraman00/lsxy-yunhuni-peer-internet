/*
 * An XML document type.
 * Localname: __isset
 * Namespace: urn:SY_SoapServer
 * Java type: sy_soapserver.IssetDocument
 *
 * Automatically generated - do not modify.
 */
package sy_soapserver.impl;
/**
 * A document containing one __isset(@urn:SY_SoapServer) element.
 *
 * This is a complex type.
 */
public class IssetDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.IssetDocument
{
    private static final long serialVersionUID = 1L;
    
    public IssetDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ISSET$0 = 
        new javax.xml.namespace.QName("urn:SY_SoapServer", "__isset");
    
    
    /**
     * Gets the "__isset" element
     */
    public sy_soapserver.IssetDocument.Isset getIsset()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.IssetDocument.Isset target = null;
            target = (sy_soapserver.IssetDocument.Isset)get_store().find_element_user(ISSET$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "__isset" element
     */
    public void setIsset(sy_soapserver.IssetDocument.Isset isset)
    {
        generatedSetterHelperImpl(isset, ISSET$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "__isset" element
     */
    public sy_soapserver.IssetDocument.Isset addNewIsset()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.IssetDocument.Isset target = null;
            target = (sy_soapserver.IssetDocument.Isset)get_store().add_element_user(ISSET$0);
            return target;
        }
    }
    /**
     * An XML __isset(@urn:SY_SoapServer).
     *
     * This is a complex type.
     */
    public static class IssetImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.IssetDocument.Isset
    {
        private static final long serialVersionUID = 1L;
        
        public IssetImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName NAME$0 = 
            new javax.xml.namespace.QName("", "name");
        
        
        /**
         * Gets the "name" element
         */
        public java.lang.String getName()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NAME$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "name" element
         */
        public org.apache.xmlbeans.XmlString xgetName()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NAME$0, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "name" element
         */
        public boolean isNilName()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NAME$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "name" element
         */
        public void setName(java.lang.String name)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NAME$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NAME$0);
                }
                target.setStringValue(name);
            }
        }
        
        /**
         * Sets (as xml) the "name" element
         */
        public void xsetName(org.apache.xmlbeans.XmlString name)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NAME$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NAME$0);
                }
                target.set(name);
            }
        }
        
        /**
         * Nils the "name" element
         */
        public void setNilName()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NAME$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NAME$0);
                }
                target.setNil();
            }
        }
    }
}
