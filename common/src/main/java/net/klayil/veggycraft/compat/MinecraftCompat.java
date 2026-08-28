package net.klayil.veggycraft.compat;

import com.mojang.math.OctahedralGroup;
import com.mojang.math.Quadrant;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public final class MinecraftCompat {
    private MinecraftCompat() {}

    public static OctahedralGroup fromXYAngles(
            Quadrant x,
            Quadrant y
    ) {
        // 21.10 ver:
        OctahedralGroup result =
                invokeFromXYAngles(OctahedralGroup.class, x, y);

        if (result != null) {
            return result;
        }

        // 21.11 ver:
        result = invokeFromXYAngles(Quadrant.class, x, y);

        if (result != null) {
            return result;
        }

        throw new IllegalStateException(
                "Code was not able to find implementation for Minecraft fromXYAngles"
        );
    }

    private static OctahedralGroup invokeFromXYAngles(
            Class<?> owner,
            Quadrant x,
            Quadrant y
    ) {
        for (Method method : owner.getDeclaredMethods()) {
            if (!Modifier.isStatic(method.getModifiers())) {
                continue;
            }

            if (!Arrays.equals(
                    method.getParameterTypes(),
                    new Class<?>[]{
                            Quadrant.class,
                            Quadrant.class
                    }
            )) {
                continue;
            }

            try {
                return (OctahedralGroup) method.invoke(null, x, y);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(
                        "Failed to run invoke with the Minecraft rotation method as octahedral group",
                        e
                );
            }
        }

        return null;
    }
}
