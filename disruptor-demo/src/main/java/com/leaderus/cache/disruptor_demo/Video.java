package com.leaderus.cache.disruptor_demo;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Video {
	
	//rtmp://4968.livepush.myqcloud.com/live/4968_e756612558?bizid=4968&txSecret=9c8cb3b44a694a1cb72e5f3747b03ec9&txTime=5812247F
	//rtmp://4968.livepush.myqcloud.com/live/4968_11212122?bizid=4968txSecret=2133c689c452f0bf81b1a88261f1d9a6&txTime=579ACB15

    public static void main(String[] args) {
    	String userId = "11212122";
    	long expireTime = 1469762325L;
//        System.out.println(getSafeUrl("txrtmp", userId, 1469762325L));
        System.out.println(getRtmpUrl(userId, expireTime));
        System.out.println(getLiveCode(userId));
        System.out.println(getPlayUrl(getLiveCode(userId)));
        
    }
    
    private static String getPlayUrl(String liveCode) {
    	StringBuffer stringBuffer = new StringBuffer("rtmp://");
    	stringBuffer.append(bizId).append(".liveplay.myqcloud.com/live/").append(liveCode);
		return stringBuffer.toString();
	}

	private static final String bizId = "4968";
    
    /**
     * 获取直播码
     * @param userId
     * @return
     */
    private static String getLiveCode(String userId) {
		return bizId + "_" + userId;
	}

    /**
     * 获取推流url
     * 
     * @param id 客户ID
     * @param txTime 当前推流url过期时间 [unix时间戳]
     * @return
     */
    private static String getRtmpUrl(String id, long txTime) {//id usually is telephone number .
    	StringBuffer stringBuffer = new StringBuffer("rtmp://");
    	stringBuffer.append(bizId).append(".livepush.myqcloud.com/live/").append(bizId).append("_")
    	.append(id).append("?bizid=").append(bizId).append(getSafeUrl("txrtmp", id, txTime));
		return stringBuffer.toString();
	}

	private static final char[] DIGITS_LOWER =
        {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	/**
	 * 获取播放地址
	 * @param bizId 您在腾讯云分配到的bizid
	 *        streamId 您用来区别不通推流地址的唯一id
	 * @return String url
	 */
	
    /*
     * KEY+ stream_id + txTime
     */
    private static String getSafeUrl(String key, String streamId, long txTime) {
        String input = new StringBuilder().
                append(key).
                append(streamId).
                append(Long.toHexString(txTime).toUpperCase()).toString();

        String txSecret = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            txSecret  = byteArrayToHexString(
                    messageDigest.digest(input.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return txSecret == null ? "" :
            new StringBuilder().
                append("txSecret=").
                append(txSecret).
                append("&").
                append("txTime=").
                append(Long.toHexString(txTime).toUpperCase()).
                toString();
    }

    private static String byteArrayToHexString(byte[] data) {
        char[] out = new char[data.length << 1];

        for (int i = 0, j = 0; i < data.length; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
    }
}

