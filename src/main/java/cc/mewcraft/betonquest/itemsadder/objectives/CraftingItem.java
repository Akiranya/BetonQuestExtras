package cc.mewcraft.betonquest.itemsadder.objectives;

import cc.mewcraft.betonquest.util.ItemsAdderUtil;
import dev.lone.itemsadder.api.CustomStack;
import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.Objective;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.utils.PlayerConverter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class CraftingItem extends Objective implements Listener {

    private final String namespacedID;
    private final ItemStack item;
    private final int amount;

    public CraftingItem(Instruction instruction) throws InstructionParseException {
        super(instruction);
        template = CraftData.class;
        amount = instruction.getInt();
        if (amount < 1) {
            throw new InstructionParseException("Amount cannot be less than 1");
        }
        namespacedID = instruction.next() + ":" + instruction.next();
        CustomStack cs = ItemsAdderUtil.validateCustomStackSilently(instruction.getPackage(), namespacedID);
        item = cs != null ? cs.getItemStack() : null;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCrafting(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        String playerID = PlayerConverter.getID(player);
        CraftData playerData = (CraftData) dataMap.get(playerID);

        if (containsPlayer(playerID) && checkConditions(playerID)) {
            ItemStack result = event.getRecipe().getResult();
            CustomStack customStack = CustomStack.byItemStack(result);
            if (customStack != null && customStack.getNamespacedID().equalsIgnoreCase(namespacedID)) {
                int absoluteCreations = countPossibleCrafts(event);
                int remainingSpace = countRemainingSpace(player);
                playerData.subtract(Math.min(remainingSpace, absoluteCreations));
                if (playerData.isZero()) completeObjective(playerID);
            }
        }
    }

    private int countPossibleCrafts(CraftItemEvent event) {
        int possibleCreations = 1;
        if (event.isShiftClick()) {
            possibleCreations = Integer.MAX_VALUE;
            for (ItemStack item : event.getInventory().getMatrix()) {
                if (item != null && !item.getType().equals(Material.AIR))
                    possibleCreations = Math.min(possibleCreations, item.getAmount());
            }
        }
        return possibleCreations * event.getRecipe().getResult().getAmount();
    }

    private int countRemainingSpace(Player player) {
        int remainingSpace = 0;
        for (ItemStack i : player.getInventory().getStorageContents()) {
            if (i == null || i.getType().equals(Material.AIR)) {
                remainingSpace += item.getMaxStackSize();
            } else if (i.equals(item)) {
                remainingSpace += item.getMaxStackSize() - i.getAmount();
            }
        }
        return remainingSpace;
    }

    @Override
    public void start() {
        Bukkit.getPluginManager().registerEvents(this, BetonQuest.getInstance());
    }

    @Override
    public void stop() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public String getDefaultDataInstruction() {
        return Integer.toString(amount);
    }

    @Override
    public String getProperty(String name, String playerID) {
        if ("left".equalsIgnoreCase(name))
            return Integer.toString(amount - ((CraftData) dataMap.get(playerID)).getAmount());
        if ("amount".equalsIgnoreCase(name))
            return Integer.toString(((CraftData) dataMap.get(playerID)).getAmount());
        return "";
    }

    public static class CraftData extends Objective.ObjectiveData {
        private int amount;

        public CraftData(String instruction, String playerID, String objID) {
            super(instruction, playerID, objID);
            amount = Integer.parseInt(instruction);
        }

        private void subtract(int amount) {
            this.amount -= amount;
            update();
        }

        private boolean isZero() {
            return (amount < 1);
        }

        private int getAmount() {
            return amount;
        }

        public String toString() {
            return String.valueOf(amount);
        }
    }
}
