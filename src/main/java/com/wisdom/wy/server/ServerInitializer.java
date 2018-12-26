/**     
 * @author js   
 * @date 2018年12月5日   
 * @version 1.0   
 */ 
package com.wisdom.wy.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// TODO Auto-generated method stub
		ChannelPipeline pipeline = ch.pipeline();

	      //数据分包，组包，粘包
		ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
		ch.pipeline().addLast(new LengthFieldPrepender(4));

		ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
		ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
	      System.out.println("from server");
	      ch.pipeline().addLast(new DiscardServerHandler());

	}
}
