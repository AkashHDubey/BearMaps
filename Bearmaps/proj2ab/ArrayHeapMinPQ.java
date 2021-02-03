package bearmaps.proj2ab;

import edu.princeton.cs.algs4.Stopwatch;

import java.util.*;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T>{

    private class PriorityNode implements Comparable<PriorityNode> {
        private T item;
        private double priority;

        PriorityNode(T e, double p) {
            this.item = e;
            this.priority = p;
        }

        T getItem() {
            return item;
        }

        double getPriority() {
            return priority;
        }

        void setPriority(double priority) {
            this.priority = priority;
        }

        @Override
        public int compareTo(PriorityNode other) {
            if (other == null) {
                return -1;
            }
            return Double.compare(this.getPriority(), other.getPriority());
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (o == null || o.getClass() != this.getClass()) {
                return false;
            } else {
                return ((PriorityNode) o).getItem().equals(getItem());
            }
        }

        @Override
        public int hashCode() {
            return item.hashCode();
        }
    }

    public List<PriorityNode> queue ;
    private Map<T,Integer> indexTree;

    public ArrayHeapMinPQ(){
        this.queue = new ArrayList<>();
        this.indexTree = new HashMap<>();
    }

    @Override
    public void add(T item, double priority) {

        if(indexTree.containsKey(item)){
            throw new IllegalArgumentException("Item already present!!");
        }

        PriorityNode newNode = new PriorityNode(item,priority);
        this.queue.add(newNode);

        int indexOfNewItem = this.queue.size() - 1;
        int parentOfNewNode = this.parent(indexOfNewItem);
        this.indexTree.put(item,indexOfNewItem);
        this.swim(indexOfNewItem,parentOfNewNode);

//        while (parentOfNewNode >= 0 && this.queue.get(indexOfNewItem).compareTo(this.queue.get(parentOfNewNode)) < 0)
//        {
//            this.swap(indexOfNewItem,parentOfNewNode);
//            indexOfNewItem = parentOfNewNode;
//            parentOfNewNode = this.parent(indexOfNewItem);
//        }

    }
    @Override
    public boolean contains(T item) {
        return this.indexTree.containsKey(item);
    }

    @Override
    public T getSmallest() {
        if(this.size() == 0){
            throw new NoSuchElementException();
        }
        return this.queue.get(0).item;
    }

    @Override
    public T removeSmallest() {

        if(this.size() == 0){
            throw new NoSuchElementException();
        }

        T smallest = this.getSmallest();
        PriorityNode previousLast = this.queue.remove(this.size()-1);
        if(this.size() == 0){
            return smallest;
        }
        this.queue.set(0,previousLast);
        this.indexTree.remove(smallest);
        this.sink(0);
        return smallest;
    }

    @Override
    public int size() {
        return this.queue.size();
    }

    @Override
    public void changePriority(T item, double priority) {

        if(!this.indexTree.containsKey(item)){
            throw new NoSuchElementException();
        }

        int index = this.indexTree.get(item);
        this.queue.get(index).setPriority(priority);
        this.swim(index,parent(index));
        this.sink(index);

    }

    private void sink(int index){

        int leftChildIndex = this.leftChild(index);

        if(leftChildIndex >= this.size()){
            return;
        }

        int compareToLeftChild = this.queue.get(index).compareTo(this.queue.get(leftChildIndex));
        int rightChildIndex = this.rightChild(index);

        if(rightChildIndex >= this.size()){

            if(compareToLeftChild > 0){
                swap(index,leftChildIndex);
                sink(leftChildIndex);
            }
            return;
        }

        PriorityNode rightChild = this.queue.get(rightChildIndex);
        PriorityNode leftChild = this.queue.get(leftChildIndex);

        int compareToRightChild = this.queue.get(index).compareTo(rightChild);
        if(compareToLeftChild < 0 && compareToRightChild < 0){
            return;
        }

        int compareOfLeftToRight = leftChild.compareTo(rightChild);
        if(compareOfLeftToRight > 0){
            swap(index,rightChildIndex);
            sink(rightChildIndex);
        }

        else {
            swap(index,leftChildIndex);
            sink(leftChildIndex);
        }
    }

    private void swim(int index,int parentIndex){

        if(parentIndex < 0 || this.queue.get(index).compareTo(this.queue.get(parentIndex)) >= 0){
            return;
        }
        this.swap(index,parentIndex);
        index = parentIndex;
        parentIndex = this.parent(index);
        swim(index,parentIndex);

    }

    private void swap(int index1,int index2){

        PriorityNode temp1 = this.queue.get(index1);
        PriorityNode temp2 = this.queue.get(index2);

        this.queue.set(index1,temp2);
        this.queue.set(index2,temp1);

        this.indexTree.put(temp1.getItem(),index2);
        this.indexTree.put(temp2.getItem(),index1);

    }

    private int parent(int k){

        if( k == 0){
            return 0;
        }

        return (k-1)/2;
    }

    private int leftChild(int k){
        return 2*k + 1 ;
    }

    private int rightChild(int k){
        return 2*k + 2 ;
    }

    public static void main(String[] args) {
//        ArrayHeapMinPQ<String> AHM = new ArrayHeapMinPQ<>();
//        AHM.add("Hello",45);
//        AHM.add("hello",44);
//        AHM.add("HeLLlo",40);
//        AHM.add("holo",100);
//        AHM.add("Eello",3);
//        AHM.add("H",44);
//        AHM.changePriority("Eello",150);
//        AHM.changePriority("H",1);
//
//        System.out.println(AHM.contains("H"));
//        System.out.println(AHM.contains("HellO"));

//        NaiveMinPQ<Double> NM = new NaiveMinPQ<>();
//        ArrayHeapMinPQ<Double> AH = new ArrayHeapMinPQ<>();
//
//        Stopwatch sw = new Stopwatch();
//        for (int i = 1; i <= 1000000 ; i++) {
//            Double num = new Random().nextDouble();
//            double priority = new Random().nextDouble();
//            NM.add(num,priority);
//            AH.add(num,priority);
//        }
//        System.out.println("Total time elapsed: " + sw.elapsedTime() +  " seconds.");

//         sw = new Stopwatch();
//        for (int i = 1; i <= 100000 ; i++) {
//            double priority = new Random().nextDouble();
//            NM.changePriority(NM.getSmallest(),priority);
//            AH.changePriority(AH.getSmallest(),priority);
//
//        }
//        System.out.println("Total time elapsed: " + sw.elapsedTime() +  " seconds.");

//        sw = new Stopwatch();
//        for (int i = 1; i <= 1000 ; i++) {
//            NM.removeSmallest();
//        }
//        double num1 = sw.elapsedTime();
//        System.out.println("Total time elapsed: " + num1+  " seconds.");
//
//        sw = new Stopwatch();
//        for (int i = 1; i <= 1000 ; i++) {
//            AH.removeSmallest();
//        }
//        double num2 = sw.elapsedTime();
//        System.out.println("Total time elapsed: " + num2 +  " seconds.");
//
//        System.out.println(num1/num2);

//        for (int i = 1; i <= 50; i++) {
//            System.out.print(NM.removeSmallest()+"  ");
//            System.out.println(AH.removeSmallest());
//        }

//        DoubleMapPQ<Double> DMPQ = new DoubleMapPQ<>();
//
//         sw = new Stopwatch();
//        for (int i = 1; i <= 1000000 ; i++) {
//            Double num = new Random().nextDouble();
//            double priority = new Random().nextDouble();
//            DMPQ.add(num,priority);
//        }
//        System.out.println("Total time elapsed: " + sw.elapsedTime() +  " seconds.");
//
//
//
//        sw = new Stopwatch();
//        for (int i = 1; i <= 1000000 ; i++) {
//            DMPQ.removeSmallest();
//        }
//        System.out.println("Total time elapsed: " + sw.elapsedTime() +  " seconds.");
    }

}
