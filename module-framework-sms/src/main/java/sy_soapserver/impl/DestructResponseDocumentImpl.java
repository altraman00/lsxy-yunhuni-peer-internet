/*
 * An XML document type.
 * Localname: __destructResponse
 * Namespace: urn:SY_SoapServer
 * Java type: sy_soapserver.DestructResponseDocument
 *
 * Automatically generated - do not modify.
 */
package sy_soapserver.impl;
/**
 * A document containing one __destructResponse(@urn:SY_SoapServer) element.
 *
 * This is a complex type.
 */
public class DestructResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.DestructResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public DestructResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DESTRUCTRESPONSE$0 = 
        new javax.xml.namespace.QName("urn:SY_SoapServer", "__destructResponse");
    
    
    /**
     * Gets the "__destructResponse" element
     */
    public sy_soapserver.DestructResponseDocument.DestructResponse getDestructResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.DestructResponseDocument.DestructResponse target = null;
            target = (sy_soapserver.DestructResponseDocument.DestructResponse)get_store().find_element_user(DESTRUCTRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "__destructResponse" element
     */
    public void setDestructResponse(sy_soapserver.DestructResponseDocument.DestructResponse destructResponse)
    {
        generatedSetterHelperImpl(destructResponse, DESTRUCTRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "__destructResponse" element
     */
    public sy_soapserver.DestructResponseDocument.DestructResponse addNewDestructResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.DestructResponseDocument.DestructResponse target = null;
            target = (sy_soapserver.DestructResponseDocument.DestructResponse)get_store().add_element_user(DESTRUCTRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML __destructResponse(@urn:SY_SoapServer).
     *
     * This is a complex type.
     */
    public static class DestructResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.DestructResponseDocument.DestructResponse
    {
        private static final long serialVersionUID = 1L;
        
        public DestructResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName DESTRUCT$0 = 
            new javax.xml.namespace.QName("", "__destruct");
        
        
        /**
         * Gets the "__destruct" element
         */
        public String getDestruct()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DESTRUCT$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "__destruct" element
         */
        public org.apache.xmlbeans.XmlString xgetDestruct()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESTRUCT$0, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "__destruct" element
         */
        public boolean isNilDestruct()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESTRUCT$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "__destruct" element
         */
        public void setDestruct(String destruct)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DESTRUCT$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DESTRUCT$0);
                }
                target.setStringValue(destruct);
            }
        }
        
        /**
         * Sets (as xml) the "__destruct" element
         */
        public void xsetDestruct(org.apache.xmlbeans.XmlString destruct)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESTRUCT$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DESTRUCT$0);
                }
                target.set(destruct);
            }
        }
        
        /**
         * Nils the "__destruct" element
         */
        public void setNilDestruct()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESTRUCT$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DESTRUCT$0);
                }
                target.setNil();
            }
        }
    }
}
