package top.berthua.whitelistmirai;

import com.alibaba.fastjson.JSONArray;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.BotConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WhiteList {
    public List<PlayerData> playerList = new ArrayList<>();
    public void onEnable(Bot bot) {
        File catalogue = new File("./WhiteListMirai/");
        File file = new File(catalogue, "players.json");
        String jsons;
        try {
            if (catalogue.exists()) {
                if (file.exists()) {
                    FileInputStream in = new FileInputStream(file);
                    byte[] fileContent = new byte[(int) file.length()];
                    in.read(fileContent);
                    String txt = new String(fileContent);
                    jsons = new String(txt.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                    playerList = JSONArray.parseArray(jsons,PlayerData.class);
                } else {
                    file.createNewFile();
                    FileOutputStream os = new FileOutputStream(file);
                    PlayerData example = new PlayerData();
                    example.setQID(114514);
                    example.setMinecraftID("example");
                    playerList.add(example);
                    os.write(new JSONArray(playerList).toString().getBytes(StandardCharsets.UTF_8));
                }
            }else{
                catalogue.mkdir();
                file.createNewFile();
                FileOutputStream os = new FileOutputStream(file);
                PlayerData example = new PlayerData();
                example.setQID(114514);
                example.setMinecraftID("example");
                playerList.add(example);
                os.write(new JSONArray(playerList).toString().getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        bot.getEventChannel().subscribeAlways(GroupMessageEvent.class, (event) -> {
            MessageChain chain = event.getMessage();
            String content = chain.contentToString();
            String playerid;
            long QID = event.getSender().getId();
            if (content.contains("#��������� ")) {
                playerid = content.replace("#��������� ", "");
                for(int t = 0; t < playerList.size(); t++){
                    if(playerList.get(t).getQID() == QID || playerList.get(t).getMinecraftID().equals(playerid)){
                        event.getSubject().sendMessage(new At(event.getSender().getId()).plus(new PlainText("���Ѿ���������MCID���������MCID�ѱ��󶨹���")));
                        break;
                    }else if(t == playerList.size()-1){
                        PlayerData playerData = new PlayerData();
                        playerData.setQID(QID);
                        playerData.setMinecraftID(playerid);
                        writePlayer(playerData);
                        WhiteListMirai.Command = "whitelist add "+ playerid;
                        event.getSubject().sendMessage(new At(event.getSender().getId()).plus(new PlainText("�ɹ������Ұ�������"+playerid+"��")));
                        break;
                    }
                }
            }
            if (content.startsWith("#ע�������� ")){
                playerid = content.replace("#ע�������� ", "");
                PlayerData data = new PlayerData();
                data.setQID(QID);
                data.setMinecraftID(playerid);
                if(playerList.contains(data)){
                    WhiteListMirai.Command = "whitelist remove "+ playerid;
                    deletePlayer(data);
                    event.getSubject().sendMessage(new At(event.getSender().getId()).plus(new PlainText("�ɹ�ע����")));
                }else{
                    event.getSubject().sendMessage(new At(event.getSender().getId()).plus(new PlainText("��û�а�MCID���ύ��MCID����")));
                }
            }
            if(content.equals("#��ѯ������")){
                for(int t = 0;t < playerList.size();t++){
                    if(playerList.get(t).getQID() == QID){
                        event.getSubject().sendMessage("QQ�ţ�"+playerList.get(t).getQID()+"\nMCID��"+playerList.get(t).getMinecraftID());
                        break;
                    }else if(t == playerList.size()-1){
                        event.getSubject().sendMessage("û�в�ѯ������");
                    }
                }
            }
        });
    }
    public void writePlayer(PlayerData x){
        File catalogue = new File("./WhiteListMirai/");
        File file = new File(catalogue, "players.json");
        try {
            FileOutputStream os = new FileOutputStream(file,false);
            playerList.add(x);
            os.write(new JSONArray(playerList).toString().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void deletePlayer(PlayerData x){
        File catalogue = new File("./WhiteListMirai/");
        File file = new File(catalogue, "players.json");
        try {
            FileOutputStream os = new FileOutputStream(file,false);
            playerList.remove(x);
            os.write(new JSONArray(playerList).toString().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void login(long QQ,String password){
        Bot bot = BotFactory.INSTANCE.newBot(QQ,password, new BotConfiguration() {{
            // ���ã����磺
            fileBasedDeviceInfo("./WhiteListMirai/device.json");
        }});
        bot.login();
        onEnable(bot);
    }
}
