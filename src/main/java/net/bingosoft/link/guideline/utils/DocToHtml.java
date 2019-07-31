package net.bingosoft.link.guideline.utils;

/**
 * @Classname S
 * @Description TODO
 * @Date 2019/7/25 11:29
 * @Created by XL
 */
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
public class DocToHtml {
    public static String wordToHtml(String filePath) throws Exception{
        if(filePath.endsWith(".doc")){
            String content=docToHtml(filePath);
            return content;
        }
        if(filePath.endsWith(".docx")){
            String content=docxToHtml(filePath);
            return content;
        }
        return null;
    }

    private static String docToHtml(String sourceFileName) throws Exception{
        String htmlPath=sourceFileName.substring(0,sourceFileName.indexOf("."))+".html";
        String imagePathStr = new File(sourceFileName).getParent();
        File htmlFile=new File(htmlPath);
        if(! htmlFile.exists()){
            htmlFile.createNewFile();
        }
        File imagePath=new File(imagePathStr);
        if(!imagePath.exists()){
            imagePath.mkdirs();
        }
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            XWPFDocument document = new XWPFDocument(new FileInputStream(sourceFileName));
            XHTMLOptions options = XHTMLOptions.create();
            // 存放图片的文件夹
            options.setExtractor(new FileImageExtractor(new File(imagePathStr)));
            // html中图片的路径
            options.URIResolver(new BasicURIResolver("image"));
            byteArrayOutputStream = new ByteArrayOutputStream();
            XHTMLConverter xhtmlConverter = (XHTMLConverter) XHTMLConverter.getInstance();
            xhtmlConverter.convert(document, byteArrayOutputStream, options);

            String content =new String(byteArrayOutputStream.toByteArray());
            String htmlContent1=content.replaceAll("&ldquo;","\"").replaceAll("&rdquo;","\"").replaceAll("&mdash;","-");
            return htmlContent1;
        } finally {
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
        }
    }

    //docx转html
    //生成html文件
    //输出html标签和内容
    public static String docxToHtml(String sourceFileName) throws Exception {
        String basePathStr = sourceFileName.substring(0,sourceFileName.indexOf("."));
        File basePath = new File(basePathStr);
        if(!basePath.exists()){
            basePath.mkdirs();
        }

        String htmlPath=basePathStr+basePathStr.substring(basePathStr.lastIndexOf("\\"))+".html";
        String imagePathStr=basePathStr;
        XWPFDocument document = new XWPFDocument(new FileInputStream(sourceFileName));
        XHTMLOptions options = XHTMLOptions.create().indent(4);
        options.setExtractor(new FileImageExtractor(new File(imagePathStr)));
        File outFile = new File(htmlPath);
        outFile.getParentFile().mkdirs();
        OutputStream out = new FileOutputStream(outFile);
        XHTMLConverter.getInstance().convert(document,out, options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XHTMLConverter.getInstance().convert(document, baos, options);
        baos.close();
        String content =new String(baos.toByteArray());
        //替换UEditor无法识别的转义字符
        String htmlContent1=content.replaceAll("&ldquo;","\"").replaceAll("&rdquo;","\"").replaceAll("&mdash;","-");
        return htmlContent1;
    }
}
