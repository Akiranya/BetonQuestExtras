package cc.mewcraft.betonquest.events;

import dev.lone.itemsadder.api.CustomStack;
import lombok.CustomLog;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.QuestEvent;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.utils.PlayerConverter;
import org.bukkit.inventory.ItemStack;

@CustomLog(topic = "BetonQuestItemsAdder")
public class RemoveItems extends QuestEvent {

    private final String namespacedID;
    private final int amount;

    public RemoveItems(Instruction instruction) throws InstructionParseException {
        super(instruction, true);
        amount = instruction.getInt();
        namespacedID = instruction.next() + ":" + instruction.next();
        CustomStack cs = CustomStack.getInstance(namespacedID);
        if (cs == null) {
            throw new InstructionParseException("Unknown item ID: " + namespacedID);
        }
    }

    @Override
    protected Void execute(String playerID) {
        ItemStack is = CustomStack.getInstance(namespacedID).getItemStack();
        is.setAmount(amount);
        PlayerConverter.getPlayer(playerID).getInventory().removeItem(is);
        return null;
    }
}
