package com.strubium.kickmod;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class KickPacket implements IMessage {
    private int targetEntityId;

    public KickPacket() {
        // Default constructor
    }

    public KickPacket(int targetEntityId) {
        this.targetEntityId = targetEntityId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        targetEntityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(targetEntityId);
    }

    public static class Handler implements IMessageHandler<KickPacket, IMessage> {
        @Override
        public IMessage onMessage(KickPacket message, MessageContext ctx) {
            // This runs on the server
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayer player = ctx.getServerHandler().player;
                Entity target = player.world.getEntityByID(message.targetEntityId);

                // Check cooldown before applying knockback
                if (CooldownManager.canKick(player)) {
                    if (target != null) {
                        applyKnockback(target, player);
                    }
                } else {
                    System.out.println(player.getName() + " is on cooldown and cannot kick!");
                }
            });
            return null; // No response packet
        }

        private void applyKnockback(Entity target, EntityPlayer player) {
            // Implement your knockback logic here
            Vec3d direction = target.getPositionVector().subtract(player.getPositionVector()).normalize();
            double knockbackStrength = 1.01; // TODO Adjust this

            target.motionX += direction.x * knockbackStrength;
            target.motionY += 0.5; // Add some upward force
            target.motionZ += direction.z * knockbackStrength;

            target.velocityChanged = true;

            // Print debug information
            System.out.println("Kicking " + target.getName() + " with motion: (" +
                    target.motionX + ", " + target.motionY + ", " +
                    target.motionZ + ")");
        }
    }
}
