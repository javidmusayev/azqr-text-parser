package dev.javidmusayev.azqrtextparser.contollers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.javidmusayev.azqrtextparser.AzqrText;
import dev.javidmusayev.azqrtextparser.CRC16;
import dev.javidmusayev.azqrtextparser.TLV;;

@RestController
public class ParseController {
    
    @GetMapping("/status")
    public String status() {
        return "ok";
    }

    @PostMapping("/check-crc")
    public String checkCrc(@RequestParam String text) {
        AzqrText azqrText = AzqrText.create(text);
        return azqrText.checkCrc();
    }
    
    @PostMapping("/calc-crc")
    public String calcCrc(@RequestParam String data) {
        CRC16 crc16 = CRC16.create(data);
        
        int crc = crc16.getValue();
        String crcHex = Integer.toHexString(crc);

        if (String.valueOf(crcHex).length() > 3)
            return crcHex;
        else
            return "0" + crcHex;
    }

    @GetMapping("/parse-as-tlv")
    public TLV parseAsTlv(@RequestParam String text) throws Exception {
        TLV tlv = TLV.create(text);

        if (!tlv.validate())
            throw new IllegalArgumentException("Invalid TLV text");
            
        return tlv;
    }
}
