package edu.bupt.wangfu.module.topologyMgr.ospf;

/**
 * OSPF状态转移：
 *         down ------> init ------> two_way
 *
 * down：初始状态
 * init：收到Hello消息，等待双向连接建立
 * two_way：双向连接状态
 */

public enum State {
    down, init, two_way
}
