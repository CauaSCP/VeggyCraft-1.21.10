package net.klayil.veggycraft.mixin;

import net.klayil.PublicStyledComponent;
import net.klayil.SequencedList;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.item.ModItems;
import net.klayil.veggycraft.item.MolassesToBrownSugar;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.klayil.veggycraft.VeggyCraft.comment;

@Mixin(ItemStack.class)
abstract
class ItemMixin {
//    @SneakyThrows
//    public ItemMixin() {
//        ItemMixinImpl.set$hoverCases(veggycraft$hoverCases);
//
////        var a = this::onHoverText;
//
//        ConsumersMore.Consumer6<
//                    ItemStack,
//                    Item.TooltipContext,
//                    TooltipDisplay,
//                    Consumer<Component>,
//                    TooltipFlag,
//                    CallbackInfo
//                > consumer = this::onHoverText;
//

    @Shadow @Final @NotNull private Item item;

    ////        consumer.acceptApplySupplierParser(consumer, *)
//
//        ItemMixinImpl.methods.put("appendHoverText", consumer.getMaster());
//    }

    @Unique
    ChatFormatting veggycraft$descriptionColor = ChatFormatting.DARK_GRAY;

    @Unique
    ResourceLocation veggycraft$id;

    @Unique
    private static final Supplier<Component> veggycraft$clOk = () -> {
        MutableComponent parsedText = Component.literal("");

        if (MolassesToBrownSugar.parsedTexts == null) return parsedText;

        parsedText = parsedText.append(MolassesToBrownSugar.parsedTexts.getFirst().copy());

        for (int i = 1; i < MolassesToBrownSugar.parsedTexts.size(); i++) {
            parsedText.append(Component.literal(" ")).append(MolassesToBrownSugar.parsedTexts.get(i).copy());
        }

        return parsedText;
    };

//    @Unique
//    private static Map<ResourceLocation, Component> get$hoverCases() {
//        return veggycraft$hoverCases;
//    }
//


//    private static Map<ResourceLocation, Component> _get$hoverCases() {
//        return veggycraft$hoverCases;
//    }
//    @Invoker("_get$hoverCase")
//    Map<ResourceLocation, Component> get$hoverCases();

