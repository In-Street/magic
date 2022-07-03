package com.magic.lc.链表有环;

import com.magic.lc.反转链表.ListNode;

/**
 *
 * @author Cheng Yufei
 * @create 2022-06-20 22:46
 **/
public class Sloution {

    public static void main(String[] args) {
        ListNode node4 = new ListNode(4, null);

        ListNode node3 = new ListNode(3, node4);

        ListNode node2 = new ListNode(2, node3);
        //node4.setNext(node2);

        ListNode node1 = new ListNode(1, node2);


        ListNode slow = node1;
        ListNode fast = node1.next;

        while (slow != fast) {
            //奇：fast == null
            if (fast == null || fast.next ==null) {
                System.out.println(false);
                return;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        System.out.println(true);

    }

    private static void print(ListNode node) {

        if (node == null) {
            return;
        }
        System.out.print(node.getValue() + " -> ");
        print(node.next);
    }
}
