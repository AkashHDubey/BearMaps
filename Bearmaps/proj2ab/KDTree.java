package bearmaps.proj2ab;

import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.PointSet;
import edu.princeton.cs.algs4.Stopwatch;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KDTree implements PointSet {

    public Node root;

    public KDTree(List<Point> points){

        this.root = null;

        for(Point p : points){
            this.insert(p);
        }

    }

    private void insert(Point data){

        if(this.root == null){
            this.root = new Node(data,0);
            return;
        }

        Node parent = this.root;
        int dimensionNumber = 0;
        double parentData = 0;
        double childrenData = 0;

        while(parent != null){

            if(dimensionNumber == 0){
                parentData = parent.dataPoint.getX();
                childrenData = data.getX();
            }
            else {
                parentData = parent.dataPoint.getY();
                childrenData = data.getY();
            }

            if(parentData > childrenData){

                if(parent.leftNode == null){
                    parent.leftNode = new Node(data,Math.floorMod(dimensionNumber+1,2));
                    parent = parent.leftNode;
                }
                parent = parent.leftNode;
            }
            else {
                if(parent.rightNode == null){
                    parent.rightNode = new Node(data,Math.floorMod(dimensionNumber+1,2));
                    parent = parent.rightNode;
                }
                parent = parent.rightNode;
            }

            dimensionNumber = Math.floorMod(dimensionNumber+1,2);
        }
    }


    @Override
    public Point nearest(double x, double y) {

        Point target = new Point(x,y);
        return this.nearest(this.root,target,this.root).dataPoint;

    }

    private Node nearest(Node current,Point target,Node best){

        if (current == null){
            return best;
        }

        if(Point.distance(current.dataPoint,target) < Point.distance(best.dataPoint,target)){
            best = current;
        }

//        best = this.nearest(current.leftNode,target,best);
//        best = this.nearest(current.rightNode,target,best);
//
//        return best;
        // Uncomment the above three lines and comment all the code below to see how
        //the simple looking optimization is so powerful.

        Node goodSide = null;
        Node badSide = null;

        double targetCoordinate = target.getX() ;
        double currentCoordinate = current.dataPoint.getX();
        double distanceSquared = (target.getX() - current.dataPoint.getX())*(target.getX() - current.dataPoint.getX());

        if(current.comparator == 1)
        {
            targetCoordinate = target.getY();
            currentCoordinate = current.dataPoint.getY();
            distanceSquared = (target.getY() - current.dataPoint.getY())*(target.getY() - current.dataPoint.getY());
        }

        if(targetCoordinate < currentCoordinate)
        {
            goodSide = current.leftNode;
            badSide = current.rightNode;
        }
        else
        {
            goodSide = current.rightNode;
            badSide = current.leftNode;
        }

        best = this.nearest(goodSide,target,best);

        double squaredDistanceFromBest = (Point.distance(best.dataPoint,target));

        //Optimization
        if(distanceSquared < squaredDistanceFromBest)
        {
            best = this.nearest(badSide,target,best);
        }

        return best;
//        if(     ((current.comparator == 0) && (distanceSquared < squaredDistanceFromBest))
//                ||
//                ((current.comparator == 1) && (distanceSquared < squaredDistanceFromBest)))
//        {
//            best = this.nearest(badSide,target,best);
//        }
    }

    private static class Node{

        public Point dataPoint;

        //comparator is used to signify whether x or y will be used for comparison : 0 for x and 1 for y
        //Calculated depending on the depth of the node
        //node depth % 2 == 0 ,x = 0
        //node depth % 2 != 0 ,x = 1

        int comparator ;
        public Node leftNode;
        public Node rightNode;

        public Node(Point data,int comparator){

            this.dataPoint = data;
            this.comparator = comparator;
            this.leftNode = null;
            this.rightNode = null;

        }

    }

    public static void main(String[] args) {
//        List<Point> pList = new ArrayList<>();
//
//        for (int i = 1; i < 1000000; i++) {
//            Point p = new Point(new Random().nextDouble(),new Random().nextDouble());
//            pList.add(p);
//        }
//
//        Stopwatch sw = new Stopwatch();
//        KDTree kd = new KDTree(pList);
//        System.out.println("Total time elapsed: " + sw.elapsedTime() +  " seconds.");
//
//        NaivePointSet NP = new NaivePointSet(pList);
//
//        sw = new Stopwatch();
//
//        for (int i = 1; i <= 1000000 ; i++) {
//            double x = new Random().nextDouble();
//            double y = new Random().nextDouble();
//            kd.nearest(x,y);
//        }
//        System.out.println("Total time elapsed: " + sw.elapsedTime() +  " seconds.");
    }
    }




