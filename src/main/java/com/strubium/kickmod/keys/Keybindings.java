package com.strubium.kickmod.keys;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class Keybindings {

    public static KeyBinding kick;

    public static void init() {
        kick = new KeyBinding("key.kick", KeyConflictContext.IN_GAME, Keyboard.KEY_V, "key.categories.kickmod");
        ClientRegistry.registerKeyBinding(kick);
    }
}
