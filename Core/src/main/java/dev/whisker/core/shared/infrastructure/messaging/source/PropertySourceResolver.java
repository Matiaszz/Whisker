package dev.whisker.core.shared.infrastructure.messaging.source;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@NoArgsConstructor
@Getter
public class PropertySourceResolver implements SourceResolver {
    public static final String CORE_SOURCE_NAME = "whisker-core";
    public static final String CLI_SOURCE_NAME = "whisker-cli";
    public static final String DESKTOP_SOURCE_NAME = "whisker-desktop";


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
                CORE_SOURCE_NAME, Source.CORE,
                CLI_SOURCE_NAME, Source.CLI,
                DESKTOP_SOURCE_NAME, Source.DESKTOP);
    }
}
