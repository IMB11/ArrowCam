package mine.block.arrowcam.mixin;

import mine.block.arrowcam.client.ArrowCamClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow @Final private Camera camera;

    @Shadow public abstract MinecraftClient getClient();

    private static Optional<Float> oldPitch = Optional.empty();
    private static Optional<Vec3d> oldPos = Optional.empty();
    private static Optional<Float> oldYaw = Optional.empty();
    private void restoreCamera() {
        if(oldPitch.isEmpty() || oldPos.isEmpty() || oldYaw.isEmpty()) return;

        ArrowCamClient.setENABLED(false);

        camera.pos = oldPos.get();
        camera.pitch = oldPitch.get();
        camera.yaw = oldYaw.get();

        oldPitch = Optional.empty();
        oldPos = Optional.empty();
        oldYaw = Optional.empty();
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void injectRenderHead(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if(ArrowCamClient.target == null) {
            restoreCamera();
            return;
        }

        if (!ArrowCamClient.target.getOwner().isSneaking()) {
            restoreCamera();
            return;
        }

        if(ArrowCamClient.target.inGround || !ArrowCamClient.target.world.isChunkLoaded(ArrowCamClient.target.getBlockPos()) || ArrowCamClient.target.isRemoved()) {
            ArrowCamClient.target = null;
            return;
        }

        ArrowCamClient.setENABLED(true);

        if(oldPitch.isEmpty()) {
            oldPitch = Optional.of(camera.getPitch());
        }

        if(oldPos.isEmpty()) {
            oldPos = Optional.of(camera.getPos());
        }

        if(oldYaw.isEmpty()) {
            oldYaw = Optional.of(camera.getYaw());
        }

        camera.yaw = (360.0F - ArrowCamClient.target.getYaw(tickDelta)) % 360.0F;
        camera.pitch = (360.0F - ArrowCamClient.target.getPitch(tickDelta)) % 360.0F;
        camera.pos = new Vec3d(MathHelper.lerp((double)tickDelta, ArrowCamClient.target.prevX, ArrowCamClient.target.getX()), MathHelper.lerp((double)tickDelta, ArrowCamClient.target.prevY, ArrowCamClient.target.getY()) + (double)MathHelper.lerp(tickDelta, camera.lastCameraY, camera.cameraY), MathHelper.lerp((double)tickDelta, ArrowCamClient.target.prevZ, ArrowCamClient.target.getZ()));
    }
}
