package dev.whisker.core.shared.infrastructure.messaging.source;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public enum Source {
    CORE,
    CLI,
    DESKTOP;
}
