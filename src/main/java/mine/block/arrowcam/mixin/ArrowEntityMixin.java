package mine.block.arrowcam.mixin;

import mine.block.arrowcam.client.ArrowCamClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ProjectileEntityRenderer.class)
public class ArrowEntityMixin<T extends PersistentProjectileEntity> {

    private static Optional<Float> oldPitch = Optional.empty();
    private static Optional<Vec3d> oldPos = Optional.empty();
    private static Optional<Float> oldYaw = Optional.empty();


    @Inject(method = "render(Lnet/minecraft/entity/projectile/PersistentProjectileEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"))
    public void injectRender(T persistentProjectileEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {

        if(persistentProjectileEntity.getOwner() == null) return;
        if(!persistentProjectileEntity.getOwner().isPlayer()) return;

        PlayerEntity playerEntity = (PlayerEntity) persistentProjectileEntity.getOwner();

        assert MinecraftClient.getInstance().player != null;
        if(playerEntity == MinecraftClient.getInstance().player) return;

        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();

        if(playerEntity.isSneaking() && !persistentProjectileEntity.isOnGround()) {
            System.out.println("enabled");
            ArrowCamClient.ENABLED = true;

            if(oldPitch.isEmpty()) {
                oldPitch = Optional.of(camera.getPitch());
            }

            if(oldPos.isEmpty()) {
                oldPos = Optional.of(camera.getPos());
            }

            if(oldYaw.isEmpty()) {
                oldYaw = Optional.of(camera.getYaw());
            }

            camera.pitch = persistentProjectileEntity.getPitch();
            camera.yaw = persistentProjectileEntity.getYaw();
            camera.pos = persistentProjectileEntity.getPos();
        } else {

            ArrowCamClient.ENABLED = false;

            if(oldPitch.isEmpty() || oldPos.isEmpty() || oldYaw.isEmpty()) return;

            camera.pos = oldPos.get();
            camera.pitch = oldPitch.get();
            camera.yaw = oldYaw.get();

            oldPitch = Optional.empty();
            oldPos = Optional.empty();
            oldYaw = Optional.empty();
        }
    }
}
