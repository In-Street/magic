import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.google.gson.JsonObject;
import com.magic.dao.mapper.UserMapper;
import com.magic.dao.model.User;
import com.magic.interview.service.validated.LombokDto;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.fields.PdfForm;
import com.spire.pdf.security.PdfSignature;
import com.spire.pdf.widget.PdfFormFieldWidgetCollection;
import com.spire.pdf.widget.PdfFormWidget;
import com.spire.pdf.widget.PdfSignatureFieldWidget;
import io.lettuce.core.ScriptOutputType;
import jodd.template.StringTemplateParser;
import jodd.util.CharUtil;
import jodd.util.StringUtil;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.functors.AnyPredicate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.WordUtils;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Node;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.*;

/**
 * @author Cheng Yufei
 * @create 2019-11-04 14:20
 **/
public class TestC {

    /**
     * Preconditions: 静态导入方法
     */
    @Test
    public void checkException() {
        String uid = "aa", pwd = " v";
        //抛出java.lang.NullPointerException
        String s = checkNotNull(uid);

        checkArgument(!StringUtils.isAnyBlank(uid, pwd), "用户名或密码为空！");

        ArrayList<String> strings = Lists.newArrayList("a", "b", "c");
        //[0,size)
        checkElementIndex(3, strings.size());
        //[0,size]
        checkPositionIndex(3, strings.size());

    }

    /**
     * BeanWrapper
     */
    @Test
    public void beanwapper() {

        LombokDto dto = new LombokDto();
        dto.setCid(1);
        dto.setName("北京");
        dto.setTime(new Date());

        Field[] fields = LombokDto.class.getDeclaredFields();

        BeanWrapper wrapper = new BeanWrapperImpl(dto);
        Stream.of(fields).forEach(f -> System.out.println(wrapper.getPropertyValue(f.getName())));
    }

    /**
     * 集合工具类
     */
    @Test
    public void collection() {

        ArrayList<String> list_1 = Lists.newArrayList("A", "B", "C", "D", "B");
        ArrayList<String> list_2 = Lists.newArrayList("C", "D", "e", "f", "H");

        //-------------------------------CollectionUtils-------------------------------

        //交集 [C, D]
        List<String> retainList = ((List<String>) CollectionUtils.intersection(list_1, list_2));
        System.out.println(retainList);

        //并集 [A, B, B, C, D, e, f, H]
        Collection<String> unionList = CollectionUtils.union(list_1, list_2);
        System.out.println(unionList);

        //差集(前者-后者): [A, B, B]---[e, f, H]
        Collection<String> subtractList = CollectionUtils.subtract(list_1, list_2);
        Collection<String> subtractList_2 = CollectionUtils.subtract(list_2, list_1);
        System.out.println(subtractList + "---" + subtractList_2);

        //两个集合的对称差异：[A, B, B, e, f, H]
        Collection<String> disjunction = CollectionUtils.disjunction(list_1, list_2);
        System.out.println(disjunction);

        //按条件取集合: [A]
        Collection<String> selectList = CollectionUtils.select(list_1, AnyPredicate.anyPredicate(e -> e.equals("A")));
        System.out.println(selectList);

        //两者元素是否完全相等: false
        boolean equalCollection = CollectionUtils.isEqualCollection(list_1, list_2);
        System.out.println(equalCollection);

        //元素出现次数：key：集合元素 value：次数  {A=1, B=2, C=1, D=1}
        Map<String, Integer> cardinalityMap = CollectionUtils.getCardinalityMap(list_1);
        System.out.println(cardinalityMap);

        //-------------------------------Collections-------------------------------

        //没有共同之处返回则true ： false
        boolean disjoint = Collections.disjoint(list_1, list_2);
        System.out.println(disjoint);


        String max = Collections.min(list_1);


        //交换位置
        Collections.swap(list_1, 0, 2);

        //所有元素都替换为指定元素
        Collections.fill(list_1, "P");

        int frequency = Collections.frequency(list_1, "B");

        Collections.sort(list_1, Comparator.naturalOrder());

        //反转
        Collections.reverse(list_1);

        //洗牌
        Collections.shuffle(list_1);

        ArrayList<String> list_3 = Lists.newArrayList("a", "b", "c");
        String[] arrays = {"d", "e"};
        //将数组直接添加到集合
        CollectionUtils.addAll(list_3, arrays);
        System.out.println(list_3);

    }

