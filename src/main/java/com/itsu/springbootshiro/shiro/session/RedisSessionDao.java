package com.itsu.springbootshiro.shiro.session;

import com.itsu.springbootshiro.util.RedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 苏犇
 * @date 2019/6/30 2:00
 */

public class RedisSessionDao extends AbstractSessionDAO {

    @Resource
    private RedisUtil redisUtil;

    public static final String KEY_PREFIX = "shiro:session:";

    public void saveSession(Session session) {
        if (session != null && session.getId() != null) {
            redisUtil.set(getKey(session.getId().toString()), SerializationUtils.serialize(session));
            redisUtil.expir(getKey(session.getId().toString()), 1800);
        }
    }

    private String getKey(String key) {
        return KEY_PREFIX + key;
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = super.generateSessionId(session);
        super.assignSessionId(session, sessionId);
        saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            return null;
        }
        String key = getKey(sessionId.toString());
        byte[] sessionByts = redisUtil.get(key);
        Session session = (Session) SerializationUtils.deserialize(sessionByts);
        return session;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    @Override
    public void delete(Session session) {
        if (session != null && session.getId() != null) {
            redisUtil.del(getKey(session.getId().toString()));
        }
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<String> keys = redisUtil.keys(KEY_PREFIX);
        Set<Session> sessions = new HashSet<>();
        keys.forEach(key -> {
            Session session = (Session) SerializationUtils.deserialize(redisUtil.get(key));
            sessions.add(session);
        });
        return sessions;
    }
}
