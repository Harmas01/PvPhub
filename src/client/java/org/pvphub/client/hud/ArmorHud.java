package org.pvphub.client.hud;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.pvphub.PvPhub;

public final class ArmorHud {
    private static final Identifier HUD_LAYER = PvPhub.id("armor_hud");
    private static final Identifier HOTBAR_TEXTURE = Identifier.withDefaultNamespace("hud/hotbar");

    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    private static final int SLOT_STEP = 20;
    private static final int FRAME_PADDING = 2;
    private static final int FRAME_HEIGHT = 22;
    private static final int HOTBAR_HALF_WIDTH = 91;
    private static final int SIDE_GAP = 27;

    private ArmorHud() {
    }

    public static void register() {
        HudElementRegistry.attachElementAfter(
                VanillaHudElements.HOTBAR,
                HUD_LAYER,
                ArmorHud::render
        );
    }

    private static void render(GuiGraphicsExtractor graphics, DeltaTracker tickCounter) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }

        int rowWidth = ARMOR_SLOTS.length * SLOT_STEP + FRAME_PADDING;
        int x = graphics.guiWidth() / 2 - HOTBAR_HALF_WIDTH - SIDE_GAP - rowWidth;
        int y = graphics.guiHeight() - FRAME_HEIGHT;

        // Keep the armor row visible on narrow GUI scales.
        x = Math.max(x, 2);

        // Draw four hotbar cells at their native size. Scaling the complete
        // nine-cell sprite squeezes all of its separators into this area.
        graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                HOTBAR_TEXTURE,
                182,
                FRAME_HEIGHT,
                0,
                0,
                x,
                y,
                rowWidth - 1,
                FRAME_HEIGHT
        );
        // Add the original right edge, which is outside the first four cells.
        graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                HOTBAR_TEXTURE,
                182,
                FRAME_HEIGHT,
                181,
                0,
                x + rowWidth - 1,
                y,
                1,
                FRAME_HEIGHT
        );

        for (int index = 0; index < ARMOR_SLOTS.length; index++) {
            int itemX = x + 3 + index * SLOT_STEP;
            int itemY = y + 3;
            ItemStack armor = minecraft.player.getItemBySlot(ARMOR_SLOTS[index]);

            if (!armor.isEmpty()) {
                graphics.item(minecraft.player, armor, itemX, itemY, index);
                graphics.itemDecorations(minecraft.font, armor, itemX, itemY);
            }
        }
    }
}
