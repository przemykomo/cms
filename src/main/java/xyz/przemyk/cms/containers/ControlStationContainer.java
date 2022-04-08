package xyz.przemyk.cms.containers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import xyz.przemyk.cms.setup.CMSContainers;

public class ControlStationContainer extends AbstractContainerMenu {

    @SuppressWarnings("unused")
    public ControlStationContainer(int id, Inventory playerInventory) {
        super(CMSContainers.CONTROL_STATION.get(), id);
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
        return ItemStack.EMPTY;
    }
}
