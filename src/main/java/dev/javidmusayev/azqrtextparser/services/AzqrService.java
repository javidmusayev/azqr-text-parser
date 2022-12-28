package dev.javidmusayev.azqrtextparser.services;

import org.springframework.stereotype.Service;

import dev.javidmusayev.azqrtextparser.domains.AzQR;
import dev.javidmusayev.azqrtextparser.domains.CRC16;

@Service
public class AzqrService {
    
    public AzQR parse(String azqrText) {
        return AzQR.create(azqrText);
    }

    public String calcCrc(String data) {
        CRC16 crc16 = CRC16.create(data);
        
        int crc = crc16.getValue();
        String crcHex = Integer.toHexString(crc);

        if (String.valueOf(crcHex).length() > 3)
            return crcHex;
        else
            return "0" + crcHex;
    }
}
