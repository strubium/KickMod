package com.strubium.kickmod;

import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;

public class CooldownManager {
    private static final HashMap<EntityPlayer, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_TIME = 1000; // 1 second cooldown in milliseconds

    public static boolean canKick(EntityPlayer player) {
        long currentTime = System.currentTimeMillis();
        long lastKickTime = cooldowns.getOrDefault(player, 0L);

        // Check if enough time has passed since the last kick
        if (currentTime - lastKickTime >= COOLDOWN_TIME) {
            // Update the last kick time
            cooldowns.put(player, currentTime);
            return true; // Player can kick
        }

        return false; // Player cannot kick yet
    }
}

