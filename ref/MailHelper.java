package org.apollo.mail.utils.mail;

import org.apollo.mail.orm.entity.gzhs.mail.GjGzhsChkMailEntity;
import org.apollo.mail.utils.ExceptionUtil;
import org.apollo.mail.utils.GjGzhsSpringBeanUtil;
import org.apollo.mail.utils.msg.WeChatComSender;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author apollo.lyu
 * @date 2022年06月08日 15:34
 */
public class MailHelper {

    private static Logger log= LoggerFactory.getLogger(MailHelper.class);
    private static Pattern FILE_PATTERN  = Pattern.compile("^.*\\.(xls|xlsx|pdf|zip|rar|txt|doc|docx|csv)");
    public static final String  EMAIL_CTX_FILE_NAME  ="EMAIL_CTX.TXT";
    public static final String  MAIL_CHAR_CODE_STR_UTF8  ="=?utf-8?B?";
    /**
     *文件名非法字符
     */
    public static final String  FILENAME_ILLEGAL_CHAR  ="[\\:\\!\\?\\*\\\\\\/\\|\\<\\>\\\"]";

    /**
     *文件名空格换行字符
     */
    public static final String  FILENAME_SPACE_CHAR  ="[\n\r\\s]";


    public static String getFromAddr(MimeMessage mimeMessage) throws Exception {
        InternetAddress[] address = (InternetAddress[])((InternetAddress[])mimeMessage.getFrom());
        return null == address[0].getAddress() ? "" : address[0].getAddress();
    }

    public static String getFromNickName(MimeMessage mimeMessage) throws Exception {
        InternetAddress[] address = (InternetAddress[])((InternetAddress[])mimeMessage.getFrom());
        return null == address[0].getPersonal() ? "" : address[0].getPersonal();
    }

