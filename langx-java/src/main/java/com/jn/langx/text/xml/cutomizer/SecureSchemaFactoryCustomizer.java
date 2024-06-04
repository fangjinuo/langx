package com.jn.langx.text.xml.cutomizer;

import com.jn.langx.Customizer;
import com.jn.langx.text.xml.Xmls;

import javax.xml.XMLConstants;
import javax.xml.validation.SchemaFactory;

/**
 * @since 5.2.9
 */
public class SecureSchemaFactoryCustomizer implements Customizer<SchemaFactory> {

    @Override
    public void customize(SchemaFactory factory) {
        Xmls.setFeature(factory, "http://apache.org/xml/features/disallow-doctype-decl", true);
        Xmls.setProperty(factory, XMLConstants.ACCESS_EXTERNAL_DTD, false);
        Xmls.setProperty(factory, XMLConstants.ACCESS_EXTERNAL_SCHEMA, false);
    }
}