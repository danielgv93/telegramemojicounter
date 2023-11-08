package es.dgv93.telegramemojicounter.chat;

import lombok.Getter;

public enum StatsType {
    MEAN("Media"),
    TOTAL("Total"),
    MAX("Maximo");
    @Getter
    private final String name;

    StatsType(String name) {
        this.name = name;
    }

}
