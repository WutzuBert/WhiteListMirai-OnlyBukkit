package top.berthua.whitelistmirai;

import com.alibaba.fastjson.JSONArray;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.bukkit.Bukkit;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WhiteList {
    public static List<PlayerData> playerList = new ArrayList<>();
    public static void onEnable() {
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
                    WhiteListMirai p = new WhiteListMirai();
                    p.getLogger().info("已加载"+playerList.size()+"条玩家数据");
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
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, (event) -> {
            MessageChain chain = event.getMessage();
            String content = chain.contentToString();
            String playerid;
            long QID = event.getSender().getId();
            if (content.contains("#申请白名单 ")) {
                playerid = content.replace("#申请白名单 ", "");
                for(int t = 0; t < playerList.size(); t++){
                    if(playerList.get(t).getQID() == QID || playerList.get(t).getMinecraftID().equals(playerid)){
                        event.getSubject().sendMessage(new At(event.getSender().getId()).plus(new PlainText("你已经绑定了其他MCID或你申请的MCID已被绑定过！")));
                        break;
                    }else if(t == playerList.size()-1){
                        PlayerData playerData = new PlayerData();
                        playerData.setQID(QID);
                        playerData.setMinecraftID(playerid);
                        writePlayer(playerData);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"whitelist add " + playerid);
                        break;
                    }
                }
            }
            if (content.startsWith("#注销白名单 ")){
                playerid = content.replace("#注销白名单 ", "");
                PlayerData data = new PlayerData();
                data.setQID(QID);
                data.setMinecraftID(playerid);
                if(playerList.contains(data)){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"whitelist remove "+ playerid);
                    deletePlayer(data);
                }else{
                    event.getSubject().sendMessage(new At(event.getSender().getId()).plus(new PlainText("你没有绑定MCID或提交的MCID有误！")));
                }
            }
            if(content.equals("#查询白名单")){
                for(int t = 0;t < playerList.size();t++){
                    if(playerList.get(t).getQID() == QID){
                        event.getSubject().sendMessage("QQ号："+playerList.get(t).getQID()+"\nMCID："+playerList.get(t).getMinecraftID());
                        break;
                    }else if(t == playerList.size()-1){
                        event.getSubject().sendMessage("没有查询到数据");
                    }
                }
            }
        });
    }
    public static void writePlayer(PlayerData x){
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
    public static void deletePlayer(PlayerData x){
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
}
