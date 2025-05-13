package cn.tablego.project.springboot.controller;

import cn.tablego.project.springboot.common.model.AjaxResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {
    public static String mp4Path = "";

    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return AjaxResult.error("请选择文件");
        }
        // /storage/thumbnails/_signature/2D5URTNLQPUJ6IAVPDSERJNHJV.jpg
        //获取文件名
        String fileName = file.getOriginalFilename();
        //设置文件上传路径
//        String uploadPath = "D:/upload/";
        String uploadPath = "/home/upload/";
        //如果上传目录不存在，创建目录
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        String filePath = uploadPath + fileName;

        //获取文件扩展名
        String extensionName = fileName.substring(fileName.lastIndexOf("."));
        //生成新的文件名
        String newFileName = UUID.randomUUID().toString().replaceAll("-", "") + extensionName;
        // return newFileName;
        String path = uploadPath + newFileName;
        System.out.println(path);


        File dest = new File(path);
        try {
            // 保存文件
            file.transferTo(dest);
//            mp4Path = "http://localhost:8888/cpt/img/" + newFileName;
//            mp4Path = "http://192.168.0.104:8888/cpt/img/" + newFileName;
            mp4Path = "http://116.62.42.63:8888/cpt/img/" + newFileName;

            return AjaxResult.success(mp4Path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return AjaxResult.error("上传失败");
    }
}
