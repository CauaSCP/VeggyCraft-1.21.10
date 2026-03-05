package net.klayil;

import lombok.SneakyThrows;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.TooltipProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.constant.Constable;
import java.lang.constant.ConstantDesc;
import java.util.*;
import java.util.stream.IntStream;

public class PublicStyledComponent implements Component {

    public abstract static class Translatable implements Map<java.lang.String, Object> {
        private abstract static class StaticTranslatable {
            static public java.lang.String get(String key) throws net.klayil.SequencedList.IncompatibleTypeError {
                String res = null;

                try {
                    res = Translatable.get(key);
                    assert res != null;
                } catch (Exception ex) {
                    if (ex instanceof RuntimeException) {
                        ex.printStackTrace(System.err);
                        System.exit(1);
                    } else {
                        throw new net.klayil.SequencedList.IncompatibleTypeError(ex.getMessage(), ex.getCause());
                    }
                }

                return res;
            }
        }


        static public java.lang.String get(String key) throws net.klayil.SequencedList.IncompatibleTypeError {
            String res = null;

            try {
                res = StaticTranslatable.get(key);
                assert res != null;
            } catch (net.klayil.SequencedList.IncompatibleTypeError e) {
                throw new net.klayil.SequencedList.IncompatibleTypeError(e);
            } catch (Exception ex) {
                if (ex instanceof RuntimeException) {
                    ex.printStackTrace(System.err);
                    System.exit(1);
                } else {
                    throw new net.klayil.SequencedList.IncompatibleTypeError(ex.getMessage(), ex.getCause());
                }
            }

            return res;
        }

        @SneakyThrows
        @Override
        public java.lang.String get(Object key) {
            java.lang.String res = null;

            try {
                res = this.getOrDefault(key);
                assert res != null;
            } catch (net.klayil.SequencedList.IncompatibleTypeError e) {
                throw new net.klayil.SequencedList.IncompatibleTypeError(e);
            } catch (Exception ex) {
                if (ex instanceof RuntimeException) {
                    ex.printStackTrace(System.err);
                    System.exit(1);
                } else {
                    throw new net.klayil.SequencedList.IncompatibleTypeError(ex.getMessage(), ex.getCause());
                }
            }

            return res;
        }


        public java.lang.String getOrDefault(Object __k, Object... otherValue) throws net.klayil.SequencedList.IncompatibleTypeError {
            if (!(__k instanceof String key))
                throw new net.klayil.SequencedList.IncompatibleTypeError("key is not a string");

            return net.minecraft.client.resources.language.I18n.get(key, (otherValue.length == 0) ? null : otherValue[0]);
        }
    }

    public static class Style {
        @Nullable
        public Integer color;
        @Nullable
        public final Integer shadowColor;
        @Nullable
        public final java.lang.Boolean bold;
        @Nullable
        public final java.lang.Boolean italic;
        @Nullable
        public final java.lang.Boolean underlined;
        @Nullable
        public final java.lang.Boolean strikethrough;
        @Nullable
        public final java.lang.Boolean obfuscated;
        @Nullable
        public final ClickEvent clickEvent;
        @Nullable
        public final HoverEvent hoverEvent;
        @Nullable
        public final java.lang.String insertion;
        @Nullable
        public SequencedList<?> font;

        static public class FontContent implements java.io.Serializable, Comparable, CharSequence, Constable {
            @NotNull final Constable self;

            @Override
            public @NotNull String toString() {
                return (String) self;
            }

            @Override
            public int length() {
                return toString().length();
            }

            @Override
            public char charAt(int index) {
                return toString().charAt(index);
            }

            @SneakyThrows
            @Override
            public boolean isEmpty() {
                if (self instanceof Boolean) {
                    throw new net.klayil.SequencedList.IncompatibleTypeError("is never empty due to be a primitive boolean");
                }
                return toString().isEmpty();
            }

            @Override
            public @NotNull CharSequence subSequence(int start, int end) {
                return toString().subSequence(start, end);
            }

            @Override
            public @NotNull IntStream chars() {
                return toString().chars();
            }

            @Override
            public @NotNull IntStream codePoints() {
                return toString().codePoints();
            }

            public FontContent(@NotNull String v) {
                self = v;
            }

            public FontContent(boolean v) {
                self = v;
            }

