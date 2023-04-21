import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HtmlUtil;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hankcs.hanlp.classification.utilities.TextProcessUtility;
import com.hankcs.hanlp.summary.TextRankSentence;
import com.hankcs.hanlp.utility.SentencesUtil;
import com.hankcs.hanlp.utility.TextUtility;
import com.magic.time.dao.model.UserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StopWatch;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 *
 * @author Cheng Yufei
 * @create 2022-01-08 6:50 PM
 **/
public class TestTime {

    @Test
    public void Hanlp() {
        String doc = "2022年1月1日，区域全面经济伙伴关系协定（RCEP）如期生效，全球最大自由贸易区正式启航！对新冠肺炎疫情阴霾之下的世界经济而言，这个消息无异于一道阳光！《消融寒冬的凛冽》，带来光明与希望。\n" +
                "\n" +
                "　　作为RCEP中经济规模最大的成员，<script type=\"text/javascript\" data-rms=\"1\" src=\"/rp/n21aGRCN5EKHB3qObygw029dyNU.br.js\"></script> 中国始终与各成员国一道，积极参与和支持RCEP机制建设，致力于将其打造成东亚经贸合作的主平台。\n" +
                "\n" +
                "　　早在2021年3月，中国就完成了RCEP的核准，成为 非东 盟国家中首个正式完成核准程序的成员国。RCEP生效实施后，中国与自贸伙伴的贸易额占比由27%增加到35%左右，超过1/3的对外贸易实现零关税。货物贸易方面，零关税水平从8%增至90%，涵盖中国约1.6万亿美元的贸易额。服务贸易方面，中国服务贸易开放承诺达到了已有自贸协定的最高水平。中国还首次在国际协定中纳入非服务业投资负面清单，对制造业、农业、林业、渔业、采矿业5个领域作出高水平自由化承诺。种种行动表明，RCEP是中国对外开放的里程碑。中国维护多边贸易体制、推动区域经济一体化进程的脚步坚定有力。\n" +
                "\n" +
                "　　逆境之下，正能量尤为可贵。近年来，保护主义、单边主义暗流涌动，经济全球化遭遇逆流。新冠肺炎疫情导致全球经济增长乏力，国际贸易遭遇严重衰退。百年变局叠加世纪疫情，呈现在世界面前的乱象错综复杂，“筑墙”“脱钩”“退群”泛滥，霸权、霸凌、霸道横行，强权政治、零和心态、冷战思维不断冲击国际秩序，形形色色的“伪多边主义”甚嚣尘上。\n" +
                "\n" +
                "　　面对严峻挑战，中国从未动摇维护多边主义的信念，从未停下开放合作的步伐，从未放弃构建开放型世界经济的努力。为推动高水平对外开放，中国提出实施自由贸易区提升战略，构建面向全球的高标准自由贸易区网络。迄今，中国的自贸伙伴达到26个，与自贸伙伴的贸易额占比达到35%左右，货物、服务、投资以及制度型开放水平得到全面提升。\n" +
                "\n" +
                "　　A cluster  is a collection of one or more nodes (servers) that together holds your entire data。RCEP生效后，将极大促进区域经济一体化，拉动区域贸易投资增长。据测算，到2025年，RCEP有望带动成员国出口、对外投资存量、国内生产总值分别比基线多增长10.4%、2.6%、1.8%。这将大大提振成员国携手实现疫后经济复苏的信心与决心，为区域乃至全球经济增长注入强劲动力。在RCEP的合作框架内，中国也将进一步为区域经济发展提供广阔市场空间和多元多样的产业体系支撑，更好参与区域产业链供应链融合发展和一体化大市场建设，为促进地区繁荣和世界经济复苏贡献中国力量。\n" +
                "\n" +
                "　　历史经验表明，不管遇到什么风险、什么灾难、什么逆流，人类社会总是要前进的，而且一定能够继续前进。各国走向开放、走向合作的大势没有改变。RCEP生效再次证明，共谋发展是历史潮流，互利共赢是人心所向。\n" +
                "\n" +
                "　　在2021年第四届上海国际进口博览会开幕式主旨演讲中，中国国家主席习近平郑重承诺：“中国扩大高水平开放的决心不会变，同世界分享发展机遇的决心不会变，推动经济全球化朝着更加开放、包容、普惠、平衡、共赢方向发展的决心不会变。”三个“不会变”彰显了中国与世界共享机遇、互利共赢的胸怀与担当，为充满不确定性的世界注入确定性。相信随着RCEP正式生效，中国将继续与各成员国一道，携手前行，推动区域实现更高质量、更深层次的经济一体化，为构建开放型世界经济注入更多活力。";
        List<String> sentenceList = TextRankSentence.getTopSentenceList(doc, doc.length());
        //sentenceList.stream().forEach(s-> System.out.println(s));

        String enDoc = "A cluster is a collection of one or more nodes (servers) that together holds your entire data and provides federated indexing and search capabilities across all nodes. A cluster is identified by a unique name which by default is \"elasticsearch\". This name is important because a node can only be part of a cluster if the node is set up to join the cluster by its name. Make sure that you don’t reuse the same cluster names in different environments,otherwise you might end up with nodes joining the wrong cluster. For instance you could use logging-dev, logging-stage, and logging-prod for the development, staging, and production clusters.";

      /*  System.out.println(">>>>>>>>>>>>>>>>>");
        String[] strings = HanUtil.content_split(enDoc);
        AtomicInteger i = new AtomicInteger();
        Lists.newArrayList(strings).stream().forEach(s -> {
            System.out.println(i + ">>" + s);
            i.getAndIncrement();
        });*/


        boolean allChinese = TextUtility.isAllNonChinese(doc.getBytes(StandardCharsets.UTF_8));
        //boolean allLetter = TextUtility.isAllLetter(enDoc);
        boolean allLetter = TextUtility.isAllNonChinese(TextProcessUtility.preprocess(enDoc).getBytes(StandardCharsets.UTF_8));
        System.out.println("chinese: " + allChinese);
        System.out.println("letter: " + allLetter);

        List<String> strings = SentencesUtil.toSentenceList(HtmlUtil.cleanHtmlTag(doc), false);
        AtomicInteger i = new AtomicInteger();
        Lists.newArrayList(strings).stream().forEach(s -> {
            System.out.println(i + ">>" + s);
            i.getAndIncrement();
        });


    }

