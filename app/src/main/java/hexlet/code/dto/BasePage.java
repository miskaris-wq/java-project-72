package hexlet.code.dto;

public class BasePage {
    private String flash;
    private String info;
    private String error;

    public String getFlash() {
        return flash;
    }
    public void setFlash(String flash) {
        this.flash = flash;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }

}
