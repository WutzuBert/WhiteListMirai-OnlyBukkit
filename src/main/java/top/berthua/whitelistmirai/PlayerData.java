package top.berthua.whitelistmirai;

public class PlayerData {
    private long QID;
    private String MinecraftID;

    public long getQID() {
        return QID;
    }

    public void setQID(long QID) {
        this.QID = QID;
    }

    public String getMinecraftID() {
        return MinecraftID;
    }

    public void setMinecraftID(String minecraftID) {
        MinecraftID = minecraftID;
    }
    @Override
    public boolean equals(Object o){
        if(o==null){
            return false;
        }
        PlayerData data = (PlayerData) o;
        return data.getMinecraftID().equals(this.MinecraftID) && data.getQID() == this.QID;
    }
}
