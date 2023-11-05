package es.dgv93.telegramemojicounter.chat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SummaryStats {
    private String user;
    private Stats stats;

    @Data
    @Builder
    public static class Stats {
        private double total;
        private double mean;
        private double max;
        private double min;
    }
}
