package com.strubium.kickmod.keys;

import com.strubium.kickmod.KickMod;
import com.strubium.kickmod.KickPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class KeyInputHandler {

    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keybindings.kick.isPressed()) {
            System.out.println("Kicking!");

            EntityPlayer player = mc.player;
            if (player != null) {
                // Get player's look direction
                Vec3d look = player.getLook(1.0F);

                // Get entities within a bounding box extending in front of the player
                List<Entity> entities = player.world.getEntitiesWithinAABB(Entity.class,
                        player.getEntityBoundingBox().expand(look.x * 10.0D, look.y * 10.0D, look.z * 10.0D));

                for (Entity target : entities) {
                    if (target != player && target.canBePushed()) { // Avoid kicking the player itself
                        // Send a packet to the server with the target entity's ID
                        KickPacket packet = new KickPacket(target.getEntityId());
                        KickMod.NETWORK.sendToServer(packet);
                        break; // Only kick the first entity detected
                    }
                }
            }
        }
    }
}
