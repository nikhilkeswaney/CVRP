//******************************************************************************
//
//  File:    ScoutBee.java
//  Author: Nikhil Haresh Keswaney
//
//  This file implements all the functionalities of the Scout bee bees
//******************************************************************************

import edu.rit.util.Queue;

public class ScoutBee {
    private Queue<CandidateSet> possibleFoodSourses;
    private int queueSize = 0;
    private int[] nodes;

    public ScoutBee(){}

    public ScoutBee(int queueSize){
        this.possibleFoodSourses = new Queue<CandidateSet>();
        this.queueSize = queueSize;
        initializeNode();
        refillfoodSource();
    }

    /**
     * Refils the queue with potential food sources such that employeed bees can use it
     */
    public void refillfoodSource(){
        while (possibleFoodSourses.size() < queueSize){
            flyAndFindFoodSource();
        }
    }

    /**
     * Find a potential food source
     */
    private void flyAndFindFoodSource(){
        CandidateSet c = new CandidateSet(nodes);
        possibleFoodSourses.push(c);
    }

    /**
     * Initialize node array to make a food source
     */
    private void initializeNode() {
        NodeManager nodeManager = CVRP.getNodeManager();
        nodes = new int[nodeManager.getNodeSize() - 1];
        for(int i = 1; i < nodeManager.getNodeSize(); i++){
            nodes[i - 1] = i;
        }
    }

    /**
     * riffils the queue comletely if it us incompletely filled
     */
    public void checkAdRefill() {
        if(getQueueSize() < queueSize){
            refillfoodSource();
        }
    }

    /**
     * Get a potential food source from the queue
     * @return potential food source
     */
    public CandidateSet getFoodSource(){
        CandidateSet returnedVal;
        if(this.possibleFoodSourses.size() == 0)
            refillfoodSource();

        while ((returnedVal = possibleFoodSourses.pop()) == null);

        return returnedVal;
    }

    /**
     * Getters and setters
     */
    public int getQueueSize() {
        return possibleFoodSourses != null ? possibleFoodSourses.size(): 0;
    }

    public int getQueueMainSize(){
        return queueSize;
    }


    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public Queue<CandidateSet> getQueue(){
        return possibleFoodSourses;
    }

}
