/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sithara
 * w165448
 * 2016372
 */
public class Node {
        double heuristicCost = 0; // assingning varibales
        double f_Cost = 0;
        double g_cost = 0;
        int y, x;
        Node parentNode;
        
        Node(int i, int j){ //setting the coordinates of the current node
            this.y = i;
            this.x = j; 
        }
        
        
}