    /**
     * commons-io
     * 文件操作：FileUtils
     * IO操作： IOUtils
     */
    @Test
    public void io() throws IOException {

        //读取指定文件所有行到List中
        List<String> strings = FileUtils.readLines(new File("D:/新建文本文档.txt"), "utf-8");
        System.out.println(strings);

        //string 写到文件中,覆盖原文本
        FileUtils.writeStringToFile(new File("D:/新建文本文档.txt"), "写数据到文件中", "utf-8");

        //在原文本后拼接
        FileUtils.writeStringToFile(new File("D:/新建文本文档.txt"), "写数据到文件中333dd", "utf-8", true);

        FileUtils.writeLines(new File("D:/新建文本文档.txt"), Lists.newArrayList("接口连接"), "utf-8", true);

        //获取路径下以固定后缀的文件
        File director = new File("D:/test");
        Collection<File> files = FileUtils.listFiles(director, new String[]{"txt"}, true);
        System.out.println(files);


        //流中读取到List中
        List<String> readLines = IOUtils.readLines(new FileInputStream(new File("D:/新建文本文档.txt")), "utf-8");

        // 将输入流信息全部输出到字节数组中
        //byte[] b = IOUtils.toByteArray(request.getInputStream());

        // 将输入流信息转化为字符串
        //String resMsg = IOUtils.toString(request.getInputStream());
    }

    @Test
    public void pairAndTriple() {
        ImmutablePair<String, String> pair = ImmutablePair.of("A", "a");
        System.out.println(pair.left + "----" + pair.right);

        ImmutableTriple<String, String, String> triple = ImmutableTriple.of("A", "a", "c");
        System.out.println(triple.left + "---" + triple.middle + "---" + triple.right);
    }

    @Test
    public void encrypt() {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword("e2TxKdz");

        String res = textEncryptor.encrypt("root");
        System.out.println(res);
        System.out.println(textEncryptor.encrypt("123456"));
    }

    @Test
    public void streamCreate() {
        //iterate: 第一个参数初始值，第二个参数操作
        Stream.iterate(10, n -> n + 1).limit(5).forEach(v -> System.out.println(v));

        Stream.generate(() -> ThreadLocalRandom.current().nextInt(10)).limit(5).forEach(v -> System.out.println(v));

        List<Double> collect = Stream.of(1.2, 2.3, 3.4).collect(Collectors.toList());
        List<Double> collect1 = DoubleStream.of(1.2, 2.3).boxed().collect(Collectors.toList());
        System.out.println(collect + "---" + collect1);
    }

    @Test
    public void concatStream() {

        //流拼接
        Stream<Integer> stream = Stream.of(1, 2, 3);
        Stream<Integer> stream2 = Stream.of(4, 5, 6);
        Stream<Integer> stream3 = Stream.of(7, 8, 9);

      /*  List<Integer> collect = Stream.concat(stream, stream2).collect(Collectors.toList());
        System.out.println(collect);*/

        List<Integer> collect1 = Stream.of(stream, stream2, stream3).flatMap(Function.identity()).collect(Collectors.toList());
        System.out.println(collect1);

    }

    @Test
    public void list() {
        ArrayList<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        System.out.println(list);
        Integer a = 1;
        //int a = 1;
        //包装类删除时，查找的是对象 而不是下标
        list.remove(a);
        System.out.println(list);

    }


    @Test
    public void html() throws IOException {

        String uri = "/Users/chengyufei/Downloads/project/self/Gitee/magic/magic-interview/src/test/java/top.html";

       /* List<String> list = Files.readAllLines(Paths.get(uri), Charset.forName("utf-8"));
        String htmls =list.toString();
        System.out.println(htmls);*/

        StringBuilder builder = new StringBuilder();
        Stream<String> lines = Files.lines(Paths.get(uri));
        lines.forEach(s -> builder.append(s));

        String htmls = builder.toString();

        HashMap<String, String> map = new HashMap<>();
        map.put("name", "榜单");
        map.put("city", "北京");

        StringTemplateParser templateParser = new StringTemplateParser();
        String parse = templateParser.parse(htmls, str -> map.get(str));
        System.out.println(parse);

    }

    /**
     * 字符串分割：StringUtils \ Splitter
     */
    @Test
    public void split() {

        String s = "a,,b,c, ,d";
        //["a","b","c"," ","d"]
        String[] split = StringUtils.split(s, ",");
        //["a","","b","c"," ","d"]
        String[] strings = StringUtils.splitByWholeSeparatorPreserveAllTokens(s, ",");

        //["a","b","c"," ","d"]
        List<String> strings1 = Splitter.on(",").omitEmptyStrings().splitToList(s);

        //["a","","b","c"," ","d"]
        List<String> strings2 = Splitter.on(",").splitToList(s);

        //["a","b","c","d"]
        List<String> strings3 = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(s);

        //Joiner.on("-").skipNulls().join()

        System.out.println();
    }

