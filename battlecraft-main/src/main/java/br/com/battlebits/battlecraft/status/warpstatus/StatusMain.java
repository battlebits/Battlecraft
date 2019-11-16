package br.com.battlebits.battlecraft.status.warpstatus;

import br.com.battlebits.battlecraft.ability.Kit;
import br.com.battlebits.battlecraft.status.WarpStatus;
import dev.morphia.annotations.Embedded;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Embedded
public class StatusMain implements WarpStatus {

    // Status
    private int kills;
    private int deaths;

    // Kits
    @Getter(value = AccessLevel.NONE)
    private Set<String> ownedKits;

    // Runtime variables
    private transient int killstreak;
    private transient int multikill;
    @Getter(value = AccessLevel.NONE)
    private transient long lastKillTimeMillis;

    // Records
    private int killstreakRecord;
    private int multikillRecord;

    public StatusMain(int kills, int deaths, int killstreakRecord, int multikillRecord) {
        this(kills, deaths, null, killstreakRecord, multikillRecord);
    }

    public StatusMain(int kills, int deaths, Set<String> kitList, int killstreakRecord, int multikillRecord) {
        super();
        this.kills = kills;
        this.deaths = deaths;
        this.killstreakRecord = killstreakRecord;
        this.multikillRecord = multikillRecord;
        if(kitList != null)
            this.ownedKits = kitList;
        else
            this.ownedKits = new HashSet<>();
    }

    /**
     * Adiciona uma kill ao jogador e ja calcula killstreak e multikills junto
     */
    public void addKill() {
        this.kills++;
        this.killstreak++;
        if (System.currentTimeMillis() < this.lastKillTimeMillis + 5000) {
            this.multikill++;
        } else {
            this.multikill = 1;
        }
        if (this.killstreak > this.killstreakRecord) {
            this.killstreakRecord = this.killstreak;
        }
        if (this.multikill > this.multikillRecord) {
            this.multikillRecord = this.multikill;
        }
        this.lastKillTimeMillis = System.currentTimeMillis();
    }

    /**
     * Adiciona uma morte ao status do jogador
     */
    public void addDeath() {
        this.deaths++;
        this.multikill = 0;
    }

    /**
     * Reseta o killstreak atual
     */
    public void resetKillstreak() {
        this.killstreak = 0;
    }

    /**
     * Da um kit para o jogador
     * @param kit kit selecionado
     */
    public void giveKit(Kit kit) {
        this.ownedKits.add(kit.getName());
    }

    /**
     * Calcula o multikill baseado na ultima kill dos ultimos 5 segundos
     * @return 0 se o tempo ja expirou ou o valor
     */
    public int getMultikill() {
        if (System.currentTimeMillis() >= this.lastKillTimeMillis + 5000)
            return 0;
        return this.multikill;
    }

    /**
     * Checka se o jogador possui um kit
     * @param kit o kit selecionado
     * @return true se possui, false se nao possui
     */
    public boolean containsKit(Kit kit) {
        return this.ownedKits.contains(kit.getName());
    }
}
