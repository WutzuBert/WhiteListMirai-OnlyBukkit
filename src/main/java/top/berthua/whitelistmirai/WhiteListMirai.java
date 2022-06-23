package top.berthua.whitelistmirai;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class WhiteListMirai extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("WhiteListMirai已加载!");
        new BukkitRunnable(){
            @Override
            public void run() {
                WhiteList.onEnable();
            }
        }.run();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("login")) {
            try {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bot bot = BotFactory.INSTANCE.newBot(Long.parseLong(args[0]), args[1], new BotConfiguration() {{
                            // 配置，例如：
                            fileBasedDeviceInfo("./WhiteListMirai/device.json");
                        }});
                        bot.login();
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
        getLogger().info("WhiteListMirai已卸载！");
    }
}
