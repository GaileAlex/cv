package gaile.enums;

public enum GoogleBot {
    GOOGLE_1("66.249.73.177"),
    GOOGLE_2("66.249.73.190");

    private final String name;

    GoogleBot(String name) {
        this.name = name;
    }

    public  String getName() {
        return name;
    }

    public static boolean isGoogleBot(String ip){
        for(GoogleBot bot : GoogleBot.values()){
            if(bot.name.equals(ip)) return true;
        }
        return false;
    }
}
