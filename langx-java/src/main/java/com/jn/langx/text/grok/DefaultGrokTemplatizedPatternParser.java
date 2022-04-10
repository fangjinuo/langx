package com.jn.langx.text.grok;

import com.jn.langx.Converter;
import com.jn.langx.text.placeholder.PlaceholderExpressionConsumer;
import com.jn.langx.text.placeholder.PropertyPlaceholderHandler;
import com.jn.langx.text.placeholder.PropertySourcePlaceholderParser;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.converter.IntegerConverter;
import com.jn.langx.util.regexp.Regexps;
import com.jn.langx.util.struct.Holder;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


/**
 * @since 4.5.0
 */
public class DefaultGrokTemplatizedPatternParser implements GrokTemplatizedPatternParser {

    private PropertySourcePlaceholderParser patternDefinitionSource;

    public void setPatternDefinitionRepository(PatternDefinitionRepository repository) {
        PatternDefinitionSource source = new PatternDefinitionSource();
        source.setRepository(repository);
        repository.startup();
        this.patternDefinitionSource = new PropertySourcePlaceholderParser(source);
    }

    @Override
    public TemplatizedPattern parse(String patternTemplate) {
        Preconditions.checkNotNull(patternTemplate, "template");

        final Set<String> fields = new LinkedHashSet<String>();
        final Map<String, Converter> converterMap = new HashMap<String, Converter>();
        PlaceholderExpressionConsumer consumer = new PlaceholderExpressionConsumer() {
            @Override
            public void accept(String variable, String expression, Holder<String> variableValueHolder) {
                if (Strings.isNotEmpty(expression) && !variableValueHolder.isEmpty()) {
                    expression = Strings.replace(expression,"][","_");
                    expression = Strings.replace(expression,"[","");
                    expression = Strings.replace(expression,"]","");
                    Converter converter = null;
                    String field = null;
                    String variableValue = variableValueHolder.get();
                    if (expression.contains(":")) {
                        String[] segments = Strings.split(expression, ":");
                        if (segments.length > 0) {
                            field = segments[0];
                            if (segments.length >= 2) {
                                String converterSegment = segments[1];
                                if ("int".equals(converterSegment)) {
                                    converter = new IntegerConverter();
                                }
                            }
                        }
                    }else{
                        field = expression;
                    }

                    if(field!=null){
                        variableValue = "(?<" + field + ">" + variableValue + ")";
                        variableValueHolder.set(variableValue);
                        fields.add(field);
                        if(converter!=null){
                            converterMap.put(field, converter);
                        }
                    }
                }
            }
        };

        PropertyPlaceholderHandler handler = new PropertyPlaceholderHandler("%{", "}", ":", false);
        handler.setExpressionConsumer(consumer);
        String parsedPattern = handler.replacePlaceholders(patternTemplate, this.patternDefinitionSource);

        TemplatizedPattern pattern = new TemplatizedPattern();
        pattern.setExpression(patternTemplate);
        pattern.setRegexp(Regexps.createRegexp(parsedPattern));
        pattern.setFields(fields);
        pattern.setExpectedConverters(converterMap);

        return pattern;
    }

}