    /**
     * Apache - commons-lang3
     * jdk8 以下 时间操作用：DateUtils \ DateFormatUtils
     */
    @Test
    public void time() {
        Date now = new Date();
        Date date = DateUtils.addDays(now, 3);

        //截断：2020-05-09 00:00:00
        Date dateTruncate = DateUtils.truncate(now, Calendar.DATE);

        //返回整点时刻，忽略分钟和秒：2020-05-09 10:00:00
        Date hourTruncate = DateUtils.truncate(now, Calendar.HOUR);

        String format = DateFormatUtils.format(hourTruncate, "yyyy-MM-dd HH:mm:ss");

        System.out.println(format);
    }

    @Test
    public void client() throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        String uri = "https://api.8.jrj.com.cn/oauth/token?grant_type=client_credentials&client_id=mchhld68sBoDGZiynhn&client_secret=glUTt9aQtKZmXnRW1saTAu9oGJkPa4oo";
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = client.execute(httpGet);
        System.out.println();
    }

    @Test
    public void jwtToken() throws UnknownHostException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        LongAdder longAdder = new LongAdder();
        longAdder.add(1000);
        for (int i = 0; i < 50; i++) {
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
            longAdder.increment();
        }

        InetAddress localHost = InetAddress.getLocalHost();
        String hostAddress = localHost.getHostAddress();

    }

    @Test
    public void utcAndZoneTime() throws ParseException {

        ZoneId shangHai = ZoneId.of("Asia/Shanghai");

        //非TZ格式UTC转换
        String isoStr = "Mon Sep 28 03:27:52 UTC 2020";
        Date date = new Date(isoStr);
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(pattern.format(LocalDateTime.from(date.toInstant().atZone(shangHai))));

        DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        ZonedDateTime parse1 = ZonedDateTime.parse(isoStr, ofPattern);
        System.out.println(parse1.toInstant().atZone(shangHai).format(pattern));


        System.out.println(">>>>时区<<<<<");
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.now(), shangHai);
        ZonedDateTime zonedDateTimeZ = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Z"));
        System.out.println(zonedDateTime.format(pattern));
        System.out.println(zonedDateTimeZ.format(pattern));


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//设置时区UTC
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
//格式化，转当地时区时间
        Date after = df.parse("2018-05-23T16:05:52.123Z");
        System.out.println(after);

        //CST: 北京时间 Fri Mar 05 16:15:47 CST 2021
        System.out.println(new Date());
    }


    @Test
    public void utc() {
        ZoneId shangHai = ZoneId.of("Asia/Shanghai");
        String time = "2018-05-23T16:05:52.123Z";
        //需指定UTC时区，否则使用ZonedDateTime parse时报错
        DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("UTC"));
        DateTimeFormatter commonFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //无效
        System.out.println(LocalDateTime.from(utcFormatter.parse(time)).atZone(shangHai).format(commonFormatter));
        System.out.println(LocalDateTime.from(utcFormatter.parse(time)).toInstant(ZoneOffset.of("+8")).atZone(shangHai).format(commonFormatter));

        ZonedDateTime parse = ZonedDateTime.parse(time, utcFormatter);
        //无效
        System.out.println(parse.toLocalDateTime().format(commonFormatter));
        System.out.println(parse.toLocalDateTime().atZone(shangHai).format(commonFormatter));

        // 有效：utc(TZ) -> cst: 2018-05-24 00:05:52
        System.out.println(parse.toInstant().atZone(shangHai).toLocalDateTime().format(commonFormatter));
        // 有效： utc -> cst:2018-05-24 00:05:52
        System.out.println(parse.withZoneSameInstant(shangHai).toLocalDateTime().format(commonFormatter));

        //有效： utc -> cst
        String isoStr = "Mon Sep 28 03:27:52 UTC 2020";
        DateTimeFormatter utcFormatter2 = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        ZonedDateTime parse1 = ZonedDateTime.parse(isoStr, utcFormatter2);
        System.out.println(parse1.toInstant().atZone(shangHai).toLocalDateTime().format(commonFormatter));

    }

    @Test
    public void with() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        //with:修改某些值
        System.out.println(now.withHour(12));

        System.out.println(LocalDateTime.now());

        System.out.println(LocalDateTime.ofInstant(Instant.ofEpochMilli(1602833429795L), ZoneId.of("Asia/Shanghai")));

        System.out.println(Clock.systemDefaultZone().millis());
        System.out.println(System.currentTimeMillis());
        System.out.println(Instant.now().toEpochMilli());
    }

    @Test
    public void uriComponent() {
        String url = "http://localhost:9090/uer/getInfo?name=123&age={age}&gender={gender}";
        UriComponents components = UriComponentsBuilder.fromHttpUrl(url).build();

        //http
        System.out.println(components.getScheme());
        //localhost
        System.out.println(components.getHost());

        System.out.println(components.getPort());
        // name=123&age={age}&gender={gender}
        System.out.println(components.getQuery());
        //   /uer/getInfo
        System.out.println(components.getPath());

        MultiValueMap<String, String> queryParams = components.getQueryParams();
        List<String> nameValue = queryParams.get("name");
        System.out.println("参数:" + nameValue);

        // 请求参数填充
        UriComponents expand = components.expand(25, "man");
        //http://localhost:9090/uer/getInfo?name=123&age=25&gender=man
        System.out.println(expand.toUriString());

    }

    @Test
    public void SecureRandomTest() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //TODO BY Cheng Yufei <-2020-11-25 14:29->
        // 使用方法？
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        int i = secureRandom.nextInt(100);
        System.out.println(i);

        byte[] bytes = new byte[20];
        SecureRandom secureRandom1 = new SecureRandom();
        secureRandom1.nextBytes(bytes);
        System.out.println(new String(Base64.getEncoder().encode(bytes)));

        String base = "abcdefghijklmnopqrstuvwxyz";
        System.out.println(RandomUtil.randomString(base, 2));
    }

    @Test
    public void lll() throws UnsupportedEncodingException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        String name = "A-1.txt";
        System.out.println(FilenameUtils.getExtension(name));
        System.out.println(FilenameUtils.getName(name));
        System.out.println(FilenameUtils.getBaseName(name));
        System.out.println(FilenameUtils.getPath(name));

        String es = "<a href=\">ssss</a>";
        System.out.println(StringEscapeUtils.escapeHtml4(es));
        System.out.println(StringEscapeUtils.unescapeHtml4(es));

        String content = "测试";
        System.out.println(URLEncoder.encode(content, "utf-8"));
        System.out.println(URLDecoder.decode(content, "utf-8"));
        System.out.println(URLDecoder.decode("%E6%B5%8B%E8%AF%95", "utf-8"));
        //unicode => 汉字
        System.out.println(UnicodeUtil.toString("\u5927\u6D1B"));
        char c = (char) Integer.parseInt("6D1B", 16);
        System.out.println(UnicodeUtil.toUnicode("大洛"));

        ReceivingAddressDto dto = new ReceivingAddressDto();
        dto.setAddress("北京市");
        dto.setUsername("测试");
        System.out.println(PropertyUtils.describe(dto));
        System.out.println(PropertyUtils.getSimpleProperty(dto, "username"));

    }

    @Test
    public void java8Map() {
        HashMap<String, String> map = new HashMap<>();
        String orDefault = map.getOrDefault("B", "空数据");
        //区别在于：map为null时，也会返回默认值。而不会报NPE。
        String b = MapUtils.getString(map, "B", "util-空数据");
        System.out.println("getOrDefault 与MapUtils : " + orDefault + "---" + b);
        //System.out.println(map.put("C", "ddd"));
        //C不存在返回null, C存在返回旧数据
        System.out.println(map.putIfAbsent("C", "dd"));
        //key 存在返回旧值，key不存在返回设定的value值
        System.out.println(map.computeIfAbsent("D", k -> "qq"));
        System.out.println(map.computeIfPresent("D", (o, n) -> n + o));
        System.out.println(map);
        //key 不存在，将对应的value设为第二个参数。key存在则将oldValue和第二个参数 进行计算后存为该key值对应的value。
        System.out.println(map.merge("C", "ccc", (o, n) -> (o + n).toUpperCase()));
        System.out.println("merge map: " + map);

        System.out.println("-----------------------------------------------");

        HashMap<String, List<Integer>> map1 = new HashMap<>();
        //当key值不存在时，会将计算结果返回，返回创建的LIst
        List<Integer> b1 = map1.computeIfAbsent("B", k -> new ArrayList<>());
        b1.add(1000);

        //key不存在时不会创建List，此时执行add会NPE
        List<Integer> b2 = map1.putIfAbsent("C", new ArrayList<>());
        //b2.add(2000);
        System.out.println(map1.get("B") + "---" + map1.get("C"));

    }

    @Test
    public void guava() {
        String s = "a,b,,c,  ,d,null";
        System.out.println(Splitter.on(",").splitToList(s));
        //去除空格
        System.out.println(Splitter.on(",").trimResults().splitToList(s));
        //去除空串
        System.out.println(Splitter.on(",").omitEmptyStrings().splitToList(s));
        //此时的null 是字符串 "null"
        List<String> strings = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(s);
        System.out.println(strings);

        //去除null
        System.out.println(Joiner.on("-").skipNulls().join(strings));

        System.out.println(CharMatcher.inRange('0', '9').retainFrom("abc12306df"));

        System.out.println(Ints.join("=", new int[]{0, 1, 2, 3}));

        ArrayList<Integer> integers = Lists.newArrayList(1, 2, 3);
        List<Integer> unmodifiableList = Collections.unmodifiableList(integers);
        System.out.println(unmodifiableList);
        ImmutableList<Integer> immutableList = ImmutableList.copyOf(integers);
        System.out.println(immutableList);
        integers.remove(0);
        System.out.println(unmodifiableList);
        System.out.println(immutableList);

        HashBiMap<Object, Object> hashBiMap = HashBiMap.create();
        hashBiMap.put("A", 1);
        hashBiMap.put("A", 2);
        hashBiMap.put("B", 3);
        //相同value：IllegalArgumentException
        //hashBiMap.put("C", 3);
        System.out.println(">>BiMap:" + hashBiMap.get("A") + ">inverse<" + hashBiMap.inverse().get(3));

        String mobile = "18235011372";
        //StringUtils.center(mobile,)
        System.out.println(StringUtils.replace("18235011372", "3501", "****"));
        System.out.println(StringUtil.replace("18235011372", "3501", "****"));
        String html = "<p><b>1、什么是Vue?</b></p><p>vue真的太好用了，是前后段分离必不可少的开发框架之一……</p><p><br></p><p><i><u>2、Vue能干什么？</u></i></p><p>模拟数据</p><p><br></p>";
        System.out.println(HtmlUtils.htmlEscapeHex(html));
        System.out.println(StringEscapeUtils.escapeHtml4(html));

        //System.out.println(HtmlUtils.htmlUnescape("&#x3c;p&#x3e;&#x3c;b&#x3e;1、什么是Vue?&#x3c;/b&#x3e;&#x3c;/p&#x3e;&#x3c;p&#x3e;vue真的太好用了，是前后段分离必不可少的开发框架之一&#x2026;&#x2026;&#x3c;/p&#x3e;&#x3c;p&#x3e;&#x3c;br&#x3e;&#x3c;/p&#x3e;&#x3c;p&#x3e;&#x3c;i&#x3e;&#x3c;u&#x3e;2、Vue能干什么？&#x3c;/u&#x3e;&#x3c;/i&#x3e;&#x3c;/p&#x3e;&#x3c;p&#x3e;模拟数据&#x3c;/p&#x3e;&#x3c;p&#x3e;&#x3c;br&#x3e;&#x3c;/p&#x3e;"));
    }

    @Test
    public void reverse() {
        String s = "qwerty";
        Stack<Character> stack = new Stack<>();

        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            stack.push(chars[i]);
        }
        for (int i = 0; i < chars.length; i++) {
            System.out.print(stack.pop());
        }

    }

    /*@Test
    public void reverse2() {
        Node head = new Node(0);
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
            if (head == null || head.next == null)
                return head;
            ListNode next = head.next;
            ListNode new_head = reverseList(next);
            next.next = head;
            head.next = null;
    }*/

    @Test
    public void print() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            new Thread(() -> {
                System.out.print(finalI);
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        System.out.println("完成");

        CyclicBarrier cyclicBarrier = new CyclicBarrier(3, () -> {
            System.out.println("完成2");
        });
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            new Thread(() -> {
                System.out.print(finalI);
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    @Test
    public void th() {
        SortPrint sortPrint = new SortPrint();
        for (int j = 0; j < 3; j++) {
            int finalJ = j;
            new Thread(() -> {
                try {
                    sortPrint.pri(finalJ);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }

    @Test
    public void sss() {
        JSONArray array = new JSONArray();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("A", 1);
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("B", 2);

        array.add(jsonObject);
        array.add(jsonObject2);
        System.out.println(array);

        List<JSONObject> strings = array.toJavaList(JSONObject.class);
        System.out.println(strings.get(0));
    }

    class SortPrint {
        ReentrantLock lock = new ReentrantLock();
        Condition[] conditions = {lock.newCondition(), lock.newCondition(), lock.newCondition()};
        private volatile int stat;

        public void pri(int para) throws InterruptedException {
            int next = para % 3 + 1;
            while (true) {
                lock.lock();
                while (para != stat) {
                    conditions[para].await();
                }
                System.out.println(para);
                this.stat = next;
                conditions[next].signal();
                lock.unlock();
            }
        }
    }

    @Test
    public void bx() {
        ThreadLocal<Object> objectThreadLocal = ThreadLocal.withInitial(() -> null);

    }

    @Test
    public void pdfHandler() throws IOException {
        PdfDocument document = new PdfDocument("/Users/chengyufei/Downloads/logo.pdf");
        int count = document.getPages().getCount();
        PdfPageBase page;
        ArrayList<String> list = null;
        int name = 0;
        for (int i = 22; i < 23; i++) {
            ArrayList<String> strings = new ArrayList<>();
            page = document.getPages().get(i);
            String text = page.extractText(false);
            list = Lists.newArrayList(text.split("\r\n"));
            System.out.println(list);

            page = document.getPages().get(i);
            BufferedImage[] bufferedImages = page.extractImages();
            for (int j = 0; j < bufferedImages.length; j++) {

                //System.out.println(list.get(j));
                //File output = new File("/Users/chengyufei/Downloads/dmg/logo/"+name+++".png");

                File output = new File("/Users/chengyufei/Downloads/dmg/logo/" + list.get(j + 2) + ".png");
                ImageIO.write(bufferedImages[j], "PNG", output);
            }
        }
        document.close();
    }

    @Test
    public void verify() {
        PdfDocument document = new PdfDocument("/Users/chengyufei/Downloads/dmg/programme/edge_downloads/E12.pdf");

        PdfFormWidget pdfFormWidget = (PdfFormWidget) document.getForm();
        PdfFormFieldWidgetCollection collection = pdfFormWidget.getFieldsWidget();

        for (int i = 0; i < collection.getCount(); i++) {
            //判定是否为签名域
            if (collection.get(i) instanceof PdfSignatureFieldWidget) {
                //获取签名域
                PdfSignatureFieldWidget signatureFieldWidget = (PdfSignatureFieldWidget) collection.get(i);
                //获取签名
                PdfSignature signature = signatureFieldWidget.getSignature();
                String signatureName = signatureFieldWidget.getName();
                System.out.println(signatureName);

                //判定签名是否有效
                boolean result = signature.verifySignature();
                if (result) {
                    System.out.println("有效签名");
                } else {
                    System.out.println("无效签名");
                }
            }

        }
    }

    //List<Integer> result = list.stream().sorted(Collections.reverseOrder()).collect(Collectors.toList());
    @Test
    public void concurrentHashMap() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("A", "a");
        map.put("B", "b");
        String putIfAbsent = map.putIfAbsent("A", "aa");
        System.out.println(putIfAbsent);

    }

    /**
     * druid 自带的对数据库链接密码的RSA加解密
     * @throws Exception
     */
    @Test
    public void druidEnc() throws Exception {
        //privateKey:MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAqTULuoNgy27BhaBA6YdVSTgWPN19JPKM/qkwXq04oSBRi3PB7k2bbyWyyVJ0Hdq7cZr+H3phSEOcxihqYKWcrwIDAQABAkAOFixzypw0U3Qo+75rsqiW/+RaDNZuTQMP8GXme+VLBJJ/4zPsw4e4jWGKkEw4QTPO+Jq5l2oD9s6n/IoqThKBAiEA+0se7fxNWHq+mBN+clPAIQ9nF43CIwMsCPq9FeLjtiMCIQCsYFhnFzAZGPMEkPS4kStNzPmjpqPN4HSLl9gV2/aaBQIgFU7AV6OeF1OJF7IHdcA+OU9qCWg+vuNE95nzMIjlkAsCIBJ8Mn67qdjHKChxMw0n5DDvrkWydRZ634Sov+94KzXRAiBwVDUHAP2t2KynzKg4lETbrXE9Y4wJ2SxcpOqHYnbJ9A==
        //publicKey:MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKk1C7qDYMtuwYWgQOmHVUk4FjzdfSTyjP6pMF6tOKEgUYtzwe5Nm28lsslSdB3au3Ga/h96YUhDnMYoamClnK8CAwEAAQ==
        //password:cGD+ThjBGNVUGVVFnsY5FOR16WWoOY1v7RGRxA84zUrEPp0OR5nwrbO8AcX/oLY/SOrnaG4TAhBl+3rGWj3PgA==
        //ConfigTools.main(new String[]{"root","123456"});

        String text = ConfigTools.encrypt("MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAqTULuoNgy27BhaBA6YdVSTgWPN19JPKM/qkwXq04oSBRi3PB7k2bbyWyyVJ0Hdq7cZr+H3phSEOcxihqYKWcrwIDAQABAkAOFixzypw0U3Qo+75rsqiW/+RaDNZuTQMP8GXme+VLBJJ/4zPsw4e4jWGKkEw4QTPO+Jq5l2oD9s6n/IoqThKBAiEA+0se7fxNWHq+mBN+clPAIQ9nF43CIwMsCPq9FeLjtiMCIQCsYFhnFzAZGPMEkPS4kStNzPmjpqPN4HSLl9gV2/aaBQIgFU7AV6OeF1OJF7IHdcA+OU9qCWg+vuNE95nzMIjlkAsCIBJ8Mn67qdjHKChxMw0n5DDvrkWydRZ634Sov+94KzXRAiBwVDUHAP2t2KynzKg4lETbrXE9Y4wJ2SxcpOqHYnbJ9A==",
                "root");
        System.out.println(text);

        String text2 = ConfigTools.encrypt("MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAqTULuoNgy27BhaBA6YdVSTgWPN19JPKM/qkwXq04oSBRi3PB7k2bbyWyyVJ0Hdq7cZr+H3phSEOcxihqYKWcrwIDAQABAkAOFixzypw0U3Qo+75rsqiW/+RaDNZuTQMP8GXme+VLBJJ/4zPsw4e4jWGKkEw4QTPO+Jq5l2oD9s6n/IoqThKBAiEA+0se7fxNWHq+mBN+clPAIQ9nF43CIwMsCPq9FeLjtiMCIQCsYFhnFzAZGPMEkPS4kStNzPmjpqPN4HSLl9gV2/aaBQIgFU7AV6OeF1OJF7IHdcA+OU9qCWg+vuNE95nzMIjlkAsCIBJ8Mn67qdjHKChxMw0n5DDvrkWydRZ634Sov+94KzXRAiBwVDUHAP2t2KynzKg4lETbrXE9Y4wJ2SxcpOqHYnbJ9A==",
                "123456");
        System.out.println(text2);

        String decrypt = ConfigTools.decrypt("MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKk1C7qDYMtuwYWgQOmHVUk4FjzdfSTyjP6pMF6tOKEgUYtzwe5Nm28lsslSdB3au3Ga/h96YUhDnMYoamClnK8CAwEAAQ=="
                , "Dc6Y5k3xuFSWcJdg40Rr3oP1GFYnAtTDIaJYAr9vzif0/Ib93PyiZLSRm769x203n7VjK2mf/UnUQwElOUF07g==");
        System.out.println(decrypt);


    }

    @Test
    public void down() throws IOException {

        //
        // ffmpeg -f concat -i /Users/chengyufei/Downloads/dmg/programme/edge_downloads/video/v.txt -vcodec copy -acodec copy /Users/chengyufei/Downloads/dmg/programme/edge_downloads/video/output.mp4
        String path = "/Users/chengyufei/Downloads/dmg/programme/edge_downloads/video/";
        String url = "http://cntv.vod.cdn.myqcloud.com/flash/mp4video63/TMS/2022/08/14/0fa6aeb9163b4675a21fcd290925fea2_h264818000nero_aac32-";
        String text = "file  '/Users/chengyufei/Downloads/dmg/programme/edge_downloads/video/";
        File file;
        for (int i = 1; i <= 18; i++) {
            file = new File(path + i + ".ts");
            //FileUtils.copyURLToFile(new URL(url + i + ".mp4"), file);
            //FileUtils.writeStringToFile(new File(path + "v.txt"), text + i + ".ts\'\r", StandardCharsets.UTF_8, true);
        }

        Response response = Request.Get("https://vdn.apps.cntv.cn/api/getHttpVideoInfo.do?pid=99b0310f4f184d88b3712dfc53ab2c99&client=flash&im=0&tsp=1661498650&vn=2049&vc=904F9F63ED024F262369729ED95E7D23&uid=undefined&wlan=")
                .execute();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNodes = objectMapper.readValue(response.returnContent().toString(), ObjectNode.class);
    }

    @Test
    public void collections() {
        ArrayList<String> list = Lists.newArrayList("照片", "版本", "阿雪", "旁边");
        //Collections.sort(list);
        //Collections.reverse(list);

        //降序两种

        //中文排序
        List<String> result = list.stream().sorted((i, j) -> Collator.getInstance(Locale.CHINESE).compare(i, j)).collect(Collectors.toList());

        System.out.println(result);

        //
        List<String> unmodifiableList = Collections.unmodifiableList(list);
        //unmodifiableList.add(5);


        ArrayList<Integer> list2 = Lists.newArrayList(8, 3, 1, 4, 2, 7, 5);
        int search = Collections.binarySearch(list2, 8);
    }


    @Test
    public void collectionutils() {
        ArrayList<Integer> list = Lists.newArrayList(3, 1, 4, 2);
        ArrayList<Integer> list2 = Lists.newArrayList(8, 3, 1, 4, 2, 7, 5);
        Collection<Integer> union = CollectionUtils.union(list, list2);
        System.out.println("union: " + union);

        Collection<Integer> intersection = CollectionUtils.intersection(list, list2);
        System.out.println("intersection: " + intersection);

        //交集的补集
        Collection<Integer> disjunction = CollectionUtils.disjunction(list2, list);
        System.out.println("disjunction: " + disjunction);

        Collection<Integer> subtract = CollectionUtils.subtract(list, list2);
        System.out.println("subtract: " + subtract);

        Collection<Integer> subtract2 = CollectionUtils.subtract(list2, list);
        System.out.println("subtract2: " + subtract2);

    }

    @Test
    public void lists() {
        ArrayList<Integer> list2 = Lists.newArrayList(8, 3, 1, 4, 2, 7, 5);
        //3个元素分一组
        List<List<Integer>> partition = Lists.partition(list2, 3);
        System.out.println(partition);

        //流式处理：返回的集合不可add , 可以remove，操作影响原集合
        List<Integer> transform = Lists.transform(list2, i -> i + 1);
        transform.remove(0);
        //原集合的操作会影响流式处理后的集合
        list2.add(10);
        System.out.println(transform);
        System.out.println(list2);
    }

    @Test
    public void objects() {
        User user = new User();
        user.setId(10);
        user.setUsername("AAA");
        int hashCode = Objects.hashCode(user);
        System.out.println(hashCode);
    }

    @Test
    public void booleanuttils() {

        Boolean a = true;
        Boolean b = false;
        Boolean c = null;
        //默认：true - 1  /  false - 0
        int aInt = BooleanUtils.toInteger(a);
        int bInt = BooleanUtils.toInteger(b);
        System.out.println(aInt + "---" + bInt);

        //自定义true false 对应的值
        int aIntCustom = BooleanUtils.toInteger(a, 100, 200, 999);
        System.out.println(aIntCustom);//100

        boolean cB = BooleanUtils.toBooleanDefaultIfNull(c, false);
        System.out.println(cB);

        String s = BooleanUtils.toStringTrueFalse(true);
        String onOff = BooleanUtils.toStringOnOff(true);
        String yesNo = BooleanUtils.toStringYesNo(true);
        //true--on--yes
        System.out.println(s + "--" + onOff + "--" + yesNo);
        //自定义对应的字符串值
        String string = BooleanUtils.toString(true, "请求成功", "请求失败", "异常");
        System.out.println(string);


        boolean intToBoo = BooleanUtils.toBoolean(1, 1, 0);
        System.out.println(intToBoo);

    }

    @Test
    public void stringutils() {
        String str = "0.1";
        boolean numeric = StringUtils.isNumeric(str);
        System.out.println(numeric);//false

        String add = "北京市大兴";
        //缩写：第二个参数为返回字符串的最大长度，最小为4，因为...已经为3了
        String abbreviate = StringUtils.abbreviate(add, 4);
        String abbreviate1 = StringUtils.abbreviate(add, 2, 4);
        System.out.println(abbreviate + " -- " + abbreviate1);

        String overlay = StringUtils.overlay("18235011372", "****", 3, 7);
        System.out.println(overlay);

        ArrayList<Integer> integers = Lists.newArrayList(1, 2, 3);
        String join = StringUtils.join(integers, ",");
        System.out.println(join);

        String appendIfMissing = StringUtils.appendIfMissing(add, "区", "区", "市", "县");
        System.out.println("appendIfMissing: " + appendIfMissing);

        //changing the first character to title case - Luke
        System.out.println(StringUtils.capitalize("luke"));

        //填充
        System.out.println(StringUtils.center(add, 6, "A"));
        System.out.println(StringUtils.leftPad(add, 8, "A"));

        //去除字符串结尾处的一个换行符，包括: \r  \n   \r\n
        System.out.println(StringUtils.chomp(add + "\r\n"));

        //去除最后一个字符
        System.out.println(StringUtils.chop(add));

        //若为 null ，返回默认值
        System.out.println(StringUtils.defaultString(null, "ABC"));

        System.out.println(StringUtils.difference("北京市大兴区", "北京市朝阳区"));

        System.out.println(RegExUtils.removeAll(" B 北京市  大兴区  C ", " "));

        StringJoiner stringJoiner = new StringJoiner(" ", "ffmpeg ", ".mp4");
        stringJoiner.add("A").add("B").add("C");
        System.out.println(stringJoiner.toString());
    }
}
