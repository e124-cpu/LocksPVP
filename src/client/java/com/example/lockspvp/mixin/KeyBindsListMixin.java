package com.example.lockspvp.mixin;

import com.example.lockspvp.HiddenKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(KeyBindsList.class)
public abstract class KeyBindsListMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void lockspvp$filter(KeyBindsScreen screen, Minecraft mc, CallbackInfo ci) {
        if (HiddenKeys.showHidden) return;

        KeyBindsList self = (KeyBindsList) (Object) this;
        List<KeyBindsList.Entry> children = self.children();

        children.removeIf(entry ->
            entry instanceof KeyBindsList.KeyEntry ke
            && HiddenKeys.isHidden(((KeyEntryAccessor) ke).getKey().getName())
        );

        for (int i = children.size() - 1; i >= 0; i--) {
            if (children.get(i) instanceof KeyBindsList.CategoryEntry) {
                boolean last = i == children.size() - 1;
                boolean nextIsCategory = !last && children.get(i + 1) instanceof KeyBindsList.CategoryEntry;
                if (last || nextIsCategory) {
                    children.remove(i);
                }
            }
        }
    }
}
