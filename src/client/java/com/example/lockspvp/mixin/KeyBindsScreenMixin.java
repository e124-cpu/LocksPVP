package com.example.lockspvp.mixin;

import com.example.lockspvp.HiddenKeys;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBindsScreen.class)
public abstract class KeyBindsScreenMixin extends Screen {

    protected KeyBindsScreenMixin(Component title) {
        super(title);
    }

    // Always start locked (hidden) when the screen is opened
    @Inject(method = "<init>", at = @At("TAIL"))
    private void lockspvp$reset(Screen parent, Options options, CallbackInfo ci) {
        HiddenKeys.showHidden = false;
    }

    // Add the lock button in the top right corner
    @Inject(method = "addContents", at = @At("TAIL"))
    private void lockspvp$addLock(CallbackInfo ci) {
        boolean shown = HiddenKeys.showHidden;

        this.addRenderableWidget(
            Button.builder(Component.literal(shown ? "U" : "L"), b -> {
                HiddenKeys.showHidden = !HiddenKeys.showHidden;
                this.rebuildWidgets();
            })
            .bounds(this.width - 28, 6, 20, 20)
            .tooltip(Tooltip.create(Component.literal(
                shown ? "Hide hidden keybinds" : "Show hidden keybinds")))
            .build()
        );
    }
}
