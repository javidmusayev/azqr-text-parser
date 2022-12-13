package dev.javidmusayev.azqrtextparser;

public class AzqrText {

    private final String text;
    
    private AzqrText(String text) {
        this.text = text;
    }

    public static AzqrText create(String text) {
        return new AzqrText(text);
    }

    public String checkCrc() {
        return "";
    }
}
