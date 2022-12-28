package dev.javidmusayev.azqrtextparser.contollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.javidmusayev.azqrtextparser.domains.AzQR;
import dev.javidmusayev.azqrtextparser.services.AzqrService;

@RestController("/azqr")
public class AzqrController {

    @Autowired
    AzqrService azqrService;
    
    @GetMapping("/status")
    public String status() {
        return "ok";
    }

    @PostMapping("/calc-crc")
    public String calcCrc(@RequestParam String data) {
        return azqrService.calcCrc(data);
    }

    @GetMapping("/parse")
    public AzQR parse(@RequestParam String azqrText) {
        return azqrService.parse(azqrText);
    }
}
