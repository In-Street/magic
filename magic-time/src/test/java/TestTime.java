import cn.hutool.core.text.escape.Html4Escape;
import cn.hutool.http.HtmlUtil;
import com.google.common.collect.Lists;
import com.hankcs.hanlp.classification.utilities.TextProcessUtility;
import com.hankcs.hanlp.summary.TextRankSentence;
import com.hankcs.hanlp.utility.SentencesUtil;
import com.hankcs.hanlp.utility.TextUtility;
import com.magic.time.common.HanUtil;
import org.junit.Test;
import org.springframework.web.util.HtmlUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
        System.out.println("chinese: "+allChinese);
        System.out.println("letter: "+allLetter);

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
    }
}
