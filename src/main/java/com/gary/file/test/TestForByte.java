package com.gary.file.test;

import com.gary.file.util.ByteAndStringUtil;

import java.util.Date;

/**
 * describe:测试byte2Int、Int2byte
 *
 * @author gary
 * @date 2018/12/27
 */
public class TestForByte {

	public static void main(String[] args) {
		byte[] buffer = new byte[16];

		ByteAndStringUtil.setIntAt(buffer, 0, 27);
		ByteAndStringUtil.setLongAt(buffer, 4, -11584858);
		ByteAndStringUtil.setIntAt(buffer, 12, -20);
		for (int index = 0; index < buffer.length; index ++) {
			System.out.print(buffer[index] + "!");
		}
		System.out.println();

		System.out.println(ByteAndStringUtil.toHex(buffer));

		int fileId = ByteAndStringUtil.getIntAt(buffer, 0);
		long offset = ByteAndStringUtil.getLongAt(buffer, 4);
		int length = ByteAndStringUtil.getIntAt(buffer, 12);

		System.out.println(fileId + ", " + offset
				+ ", " + length);


	}

}
