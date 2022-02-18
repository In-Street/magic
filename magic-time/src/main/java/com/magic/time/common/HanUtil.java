package com.magic.time.common;

import cn.hutool.core.io.FileUtil;
import jodd.util.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Cheng Yufei
 * @create 2022-01-08 7:06 PM
 **/
public class HanUtil {
    //private static String INTERVAL = "，|,|。|！|？|；|、|'|\\.|!|\\?|;|\n|\t|\r|\\s";
    private static String INTERVAL = "。|！|？|!";


    /**
     * 根据INTERVAL中定义的符号对文本进行短句切分,
     *     若某个句子长度小于10, 则将其与前一句子合并,
     *     对过滤完的每个句子过滤所有的标点符号,
     *     e.g. "李四: 砥砺前行" & "李四————砥砺前行"
     * @param text
     * @return
     */
    public static String[] content_split(String text){
        text = text.trim();
        String[] split = text.split(INTERVAL);
       /* for(int i = 0; i < split.length; i++){
            split[i] = filterPunc(split[i]);
        }*/
        int i = 1;
        while(i < split.length-1){
            //需要区分中英文, 英文单词前后用_连接
            //这样保证ES检索过程能够准确切词
            String[] findall = findall("[a-zA-Z]+", split[i]);
            String type= (ArrayUtils.isEmpty(findall)||findall[0].length()<split[i].length())?"zh":"en";
            if(split[i].length() < 50 && StringUtil.equals("zh", type)){
                split[i-1] += split[i];
                split = remove(split, i);
                i -= 1;
            }else if(split[i].length() < 10 && StringUtil.equals("en", type)){
                split[i-1] += '_' + split[i];
                split = remove(split, i);
                i -= 1;
            }
            i += 1;
        }
        return split;
    }

    public static String[] remove(String[] strs, int i){
        List<String> strings = Arrays.asList(strs);
        List<String> arrList = new ArrayList<String>(strings);
        arrList.remove(i);
        String[] res = new String[arrList.size()];

        arrList.toArray(res);
        return res;
    }

    private static String filterPunc(String text){
//        return sub("[’!\"#$%&\\'()*+,-./:;<=>?@，。?★、…【】■●《》（）：；——？“”‘’！[\\\\]^_`{|}~]+","", text);
        return text.replaceAll("[’!\"#$%&\\'()*+,-./:;<=>?@，。?★、…【】■●《》（）：；——？“”‘’！[\\\\]^_`{|}~]+","");
    }


    private static String textFilter(String text){
        text = filter_html(text);
        text = subCharEntities(text);
        text = filterEmoji(text);
        return text;
    }

    /**
     * 替换正文中的HTML实体, 过滤HTML标签语句和颜文字
     * @param text
     * @return
     */
    private static String filter_html(String text){
        // 匹配CDATA
        text = text.replaceAll("//<!\\[CDATA\\[[^>]*//\\]\\]>","");
//        text = sub("//<!\\[CDATA\\[[^>]*//\\]\\]>","", text);
        // 匹配Script
        text = text.replaceAll("<\\s*script[^>]*>[^<]*<\\s*/\\s*script\\s*>","");
        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>";
        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
        Matcher m_script=p_script.matcher(text);
        text=m_script.replaceAll("");
//        text = sub("<\\s*script[^>]*>[^<]*<\\s*/\\s*script\\s*>", "", text);
        // 匹配style
        text = text.replaceAll("<\\s*style[^>]*>[^<]*<\\s*/\\s*style\\s*>","");
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>";
        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
        Matcher m_style=p_style.matcher(text);
        text=m_style.replaceAll("");

//        text = sub("<\\s*style[^>]*>[^<]*<\\s*/\\s*style\\s*>", "", text);
        // 匹配换行
        text = text.replaceAll("<br\\s*?/?>","\n");
//        text = sub("<br\\s*?/?>", "\n", text);
        // 匹配HTML标签
        text = text.replaceAll("</?\\w+[^>]*>","");
        String regEx_html="<[^>]+>";
        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
        Matcher m_html=p_html.matcher(text);
        text=m_html.replaceAll(""); //过滤html标签

//        text = sub("</?\\w+[^>]*>", "", text);
        // 匹配HTML注释
        text = text.replaceAll("<!--[^>]*-->","");
//        text = sub("<!--[^>]*-->", "", text);
        // 去掉多余的换行
        text = text.replaceAll("\n+", "\n");
//        text = sub("\n+", "\n", text);
        text = text.replaceAll("\\s+", " ");
//        text = sub("\\s+", " ", text);
//        text = sub("\\u[0-9a-zA-Z]{3,5}", " ", text);
//        text = sub("\\x[0-9a-zA-Z]{2}", " ", text);
        return text;
    }

    /**
     * 按正则式查找并替换
     * @param regex
     * @param replace
     * @param str
     * @return
     */
    private static String sub(String regex, String replace, String str){
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
//        StringBuffer sb = new StringBuffer();
//        while (m.find()) {
//            m.appendReplacement(sb, replace);
//        }
//        m.appendTail(sb);
        String s = m.replaceAll(replace);
        return s;
    }

    /**
     * 获取符合正则式的字符串
     * @param regex
     * @param text
     * @return array
     */
    private static String[] findall(String regex, String text){
        ArrayList<String> list=new ArrayList<String>();
        Pattern r = Pattern.compile(regex);
        Matcher m = r.matcher(text);
        while (m.find()) {
            list.add(m.group());
        }
        String[] strings = new String[list.size()];
        return list.toArray(strings);
    }

    private static Map<String, String> CHAR_ENTITIES = new HashMap(){{
        put("nbsp", " ");put("160", " ");
        put("lt", "<");put("60", "<");
        put("gt", ">");put("62", ">");
        put("amp", "&");put("38", "&");
        put("quot", "\"");put("34", "\"");
    }};

    /**
     * 替换HTML常用实体
     * @param text
     * @return
     */
    private static String subCharEntities(String text){
        Pattern r = Pattern.compile("&#?(\\?P<name>\\w+);");
        Matcher m = r.matcher(text);
        while (m.find()) {
            String find = m.group("'name'");
            text.replaceAll(find, CHAR_ENTITIES.get(find));
            m = r.matcher(text);
        }
        return text;
    }

    /**
     * 过滤文本中的颜文字符号
     * @param text
     * @return
     */
    private static String filterEmoji(String text){
//        String pattern = "[u\"\\\\U0001F600-\\\\U0001F64F\"  u\"\\\\U0001F300-\\\\U0001F5FF\"   u\"\\\\U0001F680-\\\\U0001F6FF\"   u\"\\\\U0001F1E0-\\\\U0001F1FF\"   u\"\\\\U0001F926-\\\\U0001F937\" u\"\\\\U00010000-\\\\U0010FFFF\"   u\"\\u200d\" u\"\\u2640-\\u2642\"]+";
//        sub(pattern, "", text);
        Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;
        Matcher emojiMatcher = emoji.matcher(text);
        if ( emojiMatcher.find())
        {
            text = emojiMatcher.replaceAll("");
            return text ;
        }
        return text;
    }
}
