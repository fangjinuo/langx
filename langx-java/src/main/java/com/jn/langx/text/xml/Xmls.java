package com.jn.langx.text.xml;

import com.jn.langx.text.xml.cutomizer.DocumentBuilderFactoryCustomizer;
import com.jn.langx.text.xml.cutomizer.secure.SecureDocumentBuilderFactoryCustomizer;
import com.jn.langx.text.xml.errorhandler.RaiseErrorHandler;
import com.jn.langx.text.xml.resolver.DTDEntityResolver;
import com.jn.langx.text.xml.resolver.NullEntityResolver;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.io.IOs;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;

public class Xmls {
    public static final String NULL_XML_STR = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    private Xmls() {
    }

    public static Document getXmlDoc(InputStream xml) throws Exception {
        return getXmlDoc(null, xml);
    }

    public static Document getXmlDoc(EntityResolver entityResolver, final InputStream xml) throws Exception {
        return getXmlDoc(entityResolver, null, xml);
    }

    public static Document getXmlDoc(EntityResolver entityResolver, ErrorHandler errorHandler, final InputStream xml) throws Exception {
        return getXmlDoc(entityResolver, errorHandler, xml, true);
    }

    public static Document getXmlDoc(EntityResolver entityResolver, ErrorHandler errorHandler, final InputStream xml, boolean namespaceAware) throws Exception {
        return getXmlDoc(entityResolver, errorHandler, xml, false, false, namespaceAware);
    }



    public static Document getXmlDoc(
            EntityResolver entityResolver,
            ErrorHandler errorHandler,
            final InputStream xml,
            boolean ignoreComments,
            boolean ignoringElementContentWhitespace,
            boolean namespaceAware
    ) throws Exception {
        return Xmls.getXmlDoc(entityResolver, errorHandler, xml, ignoreComments, ignoringElementContentWhitespace, namespaceAware, SecureDocumentBuilderFactoryCustomizer.DEFAULT);
    }

    /**
     * @since 5.2.9
     */
    public static Document getXmlDoc(
            EntityResolver entityResolver,
            ErrorHandler errorHandler,
            final InputStream xml,
            boolean ignoreComments,
            boolean ignoringElementContentWhitespace,
            boolean namespaceAware,
            DocumentBuilderFactoryCustomizer customizer
    ) throws Exception {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(ignoreComments);
        factory.setIgnoringElementContentWhitespace(ignoringElementContentWhitespace);
        factory.setNamespaceAware(namespaceAware);
        if (entityResolver != null) {
            factory.setValidating(true);
        }

        if (customizer != null) {
            customizer.customize(factory);
        }

        final DocumentBuilder builder = factory.newDocumentBuilder();
        entityResolver = ((entityResolver == null) ? new NullEntityResolver() : entityResolver);
        builder.setEntityResolver(entityResolver);
        builder.setErrorHandler(errorHandler == null ? new RaiseErrorHandler() : errorHandler);
        return builder.parse(xml);
    }

    public static Document getXmlDoc(final InputStream dtdInputStream, final String src, final boolean srcIsPath) throws Exception {
        if (srcIsPath) {
            return getXmlDoc(dtdInputStream, src);
        }
        return getXmlDoc(new DTDEntityResolver(dtdInputStream), new ByteArrayInputStream(src.getBytes(Charsets.UTF_8)));
    }

    public static Document getXmlDoc(final InputStream dtdInputStream, final String xmlfilepathOrURI) throws Exception {
        final File file = new File(xmlfilepathOrURI);
        if (!file.exists()) {
            throw new FileNotFoundException("File '" + xmlfilepathOrURI + "' does not exist .");
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            return getXmlDoc(new DTDEntityResolver(dtdInputStream), fis);
        } finally {
            IOs.close(fis);
        }
    }

    public static void writeDocToOutputStream(final Document doc, final OutputStream out) throws Exception {
        final Transformer transformer = newTransformer();
        transformer.transform(new DOMSource(doc), new StreamResult(out));
    }

    private static final String FEATURE_SECURE_PROCESSING = "http://javax.xml.XMLConstants/feature/secure-processing";

    public static void writeDocToFile(final Document doc, final File file) throws TransformerFactoryConfigurationError, TransformerException {
        Transformer transformer = newTransformer();
        transformer.transform(new DOMSource(doc), new StreamResult(file));
    }

    public static Transformer newTransformer() throws TransformerConfigurationException {
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, factory);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, false);
        factory.setAttribute(FEATURE_SECURE_PROCESSING, true);
        final Transformer trans = factory.newTransformer();
        return trans;
    }

    public static <T> T handleXml(final String xmlpath, final XmlDocumentHandler<T> handler) {
        InputStream input = null;
        Document doc;
        try {
            final File file = new File(xmlpath);
            if (!file.exists()) {
                throw new FileNotFoundException("File '" + xmlpath + "' does not exist .");
            }
            input = new FileInputStream(file);
            doc = getXmlDoc(null, input);
            return handler.handle(doc);
        } catch (Exception ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        } finally {
            IOs.close(input);
        }
    }


    public static NodeList findNodeList(Document doc, String xpath) throws XPathExpressionException {
        NodeList nodes = new XmlAccessor(Namespaces.hasCustomNamespace(doc) ? "x" : null).getNodeList(doc, XPathFactory.newInstance(), xpath);
        return nodes;
    }

    public static Element findElement(Document doc, String xpath) throws XPathExpressionException {
        Element element = new XmlAccessor(Namespaces.hasCustomNamespace(doc) ? "x" : null).getElement(doc, XPathFactory.newInstance(), xpath);
        return element;
    }
    /**
     * @since 5.2.7
     */
    public static void setFeature(DocumentBuilderFactory factory, String feature, boolean enabled) {
        try {
            factory.setFeature(feature, enabled);
        } catch (ParserConfigurationException e) {
            // ignore it
        }
    }

    /**
     * @since 5.2.9
     */
    public static void setFeature(SAXParserFactory factory, String feature, boolean enabled) {
        try {
            factory.setFeature(feature, enabled);
        } catch (org.xml.sax.SAXNotRecognizedException e) {
            // ignore it
        } catch (org.xml.sax.SAXNotSupportedException e) {
            // ignore it
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            // ignore it
        }
    }

    public static void setProperty(XMLInputFactory factory, String feature, boolean enabled) {
        try {
            factory.setProperty(feature, enabled);
        } catch (IllegalArgumentException e) {
            // ignore it
        }
    }

    public static void setAttribute(TransformerFactory factory, String feature, boolean enabled) {
        try {
            factory.setAttribute(feature, enabled);
        } catch (IllegalArgumentException e) {
            // ignore it
        }
    }

    public static void setFeature(TransformerFactory factory, String feature, boolean enabled) {
        try {
            factory.setFeature(feature, enabled);
        } catch (javax.xml.transform.TransformerConfigurationException e) {
            // ignore it
        }
    }

    public static void setProperty(SchemaFactory factory, String feature, boolean enabled) {
        try {
            factory.setProperty(feature, enabled);
        } catch (IllegalArgumentException e) {
            // ignore it
        } catch (org.xml.sax.SAXNotRecognizedException e) {
            // ignore it
        } catch (org.xml.sax.SAXNotSupportedException e) {
            // ignore it
        }
    }

    public static void setFeature(SchemaFactory factory, String feature, boolean enabled) {
        try {
            factory.setFeature(feature, enabled);
        } catch (org.xml.sax.SAXNotSupportedException e) {
            // ignore it
        } catch (org.xml.sax.SAXNotRecognizedException e) {
            // ignore it
        }
    }

}
