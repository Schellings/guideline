package net.bingosoft.link.guideline.controller;


import com.alibaba.fastjson.JSONObject;
import net.bingosoft.link.guideline.utils.DocToHtml;
import net.bingosoft.link.guideline.utils.ImageUtil;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.Set;


/**
 * @Classname HomeController
 * @Description TODO
 * @Date 2019/7/15 22:07
 * @Created by XL
 */
@CrossOrigin(allowCredentials="true",maxAge = 3600)
@RestController
public class FileController {
    @ResponseBody
    @RequestMapping(value = "/file/resolve",method = RequestMethod.GET)
    public String resolveFile(String fileName) throws Exception {

        JSONObject result = new JSONObject();
        String content="解析失败，请重试！";
        String basePathStr = fileName.substring(0,fileName.indexOf("."));
        String filePath=basePathStr.substring(basePathStr.lastIndexOf("\\")+1);
        String basePath="http://localhost/"+filePath+"/";
        if(fileName!=null &&  !"".equals(fileName)){
            content= DocToHtml.docxToHtml(fileName);
            //处理img,basePath是图片的默认存放地址，此处使用Nginx来缓存图片
            content=ImageUtil.reloveImg(content,basePath);
        }

        result.put("basePath", basePath);
        result.put("msg", content);

        return result.toJSONString();

    }

    @ResponseBody
    @RequestMapping("/file//upload")
    public String  fileUpload(@RequestParam("file") MultipartFile file ) throws IOException {
        System.out.println("fileName："+file.getOriginalFilename());
        Long time=new Date().getTime();
        File basePath=new File("D:/"+ time);
        if(!basePath.exists()){
            basePath.mkdirs();
        }
        String path=basePath+file.getOriginalFilename();

        File newFile=new File(path);
        if (!newFile.exists()){
            newFile.createNewFile();
        }
        //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
        file.transferTo(newFile);
        JSONObject result = new JSONObject();
        result.put("status","success");
        result.put("url","http://localhost/"+time+file.getOriginalFilename());
        return result.toJSONString();
    }
    @ResponseBody
    @RequestMapping(value = "/file//save",method = RequestMethod.POST)
    public String save(HttpServletRequest request, HttpServletResponse response, String msg, String fileName) throws IOException {

        if (null != msg && (!msg.startsWith("<html>") || !msg.startsWith("<HTML>"))) {
            msg = "<html><body/>" + msg + "<body></html>";
        }
        System.out.println("html文本：" + msg);
        ByteArrayInputStream bais = null;
        FileOutputStream fos = null;
        String content = msg;
        byte b[] = content.getBytes();
        bais = new ByteArrayInputStream(b);
        POIFSFileSystem poifs = new POIFSFileSystem();
        DirectoryEntry directory = poifs.getRoot();
        DocumentEntry documentEntry = directory.createDocument("WordDocument", bais);
        fos = new FileOutputStream("D:/"+fileName+".doc");
        poifs.writeFilesystem(fos);
        bais.close();
        fos.close();
        if (fos != null) fos.close();
        if (bais != null) bais.close();
        return "";
    }
}
