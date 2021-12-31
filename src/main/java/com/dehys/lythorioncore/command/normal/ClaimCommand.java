package com.dehys.lythorioncore.command.normal;

public class ClaimCommand {

    public ClaimCommand() {
        //Main.getCommandHandler.addCommand(this);
    }

    /*@Override
    public GenericCommand getParent() {
        return null;
    }

    @Override
    public @NotNull String getName() {
        return "claim";
    }

    @Override
    public @NotNull String getHelp() {
        return "Claims a chunk for you.";
    }

    @Override
    public void execute(CommandHandler info) {
        //if (info.isPlayer()) this.execute(info.getBukkitEvent());
    }

    /*private void execute(BukkitCommandEvent event) {
        if (!(event.getSender() instanceof Player player)) return;

        if (event.getArgs().length == 0) {
            String help = """
                    §6
                    §6§nClaiming commands:
                    §e/claim new <region name/id> §7§o- Claims the chunk you are standing in into a region
                    §e/claim color <region id> <color> §7o- Change the color of the region, shown in world map
                    §e/claim desc <region id> <description> §7o- Change the description of the region
                    §e/claim rename <region id> <new name> §7o- Change the name of the region
                    §e/claim list §7§o- Lists all regions
                    §6
                    §e/unclaim <region id> §7o- Unclaims the chunk you are standing in from a region
                    §e/unclaim <region id> all §7o- Unclaims all chunks from a region and deletes the region
                    §r""";
            player.sendMessage(help);
            return;
        }

        if (event.getArgs()[0].equalsIgnoreCase("new")) {
            if (event.getArgs().length == 1) {
                player.sendMessage(ChatColor.RED + "Please specify a region name or id");
                return;
            }

            if (event.getArgs().length == 2) {
                //TODO: Check if region exists - if not, create it
                //TODO: Check if player has permission to claim for specified region
                //TODO: Check if chunk is claimed by another region or same region - send error if another region

            }
        }
    }*/
}
