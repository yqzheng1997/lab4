package com.lab4.shiyan;

import java.util.Scanner;

public class GraphTest {
  
  /**
   * 
   * @param args.
   */
  public static void main(String[] args) {
    //TODO Auto-generated method stub
    Scanner scan = new Scanner(System.in);
    System.out.println("请输入文件路径：");
    String filepath;//word1,word2,input;
    //String str;
    filepath = scan.nextLine();

    //cd D:\java-learn\shiyan1\src
    //javac ./shiyan1/test.java
    //java shiyan1.test
    //D:/lab1test/lab1test.txt
    //of wild but surely than
    //pathos to than in western
    //the of the earth for coming never to mean
    
    
    graph ggg = new graph();
	boolean success = ggg.buildGraph(filepath);
	if(success) {
	  int choice = -1;
	  do{
		choice = ggg.meau();
		switch(choice) {
		case 1:
		  ggg.outword();
		  break;
		case 2:
		  ggg.picture();
		  break;
		case 3:
		  System.out.println("请输入两个单词以生成桥接词");
		  word1 = scan.nextLine();
		  word2 = scan.nextLine();
		  str=ggg.queryBridgeWords(word1,word2);
		  if(str==null) {
			System.out.println("No "+ word1 +" or "+ word2 +" in this graph");
		  }
		  else if(str.equals(" ")) {
			System.out.println("No bridge words from "+word1+" to "+word2);
		  }
		  else {
			System.out.println("The bridge words from "+word1+" to "+word2+" are :"+str  );
		  }
		  break;
		case 4:
		  System.out.println("请输入新的一句话");
		  input = scan.nextLine();
		  System.out.println("新生成的文本是：");
		  str=ggg.generateNewText(input);
		  System.out.println(str);
		  break;
		case 5:
		  System.out.println("请输入两个单词以生成最短路径");
		  word1 = scan.nextLine();
		  word2 = scan.nextLine();
		  ggg.calcShortestPath(word1, word2);
		  break;
	    case 6:
		  System.out.print("按回车键重新进行随机游走，按q退出");
		  String go=scan.nextLine();
		  while(go.equals("q")==false) {
			str=ggg.randomWalk();
			System.out.print(str);
			go=scan.nextLine();
		  }
		  break;
		}
		}while(choice != 0);
	}
	scan.close();
  }

}
