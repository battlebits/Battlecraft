package br.com.battlebits.battlecraft.protocol;

import br.com.battlebits.battlecraft.warp.Warp;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Sound;
import org.bukkit.plugin.Plugin;

import static com.comphenix.protocol.PacketType.Play.Server.NAMED_SOUND_EFFECT;
import static com.comphenix.protocol.PacketType.Play.Server.WORLD_PARTICLES;

public class OneVsOneFilter extends PacketAdapter {
    private Warp warp;

    public OneVsOneFilter(Plugin plugin, Warp warp) {
        super(plugin, NAMED_SOUND_EFFECT, WORLD_PARTICLES);
        this.warp = warp;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (!warp.inWarp(event.getPlayer()))
            return;
        if (event.getPacketType() == NAMED_SOUND_EFFECT) {
            Sound sound = event.getPacket().getSoundEffects().read(0);
            if(sound == Sound.ENTITY_PLAYER_ATTACK_SWEEP)
                event.setCancelled(true);
        } else if (event.getPacketType() == WORLD_PARTICLES) {
            EnumWrappers.Particle particle = event.getPacket().getParticles().read(0);
            if (particle == EnumWrappers.Particle.SWEEP_ATTACK || particle == EnumWrappers.Particle.DAMAGE_INDICATOR) {
                event.setCancelled(true);
            }
        }
    }
}
