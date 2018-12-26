/**     
 * @author js   
 * @date 2018年12月17日   
 * @version 1.0   
 */ 
package com.wisdom.wy.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileInOut {

	public void fileWrite(String[] msg, String flag) {
		String filePath = "";
		String dataStr = "";
		if (flag.equals("1")) {
			filePath = "银行代收缴费记录.log";
			dataStr = "户号：" + msg[3] + "，缴费金额：" + msg[5] + "，流水号："
					+ msg[2] + "， 缴费时间：" + msg[4];
		}else {
			filePath = "银行代收作废记录.log";
			dataStr = "户号：" + msg[3] + "，作废金额：" + msg[6] + "，流水号："
					+ msg[2] + "，原交易流水号：" + msg[5] + "， 作废时间：" + msg[4];
		}
		File newDir=new File("D:/银行接口"); //声明磁盘目录
        File newFile = new File(newDir,filePath); //声明目录文件
		boolean newCreatDir=newDir.exists();
        boolean newCreatFile=newFile.exists();
		OutputStream os = null;
	try {
		if (!newCreatDir) {
			newDir.mkdirs();
			newFile.createNewFile();
		}
		os = new FileOutputStream(newFile, true);
		byte[] data = dataStr.getBytes(); // 将字符串转换为字节数组,方便下面写入

		os.write(data, 0, data.length); // 3、写入文件
		os.write("\r\n".getBytes());
		os.flush(); // 将存储在管道中的数据强制刷新出去
	} catch (FileNotFoundException e) {
		e.printStackTrace();
		System.out.println("文件没有找到！");
	} catch (IOException e) {
		e.printStackTrace();
		System.out.println("写入文件失败！");
	} finally {
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("关闭输出流失败！");
			}
		}
	}
	}
}