    @Unique
    private static final Map<ResourceLocation, Component> veggycraft$hoverCases = new HashMap<>(Map.of(
            ResourceLocation.withDefaultNamespace("wheat"), Component.translatable("klay_api.smash.wheat")
                    .withStyle(style -> style.withColor(TextColor.fromRgb(0x89F336)))

            ,

            ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "my_clock"),
            veggycraft$clOk.get().copy()

            ,

            ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "birch_pulp_modal"), Component.translatable(
                            "klay_api.description.pulp")
                    .withStyle(ChatFormatting.WHITE)
                    .withStyle(ChatFormatting.ITALIC)
                    .withStyle(ChatFormatting.BOLD)
    ));

    @Inject(
            method = "getTooltipLines",
            at = @At("TAIL"),
            cancellable = true
    )
    private void fakeClockId(Item.TooltipContext tooltipContext, Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {
        if (item == new ItemStack(ModItems.THIS_MOD_CLOCK).getItem()) {
            final List<Component> returnValue = new ArrayList<>();

            veggycraft$id = BuiltInRegistries.ITEM.getKey(item);

            cir.getReturnValue().forEach(tooltipComponent -> {
                if (tooltipFlag.isAdvanced() & tooltipComponent.getString().equals(veggycraft$id.toString())) {
                    returnValue.add(Component.literal(BuiltInRegistries.ITEM.getKey(Items.CLOCK).toString()).withStyle(tooltipComponent.getStyle()));

                    return;
                }

//                if (
//                        tooltipComponent.getStyle().isItalic()
//                                &
//                        tooltipComponent.getStyle().getColor() == TextColor.fromLegacyFormat(ChatFormatting.BLUE)
//                ) {
//                    String whereFrom = tooltipComponent.getString();
//                    if (tooltipComponent.getContents() instanceof TranslatableContents translatable) {
//                        whereFrom = translatable.getKey();
//                    }
//                    VeggyCraft.LOGGER.warn("#whereFrom: %s", whereFrom);
//
//                    returnValue.add(tooltipComponent.copy());
//                }

                returnValue.add(tooltipComponent.copy());
            });

            cir.setReturnValue(returnValue);
        }
    }

    @Inject(
            method = "addDetailsToTooltip",
            at = @At("HEAD")
//            ,
//            cancellable =
    )
    private void onHoverText(Item.TooltipContext context, TooltipDisplay tooltipDisplay, Player player, TooltipFlag tooltipFlag, Consumer<Component> tooltipAdder, CallbackInfo ci) {
        veggycraft$id = BuiltInRegistries.ITEM.getKey(item);
        ItemStack self = (ItemStack) (Object) this;

//        veggycraft$hoverCases.put(ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "my_clock"), .get());

        if (!veggycraft$hoverCases.containsKey(veggycraft$id)) {
            return;
        }

        MutableComponent tooltipComponent = Component.literal("");
        tooltipComponent.append(veggycraft$hoverCases.get(veggycraft$id));

        PublicStyledComponent tooltipComponentCheck;
        PublicStyledComponent nameComponent;
        try {
            tooltipComponentCheck = new PublicStyledComponent(veggycraft$hoverCases.get(veggycraft$id));
            nameComponent = new PublicStyledComponent(self.getItem().getName(self));
        } catch (SequencedList.IncompatibleTypeError e) {
            throw new RuntimeException(e);
        }

        if (
                   nameComponent.style.bold == tooltipComponentCheck.style.bold
                && Objects.equals(nameComponent.style.color, tooltipComponentCheck.style.color)
                && Objects.equals(nameComponent.style.shadowColor, tooltipComponentCheck.style.shadowColor)
                && nameComponent.style.italic == tooltipComponentCheck.style.italic
                && nameComponent.style.underlined == tooltipComponentCheck.style.underlined
                && nameComponent.style.strikethrough == tooltipComponentCheck.style.strikethrough
                && nameComponent.style.obfuscated == tooltipComponentCheck.style.obfuscated
                && Objects.equals(nameComponent.style.insertion, tooltipComponentCheck.style.insertion)
                && Objects.equals("%s".formatted(nameComponent.style.font), "%s".formatted(tooltipComponentCheck.style.font))
        ) {
            tooltipComponent.withStyle(veggycraft$descriptionColor);
        }


        tooltipAdder.accept(tooltipComponent);
    }

    @Inject(
            method = "getItemName",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onGetName(CallbackInfoReturnable<Component> cir) {
        ResourceLocation _id = BuiltInRegistries.ITEM.getKey(item);
        if (!_id.getNamespace().equals(VeggyCraft.MOD_ID)) return;

        veggycraft$id = _id;


        MutableComponent res = Component.empty();

        if (item instanceof BlockItem) {
            if (veggycraft$id.getPath().toLowerCase().contains("carnauba")) {
                String bfr = "veggycraft.woods.";
                String carnauba_translation = "carnauba";
                String affix = veggycraft$id.getPath().replace("_carnauba", "");

                res.append(Component.translatable(bfr + "prefix." + affix));
                res.append(Component.translatable(bfr + carnauba_translation));
                res.append(Component.translatable(bfr + "suffix." + affix));

                cir.setReturnValue(res);
            }

            if (ItemStack.isSameItem(new ItemStack(item), new ItemStack(ModItems.EVEN_STRIPPED_BIRCH_LOG))) {
                res.append(Component.translatable("even_stripped.prefix"));
                res.append(Component.translatable("block.minecraft.birch_log"));
                res.append(Component.translatable("even_stripped.suffix"));

                cir.setReturnValue(res);
            }
        }

        if (veggycraft$id == BuiltInRegistries.ITEM.getKey(ModItems.THIS_MOD_CLOCK.get())) {
            cir.setReturnValue(Component.translatable("item.minecraft.clock"));

            return;
        }

        if (Objects.equals(veggycraft$id.getPath(), ModItems.waxID)) {
            res = res.append(Component.translatable("wax.prefix"));
            res = res.append(Component.translatable("veggycraft.woods.carnauba"));
            res = res.append(Component.translatable("wax.suffix"));

            cir.setReturnValue(res);
        }

        comment("""
/*
    @Inject(method = "interactLivingEntity", at = @At("HEAD"), cancellable = true)
    public void whenInteractedLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand, CallbackInfoReturnable<InteractionResult> cir) {
        if (!(interactionTarget instanceof CopperGolem golem)) return;

        if (
                !ItemStack.isSameItem(
                        new ItemStack((Item)(Object)this),
                        new ItemStack(ModItems.CARNAUBA_WAX)
                )
        ) return;

        if ( ((CopperGollumMixin) golem).waxedLongVale() != -1L  ) return;


        //error: java.lang.ClassCastException: class net.minecraft.world.entity.animal.coppergolem.CopperGolem cannot be cast to class net.klayil.veggycraft.entity.CopperGolemExtended (net.minecraft.world.entity.animal.coppergolem.CopperGolem and net.klayil.veggycraft.entity.CopperGolemExtended are in unnamed module of loader 'knot' @4a0e3bd) (1746ms ago)


        Level level = player.level();

        level.playSound(
            player, golem.getX(), golem.getY(), golem.getZ(),
            SoundEvents.HONEYCOMB_WAX_ON, SoundSource.NEUTRAL,
            1F, 1F
        );

        if (level instanceof ServerLevel serverLevel) serverLevel.levelEvent(null, 3003, golem.blockPosition(), 0);

        InteractionResult res = InteractionResult.SUCCESS;

        if (level.isClientSide()) {
            cir.setReturnValue(res);
            cir.cancel();
        }

        ((CopperGollumMixin) golem).setWaxed(-2L);

        EquipmentSlot slot = (usedHand == InteractionHand.MAIN_HAND)
                ? EquipmentSlot.MAINHAND
                : EquipmentSlot.OFFHAND;

        stack.hurtAndBreak(1, player, slot);

        cir.setReturnValue(res);
        cir.cancel();
    }
    */
""");
    }

    @Unique
    BlockState mixin$waxedState;
    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    public void useOnMixin(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (
            !ItemStack.isSameItem(
                (ItemStack) (Object) this,
                new ItemStack(ModItems.CARNAUBA_WAX)
            )
        ) return;

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Player player = context.getPlayer();


        Util.make(HoneycombItem.WAXABLES.get().get(state.getBlock()), block ->
                mixin$waxedState = block.withPropertiesOf(state)
        );


        InteractionResult res = InteractionResult.SUCCESS;

        level.playSound(
                player, pos, SoundEvents.HONEYCOMB_WAX_ON,
                SoundSource.BLOCKS, 1F, 1F
        );

        level.levelEvent(player, 30003, pos, 0);

        if (level.isClientSide()) {
            cir.setReturnValue(res);
            cir.cancel();
        }

        if (player != null) {
            EquipmentSlot slot = (context.getHand() == InteractionHand.MAIN_HAND)
                    ? EquipmentSlot.MAINHAND
                    : EquipmentSlot.OFFHAND;

            context.getItemInHand().hurtAndBreak(
                    1,
                    player,
                    slot
            );
        }

        level.setBlockAndUpdate(pos, Util.make(() -> mixin$waxedState));

        cir.setReturnValue(res);
        cir.cancel();


        comment("""
//    @Override
//    public @NotNull Component getName(ItemStack stack) {
//        if ( (Item)(Object)this instanceof BlockItem self) {
//            ResourceLocation id = BuiltInRegistries.ITEM.getKey(self);
//
//            if (Objects.equals(id.getNamespace(), VeggyCraft.MOD_ID) & id.getPath().toLowerCase().contains("carnauba")) {
//                String[] splitText = id.getPath().split("_");
//
//                Component res = Component.literal("");
//
//                for (int i = 0; i < splitText.length ; i++) {
//                    Component toAppend = Component.translatable("veggycraft.woods.%s".formatted(splitText[i]));
//                    if (i != splitText.length-1) toAppend = toAppend
//                }
//            }
//        }
//
//        return (Component)stack.getComponents().getOrDefault(DataComponents.ITEM_NAME, CommonComponents.EMPTY);
//    }""");
    }

//    @Override
//    public Map<ResourceLocation, Component> get$HoverCases() {
//        return veggycraft$hoverCases;
//    }
}
