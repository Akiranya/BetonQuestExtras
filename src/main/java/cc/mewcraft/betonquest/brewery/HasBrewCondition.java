package cc.mewcraft.betonquest.brewery;

import com.dre.brewery.Brew;
import com.dre.brewery.recipe.BRecipe;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.Condition;
import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HasBrewCondition extends Condition {

    private final Integer count;
    private final BrewQuality quality;
    private final BRecipe brew;

    public HasBrewCondition(final Instruction instruction) throws InstructionParseException {
        super(instruction, true);

        count = instruction.getInt();
        if (count <= 0) {
            throw new InstructionParseException("Can't give less than one brew!");
        }

        String qualityString = instruction.next();
        quality = new BrewQuality(qualityString);

        final String name = instruction.next().replace("_", " ");

        BRecipe recipe = null;
        for (final BRecipe r : BRecipe.getAllRecipes()) {
            if (r.hasName(name)) {
                recipe = r;
                break;
            }
        }

        if (recipe == null) {
            throw new InstructionParseException("There is no brewing recipe with the name " + name + "!");
        } else {
            brew = recipe;
        }
    }

    @Override
    protected Boolean execute(final Profile profile) throws QuestRuntimeException {
        final Player player = profile.getOnlineProfile().getOnlinePlayer();

        int remaining = count;

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            final ItemStack item = player.getInventory().getItem(i);
            if (item != null) {
                Brew brew = Brew.get(item);
                if (brew != null && brew.getCurrentRecipe().equals(this.brew) && quality.contains(brew.getQuality())) {
                    remaining -= item.getAmount();
                    if (remaining <= 0) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
