package dev.javidmusayev.azqrtextparser;

import java.util.ArrayList;
import java.util.List;

public class TLV {
    
    private String text;
    
    private String tag;
    private int length;
    private String value;

    private List<TLVValidator> validators = new ArrayList<>();

    private TLV(String text) {
        this.text = text;
        setTag();
        setLength();
        setValue();

        validators.add(new TLVValueLengthValidator());
    }

    private void setTag() {
        this.tag = text.substring(0, 2);
    }

    private void setLength() {
        this.length = Integer.parseInt(this.text.substring(3, 4));
        
        if (!this.text.substring(2, 3).equals("0")) {
            this.length = Integer.parseInt(this.text.substring(2, 4));
        }
    }

    private void setValue() {
        this.value = text.substring(4);
    }

    public String getTag() {
        return tag;
    }

    public int getLength() {
        return length;
    }

    public String getValue() {
        return value;
    }

    public static TLV create(String text) {
        return new TLV(text);
    }

    public boolean validate() {
        for (TLVValidator tlvValidator : validators) {
            if (!tlvValidator.validate(this))
                return false;
        }
        
        return true;
    }
}
