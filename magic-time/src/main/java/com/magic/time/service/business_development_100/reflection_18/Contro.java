package com.magic.time.service.business_development_100.reflection_18;

import com.sun.media.sound.SoftTuning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 *
 * @author Cheng Yufei
 * @create 2023-04-12 18:13
 **/
@RestController
@RequestMapping("/ttt")
public class Contro {

    /*@Autowired
    //@Qualifier("testInterImpl")
    private TesInterImpl tesInter;

    @Autowired
    private Map<String, TesInter> maps;


    @GetMapping("/q")
    public String q() {
        String testInterImpl = maps.get("tesInterImpl2").getA();
        System.out.println(testInterImpl);
        return tesInter.getA();
    }*/
}
