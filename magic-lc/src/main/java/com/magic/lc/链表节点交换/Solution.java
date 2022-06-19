package com.magic.lc.链表节点交换;


import com.magic.lc.反转链表.ListNode;
import javafx.beans.binding.ListBinding;

import java.util.List;

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

        //print(node1);

        System.out.println();
        // 1 2 3 4
        // 2 1 4 3

        ListNode result = new ListNode();
        result.next = node1;
        ListNode tmp = result;

        //指针迭代
        while (tmp.next != null && tmp.next.next != null) {
            System.out.println("开始");
            ListNode n1 = tmp.next;
            ListNode n2 = tmp.next.next;
            n1.next = n2.next;
            print(result.next);
            System.out.println("=======");

            n2.next = n1;
            print(result.next);
            System.out.println("=======");

            tmp.next=n2;
            print(result.next);
            System.out.println("=======");

            tmp = n1;
            print(result.next);
            System.out.println("=======");

        }

        //递归
        //recursion(node1);

        //print(result.next);

    }

    public static void print(ListNode node) {

        if (node == null) {
            return;
        }
        System.out.print(node.getValue() + " -> ");
        print(node.getNext());
    }

    public static ListNode recursion(ListNode head) {

        if (head == null || head.next == null) {
            return head;
        }

        ListNode tmp = head.next;
        head.next = recursion(tmp.next);
        tmp.next = head;
        return tmp;
    }

}
