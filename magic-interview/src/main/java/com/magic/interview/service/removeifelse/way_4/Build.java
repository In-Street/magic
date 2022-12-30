package com.magic.interview.service.removeifelse.way_4;

import java.util.Objects;

/**
 *
 * @author Cheng Yufei
 * @create 2022-12-29 17:14
 **/
public class Build {

    private AbstractHandler head;
    private AbstractHandler tail;

    /**
     * 构造处理链： A->B->C
     * @param handler
     * @return
     */
    public Build add(AbstractHandler handler) {
        if (Objects.isNull(head)) {
            head = tail = handler;
            return this;
        }
        tail.nextHandler = handler;
        tail = handler;
        return this;
    }

    /**
     * 返回最后构造完的处理链
     * @return
     */
    public AbstractHandler build() {
        return head;
    }
}