    @Test
    public void opennlp() {
    }

    @Test
    public void len() {
        String str = "2022年1月1日，区域全面经济伙伴关系协定（RCEP）如期生效，全球最大自由贸易区正式启航。";
        System.out.println(str.length());

        String s = "set";
        System.out.println(s.getBytes(StandardCharsets.UTF_8).length);
    }

    /**
     *   AbstractHandlerMapping # getHandler
     *    RequestMappingHandlerAdapter  # invokeHandlerMethod
     *    InvocableHandlerMethod # doInvoke  -> HandlerMethod # invoke  -> 反射 委派代理实现 调用native
     * @throws InterruptedException
     * @throws IOException
     */
    @Test
    public void rest4() throws InterruptedException, IOException {

        HashMap<String, Integer> map = new HashMap<>();
        HashMap<String, Integer> map2 = new HashMap<>();
        HashMap<String, Integer> map3 = new HashMap<>();
        HashMap<String, Integer> map4 = new HashMap<>();


        int start = 679092, end = 679151;
        ;
        String host = "";
        for (int i = start; i <= end; i++) {
            String url = host + i + "/" + i + ".m3u8";
            String v = "/Users/chengyufei/Downloads/dmg/common/edge_download/" + i + ".m3u8";
            File file = new File(v);
            try {
                FileUtils.copyURLToFile(new URL(url), file);
            } catch (Exception e) {
                continue;
            }
            long count = Files.lines(Paths.get(v)).count();
            if (count < 15 || count > 200) {
                continue;
            }
            String s = FileUtils.readLines(file, StandardCharsets.UTF_8).stream().skip(count - 2).findFirst().get();
            String key = String.valueOf(i);
            String last = s.replace(".ts", "").substring(key.length());
            if (map.size() < 4) {
                map.put(key, Integer.valueOf(last));
                continue;
            }
            if (map2.size() < 4) {
                map2.put(key, Integer.valueOf(last));
                continue;
            }
            if (map3.size() < 4) {
                map3.put(key, Integer.valueOf(last));
                continue;
            }
            //if (map4.size() < 15) {
            map4.put(key, Integer.valueOf(last));
            continue;
            //}
        }

        ForkJoinPool forkJoinPool = new ForkJoinPool(5);
        forkJoinPool.submit(() -> {
            forMap(map3);
        });
        forkJoinPool.submit(() -> {
            forMap(map2);
        });
        forkJoinPool.submit(() -> {
            forMap(map);
        });
        forkJoinPool.submit(() -> {
            forMap(map4);
        });
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(2, TimeUnit.HOURS);
    }


