package com.magic.time.service.business_development_100.pool_04;

import com.magic.time.dao.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author Cheng Yufei
 * @create 2021-12-22 10:12 PM
 **/
@Service
@Slf4j
public class HttpSerivce {

    @Autowired
    private RestTemplate restTemplate;

    public void rest1() throws URISyntaxException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

       /* UserInfo userInfo = new UserInfo();
        userInfo.setUsername("请求2");
        HttpEntity<Object> httpEntity = new HttpEntity<>(userInfo, httpHeaders);
        String result = restTemplate.postForObject(new URI(""), httpEntity, String.class);*/

        ResponseEntity<UserInfo> entity = restTemplate.getForEntity("http://localhost:9090/userInfo/getUser?id={id}", UserInfo.class, 287);
        System.out.println(entity.getBody());
    }

    public void rest2() {

        FileSystemResource fileSystemResource = new FileSystemResource(new File("/Users/chengyufei/Downloads/dmg/common/pictures/jokerx-wallpaper.jpeg"));

        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        //将上传的文件放入HttpEntity中
        multiValueMap.add("file", new HttpEntity<>(fileSystemResource));
        multiValueMap.add("username","上传文件");

        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(URI.create("http://localhost:9090/userInfo/insertUser2"), multiValueMap, String.class);
        String body = stringResponseEntity.getBody();
        System.out.println(body);

    }

    public void rest3(MultipartFile file, String username) {

        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        //接受的文件获取Resource进行传输
        multiValueMap.add("file", file.getResource());
        multiValueMap.add("username", username);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(multiValueMap);

        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(URI.create("http://localhost:9090/userInfo/insertUser2"), httpEntity, String.class);
        String body = stringResponseEntity.getBody();
        System.out.println(body);
    }
}
