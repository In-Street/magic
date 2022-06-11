package com.magic.lc.反转链表;


/**
 *
 * @author Cheng Yufei
 * @create 2022-06-09 21:59
 **/
public class Solution {

    public static void main(String[] args) {
        ListNode node4 = new ListNode(4, null);

        ListNode node3 = new ListNode(3, node4);

        ListNode node2 = new ListNode(2, node3);

        ListNode node1 = new ListNode(1, node2);

        print(node1);

        System.out.println();
        /**
         * 1->2->3->4->null
         * null<-1<-2<-3<-4
         */

        ListNode pre = null;
        ListNode cur = node1;
        ListNode temp = null;

        // pre  cur-> cur.next（temp）
        while (cur != null) {
            temp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = temp;
        }
        print(node4);

    }

    public static void print(ListNode node) {

        if (node == null) {
            return;
        }
        System.out.print(node.getValue()+" -> ");
        print(node.getNext());
    }

}
