package net.klayil.veggycraft;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ListToLoopIn<Obj, ReturnType> {
    boolean doBreak = false;
    public int index = 0;
    public Obj item;
    CallbackInfoReturnable<ReturnType> valueToReturnParent;

    Iterable<Obj> toIterate;

    public ListToLoopIn(Iterable<Obj> toIterate) {
        this.toIterate = toIterate;
    }

    public void forLoop(Consumer<Obj> toRun, CallbackInfoReturnable<ReturnType> cir) {
        valueToReturnParent = cir;

        for (Obj _item: toIterate) {
            if (doBreak) break;
            item = _item;
            toRun.accept(item);
            index++;
        }
    }

    public void doBreak() { doBreak = true; }

    public void runReturn(ReturnType result) {
        valueToReturnParent.setReturnValue(result);
    }
}
