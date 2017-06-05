package delaunay;


public class Node {

float x, y; int label;

Node(float x, float y){
    this.x = x; 
    this.y = y;
    label = ++Delaunay.labels;
}

}
