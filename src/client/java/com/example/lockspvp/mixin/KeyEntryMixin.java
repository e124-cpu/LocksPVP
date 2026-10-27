package com.example.lockspvp.mixin;

import com.example.lockspvp.HiddenKeys;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(KeyBindsList.KeyEntry.class)
public abstract class KeyEntryMixin {

    @Shadow @Final private KeyMapping key;
    @Shadow @Final private Button resetButton;

    @Unique
    private Button lockspvp$hideButton;

    @Unique
    private Button lockspvp$button() {
        if (this.lockspvp$hideButton == null) {
            final String name = this.key.getName();
            this.lockspvp$hideButton = Button.builder(HiddenKeys.label(name), b -> {
                HiddenKeys.toggle(name);
                b.setMessage(HiddenKeys.label(name));
            }).bounds(0, 0, 48, 20).build();
        }
        return this.lockspvp$hideButton;
    }

    @Inject(method = "renderContent", at = @At("TAIL"))
    private void lockspvp$renderHide(GuiGraphics graphics, int mouseX, int mouseY,
                                     boolean hovered, float partialTick, CallbackInfo ci) {
        if (!HiddenKeys.showHidden) return;
        Button b = lockspvp$button();
        b.setX(this.resetButton.getX() + this.resetButton.getWidth() + 4);
        b.setY(this.resetButton.getY());
        b.render(graphics, mouseX, mouseY, partialTick);
    }

    @Inject(method = "children", at = @At("RETURN"), cancellable = true)
    private void lockspvp$children(CallbackInfoReturnable<List<? extends GuiEventListener>> cir) {
        if (!HiddenKeys.showHidden) return;
        List<GuiEventListener> list = new ArrayList<>(cir.getReturnValue());
        list.add(lockspvp$button());
        cir.setReturnValue(list);
    }
}
