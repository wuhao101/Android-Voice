package com.pyy.smilarity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ����һ����
 */
public class Word {
    private String word;
    private String type;

    /**
     * ��һ������ԭ��
     */
    private String firstPrimitive;

    /**
     * ����������ԭ��
     */
    private List<String> otherPrimitives = new ArrayList<String>();

    /**
     * �����list�ǿգ���ô���һ����ʡ� �б����ŵ��Ǹ���ʵ�һ����ԭ�����������������ʽ���
     */
    private List<String> structruralWords = new ArrayList<String>();

    /**
     * �ôʵĹ�ϵ��ԭ��key: ��ϵ��ԭ�� value�� ������ԭ|(�����)��һ���б�
     */
    private Map<String, List<String>> relationalPrimitives = new HashMap<String, List<String>>();

    /**
     * �ôʵĹ�ϵ������ԭ��Key: ��ϵ���š� value: ���ڸù�ϵ���ŵ�һ�������ԭ|(�����)
     */
    private Map<String, List<String>> relationSimbolPrimitives = new HashMap<String, List<String>>();

    /**
     * ��ȡ������
     *
     * @return 
     */
    public String getWord() {
        return word;
    }
    /**
     * �ж��Ƿ�Ϊ���
     * 
     * @return
     */
    public boolean isStructruralWord(){
        return !structruralWords.isEmpty();
    }

    /**
     * ��ʼ��������
     *
     * @param word
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * ��ȡ��������
     *
     * @return 
     */
    public String getType() {
        return type;
    }

    /**
     * ���ô�������
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * ��ȡ�������ĵ�һ������ԭ����ʽ
     *
     * @return
     */
    public String getFirstPrimitive() {
        return firstPrimitive;
    }

    /**
     * ���ô������ĵ�һ������ԭ����ʽ
     *
     * @param firstPrimitive
     */
    public void setFirstPrimitive(String firstPrimitive) {
        this.firstPrimitive = firstPrimitive;
    }

    /**
     * ��ȡ������������������ԭ����ʽ
     *
     * @return
     */
    public List<String> getOtherPrimitives() {
        return otherPrimitives;
    }

    /**
     * ���ã����������������ԭ����ʽ
     *
     * @param otherPrimitives
     */
    public void setOtherPrimitives(List<String> otherPrimitives) {
        this.otherPrimitives = otherPrimitives;
    }

    /**
     * ������ã����������������ԭ����ʽ������
     *
     * @param otherPrimitive
     */
    public void addOtherPrimitive(String otherPrimitive) {
        this.otherPrimitives.add(otherPrimitive);
    }

    /**
     *��ȡ���
     *
     * @return
     */
    public List<String> getStructruralWords() {
        return structruralWords;
    }

    /**
     *������
     *
     * @param structruralWords
     */
    public void setStructruralWords(List<String> structruralWords) {
        this.structruralWords = structruralWords;
    }

    /**
     *������
     *
     * @param structruralWord
     */
    public void addStructruralWord(String structruralWord) {
        this.structruralWords.add(structruralWord);
    }

    /**
     * ��ӹ�ϵ��ԭ����ʽ
     *
     * @param key
     * @param value
     */
    public void addRelationalPrimitive(String key, String value) {
        List<String> list = relationalPrimitives.get(key);

        if (list == null) {//�����Ի�������
            list = new ArrayList<String>();
            list.add(value);
            relationalPrimitives.put(key, list);
        } else {//���ڣ�ֱ�Ӷ�ԭ�����ṹ����׷��
            list.add(value);
        }
    }
    /**
     * ��ӷ�����ԭ����ʽ
     * 
     * @param key
     * @param value
     */
    public void addRelationSimbolPrimitive(String key,String value){
        List<String> list = relationSimbolPrimitives.get(key);

        if (list == null) {//�����Ի�������
            list = new ArrayList<String>();
            list.add(value);
            relationSimbolPrimitives.put(key, list);
        } else {//���ڣ�ֱ�Ӷ�ԭ�����ṹ����׷��
            list.add(value);
        }
    }
    /**
     * ��ȡ��ϵ��ԭ����ʽ�������ṹ
     * 
     * @return
     */
    public Map<String, List<String>> getRelationalPrimitives() {
        return relationalPrimitives;
    }
    /**
     * ��ȡ������ԭ����ʽ�������ṹ
     * 
     * @return
     */
    public Map<String, List<String>> getRelationSimbolPrimitives() {
        return relationSimbolPrimitives;
    }
}