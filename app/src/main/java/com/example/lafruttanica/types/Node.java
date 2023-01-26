package com.example.lafruttanica.types;

import androidx.annotation.NonNull;

public class Node<T> {
    private T value;
    private Node<T> next;

    public Node(T value, Node<T> next){
        this.value = value;
        this.next = next;
    }

    public Node(T value){
        this.value = value;
        this.next = null;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue(){
        return value;
    }

    public Node<T> getNext(){
        return next;
    }

    @NonNull
    public String toString(){
        return value + "-->" + next;
    }
}
