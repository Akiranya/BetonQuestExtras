package cc.mewcraft.betonquest;

import cc.mewcraft.betonquest.brewery.GiveBrewEvent;
import cc.mewcraft.betonquest.brewery.HasBrewCondition;
import cc.mewcraft.betonquest.brewery.TakeBrewEvent;
import cc.mewcraft.betonquest.itemsadder.HasItemInHandCondition;
import cc.mewcraft.betonquest.itemsadder.HasItemCondition;
import cc.mewcraft.betonquest.itemsadder.BlockCondition;
import cc.mewcraft.betonquest.itemsadder.ArmorCondition;
import cc.mewcraft.betonquest.itemsadder.GiveItemEvent;
import cc.mewcraft.betonquest.itemsadder.PlayAnimationEvent;
import cc.mewcraft.betonquest.itemsadder.RemoveItemEvent;
import cc.mewcraft.betonquest.itemsadder.SetBlockEvent;
import cc.mewcraft.betonquest.itemsadder.BreakBlockObjective;
import cc.mewcraft.betonquest.itemsadder.PlaceBlockObjective;
import cc.mewcraft.betonquest.itemsadder.CraftObjective;
import cc.mewcraft.betonquest.itemsadder.EnchantObjective;
import cc.mewcraft.betonquest.itemsadder.PickupObjective;
import cc.mewcraft.betonquest.itemsadder.SmeltObjective;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import org.betonquest.betonquest.BetonQuest;

public class Main extends ExtendedJavaPlugin {

    public static Main INSTANCE;

    @Override
    public void load() {
        INSTANCE = this;
    }

    @Override
    public void enable() {
        // Brewery
        BetonQuest.getInstance().registerConditions("hasbrew", HasBrewCondition.class);
        BetonQuest.getInstance().registerEvents("takebrew", TakeBrewEvent.class);
        BetonQuest.getInstance().registerEvents("givebrew", GiveBrewEvent.class);

        // ItemsAdder
        BetonQuest.getInstance().registerConditions("iahas", HasItemCondition.class);
        BetonQuest.getInstance().registerConditions("iaarmor", ArmorCondition.class);
        BetonQuest.getInstance().registerConditions("iahand", HasItemInHandCondition.class);
        BetonQuest.getInstance().registerConditions("iablock", BlockCondition.class);
        BetonQuest.getInstance().registerEvents("iaremove", RemoveItemEvent.class);
        BetonQuest.getInstance().registerEvents("iagive", GiveItemEvent.class);
        BetonQuest.getInstance().registerEvents("iasetblock", SetBlockEvent.class);
        BetonQuest.getInstance().registerEvents("iaplay", PlayAnimationEvent.class);
        BetonQuest.getInstance().registerObjectives("iacraft", CraftObjective.class);
        BetonQuest.getInstance().registerObjectives("iapickup", PickupObjective.class);
        BetonQuest.getInstance().registerObjectives("iabreak", BreakBlockObjective.class);
        BetonQuest.getInstance().registerObjectives("iaplace", PlaceBlockObjective.class);
        BetonQuest.getInstance().registerObjectives("iaenchant", EnchantObjective.class);
        BetonQuest.getInstance().registerObjectives("iasmelt", SmeltObjective.class);

        // Reload when itemsadder data load done
        // Events.subscribe(ItemsAdderLoadDataEvent.class)
        //         .handler(e -> BetonQuest.getInstance().reload())
        //         .bindWith(this);
    }

    @Override
    public void disable() {

    }
}
