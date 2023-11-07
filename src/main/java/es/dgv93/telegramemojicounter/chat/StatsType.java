package es.dgv93.telegramemojicounter.chat;

import lombok.Getter;

public enum StatsType {
    MEAN("mean"),
    TOTAL("total"),
    MAX("max");
    @Getter
    private final String name;

    StatsType(String name) {
        this.name = name;
    }

}
