package me.pajic.deeper_delvings.mixin.worldgen.dungeon;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.deeper_delvings.DeeperDelvings;
import me.pajic.deeper_delvings.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.MonsterRoomFeature;
import net.minecraft.world.level.storage.loot.LootTable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MonsterRoomFeature.class)
public class MonsterRoomFeatureMixin {

    @Unique private static final ResourceKey<LootTable> DD$DEEPSLATE_DUNGEON_LOOT = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(DeeperDelvings.MOD_ID, "chests/deepslate_dungeon"));

    @ModifyExpressionValue(
            method = "place",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/block/Blocks;MOSSY_COBBLESTONE:Lnet/minecraft/world/level/block/Block;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    //~ if <26.3 '@Local(argsOnly = true, name = "origin")' -> '@Local(name = "origin")'
    private Block useMossyCobbledDeepslate(Block original, @Local(argsOnly = true, name = "origin") BlockPos origin) {
        return useDeepslateVariant(origin) ? ModBlocks.MOSSY_COBBLED_DEEPSLATE : original;
    }

    @ModifyExpressionValue(
            method = "place",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/block/Blocks;COBBLESTONE:Lnet/minecraft/world/level/block/Block;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    //~ if <26.3 '@Local(argsOnly = true, name = "origin")' -> '@Local(name = "origin")'
    private Block useCobbledDeepslate(Block original, @Local(argsOnly = true, name = "origin") BlockPos origin) {
        return useDeepslateVariant(origin) ? Blocks.COBBLED_DEEPSLATE : original;
    }

    @ModifyExpressionValue(
            method = "place",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/storage/loot/BuiltInLootTables;SIMPLE_DUNGEON:Lnet/minecraft/resources/ResourceKey;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    //~ if <26.3 '@Local(argsOnly = true, name = "origin")' -> '@Local(name = "origin")'
    private ResourceKey<LootTable> useDeepslateDungeonLoot(ResourceKey<LootTable> original, @Local(argsOnly = true, name = "origin") BlockPos origin) {
        return useDeepslateVariant(origin) ? DD$DEEPSLATE_DUNGEON_LOOT : original;
    }

    @Unique
    private static boolean useDeepslateVariant(BlockPos origin) {
        return DeeperDelvings.CONFIG.deepslateDungeonVariant.get() && origin.getY() < 0;
    }
}
