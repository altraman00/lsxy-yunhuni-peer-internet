/*
 * An XML document type.
 * Localname: __destruct
 * Namespace: urn:SY_SoapServer
 * Java type: sy_soapserver.DestructDocument
 *
 * Automatically generated - do not modify.
 */
package sy_soapserver.impl;
/**
 * A document containing one __destruct(@urn:SY_SoapServer) element.
 *
 * This is a complex type.
 */
public class DestructDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.DestructDocument
{
    private static final long serialVersionUID = 1L;
    
    public DestructDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DESTRUCT$0 = 
        new javax.xml.namespace.QName("urn:SY_SoapServer", "__destruct");
    
    
    /**
     * Gets the "__destruct" element
     */
    public sy_soapserver.DestructDocument.Destruct getDestruct()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.DestructDocument.Destruct target = null;
            target = (sy_soapserver.DestructDocument.Destruct)get_store().find_element_user(DESTRUCT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "__destruct" element
     */
    public void setDestruct(sy_soapserver.DestructDocument.Destruct destruct)
    {
        generatedSetterHelperImpl(destruct, DESTRUCT$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "__destruct" element
     */
    public sy_soapserver.DestructDocument.Destruct addNewDestruct()
    {
        synchronized (monitor())
        {
            check_orphaned();
            sy_soapserver.DestructDocument.Destruct target = null;
            target = (sy_soapserver.DestructDocument.Destruct)get_store().add_element_user(DESTRUCT$0);
            return target;
        }
    }
    /**
     * An XML __destruct(@urn:SY_SoapServer).
     *
     * This is a complex type.
     */
    public static class DestructImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements sy_soapserver.DestructDocument.Destruct
    {
        private static final long serialVersionUID = 1L;
        
        public DestructImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        
    }
}
