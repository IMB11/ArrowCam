package mine.block.arrowcam.mixin;

import mine.block.arrowcam.ArrowCam;
import mine.block.arrowcam.client.ArrowCamClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.HashSet;

@Mixin(ProjectileEntityRenderer.class)
public class ArrowEntityRendererMixin {
    @Unique
    private final HashSet<Integer> hasRan = new HashSet<>();

    @Inject(method = "render(Lnet/minecraft/entity/projectile/PersistentProjectileEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"))
    public void injectRender(PersistentProjectileEntity persistentProjectileEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if(hasRan.contains(persistentProjectileEntity.getId())) return;
        hasRan.add(persistentProjectileEntity.getId());

        if(persistentProjectileEntity.getOwner() == MinecraftClient.getInstance().player) {

            ArrowCamClient.target = persistentProjectileEntity;
        }
    }
}
