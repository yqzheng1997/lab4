package com.lab4.shiyan;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

public class Graph {
  private static int N = 500;
  private int wordnum;//閸楁洝鐦濇稉顏呮殶
  private int[] edgenum = new int[N];//鏉堣娼弫锟�
  private String[] word = new String[N];//閸楁洝鐦濋弫鎵矋
  private int[][] edge = new int[N][N];//鏉堝湱娈戞禍宀�娣弫鎵矋
  /**
   * 
   * @param filepath.
   */
  
  public boolean buildGraph(String filepath) { //娴肩姴鍙嗛弬鍥︽鐠侯垰绶為敍宀�鏁撻幋鎰箒閸氭垵娴�
    try {
      wordnum = 0;                           // initialize the wordnum
      FileInputStream in = new FileInputStream(filepath);
      int size = in.available();
      int prei = 0;
      byte[] buffer = new byte[size];
      int flag = -1;
      in.read(buffer);
      in.close();
      String str = new String(buffer,"GB2312");
      str = str.toLowerCase();
      int i = 0;
      for (i = 0;i < 127;i++) {
        if (i < 65 || i > 122 || (i > 90 && i < 97)) {
          str = str.replace((char)i,' ');
        }
      }
      String[] wordt = null;  //if attributing the space for wordt like this, the space will be wasted
      wordt = str.split("\\s+");
      int j = 0;
      for (i = 0;i < wordt.length;i++) {
        flag = -1;
        for (j = 0;j < i;j++) {
          if (wordt[i].equals(word[j])) {
            flag = j;
            break;
          }
        }
        if (flag == -1) {
          if (i != 0) {
            if (edge[prei][wordnum] == 0) {
              edgenum[prei]++;
            }
            edge[prei][wordnum]++;
          }
          prei = wordnum;
          word[wordnum] = wordt[i];
          wordnum++;
        } else {
          if (edge[prei][flag] == 0) {
            edgenum[prei]++;
          }
          edge[prei][flag]++;
          prei = flag;
        }
      }
      for (i = 0;i < wordnum;i++) {
        for (j = 0;j < wordnum;j++) {
          if (edge[i][j] == 0) {
            edge[i][j] = N;
          }
        }
      }
    } catch (IOException e) {
      System.out.println("鐠囪褰囬弬鍥︽婢惰精瑙﹂敍锟�");
      e.printStackTrace();      //杈撳嚭閿欒鑰屼俊鎭�
      return false;
    }
    return true;
  }
  /**
   * 鏉堟挸鍤崡鏇＄槤閸欏﹥婀侀崥鎴濇禈閻ㄥ嫰鍋﹂幒銉х叐闂冿拷.
   */
  
  public void outword() { 
    int i = 0;
    int j = 0;
    System.out.print(wordnum + "娑擃亜宕熺拠宥忕窗\n");
    for (i = 0;i < wordnum;i++) {
      System.out.println(word[i] + "(" + edgenum[i] + ")");
    }
    for (i = 0;i < wordnum;i++) {
      for (j = 0;j < wordnum;j++) {
        if (edge[i][j] < N) {
          System.out.println(word[i] + "->" + word[j]);
        }
      }
    }
  }
  /**
   * 閺屻儴顕楀銉﹀复鐠囷拷.
   */
  
  public String queryBridgeWords(String word1,String word2) {
    int i;
    int j;
    int a = -1;
    int b = -1;
    int count = 0;
    int flag1 = -1;
    int flag = -1;
    String str = "";
    String[] bridge = new String[N];
    for (i = 0;i < wordnum;i++) {
      if (word[i].equals(word1)) {
        a = i;
        flag = 1;
      }
      if (word[i].equals(word2)) {
        b = i;
        flag1 = 1;
      }
    }
    if (flag ==  -1 || flag1 ==  -1) {
      return null;
    }
    for (i = 0;i <= wordnum;i++) {
      flag = flag1 = -1;
      if (i != a && i != b) {
        for (j = 0;j < edgenum[a];j++) {
          if (edge[a][i] < N) {
            flag = 1;
          }
        }
        for (j = 1;j <= edgenum[i];j++) {
          if (edge[i][b] < N) {
            flag1 = 1;
          }
        } 
        if (flag == 1 && flag1 == 1) {
          bridge[count] = word[i];
          count++;
        }
      }
    }
    if (count == 0) {
      return " ";
    } else {
      for (i = 0;i < count;i++) {
        str = str + bridge[i] + " ";
      }
    }
    return str;
  }
  /**
   * 閻㈢喐鍨氶弬鐗堟瀮閺堬拷.
   */
  
