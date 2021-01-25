package ee.gaile.enums;

public enum SearchEngineBot {
    GOOGLE_1("66.249.73.177"),
    GOOGLE_2("66.249.73.190"),
    YANDEX_1("2a02:6b8:0:1a33:e115:833c:16c:32e");

    private final String name;

    SearchEngineBot(String name) {
        this.name = name;
    }

    public  String getName() {
        return name;
    }

    public static boolean isGoogleBot(String ip){
        for(SearchEngineBot bot : SearchEngineBot.values()){
            if(bot.name.equals(ip)) return true;
        }
        return false;
    }
}
