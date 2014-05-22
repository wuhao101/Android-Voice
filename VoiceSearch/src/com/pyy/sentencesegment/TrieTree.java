package com.pyy.sentencesegment;

import java.util.ArrayList;


/**
 * Trie树分词操作
 */
public class TrieTree {
	/**
	 * 根节点
	 */
	public TrieTreeNode root = new TrieTreeNode();	
	/**
	 * 搜索深度
	 */
	private static final int RECURSION_TIME = 5;
	/**
	 * 词语长度
	 */
	private static int WORD_LEN = 0;
	/**
	 * 输出结果
	 */
	public ArrayList<String> result = new ArrayList<String>();
	
	/**
	 * 往Tire树中插入词语
	 * 
	 * @param word
	 */
	public void insertTrieTree(String word) {
		char[] wordChar = word.toCharArray();
		TrieTreeNode tempNode = root;
		for (int i = 0; i < wordChar.length; i++) {
			if (i < wordChar.length - 1) {
				addTrieTreeNode(wordChar, tempNode, i);
				tempNode = tempNode.childs.get(wordChar[i]);
			} else {
				addTrieTreeNode(wordChar, tempNode, i);
				tempNode.childs.get(wordChar[i]).state = 1;
			}
		}
	}

	/**
	 * 插入新节点
	 * 
	 * @param wordChar
	 * @param tempNode
	 * @param charIndex
	 */
	private void addTrieTreeNode(char[] wordChar, TrieTreeNode tempNode,
			int charIndex) {
		if (tempNode.childs.get(wordChar[charIndex]) == null) {
			tempNode.childs.put(wordChar[charIndex], new TrieTreeNode());
			tempNode.childs.get(wordChar[charIndex]).value = wordChar[charIndex];
			tempNode.childs.get(wordChar[charIndex]).parent = tempNode;
		}
		tempNode.childs.get(wordChar[charIndex]).count++;
	}

	/**
	 * 删除节点
	 * 
	 * @param word
	 */
	public void deleteTrieTree(String word) {
		char[] wordChar = word.toCharArray();
		TrieTreeNode tempNode = root;
		for (int i = 0; i < wordChar.length; i++) {
			if (tempNode.childs.get(wordChar[i]) == null) {
				return;
			}
			tempNode = tempNode.childs.get(wordChar[i]);
		}

		tempNode.state = 0;

		for (int i = wordChar.length - 1; i >= 0; i--) {
			if (tempNode != root) {
				tempNode.count--;
			}
			if (!tempNode.isDelFlg()) {
				return;
			}
			tempNode = tempNode.parent;
			tempNode.childs.put(wordChar[i], null);
		}
	}
	
