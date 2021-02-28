package xyz.przemyk.cms.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import xyz.przemyk.cms.setup.CMSContainers;

public class ControlStationContainer extends Container {

    public ControlStationContainer(int id, PlayerInventory playerInventory) {
        super(CMSContainers.CONTROL_STATION.get(), id);

//        for(int i = 0; i < 3; ++i) {
//            for(int j = 0; j < 9; ++j) {
//                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
//            }
//        }
//
//        for(int k = 0; k < 9; ++k) {
//            addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
//        }
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
