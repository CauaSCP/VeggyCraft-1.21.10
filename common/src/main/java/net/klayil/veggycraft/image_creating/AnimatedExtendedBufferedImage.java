package net.klayil.veggycraft.image_creating;

import net.klayil.veggycraft.VeggyCraft;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.util.function.BiFunction;

public class AnimatedExtendedBufferedImage extends ImageProperties {
    @Nullable BufferedImage bufferedImage = null;
    @NotNull public BufferedImage buffered() {
        assert bufferedImage != null;

        return bufferedImage;
    }

    public AnimatedExtendedBufferedImage() throws IllegalAccessException {
        super(true);
    }

    public <T extends ImageProperties> void parseProportions(T getFromThisOne) {
        if (getFromThisOne instanceof AnimatedExtendedBufferedImage) {
            throw new StackOverflowError("method parameter may not be same type...");
        }

        this.setProportions(getFromThisOne.getWidth(), getFromThisOne.getHeight());
    }

    public final void extendProportions(@Nullable final BiFunction<Integer, Integer, Integer> extApplicable, @Nullable final Integer xExtension, @Nullable final Integer yExtension) {
        if (xExtension != null | yExtension != null) {
            final ImageProperties proportions = this;
            VeggyCraft.LOGGER.warn(">>proportions: %s, %s", proportions.getWidth(), proportions.getHeight());

            assert extApplicable != null : "extApplicable may not be null";

            final Integer WIDTH = (xExtension == null) ? proportions.getWidth() : extApplicable.apply(proportions.getWidth(), xExtension);
            final Integer HEIGHT = (yExtension == null) ? proportions.getHeight() : extApplicable.apply(proportions.getHeight(), yExtension);

            this.setProportions(WIDTH, HEIGHT);
        }

        bufferedImage = new BufferedImage(
                this.getWidth(),
                this.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
    }
}
