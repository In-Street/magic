package com.magic.lc.链表入环第一个节点;

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

        ListNode node1 = new ListNode(1, node2);

        node4.setNext(node3);

        //快慢指针从head起点开始移动
        //第一次相遇时，s走了k步，s=k 。f始终是s的2倍，f=2k
        //「逆时针方向」假设相遇处离环入口处距离=m，那么依据s=k，得到head到入口距离=k-m
        //「顺时针方向」假设相遇处到环入口距离为x，f走过的距离=(x+m)+m+(k-m) 【第一次相遇时，f已经走过一个环的长度=x+m 到入口处，再走m到达相遇处】
        //(x+m)+m+(k-m)=2k =>  x=2k-m-m-k+m=k-m ，x=k-m 说明第一次相遇处到入口的距离==head到入口处的距离
        //新指针从head移动速度与s相同，当两者相遇处为环入口点

        ListNode slow = node1;
        ListNode fast = node1;
        while (true) {
            if (fast==null || fast.next==null) {
                System.out.println("无环");
                return;
            }
            slow = slow.next;
            fast = fast.next.next;
            if (slow==fast) {
                break;
            }
        }
        fast = node1;
        while (fast != slow) {
            fast = fast.next;
            slow = slow.next;
        }
        System.out.println(fast.value);
    }

    private static void print(ListNode node) {

        if (node == null) {
            return;
        }
        System.out.print(node.getValue() + " -> ");
        print(node.next);
    }
}
