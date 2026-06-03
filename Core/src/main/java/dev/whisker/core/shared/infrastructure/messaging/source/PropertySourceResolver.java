package dev.whisker.core.shared.infrastructure.messaging.source;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PropertySourceResolver implements SourceResolver {
    private final String coreSourceName;
    private final String cliSourceName;
    private final String desktopSourceName;


    @Override
    public void validate(String source, Map<String, Source> sourceMap) throws IllegalArgumentException{
        if (!sourceMap.containsKey(source)){
            throw new IllegalArgumentException("Source not allowed: " + source);
        }
    }

    @Override
    public Source resolve(String source) throws IllegalArgumentException{
        Map<String, Source> sourceMap = this.getValidSources();

        this.validate(source, sourceMap);
        return sourceMap.get(source);
    }

    private Map<String, Source> getValidSources(){
        return Map.of(
                coreSourceName, Source.CORE,
                cliSourceName, Source.CLI,
                desktopSourceName, Source.DESKTOP);
    }

    public PropertySourceResolver() {
        this.coreSourceName = "whisker-core";
        this.cliSourceName = "whisker-cli";
        this.desktopSourceName = "whisker-desktop";
    }


}
