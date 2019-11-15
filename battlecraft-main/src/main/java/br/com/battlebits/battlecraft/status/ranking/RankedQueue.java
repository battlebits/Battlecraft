package br.com.battlebits.battlecraft.status.ranking;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RankedQueue {

    private int victory;
    private int defeat;

//    private League league;
//    private double elo;
//    private double mmr;

    public void addVictory() {
        this.addVictory(null);
    }

    public void addVictory(RankedQueue against) {
        this.victory++;
//        if(against != null) {
//            this.elo += 0;
//        }
    }

    public void addDefeat() {
        this.addDefeat(null);
    }

    public void addDefeat(RankedQueue against) {
        this.defeat++;
//        if(against != null) {
////            this.elo -= 0;
//        }
    }

}
