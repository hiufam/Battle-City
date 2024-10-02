package layouts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import enums.GameComponentType;
import utils.CommonUtil;

public class MapLayout {
  private static final int ROW_SHIFT = 1;
  private static final int COL_SHIFT = 2;
  private static final int BASE_POS = 14;

  public static ArrayList<ArrayList<Integer>> readFromFile(String fileName) {
    ArrayList<ArrayList<Integer>> tempMap = new ArrayList<>();
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
      String currentLine;
      while ((currentLine = bufferedReader.readLine()) != null) {
        if (currentLine.isEmpty()) {
          continue;
        }

        ArrayList<Integer> row = new ArrayList<>();
        String[] values = currentLine.trim().split("");
        for (String string : values) {
          if (!string.isEmpty()) {
            switch (string) {
              case "#":
                row.add(1);
                break;
              case "@":
                row.add(2);
                break;
              case "~":
                row.add(4);
                break;
              case "%":
                row.add(5);
                break;
              case "P":
                row.add(6);
                break;
              default:
                row.add(0);
                break;
            }
          }
        }
        tempMap.add(row);

      }
    } catch (IOException e) {
      System.out.println(e);
    }
    return tempMap;
  }

  public static int[][] generateLevel() {
    int[][] newLevel = ScreenLayout.getInstance().layout;
    ArrayList<ArrayList<Integer>> levelReadFromFile = readFromFile("levels\\level_1");
    int[][] array = CommonUtil.arrayListToArray(levelReadFromFile);
    for (int i = ROW_SHIFT; i < array.length + ROW_SHIFT; i++) {
      for (int j = COL_SHIFT; j < array[0].length + COL_SHIFT; j++) {
        newLevel[i][j] = array[i - ROW_SHIFT][j - COL_SHIFT];
      }
    }
    newLevel[array.length - 1][BASE_POS] = GameComponentType.BASE.getValue();
    return newLevel;
  }
}
