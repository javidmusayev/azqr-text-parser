package dev.javidmusayev.azqrtextparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import dev.javidmusayev.azqrtextparser.contollers.ParseController;

@SpringBootTest
class AzqrTextParserApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testTlvCreationSuccessLesstThanTen() throws Exception {
		ParseController controller = new ParseController();
		TLV tlv = controller.parseAsTlv("0109ÜĞÖəıl.şç");
		assertEquals("01", tlv.getTag());
		assertEquals(9, tlv.getLength());
		assertEquals("ÜĞÖəıl.şç", tlv.getValue());
	}

	@Test
	void testTlvCreationSuccessMoreThanTen() throws Exception {
		ParseController controller = new ParseController();
		TLV tlv = controller.parseAsTlv("0119ÜĞÖəıl.şç1234567890");
		assertEquals("01", tlv.getTag());
		assertEquals(19, tlv.getLength());
		assertEquals("ÜĞÖəıl.şç1234567890", tlv.getValue());
	}

	@Test
	void testTlvCreationFailLessThanTen() throws Exception {
		ParseController controller = new ParseController();
		assertThrows(IllegalArgumentException.class, () -> controller.parseAsTlv("0102ÜĞÖəıl.şç"));
	}

	@Test
	void testTlvCreationFailMoreThanTen() throws Exception {
		ParseController controller = new ParseController();
		assertThrows(IllegalArgumentException.class, () -> controller.parseAsTlv("0112ÜĞÖəıl.şç"));
	}

}
