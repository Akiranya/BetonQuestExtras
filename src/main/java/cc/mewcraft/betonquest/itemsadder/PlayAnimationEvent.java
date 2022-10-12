package cc.mewcraft.betonquest.itemsadder;

import dev.lone.itemsadder.api.ItemsAdder;
import lombok.CustomLog;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.QuestEvent;
import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.exceptions.InstructionParseException;

@CustomLog
public class PlayAnimationEvent extends QuestEvent {

    private final String animation;

    public PlayAnimationEvent(Instruction instruction) throws InstructionParseException {
        super(instruction, true);
        animation = instruction.next();
    }

    @Override
    protected Void execute(Profile profile) {
        ItemsAdder.playTotemAnimation(profile.getOnlineProfile().getOnlinePlayer(), animation);
        return null;
    }

}
