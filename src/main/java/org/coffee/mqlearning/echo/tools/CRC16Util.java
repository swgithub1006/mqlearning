package org.coffee.mqlearning.echo.tools;

/**
 * 
 * CRC16算法java实现 模仿C++代码改写的Java实现
 * 
 * @author night knight http://nightknight.iteye.com/blog/317894
 *
 */
public class CRC16Util {

	static CRC16 crc16 = new CRC16();

	private CRC16Util() {
	}

	public static byte[] short2bytes(short s) {
		byte[] bytes = new byte[2];
		for (int i = 1; i >= 0; i--) {
			bytes[i] = (byte) (s % 256);
			s >>= 8;
		}
		return bytes;
	}

	public static short bytes2short(byte[] bytes) {
		short s = (short) (bytes[1] & 0xFF);
		s |= (bytes[0] << 8) & 0xFF00;
		return s;
	}

	/*
	 * 获取crc校验的byte形式
	 */
	public static byte[] crc16Bytes(byte[] data) {
		return short2bytes(crc16Short(data));
	}

	/*
	 * 获取crc校验的short形式
	 */
	public static short crc16Short(byte[] data) {
		return crc16.getCrc(data);
	}

	public static void main(String[] args) {
		byte[] test = new byte[] { 0, 1, 2, 3, 4 };
		byte[] crc = crc16Bytes(test);

		byte[] testc = new byte[test.length + 2];
		for (int i = 0; i < test.length; i++) {
			testc[i] = test[i];
		}
		testc[test.length] = crc[0];
		testc[test.length + 1] = crc[1];

		System.out.println(crc16Short(testc));
	}

}

class CRC16 {

	private short[] crcTable = new short[256];

	private int gPloy = 0x1021; // 生成多项式

	public CRC16() {
		computeCrcTable();
	}

	private short getCrcOfByte(int aByte) {
		int value = aByte << 8;

		for (int count = 7; count >= 0; count--) {
			if ((value & 0x8000) != 0) { // 高第16位为1，可以按位异或
				value = (value << 1) ^ gPloy;
			} else {
				value = value << 1; // 首位为0，左移
			}

		}
		value = value & 0xFFFF; // 取低16位的值
		return (short) value;
	}

	/*
	 * 生成0 - 255对应的CRC16校验码
	 */
	private void computeCrcTable() {
		for (int i = 0; i < 256; i++) {
			crcTable[i] = getCrcOfByte(i);
		}
	}

	public short getCrc(byte[] data) {
		int crc = 0;
		int length = data.length;
		for (int i = 0; i < length; i++) {
			crc = ((crc & 0xFF) << 8) ^ crcTable[(((crc & 0xFF00) >> 8) ^ data[i]) & 0xFF];
		}
		crc = crc & 0xFFFF;
		return (short) crc;
	}
}
