package dev.javidmusayev.azqrtextparser.domains;

public class TLVValueLengthValidator implements TLVValidator {

    @Override
    public void validate(TLV tlv) throws Exception {
        if ((tlv.getLength() != tlv.getValue().length()))
            throw new Exception("Value length should be equal to length!");
    }
    
}
