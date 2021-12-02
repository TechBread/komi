package laba;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static List <Integer> C_MADNESS(int n, Graph myG){
        //the greatest change
        List<List<Double>> graph = myG.dist;

        List<List<Double>> PrimGraph = Kristofides.Prim(graph);

        List<Integer> circle = Kristofides.GetCicle(PrimGraph,graph);

        //System.out.println(CircleLength(circle, myG, n));

        return circle;
    }


    static Graph GenerateGraph(int n) {
        List<List<Double>> g = new ArrayList<>();

        List<Point2D> vertices = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Point2D v = new Point2D.Double(Math.random()*100, Math.random()*100);
            vertices.add(v);
        }
        for (int i = 0; i < n; i++) {
            List<Double> line = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                line.add(vertices.get(i).distance(vertices.get(j)));
            }
            g.add(line);
        }

        return new Graph(g);
    }

    static void WriteOutput(List<Integer> circle){
        for (Integer ver : circle) {
            System.out.print(ver + " ");
        }
        System.out.print(circle.get(0)+" ");
        System.out.println();
    }

    static Double CircleLength(List<Integer> circle, Graph myG, int n){
        Double sum = 0.0;
        //int m = circle.size();
        for (int i = 0; i < n - 1; i++) {
            sum += myG.dist.get(circle.get(i)).get(circle.get(i+1));
        }
        sum += myG.dist.get(circle.get(n-1)).get(circle.get(0));
        return sum;
    }

    static void GetMinCircle(List<Integer> tryCircle, List<Boolean> freeVert, List<Integer> circle, int n, Graph myG){

        if(tryCircle.size() == n){//1
            if(circle.isEmpty()) {
                circle.addAll(tryCircle);
            } else {
                Double tmpLength = CircleLength(tryCircle, myG, n);
                Double circleLength = CircleLength(circle, myG, n);
                if(tmpLength < circleLength){
                    circle.clear();
                    circle.addAll(tryCircle);
                }
            }

        } else {
            for (int i = 0; i < n; i++) {
                if(freeVert.get(i)){
                    freeVert.set(i, false);
                    tryCircle.add(i);
                    GetMinCircle(tryCircle, freeVert, circle, n, myG);

                }
            }
        }
        if(tryCircle.size()>0) {//1
            freeVert.set(tryCircle.get(tryCircle.size()-1), true);
            tryCircle.remove(tryCircle.size() - 1);
        }
    }

    public static List<Integer> A_TRY_HARD(int n, Graph myG){
        List<Integer> circle = new ArrayList<>();
        List<Integer> tryCircle = new ArrayList<>();
        List<Boolean> freeVert = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            freeVert.add(true);
        }
        GetMinCircle(tryCircle, freeVert, circle, n, myG);



        //System.out.println(CircleLength(circle, myG, n));
        return circle;
    }

    public static List<Integer> B_GREED(int n, Graph myG){
        List<Integer> circle = new ArrayList<>();
        List<Boolean> freeVert = new ArrayList<>();
        int vertLeft = n - 1;
        for (int i = 0; i < n; i++) {
            freeVert.add(true);
        }
        freeVert.set(0, false);
        circle.add(0);
        int last = 0;
        while(vertLeft > 0){
            double minDist = 10000;
            int minVert = -1;
            for (int i = 0; i < n; i++) {
                if(freeVert.get(i)){
                    if(minVert == -1) {
                        minVert = i;
                        minDist = myG.dist.get(last).get(i);
                    } else if(minDist > myG.dist.get(last).get(i)){
                        minDist = myG.dist.get(last).get(i);
                        minVert = i;
                    }
                }
            }
            circle.add(minVert);
            last = minVert;
            freeVert.set(minVert, false);
            vertLeft--;
        }

       //System.out.println(CircleLength(circle, myG, n));
        return circle;
    }


    public static void main(String[] args) {

        long mainTID = Thread.currentThread().getId();
        Graph myGraph;
        Scanner myScan = new Scanner(System.in);

        System.out.println("How to input graph?: 1 - generate random graph, 2 - get from file");
        int inputWay = Integer.parseInt(myScan.nextLine());
        int n;
        int testCount = 0;
        if(inputWay == 1) {
            System.out.println("Input n:");
            n = Integer.parseInt(myScan.nextLine());
            System.out.println("Input number of tests");
            testCount = Integer.parseInt(myScan.nextLine());
            myGraph = GenerateGraph(n);
        } else {
            try {
                File inputFile = new File("input.txt");
                Scanner myReader = new Scanner(inputFile);
                n = Integer.parseInt(myReader.nextLine());
                List<List<Double>> inputGraph = new ArrayList<>();
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    List<Double> row = new ArrayList<>();
                    String[] dataArray = data.split(" ");
                    for (int i = 0; i < dataArray.length; i++) {
                        row.add(Double.parseDouble(dataArray[i]));
                    }
                    inputGraph.add(row);
                }
                myGraph = new Graph(inputGraph);
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                n = 1;
                myGraph = GenerateGraph(1);
            }
        }
        List<Integer> gamilCircle;

        long startT;
        long startPT;
        long endT;
        long endPT;


        if (inputWay == 2){
            testCount = 1;
        }
        int testATime = 0;
        int testAThreadTime = 0;
        int testBTime = 0;
        int testBThreadTime = 0;
        int testCTime = 0;
        int testCThreadTime = 0;

        Integer sovpB = 0;
        Integer sovpC = 0;

        Double sumOtclB = 0.0;
        Double sumOtclC = 0.0;

        Integer bolshe3del2 = 0;

        for(int i = 0; i < testCount; i++) {
            if(inputWay == 1) {
                myGraph = GenerateGraph(n);
            }
            System.out.println("Test:"+i);
            startT = System.nanoTime();
            startPT = ManagementFactory.getThreadMXBean().getThreadCpuTime(mainTID);
            gamilCircle = A_TRY_HARD(n, myGraph);
            //WriteOutput(gamilCircle);
            endT = System.nanoTime();
            endPT = ManagementFactory.getThreadMXBean().getThreadCpuTime(mainTID);
            Double lenghtA = CircleLength(gamilCircle,  myGraph, n);
            testATime += (endT - startT)/1000000;
            testAThreadTime += (endPT - startPT)/1000000;

            //System.out.println("B:");
            startT = System.nanoTime();
            startPT = ManagementFactory.getThreadMXBean().getThreadCpuTime(mainTID);
            gamilCircle = B_GREED(n, myGraph);
            //WriteOutput(gamilCircle);
            endT = System.nanoTime();
            endPT = ManagementFactory.getThreadMXBean().getThreadCpuTime(mainTID);
            Double lenghtB = CircleLength(gamilCircle,  myGraph, n);
            testBTime += (endT - startT)/1000000;
            testBThreadTime += (endPT - startPT)/1000000;

            //System.out.println("C:");
            startT = System.nanoTime();
            startPT = ManagementFactory.getThreadMXBean().getThreadCpuTime(mainTID);
            gamilCircle = C_MADNESS(n, myGraph);
            //WriteOutput(gamilCircle);
            endT = System.nanoTime();
            endPT = ManagementFactory.getThreadMXBean().getThreadCpuTime(mainTID);
            Double lenghtC = CircleLength(gamilCircle,  myGraph, n);
            testCTime += (endT - startT)/1000000;
            testCThreadTime += (endPT - startPT)/1000000;

            if(lenghtA.equals(lenghtB))
                sovpB++;
            if(lenghtA.equals(lenghtC))
                sovpC++;

            sumOtclB += (lenghtB - lenghtA)/lenghtA;

            Double otclC = (lenghtC - lenghtA)/lenghtA;

            if(otclC > 0.5)
                bolshe3del2++;

            sumOtclC += otclC;
        }
        System.out.println("System thread time for A");
        System.out.println(testATime/testCount);
        System.out.println("System time for A");
        System.out.println(testAThreadTime/testCount);
        System.out.println("System thread time for B");
        System.out.println(testBTime/testCount);
        System.out.println("System time for B");
        System.out.println(testBThreadTime/testCount);
        System.out.println("System thread time for C");
        System.out.println(testCTime/testCount);
        System.out.println("System time for C");
        System.out.println(testCThreadTime/testCount);
        System.out.println("SovpB = " + sovpB);
        System.out.println("SovpC = " + sovpC);
        System.out.println("Avg otcl B = " + sumOtclB/testCount);
        System.out.println("Avg otcl C = " + sumOtclC/testCount);
        System.out.println("Kristofides error count = " + bolshe3del2);
    }

}
