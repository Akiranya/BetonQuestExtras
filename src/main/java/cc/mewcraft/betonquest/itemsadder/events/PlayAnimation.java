package cc.mewcraft.betonquest.itemsadder.events;

import dev.lone.itemsadder.api.ItemsAdder;
import lombok.CustomLog;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.QuestEvent;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.utils.PlayerConverter;

@CustomLog
public class PlayAnimation extends QuestEvent {

    private final String animation;

    public PlayAnimation(Instruction instruction) throws InstructionParseException {
        super(instruction, true);
        animation = instruction.next();
    }

    @Override
    protected Void execute(String playerID) {
        ItemsAdder.playTotemAnimation(PlayerConverter.getPlayer(playerID), animation);
        return null;
    }

}
