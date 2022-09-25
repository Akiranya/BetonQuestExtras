package cc.mewcraft.betonquest.events;

import cc.mewcraft.betonquest.util.ItemsAdderUtil;
import dev.lone.itemsadder.api.CustomBlock;
import lombok.CustomLog;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.QuestEvent;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;
import org.betonquest.betonquest.utils.location.LocationData;
import org.bukkit.Location;

@CustomLog(topic = "BetonQuest -> ItemsAdder")
public class SetBlockAt extends QuestEvent {

    private final String namespacedID;
    private final LocationData locationData;

    public SetBlockAt(Instruction instruction) throws InstructionParseException {
        super(instruction, true);
        namespacedID = instruction.next() + ":" + instruction.next();
        ItemsAdderUtil.validateCustomBlockSilently(namespacedID);
        locationData = instruction.getLocation().getLocationData();
    }

    @Override
    protected Void execute(String playerID) throws QuestRuntimeException {
        CustomBlock cs = CustomBlock.getInstance(namespacedID);
        Location location = locationData.get(playerID);
        cs.place(location);
        return null;
    }
}
