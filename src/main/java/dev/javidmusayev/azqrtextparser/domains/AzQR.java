package dev.javidmusayev.azqrtextparser.domains;

import java.util.ArrayList;
import java.util.List;

public class AzQR {

    private static final List<String> nestedTags = List.of("26", "62", "64");
    private static final String CRC_VALUE_TAG = "63";
    
    private final String text;
    private boolean isValid;

    private List<TLV> fields = new ArrayList<>();
    private List<String> validationErrors = new ArrayList<>();
    
    private AzQR(String text) {
        this.text = text;
        parseFields();
        validate();
    }

    public static AzQR create(String text) {
        AzQR azqr = new AzQR(text);
        return azqr;
    }

    public boolean isValid() {
        return isValid;
    }

    public List<TLV> getFields() {
        return fields;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    private void parseFields() {
        int position = 0;
        int resetParentInPosition = 0;
        
        String parentTag = "";
        int parentLength = 0;

        String tag = "";
        String lengthString = "";
        int length = 0;
        String value = "";

        while (position < text.length()) {
            if (!parentTag.equals("") && resetParentInPosition == position) {
                parentTag = "";
                parentLength = 0;
            }
            
            tag = text.substring(position, position + 2);
            position += 2;
            lengthString = text.substring(position, position + 2);
            position += 2;

            if (lengthString.startsWith("0")) {
                length = Integer.parseInt(lengthString.substring(1, 2));
            } else {
                length = Integer.parseInt(lengthString.substring(0, 2));
            }

            if (parentTag.equals("") && nestedTags.contains(tag)) {
                parentTag = tag;
                parentLength = length;
                resetParentInPosition = position + length;
                continue;
            }

            value = text.substring(position, position + length <= text.length() ? position + length : text.length());
            position += length;

            TLV tlv = TLV.create(parentTag, parentLength, tag, length, value);
            tlv.validate();
            if (tlv.isValid())
                fields.add(tlv);
            else {
                validationErrors.add("TLV with tag '" + tlv.getTag() + "' is invalid. Errors: " + String.join("; ", tlv.getValidationErrors()));
                isValid = false;
            }
        }
    }

    private void validate() {
        isValid = true;
        
        if (!checkCrc()) {
            validationErrors.add("CRC invalid!");
            isValid = false;
        }
    }

    private boolean checkCrc() {
        String actualValue = getCrcValue();
        StringBuilder data = new StringBuilder();

        boolean parentTagAdded = false;
        
        for (TLV tlv : fields) {
            if (tlv.getTag().equals(CRC_VALUE_TAG))
                continue;

            if (!tlv.getParentTag().equals("")) {
                if (parentTagAdded) {
                    data.append(tlv.getTag() + tlv.getLengthString() + tlv.getValue());
                } else {
                    data.append(tlv.getParentTag() + tlv.getParentLengthString() + tlv.getTag() + tlv.getLengthString() + tlv.getValue());
                    parentTagAdded = true;
                }
            }                
            else {
                data.append(tlv.getTag() + tlv.getLengthString() + tlv.getValue());
                parentTagAdded = false;
            }
                
        }

        String calculatedValue = calcCrc(data.toString());

        return actualValue.equals(calculatedValue);
    }

    private String getCrcValue() {
        for (TLV tlv : fields) {
            if (tlv.getTag().equals(CRC_VALUE_TAG))
                return tlv.getValue();
        }

        return "";
    }

    private static String calcCrc(String data) {
        CRC16 crc16 = CRC16.create(data);
        
        int crc = crc16.getValue();
        String crcHex = Integer.toHexString(crc);

        if (String.valueOf(crcHex).length() > 3)
            return crcHex;
        else
            return "0" + crcHex;
    }
}
