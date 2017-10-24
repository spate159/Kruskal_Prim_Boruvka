# Kruskal_Prim_Boruvka
This program is used to compare time complexities of prim kruskal and boruvka minimum spanning tree algorithms.

MainClass.java file has the main() code to run all three algorithm

DisplayGraph.java draws the graph. It can also show the MST(by prims) in the graph (1991 algorithm of Fruchterman and Reingold implemented to Draw Graph)

GraphFileGenerator generates the random graph of given edges and vertices.

Tester.java contains the code used to execute test cases..

*******************************************************************************
**************************Running the program**********************************

1- To run all three algorithms execute in Terminal or CMD

java -jar Find3MSTs.jar <Graph_File>

2- To draw the graph

java -jar DisplayGraph.jar <Graph_File>

3- To generate a random graph

java -jar GenerateGraph.jar <VertexCount> <EdgeCount> <Graph_File_Name> <MaxWeight>


*****Special Notes on graphFile

The weight should be positive integers(This program has not been tested on negative weighted edges.)
The vertex names should be positive integers.
The graph should be connected.
The graph should not have multi-edged.

A sample graph file is included.