            @Override
            public Optional<? extends ConstantDesc> describeConstable() {
                return self.describeConstable();
            }

            @SneakyThrows
            @Override
            public int compareTo(@NotNull Object other) {
                if (!(other instanceof String | other instanceof Boolean)) {
                    throw new net.klayil.SequencedList.IncompatibleTypeError("the compareTo param is not string or boolean");
                }

                return ((Comparable) self).compareTo(other);
            }
        }

        public static class SequencedList<FontContent extends Style.FontContent> extends net.klayil.SequencedList<FontContent> {
            @Override
            public java.lang.String toString() {
                return super.toString();
            }

            public SequencedList(FontDescription parsedParam) throws IncompatibleTypeError {
                super((SequencedCollection<FontContent>) initBefore(parsedParam));
            }

            @SneakyThrows
            static net.klayil.SequencedList<Style.FontContent> initBefore(FontDescription toParse) {
                net.klayil.SequencedList<Style.FontContent> parsedParam = new net.klayil.SequencedList<>(new ArrayList<>());

                if (toParse instanceof FontDescription.Resource(net.minecraft.resources.ResourceLocation id)) {
                    parsedParam.add(new Style.FontContent(id.toString()));
                } else if (toParse instanceof FontDescription.AtlasSprite(
                        net.minecraft.resources.ResourceLocation atlasId,
                        net.minecraft.resources.ResourceLocation spriteId
                )) {
                    parsedParam.add(new Style.FontContent(atlasId.toString()));
                    parsedParam.add(new Style.FontContent(spriteId.toString()));
                } else if (toParse instanceof FontDescription.PlayerSprite(
                        net.minecraft.world.item.component.ResolvableProfile profile, boolean hat
                )) {
                    parsedParam.add(new Style.FontContent(hat));

                    ItemLore resolvableProfile = (ItemLore) (TooltipProvider) profile;

                    StringBuilder componentStr = new StringBuilder();


                    net.klayil.SequencedList<Component> lis = new net.klayil.SequencedList<>(resolvableProfile.styledLines());
                    for (Component li : lis) {
                        componentStr.append(Translatable.get(li.getString()));
                    }

                    parsedParam.add(new Style.FontContent(componentStr.toString()));
                }

//                for (Style.FontContent elem : parsedParam) {
//                    VeggyCraft.LOGGER.warn("#elem: %s", ((Constable) elem) instanceof String);
//
////                    if(!((Constable) elem instanceof Boolean || (Constable) elem instanceof String)) throw new IncompatibleTypeError("wrong element type");
//                }

                return parsedParam;
            }
        }

        @Override
        public boolean equals(Object obj) {

            return super.equals(obj);
        }

        private final net.minecraft.network.chat.Style value;

        public Style(net.minecraft.network.chat.Style style) throws net.klayil.SequencedList.IncompatibleTypeError {
            value = style;

            clickEvent = value.getClickEvent();


            if (value.getColor() == null) {
                color = ChatFormatting.WHITE.getColor();
            } else {
                color = value.getColor().getValue();
            }
            shadowColor = value.getShadowColor();
            bold = value.isBold();
            italic = value.isItalic();
            underlined = value.isUnderlined();
            strikethrough = value.isStrikethrough();
            obfuscated = value.isObfuscated();
            hoverEvent = value.getHoverEvent();
            insertion = value.getInsertion();
            font = new SequencedList<>(value.getFont());
        }
    }

    private final MutableComponent value;
    public final Style style;

    public PublicStyledComponent(Component toParse) throws net.klayil.SequencedList.IncompatibleTypeError {
        value = toParse.copy();

        style = new Style(value.getStyle());
    }

    @Override
    public @NotNull MutableComponent plainCopy() {
        return value.copy();
    }

    @Override
    public @NotNull MutableComponent copy() {
        return value.copy();
    }

    /* * public Style getStyle(Object... ignore) {
        return style;
    }
     */

    @Override
    public net.minecraft.network.chat.@NotNull Style getStyle() {
        return style.value;
    }

    @Override
    public @NotNull ComponentContents getContents() {
        return value.getContents();
    }

    @Override
    public @NotNull List<Component> getSiblings() {
        return value.getSiblings();
    }

    @Override
    public @NotNull FormattedCharSequence getVisualOrderText() {
        return value.getVisualOrderText();
    }
}
