package cc.mewcraft.betonquest.var;

import me.lucko.helper.utils.annotation.NonnullByDefault;
import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.VariableString;
import org.betonquest.betonquest.api.config.QuestPackage;
import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;

@NonnullByDefault
public class GenericVariable<T> {

    private final String instruction;
    private final QuestPackage questPackage;
    private final ObjectResolver<String, T> parser;

    public GenericVariable(String instruction, QuestPackage questPackage, ObjectResolver<String, T> parser) {
        this.instruction = instruction;
        this.questPackage = questPackage;
        this.parser = parser;
    }

    public T resolve(Profile profile) throws QuestRuntimeException {
        String parsedString;
        try {
            parsedString = new VariableString(questPackage, instruction).getString(profile);
        } catch (InstructionParseException e) {
            throw new QuestRuntimeException(e.getMessage(), e.getCause());
        }
        if (BetonQuest.resolveVariables(parsedString).size() > 0) {
            throw new QuestRuntimeException("Instruction \"" + instruction + "\" contains unresolved conversation variable(s)! " +
                                            "The instruction after resolving is \"" + parsedString + "\". " +
                                            "Please make sure the remaining unresolved variables is set before this instruction executes.");
        }
        return parser.resolve(parsedString);
    }

}
