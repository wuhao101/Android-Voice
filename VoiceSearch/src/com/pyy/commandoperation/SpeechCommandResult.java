package com.pyy.commandoperation;

/**
 * ����������
 */
public class SpeechCommandResult {
	private OperationType opType  ;
	private String opData  ;
	
	/**
	 * ��ȡ��������
	 * 
	 * @return
	 */
	public OperationType getOpType() {
		return opType;
	}
	
	/**
	 * ���ò�������
	 * 
	 * @param opType
	 */
	public void setOpType(OperationType opType) {
		this.opType = opType;
	}
	
	/**
	 * ��ȡ������������
	 * 
	 * @return
	 */
	public String getOpData() {
		return opData;
	}
	
	/**
	 * ���ò�����������
	 * 
	 * @param opData
	 */
	public void setOpData(String opData) {
		this.opData = opData;
	}
}
