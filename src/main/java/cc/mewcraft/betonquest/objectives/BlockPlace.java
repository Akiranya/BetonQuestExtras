package cc.mewcraft.betonquest.objectives;

import cc.mewcraft.betonquest.util.ItemsAdderUtil;
import dev.lone.itemsadder.api.Events.CustomBlockPlaceEvent;
import lombok.CustomLog;
import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.Objective;
import org.betonquest.betonquest.config.Config;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;
import org.betonquest.betonquest.utils.PlayerConverter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

@CustomLog
public class BlockPlace extends Objective implements Listener {

    private final String namespacedID;
    private final int amount;
    private final boolean notify;
    private final int notifyInterval;

    public BlockPlace(Instruction instruction) throws InstructionParseException {
        super(instruction);
        template = BlockData.class;
        notifyInterval = instruction.getInt(instruction.getOptional("notify"), 1);
        notify = (instruction.hasArgument("notify") || notifyInterval > 1);
        amount = instruction.getInt();
        if (amount < 1) {
            throw new InstructionParseException("Amount cannot be less than 1");
        }
        namespacedID = instruction.next() + ":" + instruction.next();
        ItemsAdderUtil.validateCustomStackSilently(instruction.getPackage(), namespacedID);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlaceBlock(CustomBlockPlaceEvent e) {
        String playerID = PlayerConverter.getID(e.getPlayer());

        if (containsPlayer(playerID)
            && checkConditions(playerID)
            && e.getNamespacedID().equalsIgnoreCase(namespacedID)) {

            BlockData playerData = (BlockData) dataMap.get(playerID);
            playerData.add();
            if (playerData.getAmount() == amount) {
                completeObjective(playerID);
            } else if (notify && playerData.getAmount() % notifyInterval == 0) {
                if (playerData.getAmount() > amount) {
                    try {
                        Config.sendNotify(instruction.getPackage().getPackagePath(), playerID, "blocks_to_place", new String[]{String.valueOf(playerData.getAmount() - amount)}, "blocks_to_place,info");
                    } catch (QuestRuntimeException ex1) {
                        try {
                            LOG.warn("The notify system was unable to play a sound for the 'blocks_to_place' category in '" + instruction.getObjective().getFullID() + "'. Error was: '" + ex1.getMessage() + "'");
                        } catch (InstructionParseException ex2) {
                            LOG.reportException(ex2);
                        }
                    }
                }
            } else {
                try {
                    Config.sendNotify(instruction.getPackage().getPackagePath(), playerID, "blocks_to_place", new String[]{String.valueOf(amount - playerData.getAmount())}, "blocks_to_place,info");
                } catch (QuestRuntimeException ex1) {
                    try {
                        LOG.warn("The notify system was unable to play a sound for the 'blocks_to_place' category in '" + instruction.getObjective().getFullID() + "'. Error was: '" + ex1.getMessage() + "'");
                    } catch (InstructionParseException ex2) {
                        LOG.reportException(ex2);
                    }
                }
            }
        }

    }

    @Override
    public void start() {
        Bukkit.getPluginManager().registerEvents(this, BetonQuest.getInstance());
    }

    @Override
    public void stop() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public String getDefaultDataInstruction() {
        return "0";
    }

    @Override
    public String getProperty(String name, String playerID) {
        if ("left".equalsIgnoreCase(name))
            return Integer.toString(amount - ((BlockData) dataMap.get(playerID)).getAmount());
        if ("amount".equalsIgnoreCase(name))
            return Integer.toString(((BlockData) dataMap.get(playerID)).getAmount());
        return "";
    }

    public static class BlockData extends Objective.ObjectiveData {
        private int amount;

        public BlockData(String instruction, String playerID, String objID) {
            super(instruction, playerID, objID);
            amount = Integer.parseInt(instruction);
        }

        private void add() {
            amount++;
            update();
        }

        private int getAmount() {
            return amount;
        }

        public String toString() {
            return String.valueOf(amount);
        }
    }
}
