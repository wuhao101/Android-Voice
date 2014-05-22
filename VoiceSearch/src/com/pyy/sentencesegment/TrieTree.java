package com.pyy.sentencesegment;

import java.util.ArrayList;


/**
 * Trie���ִʲ���
 */
public class TrieTree {
	/**
	 * ���ڵ�
	 */
	public TrieTreeNode root = new TrieTreeNode();	
	/**
	 * �������
	 */
	private static final int RECURSION_TIME = 5;
	/**
	 * ���ﳤ��
	 */
	private static int WORD_LEN = 0;
	/**
	 * ������
	 */
	public ArrayList<String> result = new ArrayList<String>();
	
	/**
	 * ��Tire���в������
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
	 * �����½ڵ�
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
	 * ɾ���ڵ�
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
	 * �ж��Ƿ�Ϊ����
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
	 * �ж��Ƿ�Ϊ��ĸ
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
	 * �ִʲ���
	 * 
	 * �Ժ��֡�Ӣ�ĵ��ʡ������Լ��ո�����˴�����Ϊ�ִʵĶ���ȫ����Google����ʶ������ľ��ӣ����Բ������������ţ����账��
	 * @param text
	 */
	public void searchTrieTree(String text) {
		char[] textChar = text.toCharArray();
		TrieTreeNode tempNode = root;
		int backStep= 0;
		for (int i = 0; i < textChar.length; i++) {
			if (isNumeric(textChar[i])) { //��Ϊ����
				int start;
				start = i;
				for ( ; i < textChar.length; i++) { //������ȫ��ȡ��
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
			} else if (isLetter(textChar[i])) { //��Ϊ��ĸ
				int start;
				start = i;
				for ( ; i < textChar.length; i++) { //����������ȡ��
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
			} else if (textChar[i] == 32) { //��Ϊ�ո��򲻽�����
				while(backStep>0){
					i--;backStep--;
				}
				tempNode = root;
				WORD_LEN = 0;
			} else if (tempNode.childs.get(textChar[i]) != null) { //��Ϊ���֣����ֵ��Ѱ
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
			} else { //��Ϊ�������ֵ䲻��Ѱ���򵥶����
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
	 * ��Tire������ȡ�����Ĵ���
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
	 * �������ƥ��Ľ� 
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
	 * �Ľ��㷨�ݹ���ֹ����
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
	 * �ַ�����������ʱʹ��
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
