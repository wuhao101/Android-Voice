package com.pyy.commandoperation;

import java.util.ArrayList;

import com.pyy.sentencesegment.TrieTree;
import com.pyy.smilarity.WordSimilarity;

/**
 * ����������棬�����ִʡ�ƥ��������ƶ�
 */
public class SpeechEngineTool {
	/**
	 * �ִʽ��
	 */
	public static ArrayList<String> out = new ArrayList<String>();
	
	public static double standard = 0.4;
	/**
	 * �������
	 * 
	 * @param w
	 * @param tt
	 * @param speakCommand
	 * @return
	 */
	public static SpeechCommandResult analysisSystemCommand(WordSimilarity w, TrieTree tt, String speakCommand)
	{		
		SpeechCommandResult result = new SpeechCommandResult();		
		//ɾ���ո�
		speakCommand = speakCommand.toLowerCase().trim(); 
		//���зִʲ���
		tt.searchTrieTree(speakCommand); 
//		//��ȡ�ִʽ��
		out = tt.result; 
		
		
		// remove the '[' and ']'
//		if (speakCommand.startsWith("[") && speakCommand.endsWith("]")) {
//			speakCommand = speakCommand.substring(1, speakCommand.length() - 1).trim();
//		}
		
		//ԭʼָ��
		String command = new String(out.get(out.size()-2));
		//ָ������
		String tdata = new String(out.get(out.size()-1));

		//���׼����������ƶȼ���
		double s1 = w.simWord(command, OperationCommandConst.DIALING);
		double s2 = w.simWord(command, OperationCommandConst.GOTO_WEB);
		double s3 = w.simWord(command, OperationCommandConst.SEARCH_GOOGLE);
		double s4 = w.simWord(command, OperationCommandConst.START_APP);
		
		
		//��ȡ�����������
		String operationtype = new String();		
		operationtype = Max(s1, s2, s3, s4); 
		
//		if(s4 >= 0.5) {//����
//			result.setOpType(OperationType.StartApp) ;
//			result.setOpData(tdata) ;
//		}
		
		/*
		 * ƥ�����ָ���ʼ�����������Լ���������
		 */	
		if(operationtype.matches(OperationCommandConst.DIALING)) {//����绰
			result.setOpType(OperationType.Dialing) ;
			result.setOpData(tdata) ;
		} else if(operationtype.matches(OperationCommandConst.GOTO_WEB)) {//������ҳ
			result.setOpType(OperationType.GotoWeb) ;
			result.setOpData(tdata) ;
		} else if(operationtype.matches(OperationCommandConst.SEARCH_GOOGLE)) {//����
			result.setOpType(OperationType.SearchGoogle) ;
			result.setOpData(tdata) ;
		} else if(operationtype.matches(OperationCommandConst.START_APP)) {//��Ӧ��
			result.setOpType(OperationType.StartApp) ;
			result.setOpData(tdata) ;
		} else {
			result.setOpType(OperationType.Unknow) ;
			result.setOpData(speakCommand) ;
		}
		
		return result ;
	}
	
	/**
	 * ���׼ָ����жԱ�
	 * 
	 * @param s1
	 * @param s2
	 * @param s3
	 * @param s4
	 * @return
	 */
	public static String Max(double s1, double s2, double s3, double s4) {
		double max = 0.0;
		int flag = 0;
		if (s1 > max) {
			max = s1;
			flag = 1;
		}
		if (s2 > max) {
			max = s2;
			flag = 2;
		}
		if (s3 > max) {
			max = s3;
			flag = 3;
		}
		if (s4 > max) {
			max = s4;
			flag = 4;
		}
		
		if (flag == 1) {
			return OperationCommandConst.DIALING;
		} else if (flag == 2) {
			return OperationCommandConst.GOTO_WEB;
		} else if (flag == 3) {
			return OperationCommandConst.SEARCH_GOOGLE;
		} else if (flag == 4) {
			return OperationCommandConst.START_APP;
		}
		
		return new String("Unkown");
	}
}

