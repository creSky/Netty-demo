/**     
 * @author js   
 * @date 2018年12月5日   
 * @version 1.0   
 */
package com.wisdom.wy.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

public class DiscardClient {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new DiscardClient().connect("localhost", 9234);
	}

	public void connect(String addr,int port) throws IOException {
		NioEventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).handler(new ClientInitializer());
			ChannelFuture future = b.connect(addr, port).sync();
			Channel channel = future.channel();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, CharsetUtil.UTF_8));
			while(true) {
				String sendMsg = reader.readLine() ;
                if ("esc".equals(sendMsg)) {
                    channel.close();
                    break;
                }

                sendMsg += "\r\n";
                channel.writeAndFlush(sendMsg);
                System.out.println(sendMsg);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			group.shutdownGracefully();
		}
	}
}
