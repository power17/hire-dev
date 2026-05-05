package com.power.controller;

import com.power.MinIOConfig;
import com.power.MinIOUtils;
import com.power.result.GraceJsonResult;
import com.power.result.ResponseStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Paths;

@RestController
@RequestMapping("file")
@Slf4j
public class FileController {
    public static final String host = "http://localhost:8000/";
    @PostMapping("uploadFace1")
    public GraceJsonResult uploadFace1(@RequestParam("file") MultipartFile file,
                                       @RequestParam("userId") String userId,
                                       HttpServletRequest request) throws Exception {

        // 获得文件原始名称
        String filename = file.getOriginalFilename();

//        "abc.123.abc.png"
        // 根据文件名中最后一个点的位置向后进行截取
        String suffixName = filename.substring(filename.lastIndexOf("."));

        // 文件新的名称
        String newFileName = userId + suffixName;
        String dir = Paths.get("").toAbsolutePath().toString();
        // 设置文件存储的路径，可以存放在指定的路径中，windows用户需要修改为对应的盘符
        String rootPath = dir + File.separator + "upload" + File.separator;
        // 图片存储的完全路径
        String filePath = rootPath + File.separator + "face" + File.separator + newFileName;
        log.info(filePath);

        File newFile = new File(filePath);
        if (!newFile.getParentFile().exists()) {
            // 如果目标文件所在目录不存在，则创建父目录
            newFile.getParentFile().mkdirs();
        }

        // 将内存中的文件数据写入到磁盘
        file.transferTo(newFile);

        // 生成web可以被访问的url地址
        String userFaceUrl = host + "static/face/" + newFileName;

        return GraceJsonResult.ok(userFaceUrl);
    }

    @Autowired
    private MinIOConfig minIOConfig;

    @PostMapping("uploadFace")
    public GraceJsonResult uploadFace(@RequestParam("file") MultipartFile file,
                                      @RequestParam("userId") String userId) throws Exception {

        if (StringUtils.isBlank(userId)) {
            return GraceJsonResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }

        // 获得文件原始名称
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            return GraceJsonResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }

        filename = userId + "-" + filename;
        MinIOUtils.uploadFile(minIOConfig.getBucketName(), filename, file.getInputStream());

        String imageUrl = minIOConfig.getFileHost()
                + "/"
                + minIOConfig.getBucketName()
                + "/"
                + filename;
        return GraceJsonResult.ok(imageUrl);
    }
}
