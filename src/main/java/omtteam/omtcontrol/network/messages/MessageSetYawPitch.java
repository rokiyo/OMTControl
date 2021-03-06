package omtteam.omtcontrol.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import omtteam.openmodularturrets.tileentity.turrets.TurretHead;

import static omtteam.omlib.util.MathUtil.getRotationXYFromYawPitch;
import static omtteam.omlib.util.MathUtil.getRotationXZFromYawPitch;


@SuppressWarnings("unused")
public class MessageSetYawPitch implements IMessage {
    private int x, y, z;
    private float yaw, pitch;

    public MessageSetYawPitch() {
    }

    @SuppressWarnings("ConstantConditions")
    public static class MessageHandlerSetYawPitch implements IMessageHandler<MessageSetYawPitch, IMessage> {
        @Override
        public IMessage onMessage(MessageSetYawPitch messageIn, MessageContext ctxIn) {
            final MessageSetYawPitch message = messageIn;
            final MessageContext ctx = ctxIn;
            ((WorldServer) ctx.getServerHandler().playerEntity.getEntityWorld()).addScheduledTask(() -> {
                World world = ctx.getServerHandler().playerEntity.getEntityWorld();
                TurretHead turret = (TurretHead) world.getTileEntity(new BlockPos(message.getX(), message.getY(), message.getZ()));

                turret.setRotationXY(getRotationXYFromYawPitch(message.yaw, message.pitch));
                turret.setRotationXZ(getRotationXZFromYawPitch(message.yaw, message.pitch));
            });
            return null;
        }
    }

    public MessageSetYawPitch(int x, int y, int z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();

        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);

        buf.writeFloat(this.yaw);
        buf.writeFloat(this.pitch);
    }

    private int getX() {
        return x;
    }

    private int getY() {
        return y;
    }

    private int getZ() {
        return z;
    }
}
