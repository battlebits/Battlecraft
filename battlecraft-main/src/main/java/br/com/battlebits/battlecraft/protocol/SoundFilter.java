package br.com.battlebits.battlecraft.protocol;

import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Sound;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;

import static com.comphenix.protocol.PacketType.Play.Server.NAMED_SOUND_EFFECT;

public class SoundFilter extends PacketAdapter {

    private Set<Sound> soundIgnore;

    public SoundFilter(Plugin plugin) {
        super(plugin, NAMED_SOUND_EFFECT);
        soundIgnore = new HashSet<>();
        soundIgnore.add(Sound.ENTITY_PLAYER_ATTACK_CRIT);
        soundIgnore.add(Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK);
        soundIgnore.add(Sound.ENTITY_PLAYER_ATTACK_NODAMAGE);
        soundIgnore.add(Sound.ENTITY_PLAYER_ATTACK_STRONG);
        soundIgnore.add(Sound.ENTITY_PLAYER_ATTACK_WEAK);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        Sound sound = event.getPacket().getSoundEffects().read(0);
        if(soundIgnore.contains(sound))
            event.setCancelled(true);
    }
}
