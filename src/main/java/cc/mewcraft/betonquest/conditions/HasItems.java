package cc.mewcraft.betonquest.conditions;

import dev.lone.itemsadder.api.CustomStack;
import lombok.CustomLog;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.Condition;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.utils.PlayerConverter;
import org.bukkit.inventory.ItemStack;

@CustomLog(topic = "BetonQuest -> ItemsAdder")
public class HasItems extends Condition {

    private final String namespacedID;
    private final int amount;

    public HasItems(Instruction instruction) throws InstructionParseException {
        super(instruction, true);
        amount = instruction.getInt();
        if (amount < 1) {
            throw new InstructionParseException("Amount cannot be less than 1");
        }
        namespacedID = instruction.next() + ":" + instruction.next();
        CustomStack cs = CustomStack.getInstance(namespacedID);
        if (cs == null) {
            throw new InstructionParseException("Unknown item ID: " + namespacedID);
        }
    }

    @Override
    protected Boolean execute(String playerID) {
        ItemStack[] inventoryItems = PlayerConverter.getPlayer(playerID).getInventory().getContents();
        int has = 0;
        for (ItemStack it : inventoryItems) {
            CustomStack cs = CustomStack.byItemStack(it);
            if (cs != null && cs.getNamespacedID().equalsIgnoreCase(namespacedID)) {
                if (it.getAmount() >= amount) {
                    return true;
                } else {
                    has += it.getAmount();
                }
            }
            if (has >= amount) {
                return true;
            }
        }
        return false;
    }
}
