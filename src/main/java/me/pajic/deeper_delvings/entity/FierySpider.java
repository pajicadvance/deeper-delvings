package me.pajic.deeper_delvings.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FierySpider extends Spider {

    public FierySpider(EntityType<? extends FierySpider> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean doHurtTarget(@NotNull ServerLevel level, @NotNull Entity target) {
        if (!super.doHurtTarget(level, target)) return false;
        if (target instanceof LivingEntity) {
            int seconds = switch (level.getDifficulty()) {
                case NORMAL -> 7;
                case HARD -> 15;
                default -> 0;
            };
            if (seconds > 0) target.igniteForSeconds(seconds);
        }
        return true;
    }

    @Override
    public void die(@NotNull DamageSource source) {
        boolean alreadyDead = isRemoved() || dead;
        super.die(source);
        if (alreadyDead || !(level() instanceof ServerLevel level) || !level.getGameRules().get(GameRules.MOB_GRIEFING)) return;
        BlockPos pos = blockPosition();
        if (BaseFireBlock.canBePlacedAt(level, pos, Direction.UP)) {
            level.setBlockAndUpdate(pos, BaseFireBlock.getState(level, pos));
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(
            @NotNull ServerLevelAccessor level,
            @NotNull DifficultyInstance difficulty,
            @NotNull EntitySpawnReason spawnReason,
            @Nullable SpawnGroupData groupData
    ) {
        return groupData;
    }

    @Override
    public @NotNull Vec3 getVehicleAttachmentPoint(@NotNull Entity vehicle) {
        return vehicle.getBbWidth() <= getBbWidth() ?
                new Vec3(0, 0.21875 * getScale(), 0) :
                super.getVehicleAttachmentPoint(vehicle);
    }
}
