package com.yjr.controller;


import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.yjr.common.annotation.LogAnnotation;
import com.yjr.common.constant.ResultCode;
import com.yjr.common.result.Result;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class UploadController {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);


    @Value("${me.upload.path}")
    private String baseFolderPath;


    @GetMapping("/getimg/{filename}/get")
    public Result  getimg(HttpServletResponse response, @PathVariable("filename") String filename){
        MongoClient mongoClient = new MongoClient("120.78.176.221", 27017);
        // 连接到数据库.如果库不存在则创建
        DB db = mongoClient.getDB("pics");
        //文件操作是在DB的基础上实现的，与表和文档没有关系
        GridFS gridFS = new GridFS(db);
        GridFSDBFile one = gridFS.findOne(filename);

        try {
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            ServletOutputStream outputStream = response.getOutputStream();
            one.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
            }catch (Exception e){
            e.printStackTrace();
        }
        return Result.success();
    }



    @PostMapping("/upload")
    @RequiresAuthentication
    @LogAnnotation(module = "文件上传", operation = "文件上传")
    public Result upload(HttpServletRequest request, MultipartFile image) {

        Result r = new Result();

        try {
            String name = image.getOriginalFilename();
            InputStream imgStream = image.getInputStream();
            MongoClient mongoClient = new MongoClient("120.78.176.221", 27017);
            // 连接到数据库.如果库不存在则创建
            DB db = mongoClient.getDB("pics");

            //文件操作是在DB的基础上实现的，与表和文档没有关系
            GridFS gridFS = new GridFS(db);
            GridFSInputFile mongofile=gridFS.createFile(imgStream);
            //可以再添加属性
            //保存
            mongofile.setFilename(name);
            mongofile.save();
            r.setResultCode(ResultCode.SUCCESS);


        } catch (IOException e) {
            logger.error("文件上传错误 , uri: {} , caused by: ", request.getRequestURI(), e);
            r.setResultCode(ResultCode.UPLOAD_ERROR);
        }

        return r;
    }
}
