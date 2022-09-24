package cc.mewcraft.betonquest.conditions;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomStack;
import lombok.CustomLog;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.Condition;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;
import org.betonquest.betonquest.utils.location.LocationData;
import org.bukkit.Location;

@CustomLog(topic = "BetonQuestItemsAdder")
public class IsBlock extends Condition {

    private final String namespacedID;
    private final LocationData locationData;

    public IsBlock(Instruction instruction) throws InstructionParseException {
        super(instruction, true);
        namespacedID = instruction.next() + ":" + instruction.next();
        CustomStack cs = CustomStack.getInstance(namespacedID);
        if (cs == null) {
            throw new InstructionParseException("Unknown item ID: " + namespacedID);
        } else if (!cs.isBlock()) {
            throw new InstructionParseException("Item is not block");
        }
        locationData = instruction.getLocation().getLocationData();
    }

    @Override
    protected Boolean execute(String playerID) throws QuestRuntimeException {
        Location location = locationData.get(playerID);
        CustomBlock cb = CustomBlock.byAlreadyPlaced(location.getBlock());
        return cb != null && cb.getNamespacedID().equalsIgnoreCase(namespacedID);
    }
}
