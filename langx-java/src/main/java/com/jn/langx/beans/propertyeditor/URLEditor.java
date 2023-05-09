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


import com.jn.langx.util.net.URLs;

/**
 * A property editor for {@link java.net.URL}.
 */
public class URLEditor extends TextPropertyEditorSupport {
    /**
     * Returns a URL for the input object converted to a string.
     *
     * @return a URL object
     * @throws RuntimeException An MalformedURLException occured.
     */
    @Override
    public Object getValue() {
        try {
            return URLs.newURL(getAsText());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
