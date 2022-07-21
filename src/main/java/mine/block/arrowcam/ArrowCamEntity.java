//package mine.block.arrowcam;
//
//import mine.block.arrowcam.client.ArrowCamClient;
//import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.entity.*;
//import net.minecraft.entity.projectile.ArrowEntity;
//import net.minecraft.nbt.NbtCompound;
//import net.minecraft.network.Packet;
//import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.registry.Registry;
//import net.minecraft.world.World;
//import net.minecraft.world.chunk.Chunk;
//
//public class ArrowCamEntity extends Entity {
//
//    public static final EntityType<?> ENTITY_TYPE = FabricEntityTypeBuilder.create(SpawnGroup.MISC, ArrowCamEntity::new).dimensions(EntityDimensions.fixed(0, 0)).build();
//    private ArrowEntity target;
//    public int deathDelay;
//
//    public ArrowCamEntity(EntityType<?> type, World world) {
//        super(type, world);
//        setInvulnerable(true);
//        deathDelay = 20;
//    }
//
//    public ArrowCamEntity(ArrowEntity arrow){
//        this(ENTITY_TYPE, arrow.world);
//        setTarget(arrow);
//        this.setPos(target.getPos().x, target.getPos().y, target.getPos().z);
//        prevX = target.prevX;
//        prevY = target.prevY;
//        prevZ = target.prevZ;
//        setVelocity(target.getVelocity());
//
//        setYaw((360.0F - target.getYaw()) % 360.0F);
//        setPitch((360.0F - target.getPitch()) % 360.0F);
//        prevYaw = (360.0F - target.prevYaw) % 360.0F;
//        prevPitch = (360.0F - target.prevPitch) % 360.0F;
//
//        this.startRiding(target, true);
//    }
//
//    public void setTarget(ArrowEntity arrow){
//        target = arrow;
//    }
//
//    @Override
//    public void tick() {
//        super.tick();
//
//        Chunk chunk = world.getChunk(new BlockPos(getX(), getY(), getZ()));
//        boolean chunkLoaded = world.isChunkLoaded(chunk.getPos().x, chunk.getPos().z);
//
//        if(!chunkLoaded || MinecraftClient.getInstance().player == null || MinecraftClient.getInstance().player.isDead() || !MinecraftClient.getInstance().player.isSneaking()) {
//            ArrowCamClient.stop();
//        }
//
//        if(target != null){
//
//            if(!target.isAlive() || target.isOnGround()){
//                setVelocity(0, 0, 0);
//                prevYaw = getYaw();
//                prevPitch = getPitch();
//
//                if(--deathDelay <= 0){
//                    ArrowCamClient.stop();
//                }
//            }else{
//                setVelocity(target.getVelocity().x, target.getVelocity().y, target.getVelocity().z);
//
//                setYaw((360.0F - target.getYaw()) % 360.0F);
//                setPitch((360.0F - target.getPitch()) % 360.0F);
//                rotationPitch = (360.0F - target.rotationPitch) % 360.0F;
//                prevRotationYaw = (360.0F - target.prevRotationYaw) % 360.0F;
//                prevRotationPitch = (360.0F - target.prevRotationPitch) % 360.0F;
//            }
//        }
//    }
//
//    public ArrowEntity getTarget() {
//        return target;
//    }
//
//    @Override
//    public boolean collides() {
//        return false;
//    }
//
//    @Override
//    public boolean isPushable() {
//        return false;
//    }
//
//    @Override
//    public boolean doesRenderOnFire() {
//        return false;
//    }
//
//    @Override
//    public float getEyeHeight(EntityPose pose) {
//        return 0.0f;
//    }
//
//    @Override
//    protected void initDataTracker() {}
//
//    @Override
//    protected void readCustomDataFromNbt(NbtCompound nbt) {}
//
//    @Override
//    protected void writeCustomDataToNbt(NbtCompound nbt) {}
//
//    public Packet<?> createSpawnPacket() {
//        return new EntitySpawnS2CPacket(this);
//    }
//}
