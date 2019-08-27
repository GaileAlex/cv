package ee.gaile.CV.message;

public enum Message {

    MindlyDescription(", congratulations! You have successfully applied."),
    ErrorBD("<br><br>Sorry, recording to the database is not possible <br><br>at the moment, try again later.");


    private String getMessage;

    Message(String getMessage) {
        this.getMessage = getMessage;
    }

    public String getMessage() {
        return getMessage;
    }
}
