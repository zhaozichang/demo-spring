package com.itheima.riggie.controller;

import com.itheima.riggie.common.R;
import lombok.extern.slf4j.Slf4j;
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
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        log.info(file.toString());

        //获取原始文件名,此处不使用原始文件名
        String originalFilename = file.getOriginalFilename();

        //截取文件后缀        截取从"."开始之后的文件名   例如abc.jpg会截取到".jpg"
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 使用UUID重新生成文件名，防止文件名字重复造成文件覆盖
        String fileName = UUID.randomUUID().toString()+ suffix;

        //创建一个目录对象
        File dir = new File(basePath);

        if (!dir.exists()) {
                dir.mkdirs();
        }

        try {
            //在此处可以直接写路径，但是不利于后期更改维护代码
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }


        return R.success(fileName);
    }

    /***
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            //输入流，通过输入流读取对象
            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));
            //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len=fileInputStream.read(bytes)) !=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            //使用完io流之后，需要关闭资源
            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
