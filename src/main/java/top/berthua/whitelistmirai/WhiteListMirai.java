package top.berthua.whitelistmirai;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class WhiteListMirai extends JavaPlugin {
    public static String Command = null;
    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("WhiteListMirai“—º”‘ÿ!");
        new BukkitRunnable(){
            @Override
            public void run(){
                if(Command != null){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),Command);
                    Command = null;
                }
            }
        }.runTaskTimer(this,0,1);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("login")) {
            try {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        WhiteList whiteList = new WhiteList();
                        whiteList.login(Long.parseLong(args[0]), args[1]);
                    }
                }.run();
            }catch (NumberFormatException e){
                return false;
            }
            return true;
        }
        return false;
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("WhiteListMirai“—–∂‘ÿ£°");
    }
}
