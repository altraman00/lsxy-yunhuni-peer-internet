/*
 * An XML document type.
 * Localname: get
 * Namespace: urn:SY_SoapServer
 * Java type: sy_soapserver.GetDocument
 *
 * Automatically generated - do not modify.
 */
package sy_soapserver.impl;
/**
 * A document containing one get(@urn:SY_SoapServer) element.
 *
 * This is a complex type.
 */
public class GetDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.GetDocument
{
    private static final long serialVersionUID = 1L;
    
    public GetDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName GET$0 = 
        new javax.xml.namespace.QName("urn:SY_SoapServer", "get");
    
    
    /**
     * Gets the "get" element
     */
    public sy_soapserver.GetDocument.Get getGet()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.GetDocument.Get target = null;
            target = (sy_soapserver.GetDocument.Get)get_store().find_element_user(GET$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "get" element
     */
    public void setGet(sy_soapserver.GetDocument.Get get)
    {
        generatedSetterHelperImpl(get, GET$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "get" element
     */
    public sy_soapserver.GetDocument.Get addNewGet()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.GetDocument.Get target = null;
            target = (sy_soapserver.GetDocument.Get)get_store().add_element_user(GET$0);
            return target;
        }
    }
    /**
     * An XML get(@urn:SY_SoapServer).
     *
     * This is a complex type.
     */
    public static class GetImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.GetDocument.Get
    {
        private static final long serialVersionUID = 1L;
        
        public GetImpl(org.apache.xmlbeans.SchemaType sType)
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
