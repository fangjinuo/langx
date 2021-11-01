/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc., and individual contributors as indicated
 * by the @authors tag.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jn.langx.beans.propertyeditor;

import org.jboss.util.Strings;

import java.beans.PropertyEditorSupport;

/**
 * Locale editor.
 */
public class LocaleEditor extends PropertyEditorSupport {
    public void setAsText(String text) {
        setValue(Strings.parseLocaleString(text));
    }

    public String getAsText() {
        Object value = getValue();
        return (value != null ? value.toString() : "");
    }
}
