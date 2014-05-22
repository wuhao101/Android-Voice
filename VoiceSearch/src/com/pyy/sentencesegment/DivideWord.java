package com.pyy.sentencesegment;

import java.io.*;
import java.util.*;

/**
 * 最大分词算法，包含正向和逆向
 * 仅用于项目前期与Trie树结构的算法进行对比测试
 */
public class DivideWord {
	/**
	 * 正向匹配
	 * 
	 * @param sentence
	 * @param max
	 * @return
	 * @throws IOException
	 */
	public String left_to_right_divide(String sentence,int max) throws IOException {
		List words = new ArrayList();	//用于存放词的集合
		int i = 0;
		String word = "";
		int maxnum = max;	//保存max的值
		while (i < sentence.length()) {
			//截取max长度的词，若总长度不足，则截取剩余部分
			if (i + max <= sentence.length()) {
				word = sentence.substring(i,i+max);
			} else {
				word = sentence.substring(i);
			}
			//如果截取的word是个词或者当前max值为1，则将word加入到词集合中，并把指针向前移动i个位
			if (isWord(word) || max == 1) {
				words.add(word);
				i = i + max;
				max = maxnum;
			} else {
				max--;
			}
		}
		//将集合合成一个字符串返回
		String result = "";
		for (int j = 0; j < words.size(); j ++) {
			result += words.get(j).toString()+"/ ";
		}
		return result;
	}
	
	/**
	 * 逆向匹配
	 * 
	 * @param sentence
	 * @param max
	 * @return
	 * @throws IOException
	 */
	public String right_to_left_divide(String sentence,int max) throws IOException {
		List words = new ArrayList();	//用于存放词的集合
		int i = sentence.length();
		String word = "";
		int maxnum = max;	//保存max的值
		while (i > 0) {
			//截取max长度的词，若总长度不足，则截取剩余部分
			if (i - max >= 0) {
				word = sentence.substring(i-max,i);
			} else {
				word = sentence.substring(0,i);
			}
			
			//如果截取的word是个词或者当前max值为1，则将word加入到词集合中，并把指针向前移动i个位
			if (isWord(word) || max == 1) {
				words.add(word);
				i = i - max;
				max = maxnum;
			} else {
				max--;
			}
		}
		//将集合合成一个字符串返回
		String result = "";
		for(int j = words.size() - 1; j >= 0; j --) {
			result += words.get(j).toString() + "/  ";
		}
		return result;
	}
	
	/**
	 * 打开词典word.txt，匹配word，判断word是否是一个词
	 * 
	 * @param word
	 * @return
	 * @throws IOException
	 */
	public boolean isWord(String word) throws IOException {
		boolean isword = false;
		BufferedReader br = new BufferedReader(new FileReader("dic/dic.txt"));
		String text;
		while ((text = br.readLine())!= null) {
			String[]key = text.split(",");
//			System.out.println(key[0]);
			if (word.equals(key[0])) {
				isword = true;
			}
		}
		br.close();
		return isword;
	}
	
//	public static void main(String[]args) throws IOException {
//		DivideWord dw = new DivideWord();
//		System.out.println(dw.right_to_left_divide("日文章鱼怎么说", 3));
//		long start = System.currentTimeMillis();
//		long end = System.currentTimeMillis();
//		System.out.println("用时：" + (end - start));
//		System.out.println(dw.left_to_right_divide("日文章鱼怎么说", 3));
//		long start2 = System.currentTimeMillis();
//		long end2 = System.currentTimeMillis();
//		System.out.println("用时：" + (end2 - start2));
//	}
}
