package dev.javidmusayev.azqrtextparser;

public class TLVValueLengthValidator implements TLVValidator {

    @Override
    public boolean validate(TLV tlv) {
        return (tlv.getLength() == tlv.getValue().length());
    }
    
}
