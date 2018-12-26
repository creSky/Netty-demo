package com.wisdom.wy.server;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wisdom.wy.util.DbUtil;
import com.wisdom.wy.util.FileInOut;
import com.wisdom.wy.util.StrLength;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * 服务端处理通道.这里只是打印一下请求的内容，并不对请求进行任何的响应 DiscardServerHandler 继承自
 * ChannelHandlerAdapter， 这个类实现了ChannelHandler接口， ChannelHandler提供了许多事件处理的接口方法，
 * 然后你可以覆盖这些方法。 现在仅仅只需要继承ChannelHandlerAdapter类而不是你自己去实现接口方法。
 *
 */
public class DiscardServerHandler extends ChannelHandlerAdapter {
	private static String cons_id;
/*	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
		
		System.out.println("新连接"+ctx.name());
		
		
		final ByteBuf time = ctx.alloc().buffer(4); // (2)
		
		int i = (int)(System.currentTimeMillis() / 1000L + 2208988800L);
        time.writeInt(i);
        
        
        
        System.out.println(i);
        final ChannelFuture f = ctx.writeAndFlush(time); // (3)
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                System.out.println(future.isSuccess());
            }
        }); // (4)
	}
	
	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		// TODO Auto-generated method stub
		super.close(ctx, promise);
		System.out.println("关闭"+ctx.name()+" "+promise.toString());
	}
	
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		// TODO Auto-generated method stub
		super.write(ctx, msg, promise);
		
		System.out.println("write:" + msg);
	}
	*/
	
