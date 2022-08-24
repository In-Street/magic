package com.magic.interview.service.valueandel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Cheng Yufei
 * @create 2021-01-28 2:40 下午
 **/
@Service
@Slf4j
public class ValueService {

    @Value("#{'${test.list:}'.split(',')}")
    private List<String> splitList;

    @Value("#{'${test.list:}'.empty ? null : '${test.list:}'.split(',')}")
    private List<String> whetherNullSplitList;

    @Value("#{'${test.list:}'}")
    private List<String> list;

    @Value("${test.list}")
    private String oriString;

    @Value("${test.list}")
    private List<String> oriList;

    @Value("#{${test.map}}")
    private Map<String, Object> map;

    @Autowired
    private ValueConfig config;

    public String getList() {
        list.stream().forEach(System.out::println);
        System.out.println("list.size: " + list.size());
        System.out.printf("========");

        splitList.stream().forEach(System.out::println);
        System.out.println("splitList.size: " + splitList.size());
        System.out.printf("========");


        System.out.println("whetherNullSplitList: " + whetherNullSplitList);

        oriList.stream().forEach(System.out::println);
        System.out.println("oriList.size: " + oriList.size());
        System.out.println(oriString);
        System.out.printf("========");

        System.out.println(config.getList2());

        return list.toString();
    }
}

/**
 * yml 中List key存在时：
 * ${test.list} 与 #{'${test.list}'} 效果一样都为 List<String>;
 *
 *${test.list} 用String接收，就为设置的字符串；
 *
 *--------------------------------------------------------------------------------------
 *
 * A: yml 中配置的List key【元素逗号分割形式】不存在时：
 *     1。@Value("${test.list}") \ @Value("#{'${test.list}'}") 两者启动均报错。
 *
 *     2。@Value("#{'${test.list:}'}")，：提供默认值，启动不报错，size=0。
 *
 *     3。@Value("#{'${test.list:}'.split(',')}")，：提供默认值，启动不报错，包含一个空串，size=1。
 *
 *  B: yml  中配置的List【元素以 - 分割形式】，需以类，属性List 来加载，耦合性高，建议不采用。
 *
 *  C: yml 中配置 Map： '{"key": value, "key": "zhangsan"}'
 *      1。@Value("#{${test.map}}")
 *
 */
