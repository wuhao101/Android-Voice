package com.pyy.commandoperation;

/**
 * 操作命令类
 */
public class SpeechCommandResult {
	private OperationType opType  ;
	private String opData  ;
	
	/**
	 * 获取操作类型
	 * 
	 * @return
	 */
	public OperationType getOpType() {
		return opType;
	}
	
	/**
	 * 设置操作类型
	 * 
	 * @param opType
	 */
	public void setOpType(OperationType opType) {
		this.opType = opType;
	}
	
	/**
	 * 获取操作内容数据
	 * 
	 * @return
	 */
	public String getOpData() {
		return opData;
	}
	
	/**
	 * 设置操作内容数据
	 * 
	 * @param opData
	 */
	public void setOpData(String opData) {
		this.opData = opData;
	}
}
