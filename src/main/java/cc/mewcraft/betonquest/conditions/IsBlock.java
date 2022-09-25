package cc.mewcraft.betonquest.conditions;

import cc.mewcraft.betonquest.util.ItemsAdderUtil;
import dev.lone.itemsadder.api.CustomBlock;
import lombok.CustomLog;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.Condition;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;
import org.betonquest.betonquest.utils.location.LocationData;
import org.bukkit.Location;

@CustomLog(topic = "BetonQuest -> ItemsAdder")
public class IsBlock extends Condition {

    private final String namespacedID;
    private final LocationData locationData;

    public IsBlock(Instruction instruction) throws InstructionParseException {
        super(instruction, true);
        namespacedID = instruction.next() + ":" + instruction.next();
        ItemsAdderUtil.validateCustomBlockSilently(namespacedID);
        locationData = instruction.getLocation().getLocationData();
    }

    @Override
    protected Boolean execute(String playerID) throws QuestRuntimeException {
        Location location = locationData.get(playerID);
        CustomBlock cb = CustomBlock.byAlreadyPlaced(location.getBlock());
        return cb != null && cb.getNamespacedID().equalsIgnoreCase(namespacedID);
    }
}
