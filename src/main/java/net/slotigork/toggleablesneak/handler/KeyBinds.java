package net.slotigork.toggleablesneak.handler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.slotigork.toggleablesneak.ToggleableSneak;
import net.slotigork.toggleablesneak.interfaces.ToggleableSneaker;
import org.lwjgl.glfw.GLFW;

public class KeyBinds {

    private static FabricKeyBinding toggleSneakKeyBinding;

    @Environment(EnvType.CLIENT)
    public static void init() {

        toggleSneakKeyBinding = FabricKeyBinding.Builder.create(
                new Identifier(ToggleableSneak.MOD_ID, "sneaktoggle"),
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                ToggleableSneak.MOD_NAME
        ).build();

        KeyBindingRegistry.INSTANCE.addCategory(ToggleableSneak.MOD_NAME);
        KeyBindingRegistry.INSTANCE.register(toggleSneakKeyBinding);

        ClientTickCallback.EVENT.register(minecraftClient ->
        {
            if (toggleSneakKeyBinding.isPressed())
            {
                handleSneakKey(minecraftClient);
            }
        }
        );
    }

    private static void handleSneakKey(MinecraftClient mc){
        PlayerEntity player = mc.player;

        if (player == null || player.removed) {
            return;
        }

        if (player.onGround) {
            System.out.println("I handleSneakKey");
            ((ToggleableSneaker) player).toggleSneak();
        }


    }
}
