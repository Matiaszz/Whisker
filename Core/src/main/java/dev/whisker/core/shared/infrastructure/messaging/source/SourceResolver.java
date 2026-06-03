package dev.whisker.core.shared.infrastructure.messaging.source;

import java.util.Map;

public interface SourceResolver {
    Source resolve(String source);
    void validate(String source, Map<String, Source> sourceMap) throws IllegalArgumentException;
}
