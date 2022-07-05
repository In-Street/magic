package com.magic.lc.k个一组翻转;

import com.magic.lc.反转链表.ListNode;

/**
 *
 * @author Cheng Yufei
 * @create 2022-07-04 21:42
 **/
public class Solution {

    public static void main(String[] args) {
        ListNode node4 = new ListNode(4, null);

        ListNode node3 = new ListNode(3, node4);

        ListNode node2 = new ListNode(2, node3);

        ListNode node1 = new ListNode(1, node2);

        // 1 -> 2 -> 3 ->4
        int k = 2;
        ListNode result = new ListNode();
        result.next = node1;
        ListNode pre = result;
        while (node1 != null) {
            ListNode tail = pre;
            for (int i = 0; i < k; i++) {
                tail = tail.next;
                if (tail == null) {
                    print(result);
                    return;
                }
            }
            ListNode next = tail.next;
            ListNode[] reverse = reverse(node1, tail);
            pre.next = reverse[0];
            reverse[1].next = next;
            pre = tail;
            node1 = next;

        }
        print(result);

    }

    public static ListNode[] reverse(ListNode head, ListNode tail) {
        ListNode pre = null;
        ListNode tailNext = tail.next;
        while (head != tailNext) {
            ListNode tmp = head.next;
            head.next = pre;
            pre = head;
            head = tmp;
        }
        print(pre);
        return new ListNode[]{tail, head};
    }

    public static void print(ListNode node) {
        if (node == null) {
            return;
        }
        System.out.println(node.getValue());
        print(node.next);

    }

}
