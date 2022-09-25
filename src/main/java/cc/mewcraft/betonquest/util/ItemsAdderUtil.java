package cc.mewcraft.betonquest.util;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomStack;
import lombok.CustomLog;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CustomLog(topic = "BetonQuest -> ItemsAdder")
public final class ItemsAdderUtil {

    public static void validateCustomStack(@NotNull String namespacedID) throws InstructionParseException {
        if (validateCustomStackSilently(namespacedID) == null) {
            throw new InstructionParseException("Unknown item ID: " + namespacedID);
        }
    }

    @Nullable
    public static CustomStack validateCustomStackSilently(@NotNull String namespacedID) {
        CustomStack cs = CustomStack.getInstance(namespacedID);
        if (cs == null) {
            LOG.error("Unknown item ID: " + namespacedID);
        }
        return cs;
    }

    @Nullable
    public static CustomBlock validateCustomBlockSilently(@NotNull String namespacedID) {
        CustomBlock cb = CustomBlock.getInstance(namespacedID);
        if (cb == null) {
            LOG.error("Unknown item ID: " + namespacedID);
        } else if (!cb.isBlock()) {
            LOG.error("Item is not block: " + namespacedID);
        }
        return cb;
    }

    private ItemsAdderUtil() {
        throw new UnsupportedOperationException("This class cannot instantiate");
    }
}
