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

            # IDEs & Tools
            .idea/
            .vscode/
            .settings/
            .wakatime/

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

            # Logs & Traces
            *.log
            *.etl

            # Lock files
            package-lock.json
            yarn.lock
            pnpm-lock.yaml
            pubspec.lock

            # Binaries & Virtual Disks
            *.exe
            *.dll
            *.so
            *.dylib
            *.jar
            *.war
            *.vhdx

            # System & AppData
            .DS_Store
            Thumbs.db
            AppData/
            Local/
            Roaming/
            LocalLow/

            # Temp & Caches
            Temp/
            tmp/
            *.tmp
            *.temp

            # Local Databases & Journals
            *-journal
            *.vscdb
            *.bdb
            *.leveldb/
            """;
}