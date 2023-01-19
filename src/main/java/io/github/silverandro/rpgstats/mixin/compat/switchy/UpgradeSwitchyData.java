package io.github.silverandro.rpgstats.mixin.compat.switchy;

import io.github.silverandro.rpgstats.mixin_logic.UpgradeSwitchyDataKt;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(Entity.class)
public class UpgradeSwitchyData {
    @Inject(method = "readNbt", at = @At("HEAD"))
    public void rpgstats$seekAndRestoreOldSwitchyData(NbtCompound nbt, CallbackInfo ci) {
        //noinspection ConstantValue
        if (((Object)this) instanceof ServerPlayerEntity spe) {
            if (!nbt.contains("switchy:presets", NbtElement.COMPOUND_TYPE)) return;

            NbtCompound switchyList = nbt.getCompound("switchy:presets").getCompound("list");
            UpgradeSwitchyDataKt.upgradeSwitchyData(switchyList);
        }
    }
}
