package com.pyy.smilarity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 代表一个词
 */
public class Word {
    private String word;
    private String type;

    /**
     * 第一基本义原。
     */
    private String firstPrimitive;

    /**
     * 其他基本义原。
     */
    private List<String> otherPrimitives = new ArrayList<String>();

    /**
     * 如果该list非空，则该词是一个虚词。 列表里存放的是该虚词的一个义原，部分虚词无中文虚词解释
     */
    private List<String> structruralWords = new ArrayList<String>();

    /**
     * 该词的关系义原。key: 关系义原。 value： 基本义原|(具体词)的一个列表
     */
    private Map<String, List<String>> relationalPrimitives = new HashMap<String, List<String>>();

    /**
     * 该词的关系符号义原。Key: 关系符号。 value: 属于该挂系符号的一组基本义原|(具体词)
     */
    private Map<String, List<String>> relationSimbolPrimitives = new HashMap<String, List<String>>();

    /**
     * 获取词语（概念）
     *
     * @return 
     */
    public String getWord() {
        return word;
    }
    /**
     * 判断是否为虚词
     * 
     * @return
     */
    public boolean isStructruralWord(){
        return !structruralWords.isEmpty();
    }

    /**
     * 初始化词语（概念）
     *
     * @param word
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * 获取词语类型
     *
     * @return 
     */
    public String getType() {
        return type;
    }

    /**
     * 设置词语类型
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取词语（概念）的第一基本义原描述式
     *
     * @return
     */
    public String getFirstPrimitive() {
        return firstPrimitive;
    }

    /**
     * 设置词语（概念）的第一基本义原描述式
     *
     * @param firstPrimitive
     */
    public void setFirstPrimitive(String firstPrimitive) {
        this.firstPrimitive = firstPrimitive;
    }

    /**
     * 获取词语（概念）的其它基本义原描述式
     *
     * @return
     */
    public List<String> getOtherPrimitives() {
        return otherPrimitives;
    }

    /**
     * 设置（概念）的其它基本义原描述式
     *
     * @param otherPrimitives
     */
    public void setOtherPrimitives(List<String> otherPrimitives) {
        this.otherPrimitives = otherPrimitives;
    }

    /**
     * 添加设置（概念）的其它基本义原描述式到集合
     *
     * @param otherPrimitive
     */
    public void addOtherPrimitive(String otherPrimitive) {
        this.otherPrimitives.add(otherPrimitive);
    }

    /**
     *获取虚词
     *
     * @return
     */
    public List<String> getStructruralWords() {
        return structruralWords;
    }

    /**
     *添加虚词
     *
     * @param structruralWords
     */
    public void setStructruralWords(List<String> structruralWords) {
        this.structruralWords = structruralWords;
    }

    /**
     *添加虚词
     *
     * @param structruralWord
     */
    public void addStructruralWord(String structruralWord) {
        this.structruralWords.add(structruralWord);
    }

    /**
     * 添加关系义原描述式
     *
     * @param key
     * @param value
     */
    public void addRelationalPrimitive(String key, String value) {
        List<String> list = relationalPrimitives.get(key);

        if (list == null) {//该属性还不存在
            list = new ArrayList<String>();
            list.add(value);
            relationalPrimitives.put(key, list);
        } else {//存在，直接对原特征结构进行追加
            list.add(value);
        }
    }
    /**
     * 添加符号义原描述式
     * 
     * @param key
     * @param value
     */
    public void addRelationSimbolPrimitive(String key,String value){
        List<String> list = relationSimbolPrimitives.get(key);

        if (list == null) {//该属性还不存在
            list = new ArrayList<String>();
            list.add(value);
            relationSimbolPrimitives.put(key, list);
        } else {//存在，直接对原特征结构进行追加
            list.add(value);
        }
    }
    /**
     * 获取关系义原描述式的特征结构
     * 
     * @return
     */
    public Map<String, List<String>> getRelationalPrimitives() {
        return relationalPrimitives;
    }
    /**
     * 获取符号义原描述式的特征结构
     * 
     * @return
     */
    public Map<String, List<String>> getRelationSimbolPrimitives() {
        return relationSimbolPrimitives;
    }
}