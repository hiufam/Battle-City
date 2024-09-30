package utils;

import java.util.ArrayList;

public class CommonUtil {
  public static int[][] arrayListToArray(ArrayList<ArrayList<Integer>> arrayList) {
    int[][] intArray = arrayList.stream().map(u -> u.stream().mapToInt(
        i -> i).toArray()).toArray(int[][]::new);
    return intArray;
  }
}
