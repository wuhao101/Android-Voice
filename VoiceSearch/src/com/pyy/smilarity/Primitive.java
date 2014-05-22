package com.pyy.smilarity;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ��ԭ
 */
public class Primitive {
    public static Map<Integer, Primitive> ALLPRIMITIVES = new HashMap<Integer, Primitive>();
    public static Map<String, Integer> PRIMITIVESID = new HashMap<String, Integer>();
    /**
     * ������ԭ�ļ���
     */
    Primitive(BufferedReader reader) {
        String line = null;
        try {
            line = reader.readLine();

            while (line != null) {
                line = line.trim().replaceAll("\\s+", " ");

                String[] strs = line.split(" ");
                int id = Integer.parseInt(strs[0]);
                String[] words = strs[1].split("\\|");
                String english = words[0];
                String chinaese = strs[1].split("\\|")[1];
                int parentId = Integer.parseInt(strs[2]);
                ALLPRIMITIVES.put(id, new Primitive(id, chinaese, parentId));
                //ALLPRIMITIVES.put(id, new Primitive(id, english, parentId));
                PRIMITIVESID.put(chinaese, id);
                PRIMITIVESID.put(english, id);
                // System.out.println("add: " + primitive + " " + id + " " + parentId);
                line = reader.readLine();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(line);
            e.printStackTrace();
        }
    }

    private String primitive;

    /**
     * id number
     */
    private int id;
    private int parentId;

    /**
     * ��ԭ����
     * 
     * @param id
     * @param primitive
     * @param parentId
     */
    public Primitive(int id, String primitive, int parentId) {
        this.id = id;
        this.parentId = parentId;
        this.primitive = primitive;
    }

    /**
     * ��ȡ��ԭ
     * 
     * @return
     */
    public String getPrimitive() {
        return primitive;
    }

    /**
     * ��ȡ��ԭID
     * 
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * ��ȡ����ԭID
     * 
     * @return
     */
    public int getParentId() {
        return parentId;
    }

    /**
     * ����Ƿ�Ϊ���ڵ�
     * 
     * @return
     */
    public boolean isTop() {
        return id == parentId;
    }

    /**
     * ���һ����ԭ�����и���ԭ��ֱ������λ�á�
     * 
     * @param primitive
     * @return ������ҵ���ԭû�в��ҵ����򷵻�һ����list
     */
    public static List<Integer> getParents(String primitive) {
        List<Integer> list = new ArrayList<Integer>();

        // get the id of this primitive
        Integer id = PRIMITIVESID.get(primitive);

        if (id != null) {
            Primitive parent = ALLPRIMITIVES.get(id);
//            list.add(id);
            while (!parent.isTop()) {
                list.add(parent.getId());
                parent = ALLPRIMITIVES.get(parent.getParentId());
            }
            list.add(parent.getId());
        }

        return list;
    }
    
    
    /**
     * �ж��Ƿ�Ϊ��ԭ
     * 
     * @param primitive
     * @return
     */
    public static boolean isPrimitive(String primitive){
        return PRIMITIVESID.containsKey(primitive);
    }
    
    /**
     * ��õ�һ���������ڵ����ȣ������������ԭ���ƶ�ƥ��ĵ�3����ʽ
     * 
     * @param premitive1
     * @param premitive2
     * @return
     */
    public static int getcommonparentdepth(String premitive1, String premitive2) {
    	List<Integer> list1 = new ArrayList<Integer>();
    	List<Integer> list2 = new ArrayList<Integer>();
    	
    	list1 = getParents(premitive1);
    	list2 = getParents(premitive2);
    	
    	int num1 = list1.size();
    	int num2 = list2.size();
    	
    	if (num1 == 1 || num2 == 1) {
    		return 1;
    	}
    	
    	int num = 1;
    	int icount;
    	int jcount;
    	
    	if (num1 < num2) {
    		for (int i = 1; i < num1; i++) {
    			icount = list1.get(i);
    			for (int j = 1; j < num2; j++) {
    				jcount = list2.get(j);
    				if (jcount == icount) {
    					return (num1 - i);
    				}
    			}
    		}
    	} else {
    		for (int i = 1; i < num2; i++) {
    			icount = list2.get(i);
    			for (int j = 1; j < num1; j++) {
    				jcount = list1.get(j);
    				if (jcount == icount) {
    					return (num2 - i);
    				}
    			}
    		}
    	}
    	
    	return num;
    }
}