    public static String getSubjectNew(MimeMessage mimeMessage) throws MessagingException {
        String subject = "";

        try {
            subject = MimeUtility.decodeText(mimeMessage.getSubject());
            if(MailHelper.isMessyCodeNew(subject)){
                String subjectBody = mimeMessage.getHeader("Subject", null);
                if(MailHelper.isMessyCodeNew(subjectBody)){
                    subject=  MailHelper.transeferMessyCode( mimeMessage.getHeader("Subject", null));
                }else{
                    subject=MailHelper.decodeBase64Ew(subjectBody);
                }
            }
            if (subject == null) {
                subject = "";
            }

        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return subject;
    }

    public static String getMailAddress(MimeMessage mimeMessage,String type) throws Exception {
        String mailAddr = "";
        String addType = type.toUpperCase();
        InternetAddress[] address = null;
        if (!addType.equals("TO") && !addType.equals("CC") && !addType.equals("BCC")) {
            throw new Exception("错误的电子邮件类型!");
        } else {
            if (addType.equals("TO")) {
                address = (InternetAddress[])((InternetAddress[])mimeMessage.getRecipients(Message.RecipientType.TO));
            } else if (addType.equals("CC")) {
                address = (InternetAddress[])((InternetAddress[])mimeMessage.getRecipients(Message.RecipientType.CC));
            } else {
                address = (InternetAddress[])((InternetAddress[])mimeMessage.getRecipients(Message.RecipientType.BCC));
            }

            if (address != null) {
                for(int i = 0; i < address.length; ++i) {
                    String emailAddr = address[i].getAddress();
                    if (emailAddr == null) {
                        emailAddr = "";
                    } else {
                        emailAddr = MimeUtility.decodeText(emailAddr);
                    }

                    mailAddr = mailAddr + "," + emailAddr;
                }

                if(mailAddr.length()>1){
                    mailAddr = mailAddr.substring(1);
                }
            }

            return mailAddr;
        }
    }

    public static String getSubject(MimeMessage mimeMessage) throws MessagingException {
        String subject = "";

        try {
            subject = MimeUtility.decodeText(mimeMessage.getSubject());
            if (subject == null) {
                subject = "";
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return subject;
    }

    /**
     * 读取输入流中的数据保存至指定目录
     *
     * @param is       输入流
     * @param fileName 文件名
     * @param destDir  文件存储目录
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static  void saveFile(InputStream is, String destDir, String fileName)
            throws FileNotFoundException, IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(destDir + fileName));
        int len = -1;
        while ((len = bis.read()) != -1) {
            bos.write(len);
            bos.flush();
        }
        bos.close();
        bis.close();
    }

    /**
     * 读出邮件文件
     */
    public static String readEMailCtxFileUTF8(String rootPath,String vcMailuid,String vcMailtype,String ymd) {
        //rootPath+ File.separator+"mailchk"+File.separator+"local"
        String path=rootPath+File.separator+vcMailtype+File.separator+ymd+File.separator+vcMailuid+File.separator;
        File file02 = new File(path, EMAIL_CTX_FILE_NAME);
        FileInputStream is = null;
        StringBuffer sbf = new StringBuffer();
        try {
            if (file02.length() != 0) {
                /**
                 * 文件有内容才去读文件
                 */
                is = new FileInputStream(file02);
                InputStreamReader streamReader = new InputStreamReader(is,"UTF-8");
                BufferedReader reader = new BufferedReader(streamReader);
                String line;
                //stringBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    // stringBuilder.append(line);
                    sbf.append(line);
                }
                reader.close();
                is.close();
            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(sbf);

    }

    public static String readEMailCtxFileUTF8(GjGzhsChkMailEntity r, String yjsPath) {
        //String rootPath=SystemUtil.filePathTranslate(tmpFilePath+ File.separator+"mailchk"+File.separator+"local");
        String rootPath=yjsPath;
        String vcMailuid = r.getVcMailuid();
        String vcMailtype = r.getVcMailtype();
        String vcRecvym = r.getVcRecvym();
        String yyyymm = vcRecvym.replaceAll("-", "");
        String vcRecvd = r.getVcRecvd();
        String yyyymmdd=yyyymm+vcRecvd;
        //rootPath+ File.separator+"mailchk"+File.separator+"local"
        String str = MailHelper.readEMailCtxFileUTF8(rootPath, vcMailuid, vcMailtype, yyyymmdd);
        return str;
    }


    public static   void saveFile(String info, String destDir, String fileName,Charset charset)
            throws FileNotFoundException, IOException {
        InputStream is = convertStringToInputStream(info,charset);
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(destDir + fileName));
        int len = -1;
        while ((len = bis.read()) != -1) {
            bos.write(len);
            bos.flush();
        }
        bos.close();
        bis.close();
    }

    public static   void saveFileUTF8(String info, String destDir, String fileName)
            throws FileNotFoundException, IOException {
        InputStream is = convertStringToInputStream(info,StandardCharsets.UTF_8);
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(destDir + fileName));
        int len = -1;
        while ((len = bis.read()) != -1) {
            bos.write(len);
            bos.flush();
        }
        bos.close();
        bis.close();
    }


    public static  void saveAttachment(Part part, String destDir,StringBuffer path,String mailSubject) throws UnsupportedEncodingException, MessagingException,
            FileNotFoundException, IOException {
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();    //复杂体邮件
            //复杂体邮件包含多个邮件体
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                //获得复杂体邮件中其中一个邮件体
                BodyPart bodyPart = multipart.getBodyPart(i);
                //某一个邮件体也有可能是由多个邮件体组成的复杂体
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    InputStream is = bodyPart.getInputStream();
                    if (StringUtils.isNotEmpty(bodyPart.getFileName())) {
                        String fileName = MimeUtility.decodeText(bodyPart.getFileName());
                        if(MailHelper.isMessyCodeNew(fileName)){
                            fileName= MailHelper.transeferMessyCode(bodyPart.getFileName());
                        }
                        if(MailHelper.isMessyCodeNew(fileName)){
                            fileName=MailHelper.decodeBase64Ew(bodyPart.getFileName());
                        }
                        if(fileName.contains(MAIL_CHAR_CODE_STR_UTF8)){
                            fileName=MailHelper.decodeBase64Ew(bodyPart.getFileName());
                        }
                        if (StringUtils.isNotEmpty(fileName)) {
                            if(FILE_PATTERN.matcher(fileName.toLowerCase()).matches()){
                                fileName=fileName.replaceAll("/","");
                                fileName = getMailFileSpecialArchive(mailSubject, fileName);
                                fileName = fileName.replaceAll(FILENAME_ILLEGAL_CHAR,"");//非法字符去除
                                fileName = fileName.replaceAll(FILENAME_SPACE_CHAR,"");//空格换行去除
                                saveFile(is, destDir, fileName);
                                path.append(fileName).append(",");
                            }

                        }
                    }

                } else if (bodyPart.isMimeType("multipart/*")) {
                    saveAttachment(bodyPart, destDir, path,mailSubject);
                    // saveAttachment(bodyPart, destDir);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {
                        if (StringUtils.isNotEmpty(bodyPart.getFileName())) {
                            String fileName = MimeUtility.decodeText(bodyPart.getFileName());
                            if(MailHelper.isMessyCodeNew(fileName)){
                                fileName= MailHelper.transeferMessyCode(bodyPart.getFileName());
                            }
                            if(MailHelper.isMessyCodeNew(fileName)){
                                fileName=MailHelper.decodeBase64Ew(bodyPart.getFileName());
                            }
                            if(fileName.contains(MAIL_CHAR_CODE_STR_UTF8)){
                                fileName=MailHelper.decodeBase64Ew(bodyPart.getFileName());
                            }
                            if (StringUtils.isNotEmpty(fileName)) {
                                //新增不规则文件名处理
                                if(FILE_PATTERN.matcher(fileName.toLowerCase()).matches()) {
                                    fileName=fileName.replaceAll("/","");
                                    fileName = getMailFileSpecialArchive(mailSubject, fileName);
                                    fileName = fileName.replaceAll(FILENAME_ILLEGAL_CHAR,"");//非法字符去除
                                    fileName = fileName.replaceAll(FILENAME_SPACE_CHAR,"");//空格换行去除
                                    saveFile(bodyPart.getInputStream(), destDir, fileName);
                                    path.append(fileName).append(",");
                                }
                            }
                        }
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachment((Part) part.getContent(), destDir, path,mailSubject);
            //saveAttachment((Part) part.getContent(), destDir);
        }
    }

    /**
     * 特殊处理落地邮件附件
     * @param mailSubject
     * @param fileName
     * @return
     */
    private static String getMailFileSpecialArchive(String mailSubject, String fileName) {
        if(StringUtils.isNotBlank(mailSubject)){
            mailSubject.replaceAll("\\?","");
        }
        if(fileName.contains("?")){
            String fileSuffixLastName = getFileSuffixLastName(fileName);
            if(StringUtils.isNotBlank(mailSubject)){
                if(StringUtils.isNotBlank(fileSuffixLastName)){
                    fileName ="gzMailArchive_"+ mailSubject +fileSuffixLastName; //落地特殊处理
                }else{
                    fileName ="gzMailArchive_"+ mailSubject;
                }
                try {
                    sendWechatComMessage(mailSubject,fileName);
                }catch (Exception e){
                    ExceptionUtil.logErrorException(log,e);
                }
            }
        }
        return fileName;
    }

    public static void sendWechatComMessage(String mailSubject, String fileNameStr) {
        ExecutorService executorService = GjGzhsSpringBeanUtil.getBean("defaultExecutor");
        executorService.execute(() -> {
            WeChatComSender.sendMessage4GzCoreMailLocal("该邮件主题："+mailSubject+",接收到的附件涉及邮件编码问题，落地附件名为："+fileNameStr);
        });
    }

    // String -> InputStream
    public static InputStream convertStringToInputStream(String name) {
        InputStream result = new ByteArrayInputStream(name.getBytes(StandardCharsets.UTF_8));
        return result;
    }

    public static InputStream convertStringToInputStream(String name, Charset charset) {
        InputStream result = new ByteArrayInputStream(name.getBytes(charset));
        return result;
    }

    /*public String translateUtf8Base64(String eword,String charset){
        String rs= null;
        try {
            String s = decodeBase64Ew(eword);
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] decode = decoder.decode(s);
            rs = new String(decode,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return rs;
        }
        return rs;
    }*/

    /**
     * 判断邮件中是否包含附件
     * @return
     */
    public  static boolean isContainAttachment(Part part) throws Exception {
        boolean flag = false;
        if (part.isMimeType("multipart/*")) {
            MimeMultipart multipart = (MimeMultipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    flag = true;
                } else if (bodyPart.isMimeType("multipart/*")) {
                    flag = isContainAttachment(bodyPart);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("application") != -1) {
                        flag = true;
                    }

                    if (contentType.indexOf("name") != -1) {
                        flag = true;
                    }
                }
                if (flag) break;
            }
        } else if (part.isMimeType("message/rfc822")) {
            flag = isContainAttachment((Part)part.getContent());
        }
        return flag;
    }


    //获取发件人
    public static   String getFrom (Message message) throws Exception {
        InternetAddress[] address = (InternetAddress[]) message.getFrom();
        String from = address[0].getAddress();
        if (from == null){
            from = "";
        }
        return from;
    }

    //使用lastIndexOf()结合subString()获取后缀名
    private static String getFileSuffixLastName(String fileName){
        if(fileName==null) {
            return "";
        }
        if(fileName.lastIndexOf(".")==-1){
            return "";//文件没有后缀名的情况
        }
        //此时返回的是带有 . 的后缀名，
        //return filename.subString(filename.lastIndexOf(".")+1);// 这种返回的是没有.的后缀名
        // 下面这种如果对于String类型可能有问题，如 以.结尾的字符串，会报错。但是文件没有以点结尾的
        return fileName.substring(fileName.lastIndexOf("."));

    }

    /**
     * 获取邮件正文
     * @param part
     * @return
     * @throws MessagingException
     * @throws IOException
     */
    public static String getMailContent(Part part) throws MessagingException, IOException {
        StringBuffer bodytext = new StringBuffer();//存放邮件内容
        //判断邮件类型,不同类型操作不同
        if (part.isMimeType("text/plain")) {
            bodytext.append((String) part.getContent());
        }
        else if (part.isMimeType("text/html")) {
            bodytext.append((String) part.getContent());
        }
        else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int counts = multipart.getCount();
            for (int i = 0; i < counts; i++) {
                bodytext.append(getMailContent(multipart.getBodyPart(i)));
            }
        }
        else if (part.isMimeType("message/rfc822")) {
            getMailContent((Part) part.getContent());
        }
        else {

        }
        return bodytext.toString();
    }

    /**
     * 去除多余  标识服
     * @param eword
     */
    public static String decodeBase64Ew(String eword) {
        String rs = null;
        try {
            if (!eword.startsWith("=?")) { // not an encoded word
                throw new ParseException("encoded word does not start with \"=?\": " + eword);
            }
            // get charset
            int start = 2;
            int pos;
            if ((pos = eword.indexOf('?', start)) == -1) {
                throw new ParseException(
                        "encoded word does not include charset: " + eword);
            }
            String charset = eword.substring(start, pos);
            int lpos = charset.indexOf('*');	// RFC 2231 language specified?
            if (lpos >= 0)	{
                // yes, throw it away
                charset = charset.substring(0, lpos);
            }
            String srcCharset=charset;
            charset = MimeUtility.javaCharset(charset);

            // get encoding
            start = pos+1;
            if ((pos = eword.indexOf('?', start)) == -1) {
                throw new ParseException("encoded word does not include encoding: " + eword);
            }
            String encoding = eword.substring(start, pos);

            // get encoded-sequence
            start = pos+1;
            if ((pos = eword.indexOf("?=", start)) == -1) {
                throw new ParseException("encoded word does not end with \"?=\": " + eword);
            }
            String split="\\=\\?"+srcCharset+"\\?"+encoding+"\\?";
            String[] sarr = eword.split(split);
            rs = Arrays.stream(sarr).map(s->s.replaceAll("\\?\\=","")).collect(Collectors.joining(""));
            rs = rs.replaceAll("\n","")
                    .replaceAll("\r","")
                    .replaceAll("\t","")
                    .replaceAll(" ","");
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] decode = decoder.decode(rs);
            String rsVal = new String(decode,charset);
            return rsVal;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String transeferMessyCode(String str) {
        String decode = null;
        String code = null;
        String[] charsets = new String[]{"GBK", "GB2312", "ISO-8859-1", "windows-1252", "GB18030", "Big5", "UTF-8"};
        for (int i = 0; i < charsets.length; i++) {
            for (int j = 0; j < charsets.length; j++) {
                if (i != j) {
                    try {
                        String s = new String(str.getBytes(charsets[i]), charsets[j]);
                        if(!isMessyCode(s)){
                            /*code = charsets[i];
                            decode = charsets[j];*/
                            return s;
                        }
                    } catch (UnsupportedEncodingException e) {
                        return str;
                    }
                    //System.out.println(s);
                    //System.out.printf("先按照%s获取字符串的二进制：,然后按%s编码解读这个二进制，得到一个新的字符串：%s,字符编码: %s%n", charsets[i], charsets[j], s, Arrays.toString(str.getBytes(charsets[i])));
                }
            }
        }
        /*if (decode == null) {
            //System.out.println("无法还原");
            return str;
        } else {
            //System.out.println("编码: " + code + ", 解码: " + decode);
            return str;
        }*/
        return str;
    }

    public static boolean isMessyCode(String strName) {
        try {
            Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
            Matcher m = p.matcher(strName);
            String after = m.replaceAll("");
            String temp = after.replaceAll("\\p{P}", "");
            char[] ch = temp.trim().toCharArray();

            int length = (ch != null) ? ch.length : 0;
            for (int i = 0; i < length; i++) {
                char c = ch[i];
                if (!Character.isLetterOrDigit(c)) {
                    String str = "" + ch[i];
                    if (!str.matches("[\u4e00-\u9fa5]+")) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * isMessyCode.
     * @param strName strName
     * @param print log
     * @return boolean
     */
    public static boolean isMessyCodeNew(final String strName, final String print) {
        if (StringUtils.isBlank(strName)) {
            return false;
        }
        final Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
        final Matcher m = p.matcher(strName);
        final String after = m.replaceAll("");
        final String temp = after.replaceAll("\\p{P}", "")
                .replaceAll("`", "")
                .replaceAll("~", "")
                .replaceAll("\\$", "")
                .replaceAll("\\^", "")
                .replaceAll("\\+", "")
                .replaceAll("=", "")
                .replaceAll("<", "")
                .replaceAll(">", "")
                .replaceAll("\\|", "");
        final char[] ch = temp.trim().toCharArray();
        for (int i = 0; i < ch.length; i++) {
            final char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {

                if (!isChinese(c)) {
                    log.debug("isMessyCode: " + c + " -> " + strName + " -> " + print);
                    return true;
                }
            }
        }
        return false;

    }

    /**
     * isChinese.
     * @param c c
     * @return boolean
     */
    public static boolean isChinese(final char c) {
        final Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * isMessyCode.
     * @param strName strName
     * @return boolean
     */
    public static boolean isMessyCodeNew(final String strName) {
        return isMessyCodeNew(strName, "");
    }



}
