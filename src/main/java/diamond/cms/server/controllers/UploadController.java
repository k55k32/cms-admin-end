package diamond.cms.server.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import diamond.cms.server.annotations.IgnoreToken;

@RestController
@RequestMapping("upload")
public class UploadController {

    static final String PATH = "D:/images/";

    @RequestMapping("img/{name}")
    @IgnoreToken
    public void getImage(@PathVariable String name, HttpServletResponse response) throws IOException{
        File file = new File(PATH + name);
        if (file.exists()) {
            response.setContentType("image/png");
            FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
        } else {
            response.sendError(404);
        }
    }

    @RequestMapping("img")
    public String uploadImage (HttpServletRequest request, MultipartHttpServletRequest file) throws IOException {
        MultipartFile mf = file.getFile("file");
        String filename = "IMG_" + UUID.randomUUID().toString();
        File realFile = new File(PATH + filename);
        realFile.createNewFile();
        mf.transferTo(realFile);

        return request.getRequestURL() + "/" + filename;
    }
}
