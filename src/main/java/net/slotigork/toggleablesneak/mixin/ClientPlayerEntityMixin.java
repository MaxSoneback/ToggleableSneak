package net.slotigork.toggleablesneak.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.slotigork.toggleablesneak.interfaces.ToggleableSneaker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends Entity implements ToggleableSneaker {
    public boolean sneakToggle = false;
    @Shadow
    Input input;

    public ClientPlayerEntityMixin(EntityType<?> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void toggleSneak(){
        sneakToggle = !sneakToggle;

        this.input.sneaking = sneakToggle;
        this.setSneaking(sneakToggle);
    }

    @Inject(method = "isHoldingSneakKey", at = @At("HEAD"), cancellable = true)
    protected void setSneakingToTrue(CallbackInfoReturnable<Boolean> cir){
        if (sneakToggle){
            if(this.onGround && !this.inLava && !this.inWater)
            cir.setReturnValue(true);
        }
    }

}
