package delaunay;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Delaunay {
    
static int labels = 0;
public TriangleTable triangles = new TriangleTable();
private int w = 500;
private int h = 500;
private Triangle st;
private boolean drawDelaunay = true;
private boolean drawVoronoi = false;
private boolean drawBackground = false;
private Color delaunayColor = Color.WHITE;
private Color voronoiColor = Color.RED;
private Color backgroundColor = Color.DARK_GRAY;

public Delaunay(int w, int h){
this.w = w;
this.h = h;
st = addVertices(-5000, h+5000, w/2, -5000, w+5000, h+5000);
st.boundary = true;
}

public Delaunay(){
st = addVertices(-5000, h+5000, w/2, -5000, w+5000, h+5000);
st.boundary = true;
}

public void setSize(int w, int h){
this.w = w;
this.h = h;
st = addVertices(-5000, h+5000, w/2, -5000, w+5000, h+5000);
st.boundary = true;    
}

private Node addVertices(int x, int y){
    return new Node(x, y);
}

private Triangle addVertices(int x1, int y1, int x2, int y2, int x3, int y3){
   Triangle t = new Triangle(new Node(x1, y1), new Node(x2, y2), new Node(x3, y3));
   triangles.add(t);
   return t;
}

public void addPoint(int x, int y){
Triangle t = null;
Node n = addVertices(x, y);

ArrayList<Triangle> list = new ArrayList<>(triangles.triangles.values());

for(int i = list.size()-1; i >= 0; i --){
    Triangle item = list.get(i);
        if(isInTriangle(item, n.x, n.y)){
        t = item; break;
        }
}

if(t != null){
     
   Node a = t.a;
   Node b = t.b;
   Node c = t.c;

   if(n.x == a.x && n.y == a.y || n.x == b.x && n.y == b.y || n.x == c.x && n.y == c.y){return;} //same point
   
   Triangle ta = new Triangle(n, a, b); 
   Triangle tb = new Triangle(n, b, c); 
   Triangle tc = new Triangle(n, a, c); 
   
   triangles.add(ta);
   triangles.add(tb);
   triangles.add(tc);  
   
   if(!t.equals(st)){
     triangles.remove(t);
   } 
   
    check(ta, a, b);
    check(tb, b, c);
    check(tc, a, c);
       
}

}

private void check(Triangle triA, Node a, Node b){
    
Node p = triA.getOppositePoint(a, b); 

Triangle triB = triangles.getNeighbor(triA, a, b);
if(triB == null){return;}

Node d = triB.getOppositePoint(a, b);

if(isDelaunay(triA, d)){
    triA.boundary = isBoundary(triA);
//    setNeighbors(triA); 
//    setNeighbors(triB);
    return;
}

    triangles.remove(triA);
    triangles.remove(triB); 
    try{

    Triangle t1 = new Triangle(p, a, d);
    Triangle t2 = new Triangle(p, b, d);
    
  

    triangles.add(t1);
    triangles.add(t2);

//    setNeighbors(triangles.getNeighbor(t1, a, p));
//    setNeighbors(triangles.getNeighbor(t2, b, p));
    
    check(t1, a, d);
    check(t2, b, d);
    
      }catch(NullPointerException e){
        
    }
    
}

private boolean isInTriangle(Triangle t, double x, double y){
// http://totologic.blogspot.fr/2014/01/accurate-point-in-triangle-test.html    
double x1 = t.a.x; 
double x2 = t.b.x; 
double x3 = t.c.x;
double y1 = t.a.y; 
double y2 = t.b.y; 
double y3 = t.c.y;    

double a = ((y2 - y3)*(x - x3) + (x3 - x2)*(y - y3)) / (double)((y2 - y3)*(x1 - x3) + (x3 - x2)*(y1 - y3));
double b = ((y3 - y1)*(x - x3) + (x1 - x3)*(y - y3)) / (double)((y2 - y3)*(x1 - x3) + (x3 - x2)*(y1 - y3));
double c = 1 - a - b;
    
return ( (0 <= a && a <= 1) && (0 <= b && b <= 1) && (0 <= c && c <= 1) );
}

static float[] circumCenter(Triangle tri){ 
// https://www.ics.uci.edu/~eppstein/junkyard/circumcenter.html
// https://en.wikipedia.org/wiki/Circumscribed_circle
Node a = tri.a; Node b = tri.b; Node c = tri.c;

double D = (a.x - c.x) * (b.y - c.y) - (b.x - c.x) * (a.y - c.y);

double pX = (((a.x - c.x) * (a.x + c.x) + (a.y - c.y) * (a.y + c.y)) / 2.0 * (b.y - c.y) 
     -  ((b.x - c.x) * (b.x + c.x) + (b.y - c.y) * (b.y + c.y)) / 2.0 * (a.y - c.y)) / D;

double pY = (((b.x - c.x) * (b.x + c.x) + (b.y - c.y) * (b.y + c.y)) / 2 * (a.x - c.x)
     -  ((a.x - c.x) * (a.x + c.x) + (a.y - c.y) * (a.y + c.y)) / 2.0 * (b.x - c.x)) / D;
    
double r =  Math.hypot(a.x-pX, a.y-pY);

return new float[]{(float)pX, (float)pY, (float)r};    
}

private boolean isBoundary(Triangle t){
 return  t.a.equals(st.a) || t.a.equals(st.b) || t.a.equals(st.c) || t.b.equals(st.a) || t.b.equals(st.b) || t.b.equals(st.c) || t.c.equals(st.a) || t.c.equals(st.b) || t.c.equals(st.c); 
}

private boolean isDelaunay(Triangle tri, Node point){
return ((Math.hypot(tri.center.x-point.x, tri.center.y-point.y)-tri.r) >= 0);
}

public void setNeighbors(Triangle t){
Triangle ta = triangles.getNeighbor(t, t.a, t.b);
Triangle tb = triangles.getNeighbor(t, t.b, t.c);
Triangle tc = triangles.getNeighbor(t, t.a, t.c);

if(ta != null)
t.va = ta.center;
if(tb != null)
t.vb = tb.center;
if(tc != null)
t.vc = tc.center;

}

public void reset(){
triangles.triangles.clear();
triangles.edgetriangles.clear();
st = addVertices(-5000, h+5000, w/2, -5000, w+5000, h+5000);
st.boundary = true;
}

public void drawDelaunay(boolean in){
    drawDelaunay = in;
}

public void drawVoronoi(boolean in){
    drawVoronoi = in;
}

public void drawBackground(boolean in){
    drawBackground = in;
}

public void setDelaunayColor(Color c){
    delaunayColor = c;
}

public void setVoronoiColor(Color c){
    voronoiColor = c;
}

public void setBackgroundColor(Color c){
   backgroundColor = c;
}

public void draw(Graphics g){
    
    if(drawBackground){
        g.setColor(backgroundColor);
        g.fillRect(0, 0, w, h);
    }
    
    for(Triangle t : triangles.triangles.values()){
        setNeighbors(t);
        
        if(!t.boundary && drawDelaunay){
        g.setColor(delaunayColor);    
        g.drawPolygon(new int[] {(int)t.a.x, (int)t.b.x, (int)t.c.x}, new int[] {(int)t.a.y, (int)t.b.y, (int)t.c.y}, 3);
        }
        if(drawVoronoi){
        g.setColor(voronoiColor);
        try{
        g.drawLine((int)t.center.x, (int)t.center.y, (int)t.va.x, (int)t.va.y);
        g.drawLine((int)t.center.x, (int)t.center.y, (int)t.vb.x, (int)t.vb.y);
        g.drawLine((int)t.center.x, (int)t.center.y, (int)t.vc.x, (int)t.vc.y);
        }catch(NullPointerException e){
            
        }
        }
        
    }

      g.dispose();
}


}
