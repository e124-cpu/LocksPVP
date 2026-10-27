
package com.example.lockspvp.mixin;

import com.example.lockspvp.HiddenKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(KeyBindsList.class)
public abstract class KeyBindsListMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void lockspvp$filter(
            KeyBindsScreen screen,
            Minecraft mc,
            CallbackInfo ci
    ) {
        // Do not mutate Minecraft's unmodifiable children list here.
        // Filtering is performed in the children() accessor below.
    }

    @Inject(method = "children", at = @At("RETURN"), cancellable = true)
    private void lockspvp$filterChildren(
            CallbackInfoReturnable<List<? extends GuiEventListener>> cir
    ) {
        if (HiddenKeys.showHidden) {
            return;
        }

        List<? extends GuiEventListener> original = cir.getReturnValue();
        List<GuiEventListener> filtered = new ArrayList<>();

        for (GuiEventListener child : original) {
            if (child instanceof KeyBindsList.KeyEntry entry) {
                String name =
                        ((KeyEntryAccessor) entry).getKey().getName();

                if (HiddenKeys.isHidden(name)) {
                    continue;
                }
            }

            filtered.add(child);
        }

        cir.setReturnValue(filtered);
    }
}
