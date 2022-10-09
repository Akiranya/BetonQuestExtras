package cc.mewcraft.betonquest;

import cc.mewcraft.betonquest.itemsadder.conditions.HasItemInHand;
import cc.mewcraft.betonquest.itemsadder.conditions.HasItems;
import cc.mewcraft.betonquest.itemsadder.conditions.IsBlock;
import cc.mewcraft.betonquest.itemsadder.conditions.WearItems;
import cc.mewcraft.betonquest.itemsadder.events.GiveItems;
import cc.mewcraft.betonquest.itemsadder.events.PlayAnimation;
import cc.mewcraft.betonquest.itemsadder.events.RemoveItems;
import cc.mewcraft.betonquest.itemsadder.events.SetBlockAt;
import cc.mewcraft.betonquest.itemsadder.objectives.BlockBreak;
import cc.mewcraft.betonquest.itemsadder.objectives.BlockPlace;
import cc.mewcraft.betonquest.itemsadder.objectives.CraftingItem;
import cc.mewcraft.betonquest.itemsadder.objectives.EnchantItem;
import cc.mewcraft.betonquest.itemsadder.objectives.PickupItem;
import cc.mewcraft.betonquest.itemsadder.objectives.SmeltingItem;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import me.lucko.helper.Events;
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
        // Conditions
        BetonQuest.getInstance().registerConditions("hasitems", HasItems.class);
        BetonQuest.getInstance().registerConditions("wearitems", WearItems.class);
        BetonQuest.getInstance().registerConditions("hasiteminhand", HasItemInHand.class);
        BetonQuest.getInstance().registerConditions("isblock", IsBlock.class);

        // Events
        BetonQuest.getInstance().registerEvents("removeitems", RemoveItems.class);
        BetonQuest.getInstance().registerEvents("giveitems", GiveItems.class);
        BetonQuest.getInstance().registerEvents("setblockat", SetBlockAt.class);
        BetonQuest.getInstance().registerEvents("playanimation", PlayAnimation.class);

        // Objectives
        BetonQuest.getInstance().registerObjectives("craftitems", CraftingItem.class);
        BetonQuest.getInstance().registerObjectives("pickupitems", PickupItem.class);
        BetonQuest.getInstance().registerObjectives("blockbreak", BlockBreak.class);
        BetonQuest.getInstance().registerObjectives("blockplace", BlockPlace.class);
        BetonQuest.getInstance().registerObjectives("enchantitem", EnchantItem.class);
        BetonQuest.getInstance().registerObjectives("smeltingitems", SmeltingItem.class);

        // Reload when itemsadder data load done
        Events.subscribe(ItemsAdderLoadDataEvent.class)
                .handler(e -> BetonQuest.getInstance().reload())
                .bindWith(this);
    }

    @Override
    public void disable() {

    }
}
