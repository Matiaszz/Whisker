package dev.whisker.core.monitoring.infrastructure.files;

import dev.whisker.core.shared.domain.system.exceptions.DriveRootMonitoringException;
import jakarta.annotation.Nonnull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static dev.whisker.core.monitoring.infrastructure.files.WhiskerConstants.DEFAULT_WHISKER_IGNORE;
import static java.nio.file.StandardWatchEventKinds.*;

@Slf4j
@Component
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class FileSystemAdapter {
    private Path workdir;
    private final List<PathMatcher> matchers = new ArrayList<>();
    private WatchService watcher;
    private boolean setup = true;
    private boolean initialized = false;

    public void setup(Path workdir) throws IOException, DriveRootMonitoringException {
        // Security validation: Do not allow the root directory
        if (workdir.getParent() == null) {
            throw new DriveRootMonitoringException("Monitoring the system root directory is not allowed.");
        }

        this.workdir = workdir;
        this.watcher = FileSystems.getDefault().newWatchService();
        this.loadWhiskerIgnore();
        this.registerDirectory(workdir);
        this.setup = false;
        this.initialized = true;
    }

    public void registerDirectory(Path root) throws IOException {
        if (!isSetup() && !isInitialized()){
            throw new IllegalStateException(
                    "FileSystemAdapter must be set up before registering directories"
            );
        }

        // Double check: Prevents sub-registrations from attempting to map the root
        if (root.getParent() == null) {
            log.warn("[WATCHER] Ignoring attempt to register the root directory: {}", root);
            return;
        }

        // Replacing Files.walk with walkFileTree for access control
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            @Nonnull
            public FileVisitResult preVisitDirectory(@Nonnull Path dir, @NonNull BasicFileAttributes attrs) {
                if (shouldIgnore(dir)) {
                    return FileVisitResult.SKIP_SUBTREE; // Optimization: ignore everything inside the folder
                }

                try {
                    register(dir);
                } catch (AccessDeniedException e) {
                    log.warn("[WATCHER] No permission to register directory (ignored): {}", dir);
                    return FileVisitResult.SKIP_SUBTREE;
                } catch (IOException e) {
                    log.error("[WATCHER] Failed to register {}", dir, e);
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            @Nonnull
            public FileVisitResult visitFileFailed(@Nonnull Path file, @Nonnull IOException exc) {
                // Catches AccessDeniedException thrown during walkFileTree's deep read
                if (exc instanceof AccessDeniedException) {
                    log.debug("[WATCHER] Access denied ignored at: {}", file);
                } else {
                    log.warn("[WATCHER] Failed to access {}: {}", file, exc.getMessage());
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public boolean shouldIgnore(Path path) {
        Path relative;

        Path fileNamePath = path.getFileName();

        if (fileNamePath == null) {
            return false;
        }

        String fileName = fileNamePath.toString();
        boolean isIgnorable = fileName.endsWith("~") || fileName.endsWith(".tmp") || fileName.endsWith(".temp");

        if (isIgnorable) {
            return true;
        }

        try {
            relative = workdir.relativize(path);
        } catch (Exception e) {
            return false;
        }

        return matchers.stream()
                .anyMatch(matcher -> matcher.matches(relative));
    }

    private void register(Path dir) throws IOException {
        if (shouldIgnore(dir)) {
            return;
        }

        dir.register(
                watcher,
                ENTRY_CREATE,
                ENTRY_DELETE,
                ENTRY_MODIFY
        );

        log.debug("[WATCHER] Registered {}", dir);
    }

    private void loadWhiskerIgnore() throws IOException {
        matchers.clear();

        // Usamos um Set para garantir que não haverá padrões duplicados
        Set<String> uniquePatterns = new HashSet<>();

        // 1. Carrega sempre as regras padrão
        DEFAULT_WHISKER_IGNORE.lines().forEach(line -> extractPattern(line, uniquePatterns));

        // 2. Verifica se existe um arquivo customizado
        Path whiskerIgnore = workdir.resolve(".whiskerignore");

        if (Files.exists(whiskerIgnore)) {
            // 3. Adiciona as regras extras (o Set ignora automaticamente as que já existem no default)
            List<String> lines = Files.readAllLines(whiskerIgnore);
            lines.forEach(line -> extractPattern(line, uniquePatterns));

            log.debug("[WATCHER] Custom .whiskerignore loaded with additional rules.");
        }

        // 4. Converte os padrões únicos em matchers reais
        for (String pattern : uniquePatterns) {
            matchers.add(
                    FileSystems.getDefault().getPathMatcher("glob:" + pattern)
            );
        }
    }

    private void extractPattern(String line, Set<String> patterns) {
        line = line.trim();

        if (line.isBlank() || line.startsWith("#")) {
            return;
        }

        if (line.endsWith("/")) {
            line += "**";
        }

        patterns.add(line);
    }
}