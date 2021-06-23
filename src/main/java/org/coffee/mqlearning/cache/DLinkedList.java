package org.coffee.mqlearning.cache;

import lombok.ToString;

@ToString
public class DLinkedList<E> {

	private Node head, tail;

	// private LinkedList<String> list = new LinkedList<>();

	public DLinkedList() {
		this.head = this.tail = null;
	}

	public void addToTail(E e) {
		if (e == null) {
			throw new NullPointerException();
		}
		final Node t = tail;
		final Node newNode = new Node(t, e, null);
		tail = newNode;
		if (t == null)
			head = newNode;
		else
			t.next = newNode;
	}

	public void addToHead(E e) {
		if (e == null) {
			throw new NullPointerException();
		}
		final Node h = head;
		final Node newNode = new Node(null, e, h);
		head = newNode;
		if (h == null)
			tail = newNode;
		else
			h.prev = newNode;

	}

	public boolean isEmpty() {
		boolean isEmpty = (head == null);
		return isEmpty;
	}

	class Node {
		E item;

		Node prev, next;

		Node(E e) {
			this.item = e;
			this.prev = this.next = null;
		}

		Node(Node pre, E e, Node next) {
			this.item = e;
			this.prev = pre;
			this.next = next;
		}

		@Override
		public String toString() {
			return String.format("Node:[prev:%s,item:%s,next:%s]", prev == null ? "NULL" : prev.item, item,
					next == null ? "NULL" : next.item);
		}

	}

	public static void main(String[] args) {
		DLinkedList<String> lls = new DLinkedList<>();
		/*
		lls.addToHead("123");
		lls.addToHead("456");
		lls.addToHead("789");
		*/
		lls.addToTail("123");
		lls.addToTail("456");
		lls.addToTail("789");
		System.out.println(lls);
	}

}
