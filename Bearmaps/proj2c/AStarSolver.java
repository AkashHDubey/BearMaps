package bearmaps.proj2c;

import java.util.*;

import bearmaps.proj2ab.*;
import edu.princeton.cs.algs4.Stopwatch;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {

    private int statesExplored;
    private double timeTaken;
    private Map<Vertex,Double> distFrom;
    private Map<Vertex,Vertex> edgeFrom;
    private SolverOutcome SO;
    private Vertex end;
    private Vertex start ;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout){

        Stopwatch sw = new Stopwatch();

        ExtrinsicMinPQ<Vertex> PQ = new ArrayHeapMinPQ<>();
        this.distFrom = new HashMap<>();
        this.edgeFrom = new HashMap<>();
        this.end = end;
        this.start = start;

        PQ.add(start,0);
        this.distFrom.put(start,0.0);

        Vertex p = null;
        while (PQ.size() != 0 && this.timeTaken < timeout){

            p = PQ.removeSmallest();
            this.statesExplored++;

            if(p.equals(end)){
                break;
            }

            for (WeightedEdge<Vertex> connection : input.neighbors(p)){

                Vertex from =  connection.from();
                Vertex to = connection.to();
                double edgeWeight= connection.weight();

                if(!this.distFrom.containsKey(to)){
                    this.distFrom.put(to,Double.POSITIVE_INFINITY);
                }

                double distanceTo = this.distFrom.get(to);
                double newWeight = this.distFrom.get(from) + edgeWeight;

                if(newWeight < distanceTo){
                    this.distFrom.put(to,newWeight);
                    this.edgeFrom.put(to,from);

                    if(!PQ.contains(to)){
                        PQ.add(to,newWeight+input.estimatedDistanceToGoal(to,end));
                    }
                    PQ.changePriority(to,newWeight+input.estimatedDistanceToGoal(to,end));

                }

            }

            this.timeTaken = sw.elapsedTime();
        }

        if(this.timeTaken > timeout){
            this.SO = SolverOutcome.TIMEOUT;
        }

        else if(p != null && p.equals(end)){
            this.SO = SolverOutcome.SOLVED;
        }
        else {
            this.SO = SolverOutcome.UNSOLVABLE;
        }

    }

    @Override
    public SolverOutcome outcome() {
        return this.SO;
    }

    @Override
    public List<Vertex> solution() {

        if(this.SO == SolverOutcome.TIMEOUT || this.SO == SolverOutcome.UNSOLVABLE){
            return new ArrayList<>();
        }

        List<Vertex> solution = new ArrayList<>();
        Vertex e = this.end;
        solution.add(e);

        while (!e.equals(start)){
            e = this.edgeFrom.get(e);
            solution.add(e);
        }

        Collections.reverse(solution);
        return solution;
    }

    @Override
    public double solutionWeight() {
        if(this.SO == SolverOutcome.TIMEOUT || this.SO == SolverOutcome.UNSOLVABLE){
            return 0.0;
        }
        return this.distFrom.get(end);
    }

    @Override
    public int numStatesExplored() {
        return this.statesExplored;
    }

    @Override
    public double explorationTime() {
        return this.timeTaken;
    }
}
