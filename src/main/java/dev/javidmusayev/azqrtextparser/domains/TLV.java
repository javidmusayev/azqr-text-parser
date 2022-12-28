package dev.javidmusayev.azqrtextparser.domains;

import java.util.ArrayList;
import java.util.List;

public class TLV {
    
    private String parentTag;
    private int parentLength;
    
    private String tag;
    private int length;
    private String value;

    private boolean isValid;

    private List<TLVValidator> validators = new ArrayList<>();
    private List<String> validationErrors = new ArrayList<>();

    private TLV(String parentTag, int parentLength, String tag, int length, String value) {
        this.parentTag = parentTag;
        this.parentLength = parentLength;
        this.tag = tag;
        this.length = length;
        this.value = value;

        validators.add(new TLVValueLengthValidator());
    }

    public String getParentTag() {
        return parentTag;
    }
    
    public int getParentLength() {
        return parentLength;
    }

    public String getParentLengthString() {
        if (parentTag.equals("") || parentLength == 0)
            return "";
        
        if (parentLength > 9)
            return Integer.toString(parentLength);
        else
            return "0" + Integer.toString(parentLength);
    }

    public String getTag() {
        return tag;
    }

    public int getLength() {
        return length;
    }

    public String getLengthString() {
        if (length > 9)
            return Integer.toString(length);
        else
            return "0" + Integer.toString(length);
    }

    public String getValue() {
        return value;
    }

    public boolean isValid() {
        return isValid;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }
    
    public static TLV create(String parentTag, int parentLength, String tag, int length, String value) {
        return new TLV(parentTag, parentLength, tag, length, value);
    }

    public void validate() {
        boolean isValid = true;
        
        for (TLVValidator tlvValidator : validators) {
            try {
                tlvValidator.validate(this);
            } catch (Exception e) {
                validationErrors.add(e.getMessage());
                isValid = false;
            }
        }
        
        this.isValid = isValid;
    }
}