	/**
	 * 判断是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(char str) {
		int chr=str;
		if(chr < 48 || chr > 57) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断是否为字母
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isLetter(char str) {
		int chr=str;
		if(chr >= 65 && chr <= 90) {
			return true;
		} else if(chr >= 97 && chr <= 122) {
			return true;
		}
		return false;
	}
	
	/**
	 * 分词操作
	 * 
	 * 对汉字、英文单词、数字以及空格进行了处理，因为分词的对象全部是Google语音识别出来的句子，所以不存在其它符号，不予处理
	 * @param text
	 */
	public void searchTrieTree(String text) {
		char[] textChar = text.toCharArray();
		TrieTreeNode tempNode = root;
		int backStep= 0;
		for (int i = 0; i < textChar.length; i++) {
			if (isNumeric(textChar[i])) { //若为数字
				int start;
				start = i;
				for ( ; i < textChar.length; i++) { //将数字全部取出
					if (isNumeric(textChar[i])) {
						WORD_LEN ++;
					} else {
						i --;
						break;
					}
				}

				String str = constructWord(textChar, start + WORD_LEN - 1, WORD_LEN);
				result.add(str);
				System.out.println(str);
				WORD_LEN = 0; 
			} else if (isLetter(textChar[i])) { //若为字母
				int start;
				start = i;
				for ( ; i < textChar.length; i++) { //将整个单词取出
					if (isLetter(textChar[i])) {
						WORD_LEN ++;
					} else {
						i --;
						break;
					}
				}

				String str = constructWord(textChar, start + WORD_LEN - 1, WORD_LEN);
				str = str.toLowerCase();
				result.add(str);
				System.out.println(str);
				WORD_LEN = 0; 
			} else if (textChar[i] == 32) { //若为空格则不进入结果
				while(backStep>0){
					i--;backStep--;
				}
				tempNode = root;
				WORD_LEN = 0;
			} else if (tempNode.childs.get(textChar[i]) != null) { //若为汉字，且字典可寻
				if (tempNode.childs.get(textChar[i]).value == textChar[i]) {
					if (tempNode.childs.get(textChar[i]).state != 1) {
						WORD_LEN++;
						backStep++;
						tempNode = tempNode.childs.get(textChar[i]);
					} else {
						WORD_LEN++;
						backStep = 0;
						i = searchMaxWord(tempNode.childs.get(textChar[i]),
								textChar, i + 1);
						String str = constructWord(textChar, i, WORD_LEN);
						result.add(str);
						System.out.println(str);
						tempNode = root;
						WORD_LEN = 0;
					}
				} else {
					tempNode = root;
					WORD_LEN = 0;
				}
			} else { //若为汉字且字典不可寻，则单独输出
				String str = constructWord(textChar, i, 1);
				result.add(str);
				System.out.println(str);
				while(backStep>0){
					i--;backStep--;
				}
				tempNode = root;
				WORD_LEN = 0;
			}
		}
	}
	
	/**
	 * 从Tire树中提取完整的词语
	 * 
	 * @param textChar
	 * @param endIndex
	 * @param len
	 * @return
	 */
	private String constructWord(char[] textChar, int endIndex, int len) {
		int startIndex = endIndex + 1 - len;
		StringBuffer str = new StringBuffer();
		for (int i = startIndex; i <= endIndex; i++) {
			str.append(textChar[i]);
		}
		return str.toString();
	}

	/** 
	 * 最大正向匹配改进 
	 * 
	 * @param node
	 * @param textChar
	 * @param index
	 * @return
	 */
	private int searchMaxWord(TrieTreeNode node, char[] textChar, int index) {
		if (terminateCondition(node, textChar, index)) {
			return --index;
		}
		TrieTreeNode tempNode = node;
		for (int i = index; i < index + RECURSION_TIME; i++) {
			if (tempNode.childs.get(textChar[i]).state != 1) {
				WORD_LEN++;
				tempNode = tempNode.childs.get(textChar[i]);
			} else {
				WORD_LEN++;
				return searchMaxWord(tempNode.childs.get(textChar[i]),
						textChar, i + 1);
			}
		}
		return -1;
	}

	/**
	 * 改进算法递归终止条件
	 * 
	 * @param node
	 * @param textChar
	 * @param index
	 * @return
	 */
	private boolean terminateCondition(TrieTreeNode node, char[] textChar,
			int index) {
		TrieTreeNode tempNode = node;
		for (int i = index; i < index + RECURSION_TIME; i++) {
			if (i > textChar.length - 1) {
				return true;
			}
			if (tempNode.childs.get(textChar[i]) == null) {
				return true;
			}
			if (tempNode.childs.get(textChar[i]).state != 1) {
				tempNode = tempNode.childs.get(textChar[i]);
			} else {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 字符串处理，测试时使用
	 * 
	 * @param word
	 * @return
	 */
	public String toString(String word) {
		char[] c = word.toCharArray();
		TrieTreeNode tempNode = root;
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < c.length; i++) {
			TrieTreeNode currentNode = tempNode.childs.get(c[i]);
			if (currentNode != null) {
				buf.append(currentNode.toString());
//				buf.append("\n");
			}
			tempNode = tempNode.childs.get(c[i]);
		}
		return buf.toString();
	}
}