    private void forMap(Map<String, Integer> map) {
        if (map.size() == 0) {
            return;
        }
        map.forEach((str, index) -> {
            System.out.println(Thread.currentThread().getId() + ">>正在处理：" + str);

            String host = "";
            File result = new File("/Users/chengyufei/Downloads/dmg/common/edge_download/" + str + ".txt");
            for (int i = 0; i <= index; i++) {
                String v = "/Users/chengyufei/Downloads/dmg/common/edge_download/" + str + i + ".ts";
                try {
                    FileUtils.copyURLToFile(new URL(host + str + "/" + str + i + ".ts"), new File(v));
                    FileUtils.write(result, "file '" + v + "'\n", "utf-8", true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Process exec = Runtime.getRuntime().exec("/Users/chengyufei/Downloads/dmg/common/edge_download/未命名文件夹/com.sh " + str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    @Test
    public void fileLineCount() throws IOException {
        String path = "/Users/chengyufei/Downloads/dmg/common/pictures/614748.m3u8";
        long count = Files.lines(Paths.get(path)).count();
        System.out.println(count);
        String s = FileUtils.readLines(new File(path), StandardCharsets.UTF_8).stream().skip(count - 2).findFirst().get();
        System.out.println(s.replace(".ts", "").substring("614748".length()));
    }

    @Test
    public void dd() {
        String s = IntStream.rangeClosed(1, 10).mapToObj(__ -> "a").collect(Collectors.joining("")) + UUID.randomUUID().toString();
        System.out.println(s);

        Map<Class<?>, Object> DEFAULT_VALUES = Stream.of(boolean.class, byte.class, char.class, double.class, float.class, int.class, long.class, short.class)
                .collect(toMap(clazz -> clazz, clazz -> Array.get(Array.newInstance(clazz, 1), 0)));

        System.out.println(DEFAULT_VALUES);
    }

    @Test
    public void rest3() {

        HashMap<String, Integer> map = new HashMap<>();
        map.put("601537", 13);
        map.put("601625", 39);
        map.put("601642", 28);
        map.put("601686", 55);
        map.put("601713", 27);
        map.put("601781", 86);
        map.put("601834", 59);
        map.forEach((str, index) -> {
            String host = "https://cdn77.91p49.com/m3u8/";
            //String host = "https://la.killcovid2021.com/m3u8/";
            File result = new File("/Users/chengyufei/Downloads/dmg/common/edge_download/" + str + ".txt");
            for (int i = 0; i <= index; i++) {
                String v = "/Users/chengyufei/Downloads/dmg/common/edge_download/" + str + i + ".ts";
                try {
                    FileUtils.copyURLToFile(new URL(host + str + "/" + str + i + ".ts"), new File(v));
                    FileUtils.write(result, "file '" + v + "'\n", "utf-8", true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void rr() {
        System.out.println(0.1 + 0.2);

        //相乘：结果值的scale=两个乘数scale相加
        System.out.println(new BigDecimal("4.015").multiply(new BigDecimal(Double.toString(100))));

        //用double构造BigDecimal 结果只是精度提高了，仍会有精度丢失
        System.out.println(new BigDecimal(0.1).add(new BigDecimal(0.2)));
        //以下三种BigDecimal的构造方式结果准确
        System.out.println(new BigDecimal("0.1").add(new BigDecimal("0.2")));
        System.out.println(BigDecimal.valueOf(0.1).add(BigDecimal.valueOf(0.2)));
        System.out.println(new BigDecimal(Double.toString(0.1)).add(new BigDecimal(Double.toString(0.2))));
    }

    @Test
    public void tt() {
        int[] arr = {1, 2, 3};
        List<int[]> ints = Arrays.asList(arr);
        System.out.println(ints.size());

        Integer[] arrInteger = {7, 8, 9};
        arrInteger[1] = 10;
        //元素替换 ； 不可add
        System.out.println(Arrays.asList(arrInteger));

        arr[1] = 4;
        List<Integer> collect = Arrays.stream(arr).boxed().collect(Collectors.toList());
        collect.add(5);
        //collect.remove(0);
        //元素替换 ； 可以add\remove
        System.out.println(collect);
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }

        System.out.println("///////////////////////////////////////////////////////");
        ArrayList<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
        List<Integer> sub = list.subList(0, 2);
        System.out.println(sub);
        //list.add(0,6);
        //ConcurrentModificationException
        //System.out.println(sub);

        //sub.remove(1);
        sub.add(7);
        System.out.println(list);
        System.out.println(sub);
    }

    @Test
    public void yy() {
        int elementCount = 100000;
        int loopCount = 100000;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("linkedListGet");
        linkedListGet(elementCount, loopCount);
        stopWatch.stop();
        stopWatch.start("arrayListGet");
        arrayListGet(elementCount, loopCount);
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());


        StopWatch stopWatch2 = new StopWatch();
        stopWatch2.start("linkedListAdd");
        linkedListAdd(elementCount, loopCount);
        stopWatch2.stop();
        stopWatch2.start("arrayListAdd");
        arrayListAdd(elementCount, loopCount);
        stopWatch2.stop();
        System.out.println(stopWatch2.prettyPrint());
    }

    //LinkedList访问
    private static void linkedListGet(int elementCount, int loopCount) {
        List<Integer> list = IntStream.rangeClosed(1, elementCount).boxed().collect(Collectors.toCollection(LinkedList::new));
        IntStream.rangeClosed(1, loopCount).forEach(i -> list.get(ThreadLocalRandom.current().nextInt(elementCount)));
    }

    //ArrayList访问
    private static void arrayListGet(int elementCount, int loopCount) {
        List<Integer> list = IntStream.rangeClosed(1, elementCount).boxed().collect(Collectors.toCollection(ArrayList::new));
        IntStream.rangeClosed(1, loopCount).forEach(i -> list.get(ThreadLocalRandom.current().nextInt(elementCount)));
    }

    //LinkedList插入
    private static void linkedListAdd(int elementCount, int loopCount) {
        List<Integer> list = IntStream.rangeClosed(1, elementCount).boxed().collect(Collectors.toCollection(LinkedList::new));
        IntStream.rangeClosed(1, loopCount).forEach(i -> list.add(ThreadLocalRandom.current().nextInt(elementCount), 1));
        //IntStream.rangeClosed(1, loopCount).forEach(i -> list.add(ThreadLocalRandom.current().nextInt(elementCount)));
    }

    //ArrayList插入
    private static void arrayListAdd(int elementCount, int loopCount) {
        List<Integer> list = IntStream.rangeClosed(1, elementCount).boxed().collect(Collectors.toCollection(ArrayList::new));
        IntStream.rangeClosed(1, loopCount).forEach(i -> list.add(ThreadLocalRandom.current().nextInt(elementCount), 1));
        //IntStream.rangeClosed(1, loopCount).forEach(i -> list.add(ThreadLocalRandom.current().nextInt(elementCount)));
    }

    @Test
    public void ee() {
        String s = "AB";
        String[] copies = s.split("&&");
        System.out.println(copies.length);

        UserInfo a = new UserInfo(1, "A");
        UserInfo b = new UserInfo(1, "B");
        UserInfo c = new UserInfo(2, "C");

        List<UserInfo> list = Lists.newArrayList(a);
        List<UserInfo> list2 = Lists.newArrayList(b, c);

        Collection<UserInfo> res = CollectionUtils.retainAll(list2, list, new Equator<UserInfo>() {
            @Override
            public boolean equate(UserInfo o1, UserInfo o2) {
                return o1.getId().equals(o2.getId());
            }

            @Override
            public int hash(UserInfo o) {
                return 0;
            }
        });
        //System.out.println(res);


        List<UserInfo> collate = CollectionUtils.collate(list, list2, Comparator.comparing(u -> u.getId()), false);
        //System.out.println(collate);

        Collection<UserInfo> infos = CollectionUtils.removeAll(list, list2, new Equator<UserInfo>() {
            @Override
            public boolean equate(UserInfo o1, UserInfo o2) {
                return !o1.getId().equals(o2.getId());
            }

            @Override
            public int hash(UserInfo o) {
                return 0;
            }
        });
        System.out.println(infos);

        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - 60);
        long endTime = calendar.getTimeInMillis();
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - 11);
        long startTime = calendar.getTimeInMillis();
        List<String> strings = Arrays.asList(instance.format(startTime), instance.format(endTime));
        System.out.println(strings);
    }

    @Test
    public void t3() {

        Map<? extends Class<?>, Object> collect = Stream.of(boolean.class, double.class).collect(toMap(c -> (Class<?>) c, cla -> Array.get(Array.newInstance(cla, 1), 0)));
        //{double=0.0, boolean=false}
        System.out.println(collect);

        ArrayList<String> strings = Lists.newArrayList("A", "B", "C", "A");
        Set<String> strings1 = new HashSet<>(strings);
        System.out.println(CollectionUtils.disjunction(strings, strings1));


        List<String> collect1 = strings.stream().collect(Collectors.groupingBy(s -> s)).entrySet().stream().filter(v -> v.getValue().size() > 1).map(e -> e.getKey()).collect(Collectors.toList());
        //System.out.println(collect1);

        List<String> collect2 = strings.stream().filter(s -> Collections.frequency(strings, s) > 1).collect(Collectors.toList());
        //System.out.println(collect2);


    }

    @Test
    public void lb() {
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("B", "b");
        linkedHashMap.put("A", "a");
        linkedHashMap.remove("A", "A");

        Integer a = -128;
        Integer b = -128;
        System.out.println(a == b);

        String s1 = "A";
        String s2 = "A";
        System.out.println(s1 == s2);

        String s = "$2a$10$BhYolP0zcLKxuj5vJgQMH.VE73Ge3iZfaWqv6D5n4SGXDHEKhCNAO";
        System.out.println(s.length());
        System.out.println(Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void te() {
        Integer a = 100;
        Integer b = 100;
        Integer c = 200;
        Integer d = 200;
        Long e = 100L;

        System.out.println(a == b);
        System.out.println(c == d);

        System.out.println(c == (a + b));

        System.out.println(a.equals(e));

        System.out.println(0.1+0.2);
        System.out.println(1.0-0.7);

        String ss = "100";
        ddd(ss);
        System.out.println(ss);
    }

    public void ddd(String integer) {
        integer = "3";
        System.out.println(integer);
    }
}
