package com.xiaoli.library.db;

import android.database.Cursor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sqlite数据层访问基类
 *  xiaokx
 *  hioyes@qq.com
 *  2014-11-6
 */
public class DaoUtils {
    public static String buildPageSql(String _sql, int pageNo, int pageSize) {
        String sql_str = _sql + " LIMIT " + (pageNo - 1) * pageSize + "," + pageSize;
        return sql_str.toString();

    }

    /**
     * 去除sql的select 子句，未考虑union的情况,用于pagedQuery.
     */
    public static String removeSelect(String sql) {
        int beginPos = sql.toLowerCase().indexOf("from");
        return sql.substring(beginPos);
    }


    /**
     * 去除sql的orderby 子句，用于pagedQuery.
     */
    public static String removeOrders(String sql) {
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*",
                Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 格式化列名
     *
     * @param col
     * @return
     */
    public static String quoteCol(String col) {
        if (col == null || col.equals("")) {
            return "";
        } else {
            return col;
        }
    }


    /**
     * 将数组成str连接成字符串
     *
     * @param str
     * @param array
     * @return
     */
    public static String implode(String str, Object[] array) {
        if (str == null || array == null) {
            return "";
        }
        String result = "";
        for (int i = 0; i < array.length; i++) {
            if (i == array.length - 1) {
                result += array[i].toString();
            } else {
                result += array[i].toString() + str;
            }
        }
        return result;
    }

    public static String implodeValue(String str, Object[] array) {
        if (str == null || array == null) {
            return "";
        }
        String result = "";
        for (int i = 0; i < array.length; i++) {
            String val = null;
            if (array[i] != null) {
                val = "'" + array[i].toString().replace("'", "") + "'";
            }
            if (i == array.length - 1) {
                result += val;
            } else {
                result += val + str;
            }
        }
        return result;
    }

    /**
     * 检查字段是否为基本类型
     * @param field
     * @return
     */
    public static boolean checkBasicType(Field field){
        if("serialVersionUID".equals(field.getName()))return false;
        String gt = field.getGenericType().toString();
        List<String> list = new ArrayList<String>();
        list.add("class java.lang.String");
        list.add("class java.lang.Integer");
        list.add("class java.lang.Long");
        list.add("class java.lang.Double");
        list.add("class java.lang.Boolean");
        list.add("class java.util.Date");
        list.add("class java.lang.Short");
        list.add("int");
        list.add("double");
        list.add("long");
        list.add("double");
        list.add("short");
        list.add("boolean");
        if(list.contains(gt))return true;
        return false;
    }

    /**
     * vo转成map
     *
     * @param vo
     * @return
     */
    public static Map voToMap(Object vo) {
        Map map = new HashMap();
        Field[] fields = vo.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if(!checkBasicType(field)){
                continue;
            }
            String name = field.getName();
            Object obj = null;
            try {
                obj = field.get(vo);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            map.put(name, obj);
        }
        if (map.isEmpty()) return null;
        return map;
    }

    /**
     * 把值设置进类属性里
     *
     * @param c
     * @param clazz
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public static <T> T cursorToVo(Cursor c, Class clazz) {
        if(clazz.toString().startsWith(Map.class.toString())){
            return cursorToMap(c);
        }
        Object obj = null;
        String[] columnNames = c.getColumnNames();// 字段数组
        try {
            obj = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field _field : fields) {
                Class<? extends Object> typeClass = _field.getType();// 属性类型
                if(typeClass.toString().startsWith("interface"))continue;
                for (int j = 0; j < columnNames.length; j++) {
                    String columnName = columnNames[j];
                    typeClass = getBasicClass(typeClass);
                    //if typeclass is basic class ,package.if not,no change
                    boolean isBasicType = isBasicType(typeClass);

                    if (isBasicType) {// 是基本类型
                        if (columnName.equalsIgnoreCase(_field.getName())) {
                            int cindex = c.getColumnIndex(columnName);
                            String _str = c.getString(cindex);
                            if (_str == null) {
                                break;
                            }
                            _str = _str == null ? "" : _str;
                            //if value is null,make it to ""
                            //use the constructor to init a attribute instance by the value
                            Constructor<? extends Object> cons = typeClass
                                    .getConstructor(String.class);
                            Object attribute = cons.newInstance(_str);
                            _field.setAccessible(true);
                            //give the obj the attr
                            _field.set(obj, attribute);
                            break;
                        }
                    } else {
                        Object obj2 = cursorToVo(c, typeClass);// 递归
                        _field.set(obj, obj2);
                        break;
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) obj;
    }

    public static <T> T cursorToMap(Cursor c) {
        Map map = new HashMap();
        for (int i=0;i<c.getColumnCount();i++){
            map.put(c.getColumnName(i),c.getString(i));
        }
        return (T) map;
    }

    /**
     * 通过Cursor转换成对应的VO集合。注意：Cursor里的字段名（可用别名）必须要和VO的属性名一致
     *
     * @param c
     * @param clazz
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List cursorToList(Cursor c, Class clazz) {
        if (c == null) {
            return null;
        }
        List list = new LinkedList();
        Object obj;
        try {
            while (c.moveToNext()) {
                obj = cursorToVo(c, clazz);

                list.add(obj);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR @：cursor2VOList");
            return null;
        } finally {
            c.close();
        }
    }

    /**
     * 获得包装类
     *
     * @param typeClass
     * @return
     */
    @SuppressWarnings("all")
    public static Class<? extends Object> getBasicClass(Class typeClass) {
        Class _class = basicMap.get(typeClass);
        if (_class == null)
            _class = typeClass;
        return _class;
    }

    /**
     * 判断是不是基本类型
     *
     * @param typeClass
     * @return
     */
    @SuppressWarnings("rawtypes")
    private static boolean isBasicType(Class typeClass) {
        if (typeClass.equals(Integer.class) || typeClass.equals(Long.class)
                || typeClass.equals(Float.class)
                || typeClass.equals(Double.class)
                || typeClass.equals(Boolean.class)
                || typeClass.equals(Byte.class)
                || typeClass.equals(Short.class)
                || typeClass.equals(String.class)) {

            return true;

        } else {
            return false;
        }
    }

    @SuppressWarnings("rawtypes")
    private static Map<Class, Class> basicMap = new HashMap<Class, Class>();

    static {
        basicMap.put(int.class, Integer.class);
        basicMap.put(long.class, Long.class);
        basicMap.put(float.class, Float.class);
        basicMap.put(double.class, Double.class);
        basicMap.put(boolean.class, Boolean.class);
        basicMap.put(byte.class, Byte.class);
        basicMap.put(short.class, Short.class);
    }
}
