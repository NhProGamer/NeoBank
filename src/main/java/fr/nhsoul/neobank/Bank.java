package fr.nhsoul.neobank;

import fr.nhsoul.neobank.init.NeobankModItems;
import fr.nhsoul.neobank.network.NeoBits;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.neoforged.neoforge.items.ItemHandlerHelper.giveItemToPlayer;

public class Bank {
    private static final int[] BILLS = {500, 200, 100, 50, 20, 10, 5, 1};

    public static int getMoneyValue(ItemStack stack) {
        if (NeobankModItems.BILL_1.get() == stack.getItem()) return 1;
        if (NeobankModItems.BILL_5.get() == stack.getItem()) return 5;
        if (NeobankModItems.BILL_10.get() == stack.getItem()) return 10;
        if (NeobankModItems.BILL_20.get() == stack.getItem()) return 20;
        if (NeobankModItems.BILL_50.get() == stack.getItem()) return 50;
        if (NeobankModItems.BILL_100.get() == stack.getItem()) return 100;
        if (NeobankModItems.BILL_200.get() == stack.getItem()) return 200;
        if (NeobankModItems.BILL_500.get() == stack.getItem()) return 500;
        return 0;
    }

    public static ItemStack getMoneyItem(int amount, int billsAmount) {
        return switch (amount) {
            case 1 -> new ItemStack(NeobankModItems.BILL_1.get(),billsAmount);
            case 5 -> new ItemStack(NeobankModItems.BILL_5.get(),billsAmount);
            case 10 -> new ItemStack(NeobankModItems.BILL_10.get(),billsAmount);
            case 20 -> new ItemStack(NeobankModItems.BILL_20.get(),billsAmount);
            case 50 -> new ItemStack(NeobankModItems.BILL_50.get(),billsAmount);
            case 100 -> new ItemStack(NeobankModItems.BILL_100.get(),billsAmount);
            case 200 -> new ItemStack(NeobankModItems.BILL_200.get(),billsAmount);
            case 500 -> new ItemStack(NeobankModItems.BILL_500.get(),billsAmount);
            default -> new ItemStack(Items.DIRT);
        };
    }

    public static void addMoney(Player player, int amount) {
        CompoundTag playerNBT = player.getPersistentData();
        int currentMoney = playerNBT.getInt("NeoBits");

        playerNBT.putInt("NeoBits", currentMoney + amount);
        PacketDistributor.sendToPlayer((ServerPlayer) player, new NeoBits(playerNBT.getInt("NeoBits")));
    }

    public static int getMoney(Player player) {
        return player.getPersistentData().getInt("NeoBits");
    }

    public static boolean removeMoney(Player player, int amount) {
        CompoundTag playerNBT = player.getPersistentData();
        int currentMoney = playerNBT.getInt("NeoBits");

        if (currentMoney >= amount) {
            playerNBT.putInt("NeoBits", currentMoney - amount);
            PacketDistributor.sendToPlayer((ServerPlayer) player, new NeoBits(playerNBT.getInt("NeoBits")));
            return true;
        } else {
            return false;
        }
    }

    public static void giveChange(int money, Player player) {
        boolean res = Bank.removeMoney(player, money);
        if (res) {
            for (int billValue : BILLS) {
                // Calcul du nombre de billets de cette valeur à rendre
                int billAmount = money / billValue;

                if (billAmount > 0) {
                    money = money % billValue;
                    giveItemToPlayer(player, getMoneyItem(billValue, billAmount));

                }
            }
        }
    }


}
