package es.dgv93.telegramemojicounter.chat;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Stats {

    @Data
    @Builder
    static class StatsByUser {
        private String user;
        private Map<String, Double> statsByUser;

    }

    @Data
    @Builder
    static class GeneralStats {
        private String statName;
        private Map<String, Double> summaryStats;

    }

}
