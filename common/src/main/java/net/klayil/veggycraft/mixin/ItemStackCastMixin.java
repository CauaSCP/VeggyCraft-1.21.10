package net.klayil.veggycraft.mixin;

import jdk.internal.vm.annotation.IntrinsicCandidate;
import net.klayil.veggycraft.VeggyCraft;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.constant.Constable;
import java.lang.invoke.TypeDescriptor;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.util.function.Supplier;

@Mixin(Class.class)
abstract public class ItemStackCastMixin<T extends Iterable<ItemStack>> implements java.io.Serializable,
        GenericDeclaration,
        Type,
        AnnotatedElement,
        TypeDescriptor.OfField<Class<?>>,
        Constable {

    @Shadow private transient Object[] enumConstants;

    @Inject(
            method = "cast",
            at = @At("HEAD"),
            cancellable = true
    )
    private void changedCast(Object param, CallbackInfoReturnable<ItemStack> cir) {
        if (!(this.enumConstants instanceof ItemStack[])) return;


    }

    private void checkIfIsContainer(Object param) {


        Supplier<?> supplier = () -> new Supplier<>() {
            @Override
            public T get() {

                return
            }
        };

        Container cont;

        if (
                supplier instanceof Supplier<? extends Container>
                        &&
                        supplier.get().getClass().getSuperclass() != Container.class
        ) cont = param;

        else if (param instanceof Container _cont) cont = _cont;
    }

    ItemStack cast(T param) {
        throw new NullPointerException("parsed parameter doesn't extend Container");

        return cont.getItem(0);
    }
}
