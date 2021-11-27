package laba;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Kristofides {
    static List<List<Double>> Prim(List<List<Double>> graph)
    {
        int n = graph.size();
        int index = 0;
        int [] prims = new int [n];
        double [] weights = new double [n];
        Point [] weidhtsV = new Point [n];
        List<List<Double>> result = Graph.getEmptyGraph(n);
        Point [] resultV = new Point [n];
        prims[index++] = 1;


        for (int i = 0; i < n; i++) {
            weights[i] = graph.get(0).get(i);
            weidhtsV[i] = new Point(0,i);
            //resultV[i] = weidhtsV[i];
        }

        for (int i = 1; i < n; i++) {

            int k = 0;
            double min = Double.MAX_VALUE;
            for (int j = 0; j < n; j++) {
                if(weights[j] != 0 && weights[j] < min) {

                    min = weights[j];
                    k = j;
                }
            }

            // После описанной выше обработки среди вершин, которые не были добавлены в минимальное остовное дерево, вершина с наименьшим весом является k-й вершиной.
            // Добавить k-ю вершину в массив результатов минимального остовного дерева
            prims[index++] = k;
            // Помечаем «вес k-й вершины» как 0, что означает, что k-я вершина была отсортирована (или она была добавлена минимальному результату дерева).
            weights[k] = 0;
            resultV[k] = new Point(weidhtsV[k]);
            // Когда k-тая вершина добавляется в результирующий массив минимального остовного дерева, обновляем веса других вершин.
            for (int j = 0 ; j < n; j++) {
                // Когда j-й узел не был обработан и нуждается в обновлении, он будет обновлен.
                if (weights[j] != 0 && graph.get(k).get(j) < weights[j]) {
                    weights[j] = graph.get(k).get(j);
                    weidhtsV[j].setLocation(k,j);
                }
            }
        }


        for (int i = 1; i < n; i++) {
//            System.out.println(resultV[i].x + "  " + resultV[i].y);
            result.get(resultV[i].x).set(resultV[i].y, graph.get(resultV[i].x).get(resultV[i].y));
            result.get(resultV[i].y).set(resultV[i].x, graph.get(resultV[i].y).get(resultV[i].x));
        }


        return result;
    }

    public static List<Integer> GetCicle(List<List<Double>> MST, List<List<Double>> graph){
        int n = graph.size();

        temp = new ArrayList<>();
        dfs(0, new boolean[n], MST);
/*        for (int i = 0; i < temp.size(); i++) {
            System.out.println(temp.get(i));
        }*/

        return temp;
    }

    private static List<Integer> temp;
    public static void dfs(Integer cur, boolean [] visited, List<List<Double>> MST){
        temp.add(cur);
        visited[cur] = true;
        for (int i = 0; i < MST.size(); i++) {
            if(!visited[i] && MST.get(cur).get(i)!= 0){
                dfs(i,visited, MST);
            }
        }
    }
}
