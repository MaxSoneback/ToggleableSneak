package net.slotigork.toggleablesneak;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.slotigork.toggleablesneak.interfaces.ToggleableSneaker;
import org.lwjgl.glfw.GLFW;

public class ToggleableSneak implements ClientModInitializer {
    public static final String MOD_ID = "toggleablesneak";
    public static final String MOD_NAME = "Toggleable Sneak";
    private static FabricKeyBinding toggleSneakKeyBinding;
    private static int timeOfPress=0;
    private static int lastTimeOfPress=0;
    @Override
    public void onInitializeClient(){


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
                            lastTimeOfPress = timeOfPress;
                            timeOfPress = (int) minecraftClient.world.getTime();

                            if(Math.abs(lastTimeOfPress-timeOfPress)>5) {
                                handleSneakKey(minecraftClient);
                            }
                        }
                    });

        ClientTickCallback.EVENT.register(minecraftClient ->
        {
            if (minecraftClient.options.keySneak.isPressed()) {
                lastTimeOfPress = timeOfPress;
                timeOfPress = (int) minecraftClient.world.getTime();
                int timeDiff = Math.abs(lastTimeOfPress - timeOfPress);

                if (timeDiff > 1 && timeDiff < 6) {
                    handleSneakKey(minecraftClient);
                }
            }
        });
        }

        private static void handleSneakKey(MinecraftClient mc){
            PlayerEntity player = mc.player;

            if (player == null || player.removed) {
                return;
            }
                ((ToggleableSneaker) player).toggleSneak();
        }
}