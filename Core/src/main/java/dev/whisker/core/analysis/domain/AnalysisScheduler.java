package dev.whisker.core.analysis.domain;

import dev.whisker.core.monitoring.domain.FileWatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisScheduler {

    @Scheduled(fixedDelayString = "${whisker.analysis-interval:PT30M}")
    public void analyze() {

        log.info("[SCHEDULER] Iniciando ciclo de análise agendada...");

        // Aqui entrará a lógica de chamar o AIAnalyzer e GitAnalyzer
        
        log.info("[SCHEDULER] Aguardando próximo ciclo...");
    }
}
