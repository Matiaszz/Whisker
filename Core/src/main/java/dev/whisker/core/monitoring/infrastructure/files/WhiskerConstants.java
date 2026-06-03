package dev.whisker.core.monitoring.infrastructure.files;

public class WhiskerConstants {
    public static final String DEFAULT_WHISKER_IGNORE = """
            # Dependencies
            node_modules/
            vendor/
            .venv/
            venv/
            __pycache__/

            # Git
            .git/

            # IDEs
            .idea/
            .vscode/
            .settings/

            # Java
            target/
            .gradle/
            build/
            out/

            # Flutter / Dart
            .dart_tool/
            .flutter-plugins
            .flutter-plugins-dependencies

            # Front-end
            .next/
            dist/
            coverage/

            # Logs
            *.log

            # Lock files
            package-lock.json
            yarn.lock
            pnpm-lock.yaml
            pubspec.lock

            # Binaries
            *.exe
            *.dll
            *.so
            *.dylib
            *.jar
            *.war

            # System
            .DS_Store
            Thumbs.db
            """;
}