    /**
     * 这里我们覆盖了chanelRead()事件处理方法。 每当从客户端收到新的数据时， 这个方法会在收到消息时被调用，
     * 这个例子中，收到的消息的类型是ByteBuf
     * 
     * @param ctx
     *            通道处理的上下文信息
     * @param msg
     *            接收的消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        try {
            ByteBuf in = (ByteBuf) msg;
            String errCode="";
            String str2="";
            // 打印客户端输入，传输过来的的字符
            System.out.print(in.toString(CharsetUtil.UTF_8));
            String str1=in.toString(CharsetUtil.UTF_8);
            String[] str=str1.split("\\|");
            int length=Integer.parseInt( str[0].substring(4));
            System.out.println(",报文长度："+in.toString(CharsetUtil.UTF_8).length());
            if(length==in.toString(CharsetUtil.UTF_8).length()&&in!=null) {
            	errCode="00";
            	switch(str[1]) {
            	case "10":
            		str2=userQuery(str);
            		break;
            	case "11":
            		str2=userRegister(str);
            		break;
            	case "12":
            		str2=payMoney(str);
            		break;
            	case "13":
            		str2=checkBill(str);
            		break;
            	case "14":
            		str2=backMoney(str);
            		break;
            	}
            	channelWrite(ctx,str2);
            	System.out.println(ctx.channel().remoteAddress() + ",to client:" + str2);
            }else {
            	errCode="87";
            }
        } finally {
            /**
             * ByteBuf是一个引用计数对象，这个对象必须显示地调用release()方法来释放。
             * 请记住处理器的职责是释放所有传递到处理器的引用计数对象。
             */
            // 抛弃收到的数据
            ReferenceCountUtil.release(msg);
        }

    }
    /*返回信息*/
    public void channelWrite(ChannelHandlerContext ctx, String msg) {
    	//[-26, -120, -112, -27, -118, -97]
    	ByteBuf out=ctx.alloc().buffer(128);
    	byte[] b=msg.getBytes();
    	for(int i=0;i<b.length;i++) {
    		out.writeByte(b[i]);
    	}
      	ctx.write(out); // (1)
        ctx.flush(); // (2)
    }
    /***
     * 用户信息查询
     */
    public String userQuery(String[] msg) {
    	DbUtil dbUtil = new DbUtil();
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    	String str="";
    	String head="";
    	String errCode="81";
    	String errMsg="一体化系统中无该用户档案";
    	String consNo=msg[3];
    	String consName="";
    	String balance = "";
    	String tripLimit = "";
    	list = dbUtil.queryForList(consNo);
    	SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	String nowTime = format.format(new Date());
    	if (list.size()>0) {
    		for (int i = 0; i < list.size(); i++) {
				Map<String, Object> entity = list.get(i);
				consName = entity.get("CONS_NAME").toString();
				cons_id = entity.get("CONS_ID").toString();
				balance = entity.get("BALANCE")==null?"0":entity.get("BALANCE").toString();
				tripLimit = entity.get("TRIP_LIMIT")==null?"":entity.get("TRIP_LIMIT").toString();
    		}
    		errCode="00";
    		errMsg="成功";
    		str="|"+msg[1]+"|"+errCode+"|"+errMsg+"|"+msg[2]+"|"+consNo+"|"
    				+consName+"|"+balance+"|"+tripLimit+"|"+nowTime+"|";
		}else {
			str="|"+msg[1]+"|"+errCode+"|"+errMsg+"|"+msg[2]+"|";
		}
    	System.out.println(str.length());
    	head = new StrLength().head(str.length());
    	str = head+str;
    	return str;
    }
    /***
     * 用户绑定
     */
    public String userRegister(String[] msg) {
    	String str="";
    	return str;
    }
    /***
     * 缴 费
     */
    public String payMoney(String[] msg) {
    	DbUtil dbUtil = new DbUtil();
    	String str="";
    	String head="";
    	String errCode="87";
    	String errMsg="缴费失败";
    	String consNo=msg[3];
    	String factMoney=msg[5]==null?"0":msg[5];
    	if (Double.parseDouble(factMoney)<=0) {
			str = "|" + msg[1] + "|" + "83" + "|" + "缴费金额为0" + "|" + msg[2] + "|";
		}else {
			if (dbUtil.executeSql(consNo, factMoney)) {//缴费成功
				str = "|" + msg[1] + "|" + "00" + "|" + "缴费成功" + "|" + msg[2] + "|";
				dbUtil.recordSql(msg,cons_id);
				new FileInOut().fileWrite(msg, "1");//保存日志
			}else {
				str = "|" + msg[1] + "|" + errCode + "|" + errMsg + "|" + msg[2] + "|";
			}
		}
    	System.out.println(str.length());
    	head = new StrLength().head(str.length());
    	str = head+str;
    	return str;
    }
    
    /**
     * 对账
     */
    
    public String checkBill(String[] msg) {
    	String str = "";
    	String head = "";
    	File file = new File(msg[2]);
    	if (file.exists()) {
			str = "|" + msg[1] + "|" + "00" + "|" + "成功" + "|";
		}else {
			str = "|" + msg[1] + "|" + "88" + "|" + "该文件不存在" + "|";
		}
    	System.out.println(str.length());
    	head = new StrLength().head(str.length());
    	str = head+str;
    	return str;
    }
    
    /***
     * 抹账
     */
    public String backMoney(String[] msg) {
    	DbUtil dbUtil = new DbUtil();
    	String str="";
    	String head="";
    	String consNo=msg[3];
    	String factMoney=msg[6]==null?"0":msg[6];
    	if (Double.parseDouble(factMoney)<=0) {
			
		}else {
			factMoney = "-"+factMoney;
			if (dbUtil.executeSql(consNo, factMoney)) {//作废成功
				str = "|" + msg[1] + "|" + "00" + "|" + "成功" + "|" + msg[2] + "|";
				dbUtil.deleteSql(msg);
				new FileInOut().fileWrite(msg, "2");//保存日志
			}else {
			}
		}
    	head = new StrLength().head(str.length());
    	str = head+str;
    	return str;
    }
    /***
     * 这个方法会在发生异常时触发
     * 
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        /**
         * exceptionCaught() 事件处理方法是当出现 Throwable 对象才会被调用，即当 Netty 由于 IO
         * 错误或者处理器在处理事件时抛出的异常时。在大部分情况下，捕获的异常应该被记录下来 并且把关联的 channel
         * 给关闭掉。然而这个方法的处理方式会在遇到不同异常的情况下有不 同的实现，比如你可能想在关闭连接之前发送一个错误码的响应消息。
         */
        // 出现异常就关闭
        cause.printStackTrace();
        ctx.close();
    }

}
