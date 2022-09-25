package cc.mewcraft.betonquest.conditions;

import cc.mewcraft.betonquest.util.ItemsAdderUtil;
import dev.lone.itemsadder.api.CustomStack;
import lombok.CustomLog;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.Condition;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.utils.PlayerConverter;
import org.bukkit.inventory.ItemStack;

@CustomLog
public class WearItems extends Condition {
    private final String namespacedID;

    public WearItems(Instruction instruction) throws InstructionParseException {
        super(instruction, true);
        namespacedID = instruction.next() + ":" + instruction.next();
        ItemsAdderUtil.validateCustomStackSilently(instruction.getPackage(), namespacedID);
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
