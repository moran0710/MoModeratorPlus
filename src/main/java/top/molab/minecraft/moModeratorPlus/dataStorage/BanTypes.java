package top.molab.minecraft.moModeratorPlus.dataStorage;

public enum BanTypes {
    Ban("ban"),
    Mute("mute"),
    Kick("kick"),
    BanIP("banip");

    private final String type;

    BanTypes(String type) {
        this.type = type;
    }

    public static BanTypes getTypeByString(String type) {
        for (BanTypes banType : BanTypes.values()) {
            if (banType.getType().equals(type)) {
                return banType;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }
}
