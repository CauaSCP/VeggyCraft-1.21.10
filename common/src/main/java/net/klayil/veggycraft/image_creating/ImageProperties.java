package net.klayil.veggycraft.image_creating;

import lombok.Getter;
import lombok.SneakyThrows;

import net.minecraft.server.packs.resources.Resource;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class ImageProperties {
    final boolean initialized;

    @Getter private int width;
    @Getter private int height;
    static ImageProperties self;

    public final static String constructorErrMsg = "Has to use init method";

    protected ImageProperties(@NotNull final Boolean initialized) throws IllegalAccessException {
        this.initialized = initialized;
        self = this;

        if (!self.initialized) throw new IllegalAccessException(constructorErrMsg);
    }

    public void setProportions(@NotNull final Integer width, @NotNull final Integer height) {
        if (width <= 0 | height <= 0) {
            throw new RuntimeException();
        }

        this.width = width; this.height = height;
    }

    public Consumer<Resource> setProportions() {
        return this::processResource;
    }

    @SneakyThrows
    void processResource(Resource resource) {
        BufferedImage hasProportions = ImageIO.read(resource.open());

        final int WIDTH = hasProportions.getWidth();
        final int HEIGHT = hasProportions.getHeight();

        this.setProportions(WIDTH, HEIGHT);
    }

    @SneakyThrows
    public static ImageProperties init() {
        return new ImageProperties(true);
    }
}
