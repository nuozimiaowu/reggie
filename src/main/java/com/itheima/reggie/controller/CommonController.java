package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 处理文件上传下载
 */
@RestController
@RequestMapping("common")
public class CommonController {

    //从配置文件里拿到存储图片的地址实现解耦
    @Value("${reggie.path}")
    private String basePath;

    /*
    * 文件上传
    * 1.上传的目录需要配置
    * 2.上传的文件名不能重复
    * 3.需要把生成的文件名响应到客户端
    * */
    @PostMapping("upload")
    public R<String> upload(MultipartFile file) throws IOException {
        //MultipartFile表示的文件上传的内容，临时文件

        //判断储存的目录是不是存在，不存在就创建
        File dir = new File(basePath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        //要生成唯一的UUID文件名:
        String uuid = UUID.randomUUID().toString();
        //获取源文件最后一个点的索引
        int index = file.getOriginalFilename().lastIndexOf(".");
        //获取源文件的后缀名字
        String suffix = file.getOriginalFilename().substring(index);
        //拼接出来一个唯一的文件名
        String uuidFileName = uuid+suffix;
        //把这个临时文件存储到指定位置,抛出异常
        file.transferTo(new File(basePath+"/"+uuidFileName));
        return R.success(uuidFileName);
    }

    //文件的下载：文件数据的回显。
    @GetMapping("download")
    public void download(String name, HttpServletResponse response) throws IOException {
        //文件的下载：把文件以流的形式写给客户端
        //输入流：文件的流        输出流：response.getOutputStream
        FileInputStream is = new FileInputStream(basePath+"/"+name);
        ServletOutputStream outputStream = response.getOutputStream();
        //输入流和输出流对接：
        IOUtils.copy(is,outputStream);
        is.close();
    }
}

















