/**     
 * @author js   
 * @date 2018年12月21日   
 * @version 1.0   
 */ 
package com.wisdom.wy.util;

public class StrLength {

	public String head(int len) {
		String head = "";
		if (len<2) {
    		head = "0000000" + (len+8);
		}else if (len<92&&len>1) {
			head = "000000" + (len+8);
		}else if (len<992&&len>91) {
			head = "00000" + (len+8);
		}
		return head;
	}
}
