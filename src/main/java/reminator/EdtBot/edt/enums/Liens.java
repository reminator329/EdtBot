package reminator.EdtBot.edt.enums;

public enum Liens {

    CSV_1A("");

    private String url;

    Liens (String url) {
        this.url = url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
