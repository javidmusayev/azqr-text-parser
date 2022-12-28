package dev.javidmusayev.azqrtextparser.domains;

public class CRC16 {
    private static final int POLYNOMIAL = 0x1021;
    private static final int PRESET_VALUE = 0xFFFF;

    private int value;
    private String data;
    
    private CRC16(String data) {
        this.data = data;
        calcValue();
    }

    private void calcValue() {
        int current_crc_value = PRESET_VALUE;

        for (int i = 0; i < data.length(); i++) {
            int codePointAti = Character.codePointAt(data, i);
            current_crc_value ^= codePointAti << 8;

            for (int j = 0; j < 8; j++) {
                if ((current_crc_value & 0x8000) != 0) {
                    current_crc_value = (current_crc_value << 1) ^ POLYNOMIAL;
                } else {
                    current_crc_value = current_crc_value << 1;
                }
            }
        }

        value = current_crc_value & 0xFFFF;
    }

    public static CRC16 create(String data) {
        return new CRC16(data);
    }

    public int getValue() {
        return value;
    }
}
