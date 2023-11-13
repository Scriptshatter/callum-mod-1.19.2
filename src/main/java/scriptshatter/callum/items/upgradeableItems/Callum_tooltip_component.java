package scriptshatter.callum.items.upgradeableItems;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.HashMap;

@Environment(EnvType.CLIENT)
public class Callum_tooltip_component implements TooltipComponent {
    public static final Identifier TEXTURE = new Identifier("textures/gui/container/bundle.png");
    private final int upgrade_cap;
    private final ItemStack hover;
    private final HashMap<Integer, ItemStack> upgrades;
    private final Identifier upgrade_target;
    private final int selected_slot;
    private final Callum_tooltip_data data;

    public Callum_tooltip_component(Callum_tooltip_data data){
        this.upgrade_cap = ((IUpgradeableItem)data.upgradeable().getItem()).getUpgrade_cap();
        this.upgrades = IUpgradeableItem.getUpgrades(data.upgradeable());
        this.hover = data.hover();
        this.upgrade_target = ((IUpgradeableItem)data.upgradeable().getItem()).get_id();
        this.selected_slot = ((IUpgradeableItem)data.upgradeable().getItem()).get_selected_slot(data.upgradeable());
        this.data = data;
    }

    @Override
    public int getHeight() {
        return 26;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return this.getColumns() * 18 + 2;
    }

    private void drawSlot(int x, int y, int index, boolean shouldBlock, TextRenderer textRenderer, MatrixStack matrices, ItemRenderer itemRenderer, int z) {
        ItemStack itemStack = this.upgrades.get(index);
        if(itemStack == null){
            itemStack = ItemStack.EMPTY;
        }
        this.draw(matrices, x, y, z, shouldBlock ? Callum_tooltip_component.Sprite.BLOCKED_SLOT : Callum_tooltip_component.Sprite.SLOT);
        itemRenderer.renderInGuiWithOverrides(itemStack, x + 1, y + 1, index);
        itemRenderer.renderGuiItemOverlay(textRenderer, itemStack, x + 1, y + 1);
        if (index == selected_slot) {
            HandledScreen.drawSlotHighlight(matrices, x + 1, y + 1, z);
        }
    }

    private void draw(MatrixStack matrices, int x, int y, int z, Callum_tooltip_component.Sprite sprite) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        DrawableHelper.drawTexture(matrices, x, y, z, (float)sprite.u, (float)sprite.v, sprite.width, sprite.height, 128, 128);
    }

    private int getColumns() {
        return this.upgrade_cap;
    }

    private void drawOutline(int x, int y, int columns, int rows, MatrixStack matrices, int z) {
        this.draw(matrices, x, y, z, Callum_tooltip_component.Sprite.BORDER_CORNER_TOP);
        this.draw(matrices, x + columns * 18 + 1, y, z, Callum_tooltip_component.Sprite.BORDER_CORNER_TOP);

        int i;
        for(i = 0; i < columns; ++i) {
            this.draw(matrices, x + 1 + i * 18, y, z, Callum_tooltip_component.Sprite.BORDER_HORIZONTAL_TOP);
            this.draw(matrices, x + 1 + i * 18, y + rows * 20, z, Callum_tooltip_component.Sprite.BORDER_HORIZONTAL_BOTTOM);
        }

        for(i = 0; i < rows; ++i) {
            this.draw(matrices, x, y + i * 20 + 1, z, Callum_tooltip_component.Sprite.BORDER_VERTICAL);
            this.draw(matrices, x + columns * 18 + 1, y + i * 20 + 1, z, Callum_tooltip_component.Sprite.BORDER_VERTICAL);
        }

        this.draw(matrices, x, y + rows * 20, z, Callum_tooltip_component.Sprite.BORDER_CORNER_BOTTOM);
        this.draw(matrices, x + columns * 18 + 1, y + rows * 20, z, Callum_tooltip_component.Sprite.BORDER_CORNER_BOTTOM);
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer, int z) {
        int i = this.getColumns();
        int j = 1;
        boolean bl = !IUpgradeableItem.can_enter(hover, upgrade_target, data.upgradeable());
        int k = 1;

        for(int l = 0; l < j; ++l) {
            for(int m = 0; m < i; ++m) {
                int n = x + m * 18 + 1;
                int o = y + 1;
                this.drawSlot(n, o, k++, bl, textRenderer, matrices, itemRenderer, z);
            }
        }

        this.drawOutline(x, y, i, j, matrices, z);
    }

    @Environment(EnvType.CLIENT)
    private enum Sprite {
        SLOT(0, 0, 18, 20),
        BLOCKED_SLOT(0, 40, 18, 20),
        BORDER_VERTICAL(0, 18, 1, 20),
        BORDER_HORIZONTAL_TOP(0, 20, 18, 1),
        BORDER_HORIZONTAL_BOTTOM(0, 60, 18, 1),
        BORDER_CORNER_TOP(0, 20, 1, 1),
        BORDER_CORNER_BOTTOM(0, 60, 1, 1);

        public final int u;
        public final int v;
        public final int width;
        public final int height;

        Sprite(int u, int v, int width, int height) {
            this.u = u;
            this.v = v;
            this.width = width;
            this.height = height;
        }
    }
}
