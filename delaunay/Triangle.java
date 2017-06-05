package delaunay;

public class Triangle {

Node a, b, c, center;
Node va, vb, vc; 
float r;
boolean boundary = false;

Triangle(Node a, Node b, Node c){
    this.a = a; 
    this.b = b; 
    this.c = c;
    float[] circ = Delaunay.circumCenter(this);
    center = new Node(circ[0], circ[1]);
    r = circ[2];
}

Node getOppositePoint(Node p1, Node p2){
    
    if(!a.equals(p1) && !a.equals(p2)){
        return a;
    }
    if(!b.equals(p1) && !b.equals(p2)){
        return b;
    }    
    if(!c.equals(p1) && !c.equals(p2)){
        return c;
    }    
    return null;
}
    
}