  public String generateNewText(String input) {
    String temp;
    String[] bri = new String[N];
    int start = 0;
    int i;
    String[] word1 = new String[N];
    String[] word2 = new String[N];
    String[] ttemp = new String[N];
    int tcount = 0;
    String str = "";
    String[] newtext = new String[N];
    newtext = input.split(" ");
    for (i = 0;i < newtext.length - 1;i++) {
      temp = this.queryBridgeWords(newtext[i], newtext[i + 1]);
      if (temp !=  null && temp.equals(" ") == false) {
        bri = temp.split(" ");
        temp = bri[0];
        word1[tcount] = newtext[i];
        word2[tcount] = newtext[i + 1];
        ttemp[tcount] = temp;
        tcount++;
        
        for (int j = start;j <= i;j++) {
          str = str + newtext[j] + " ";
        }
        str = str + temp + " ";
        start = i + 1;
      }
    }    
    this.picturebridge(word1,word2,ttemp,tcount);
    for (i = start;i < newtext.length;i++) {
      str = str + newtext[i] + " ";
    }
    return str;
  }
  /**
   * 閸ュ墽澧栨潏鎾冲毉濡椼儲甯寸拠锟�.
   */
  
  public void picturebridge(String[] word1,String[] word2,String[] temp,int tcount) {
    int[][] flagedge = new int[N][N];
    int k = 0;
    int j = 0;
    int ti = 0;
    int ei = 0;
    int ej = 0;
    for (k = 0;k < tcount;k++) {
      for (j = 0;j < wordnum;j++) {
        if (temp[k].equals(word[j])) {
          ti = j;
        }
        if (word1[k].equals(word[j])) {
          ei = j;
        }
        if (word2[k].equals(word[j])) {
          ej = j;
        }
      }
      flagedge[ei][ti] = 1;
      flagedge[ti][ej] = 1;
    }
    Graphviz gv = new Graphviz();
    gv.addln(gv.start_graph());
    int i;
    for (i = 0;i < wordnum;i++) {
      for (j = 0;j < wordnum;j++) {
        if (edge[i][j] < N) {
          if (flagedge[i][j] == 0) {
            gv.addln(word[i] + "->" + word[j] + "[label = " + edge[i][j] + "]");
          } else {
            gv.addln(word[i] + "[color = red]");
            gv.addln(word[j] + "[color = red]");
            gv.addln(word[ti] + "[color = red]");
            gv.addln(word[i] + "->" + word[j] + "[color = orange,label = " + edge[i][j] + "]");
          }
        }
      }
    }
    gv.addln(gv.end_graph());
    gv.increaseDpi();   // 106 dpi
    String type = "png";
    String repesentationType =  "dot";
    File out = new File("D:/graphbridge." + type);    // Windows
    gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type, repesentationType), out);
  }
  /**
   * 鐠侊紕鐣婚張锟介惌顓＄熅瀵帮拷.
   */
  
  public String calcShortestPath(String word1,String word2) {
    int j;
    int[][] path = new int[N][N];
    int[][] dis = new int[N][N];
    String str = "";
    int i;
    int a = -1;
    int flag = -1;
    int flag1 = -1;
    int b = -1;
    for (i = 0;i < wordnum;i++) {
      if (word[i].equals(word1)) {
        a = i;
        flag = 1;
      }
      if (word[i].equals(word2)) {
        b = i;
        flag1 = 1;
      }
    }
    if (flag ==  -1 || flag1 ==  -1) {
      System.out.println("No " + word1 + " or " + word2 + " in this graph");
      return null;
    }
    for (i = 0;i < wordnum;i++) {
      for (j = 0;j < wordnum;j++) {
        path[i][j] = j;
        dis[i][j] = edge[i][j];
      }      
    }
    for (int k = 0;k < wordnum;k++) {
      for (i = 0;i < wordnum;i++) {
        for (j = 0;j < wordnum;j++) {
          if (dis[i][k] + dis[k][j] < dis[i][j]) {
            dis[i][j] = dis[i][k] + dis[k][j];
            path[i][j] = path[i][k];
          }
        }
      }
    }
    if (dis[a][b] < N) {
      System.out.println(word1 + " 閸掞拷 " + word2 + " 閻ㄥ嫭娓堕惌顓＄熅瀵板嫰鏆辨惔锔胯礋閿涳拷" + dis[a][b]);
      str = this.pictureNew(path, a, b);
      return str;
    } else {
      System.out.println(word1 + " 閸掞拷 " + word2 + " 濞屸剝婀佺捄锟�");
      return null;
    }
  }
  /**
   * randomwalk.
   */
  
  public String randomWalk() {
    String str = "";
    int[] strword = new int[N];
    Random rand = new Random();
    int i;
    int count = 0;
    int countword = 0;
    int flag = 1;
    int prei;
    i = rand.nextInt(wordnum);
    prei = i;
    int k;
    int j;
    while (edgenum[i] != 0 && flag == 1) {
      for (k = 1;k < countword;k++) {
        if (strword[k] == i && prei == strword[k - 1] && countword != 0) {
          flag = 0;
          break;
        }
      }
      strword[countword] = i;
      countword++;
      if (flag == 1) {
        j = rand.nextInt(edgenum[i]);
        count = 0;
        for (k = 0;k < wordnum;k++) {
          if (edge[i][k] < N) {
            count++;
            if (count - 1 == j) {
              break;
            }
          }
        }
        if (edge[i][k] < N) {
          prei = i;
          i = k;
        }
      }
    }
    for (j = 0;j < countword;j++) {
      str = str + word[strword[j]] + " ";
    }
    if (edgenum[i] == 0) {
      str = str + word[i];
    }
    return str;
  }
  /**
   * pictureNew.
   */
  
  public String pictureNew(int[][] path,int a,int b) {
    int i;
    String str = "";
    int[][] flag = new int[N][N];
    Graphviz gv = new Graphviz();
    gv.addln(gv.start_graph());
    i = a;
    str = word[a];
    while (i != b) {
      str = str + word[path[i][b]];
      flag[i][path[i][b]] = 1;
      i = path[i][b];
    }
    int j;
    for (i = 0;i < wordnum;i++) {
      for (j = 0;j < wordnum;j++) {
        if (edge[i][j] < N) {
          if (flag[i][j] == 0) {
            gv.addln(word[i] + "->" + word[j] + "[label = " + edge[i][j] + "]");
          } else {
            gv.addln(word[i] + "->" + word[j] + "[color = blue,label = " + edge[i][j] + "]");
          }
        }
      }
    }
    gv.addln(gv.end_graph());
    //System.out.println(gv.getDotSource());
    gv.increaseDpi();   // 106 dpi
    String type = "png";
    String repesentationType =  "dot";
    File out = new File("D:/lab1test/graphnew." + type);    // Windows
    gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type, repesentationType), out);
    return str;
  }
  /**
   * picture.
   */
  
  public void picture() {
    int i;
    int j;
    Graphviz gv = new Graphviz();
    gv.addln(gv.start_graph());
    for (i = 0;i < wordnum;i++) {
      for (j = 0;j < wordnum;j++) {
        if (edge[i][j] < N) {
          gv.addln(word[i] + "->" + word[j] + "[label = " + edge[i][j] + "]");
        }
      }
    }
    gv.addln(gv.end_graph());
    gv.increaseDpi();   // 106 dpi
    String type = "png";
    String repesentationType =  "dot";
    File out = new File("D:/graph." + type);    // Windows
    gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type, repesentationType), out);
  }
  public int meau() {
	int choice = -1 ;
  	@SuppressWarnings("resource")
	Scanner scan = new Scanner(System.in);
  	do {
  	  System.out.println("欢迎来到本系统！");
  	  System.out.println("1,展示有向图邻接矩阵");
  	  System.out.println("2,展示有向图");
  	  System.out.println("3,查询桥接词");
  	  System.out.println("4,生成新文本");
  	  System.out.println("5,最短路径");
  	  System.out.println("6,随机游走");
  	  System.out.println("0,退出本系统");
  	  System.out.println("请输入您的选择");
  	  if(scan.hasNextInt())
  	    choice = scan.nextInt();
  	}while(choice < 0 || choice > 6);
  	return choice;
  }
}
