package com.bx.imclient.sender;

import com.bx.common.contant.RedisKey;
import com.bx.common.enums.IMCmdType;
import com.bx.common.enums.ListenerType;
import com.bx.common.enums.SendResultType;
import com.bx.common.model.im.GroupMessageInfo;
import com.bx.common.model.im.IMRecvInfo;
import com.bx.common.model.im.PrivateMessageInfo;
import com.bx.common.model.im.SendResult;
import com.bx.imclient.listener.MessageListenerMulticaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IMSender {

    @Autowired
    @Qualifier("IMRedisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    private MessageListenerMulticaster listenerMulticaster;

    public void sendPrivateMessage(Long recvId, PrivateMessageInfo... messageInfos){
        // 获取对方连接的channelId
        String key = RedisKey.IM_USER_SERVER_ID + recvId;
        Integer serverId = (Integer) redisTemplate.opsForValue().get(key);
        // 如果对方在线，将数据存储至redis，等待拉取推送
        if (serverId != null) {
            String sendKey = RedisKey.IM_UNREAD_PRIVATE_QUEUE + serverId;
            IMRecvInfo[] recvInfos = new IMRecvInfo[messageInfos.length];
            for (int i=0;i<messageInfos.length;i++){
                IMRecvInfo<PrivateMessageInfo> recvInfo = new IMRecvInfo<>();
                recvInfo.setCmd(IMCmdType.PRIVATE_MESSAGE.getCode());
                List recvIds = new LinkedList();
                recvIds.add(recvId);
                recvInfo.setRecvIds(recvIds);
                recvInfo.setData(messageInfos[i]);
                recvInfos[i] = recvInfo;
            }
            redisTemplate.opsForList().rightPushAll(sendKey, recvInfos);
        }else{
            // 回复消息状态
            for(PrivateMessageInfo messageInfo : messageInfos ) {
                SendResult result = new SendResult();
                result.setMessageInfo(messageInfo);
                result.setRecvId(recvId);
                result.setResult(SendResultType.FAIL);
                result.setFailReason("用户不在线");
                listenerMulticaster.multicast(ListenerType.PRIVATE_MESSAGE, result);
            }
        }
    }

    public void sendGroupMessage(List<Long> recvIds, GroupMessageInfo... messageInfos){
        // 根据群聊每个成员所连的IM-server，进行分组
        List<Long> offLineIds = Collections.synchronizedList(new LinkedList<Long>());
        Map<Integer, List<Long>> serverMap = new ConcurrentHashMap<>();
        recvIds.parallelStream().forEach(id->{
            String key = RedisKey.IM_USER_SERVER_ID + id;
            Integer serverId = (Integer)redisTemplate.opsForValue().get(key);
            if(serverId != null){
                if(serverMap.containsKey(serverId)){
                    serverMap.get(serverId).add(id);
                }else {
                    // 此处需要加锁，否则list可以会被覆盖
                    synchronized(serverMap){
                        List<Long> list = Collections.synchronizedList(new LinkedList<Long>());
                        list.add(id);
                        serverMap.put(serverId,list);
                    }
                }
            }else{
                offLineIds.add(id);
            }
        });
        // 逐个server发送
        for (Map.Entry<Integer,List<Long>> entry : serverMap.entrySet()) {
            IMRecvInfo[] recvInfos = new IMRecvInfo[messageInfos.length];
            for (int i=0;i<messageInfos.length;i++){
                IMRecvInfo<GroupMessageInfo> recvInfo = new IMRecvInfo<>();
                recvInfo.setCmd(IMCmdType.GROUP_MESSAGE.getCode());
                recvInfo.setRecvIds(new LinkedList<>(entry.getValue()));
                recvInfo.setData(messageInfos[i]);
                recvInfos[i] = recvInfo;
            }
            String key = RedisKey.IM_UNREAD_GROUP_QUEUE +entry.getKey();
            redisTemplate.opsForList().rightPushAll(key,recvInfos);
        }
        // 不在线的用户，回复消息状态
        for(GroupMessageInfo messageInfo:messageInfos ){
            for(Long id : offLineIds){
                // 回复消息状态
                SendResult result = new SendResult();
                result.setMessageInfo(messageInfo);
                result.setRecvId(id);
                result.setResult(SendResultType.FAIL);
                result.setFailReason("用户不在线");
                listenerMulticaster.multicast(ListenerType.GROUP_MESSAGE,result);
            }
        }
    }
}
