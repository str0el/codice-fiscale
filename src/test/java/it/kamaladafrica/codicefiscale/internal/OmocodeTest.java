package it.kamaladafrica.codicefiscale.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.primitives.ImmutableIntArray;

public class OmocodeTest {

	@Test
	public void test_of() {
		assertTrue(Omocode.of(new int[0]).isSameLevel(Omocode.of(ImmutableIntArray.of())));
	}

	@Test
	public void test_apply() {
		String[] res = { "X00XL", "X0LX0", "X0LXL", "XL0X0", "XL0XL", "XLLX0", "XLLXL" };
		for (int i = 0; i < 7; i++) {
			assertEquals(res[i], Omocode.of(i + 1, new int[] { 1, 2, 4 }).apply("X00X0"));
		}
	}

	@Test
	public void test_ofValue() {
		String[] res = { "X00XL", "X0LX0", "X0LXL", "XL0X0", "XL0XL", "XLLX0", "XLLXL" };
		for (int i = 0; i < 7; i++) {
			assertEquals(i + 1, Omocode.of(res[i], new int[] { 1, 2, 4 }).getLevel());
		}
	}

	@Test
	public void test_new_throws() {
		assertThrows(NullPointerException.class, () -> Omocode.of(0, (int[]) null));
		assertThrows(IllegalArgumentException.class, () -> Omocode.of(1, new int[] {}));
		assertThrows(IllegalArgumentException.class, () -> Omocode.of(2, new int[] { 1 }));
		assertThrows(IllegalArgumentException.class, () -> Omocode.of(4, new int[] { 1, 2 }));
		assertThrows(IllegalArgumentException.class, () -> Omocode.of(-1, new int[] { 1, 2, 3 }));
	}

	@Test
	public void test_getMaxLevel() {
		assertEquals(0, Omocode.of(0, new int[] {}).getMaxLevel());
		assertEquals(1, Omocode.of(0, new int[] { 0 }).getMaxLevel());
		assertEquals(3, Omocode.of(0, new int[] { 0, 0 }).getMaxLevel());
		assertEquals(7, Omocode.of(0, new int[] { 0, 0, 0 }).getMaxLevel());
	}

	@Test
	public void testNormalize() {
		String[] res = { "X00XL", "X0LX0", "X0LXL", "XL0X0", "XL0XL", "XLLX0", "XLLXL" };
		for (int i = 0; i < 7; i++) {
			assertEquals("X00X0", Omocode.of(i + 1, new int[] { 1, 2, 4 }).normalize(res[1]));
		}
	}

	@Test
	public void test_isSameLevel() {
		assertTrue(Omocode.of(0, new int[0]).isSameLevel(Omocode.of(0, new int[0])));
		assertTrue(Omocode.of(1, new int[1]).isSameLevel(Omocode.of(1, new int[1])));
		assertFalse(Omocode.of(1, new int[1]).isSameLevel(Omocode.of(1, new int[2])));
		assertFalse(Omocode.of(1, new int[] { 1 }).isSameLevel(Omocode.of(1, new int[] { 2 })));
	}

	@Test
	public void test_isLevel() {
		assertTrue(Omocode.of(0, new int[0]).isLevel(0));
		assertTrue(Omocode.of(1, new int[1]).isLevel(1));
		assertFalse(Omocode.of(0, new int[1]).isLevel(1));
		assertFalse(Omocode.of(2, new int[] { 1, 2 }).isLevel(1));
	}

	@Test
	public void test_apply_IndexOutOfBound() {
		Omocode omocode = Omocode.of(1, new int[] { 0, 1, 4 });
		assertThrows(IndexOutOfBoundsException.class, () -> omocode.apply("A12"));
	}

	@Test
	public void test_normalize_IndexOutOfBound() {
		Omocode omocode = Omocode.of(1, new int[] { 0, 1, 4 });
		assertThrows(IndexOutOfBoundsException.class, () -> omocode.normalize("AM2"));
	}

	@Test
	public void test_omocodeCharToDigit() {
		assertEquals('1', Omocode.omocodeCharToDigit('M'));
		assertEquals('1', Omocode.omocodeCharToDigit('1'));
	}

	@Test
	public void testToOmocodeChar() {
		assertEquals('M', Omocode.digitToOmocodeChar('1'));
		assertEquals('M', Omocode.digitToOmocodeChar('M'));
	}

}
