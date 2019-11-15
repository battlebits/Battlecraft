package br.com.battlebits.battlecraft.status.warpstatus;

import br.com.battlebits.battlecraft.ability.Kit;
import br.com.battlebits.battlecraft.status.WarpStatus;
import br.com.battlebits.battlecraft.warp.Warp;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class StatusMain extends WarpStatus {

    // Status
    private int kills;
    private int deaths;

    // Kits
    @Getter(value = AccessLevel.NONE)
    private Set<Kit> ownedKits;

    // Runtime variables
    private transient int killstreak;
    private transient int multikill;
    @Getter(value = AccessLevel.NONE)
    private transient long lastKillTimeMillis;

    // Records
    private int killstreakRecord;
    private int multikillRecord;

    public StatusMain(Warp warp, int kills, int deaths, int killstreakRecord, int multikillRecord) {
        this(warp, kills, deaths, null, killstreakRecord, multikillRecord);
    }

    public StatusMain(Warp warp, int kills, int deaths, List<String> kitList, int killstreakRecord, int multikillRecord) {
        super();
        this.kills = kills;
        this.deaths = deaths;
        this.killstreakRecord = killstreakRecord;
        this.multikillRecord = multikillRecord;
        this.ownedKits = new HashSet<>();
        if(kitList != null)
            warp.getKits().stream().filter(kit -> kitList.contains(kit.getName())).forEach(kit -> this.ownedKits.add(kit));
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
            this.save("killstreakRecord", killstreakRecord);
        }
        if (this.multikill > this.multikillRecord) {
            this.multikillRecord = this.multikill;
            this.save("multikillRecord", multikillRecord);
        }
        this.lastKillTimeMillis = System.currentTimeMillis();
        this.save("kills", kills);
    }

    /**
     * Adiciona uma morte ao status do jogador
     */
    public void addDeath() {
        this.deaths++;
        this.multikill = 0;
        this.save("deaths", deaths);
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
        this.ownedKits.add(kit);
        List<String> kitList = new ArrayList<>();
        this.ownedKits.forEach(kit1 -> kitList.add(kit1.getName()));
        this.save("ownedKits", kitList);
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
        return this.ownedKits.contains(kit);
    }
}
