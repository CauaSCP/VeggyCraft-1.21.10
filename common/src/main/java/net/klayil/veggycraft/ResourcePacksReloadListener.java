package net.klayil.veggycraft;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

public class ResourcePacksReloadListener extends SimplePreparableReloadListener<Void> {
    @Override
    protected @NotNull Void prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        return null;
    }

    @Override
    public void apply(Void ignoreData, ResourceManager resourceManager, ProfilerFiller ignoreProfiler) {
        VeggyCraft.imageGen(resourceManager);
        VeggyCraft.enableResourcePack(Minecraft.getInstance(), VeggyCraft.animatedClockResourcePack);
    }
}
