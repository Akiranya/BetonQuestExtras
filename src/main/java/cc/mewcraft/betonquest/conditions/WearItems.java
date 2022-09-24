package cc.mewcraft.betonquest.conditions;

import dev.lone.itemsadder.api.CustomStack;
import lombok.CustomLog;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.Condition;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.utils.PlayerConverter;
import org.bukkit.inventory.ItemStack;

@CustomLog(topic = "BetonQuestItemsAdder")
public class WearItems extends Condition {
    private final String namespacedID;

    public WearItems(Instruction instruction) throws InstructionParseException {
        super(instruction, true);
        namespacedID = instruction.next() + ":" + instruction.next();
        CustomStack cs = CustomStack.getInstance(namespacedID);
        if (cs == null) {
            throw new InstructionParseException("Unknown item ID: " + namespacedID);
        }
    }

    @Override
    protected Boolean execute(String playerID) {
        ItemStack[] inventoryItems = PlayerConverter.getPlayer(playerID).getInventory().getArmorContents();
        for (ItemStack is : inventoryItems) {
            CustomStack cs = CustomStack.byItemStack(is);
            if (cs != null && cs.getNamespacedID().equalsIgnoreCase(namespacedID)) {
                return true;
            }
        }
        return false;
    }
}
