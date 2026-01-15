package net.klayil.veggycraft.neoforge.mixin.mixin_platform;

import net.klayil.veggycraft.platform.PlatformHelper;
import net.klayil.veggycraft.platform.PlatformHelperMaster;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlatformHelper.class)
public class PlatformHelperMixin extends PlatformHelperMaster {

//    @Override
//    public boolean isDataGenNonStatic() {
//        var args = FMLLoader.getCurrent().getProgramArgs();
//
//        return args.hasValue("--output") & args.hasValue("--existing");
//    }
}
