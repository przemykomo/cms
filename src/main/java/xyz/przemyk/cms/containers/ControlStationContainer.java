package xyz.przemyk.cms.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import xyz.przemyk.cms.setup.CMSContainers;

public class ControlStationContainer extends Container {

    @SuppressWarnings("unused")
    public ControlStationContainer(int id, PlayerInventory playerInventory) {
        super(CMSContainers.CONTROL_STATION.get(), id);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        return ItemStack.EMPTY;
    }
}
