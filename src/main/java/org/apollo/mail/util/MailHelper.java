package org.apollo.mail.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MailHelper {
    private static final Pattern FILE_PATTERN = Pattern.compile("^.*\\.(xls|xlsx|pdf|zip|rar|txt|doc|docx|csv)$");
    public static final String EMAIL_CTX_FILE_NAME = "EMAIL_CTX.TXT";
    public static final String MAIL_CHAR_CODE_STR_UTF8 = "=?utf-8?B?";
    
    public static String getFromAddr(MimeMessage mimeMessage) throws MessagingException {
        InternetAddress[] addresses = (InternetAddress[]) mimeMessage.getFrom();
        return addresses != null && addresses.length > 0 && addresses[0].getAddress() != null ?
               addresses[0].getAddress() : "";
    }

    public static String getSubjectNew(MimeMessage mimeMessage) {
        String subject = "";
        try {
            subject = MimeUtility.decodeText(mimeMessage.getSubject());
            if (isMessyCodeNew(subject)) {
                String subjectBody = mimeMessage.getHeader("Subject", null);
                if (isMessyCodeNew(subjectBody)) {
                    subject = transeferMessyCode(mimeMessage.getHeader("Subject", null));
                } else {
                    subject = decodeBase64Ew(subjectBody);
                }
            }
            if (subject == null) subject = "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subject;
    }

    public static String getMailContent(Part part) throws MessagingException, IOException {
        StringBuilder bodyText = new StringBuilder();
        if (part.isMimeType("text/plain") || part.isMimeType("text/html")) {
            bodyText.append((String) part.getContent());
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                bodyText.append(getMailContent(multipart.getBodyPart(i)));
            }
        } else if (part.isMimeType("message/rfc822")) {
            bodyText.append(getMailContent((Part) part.getContent()));
        }
        return bodyText.toString();
    }

    public static boolean isContainAttachment(Part part) throws MessagingException, IOException {
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disposition = bodyPart.getDisposition();
                if ((disposition != null) &&
                    (disposition.equalsIgnoreCase(Part.ATTACHMENT) ||
                     disposition.equalsIgnoreCase(Part.INLINE))) {
                    return true;
                } else if (bodyPart.isMimeType("multipart/*")) {
                    return isContainAttachment(bodyPart);
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            return isContainAttachment((Part) part.getContent());
        }
        return false;
    }

    /**
     * 保存邮件附件
     * @param part 邮件部分 (Message or BodyPart)
     * @param filepath 保存路径
     * @param attachmentPaths 用于存储附件路径的StringBuilder
     * @param subject 邮件主题 (用于日志)
     * @return 是否成功保存了附件
     * @throws Exception 保存过程中可能发生的异常
     */
    public static boolean saveAttachment(Part part, String filepath, StringBuilder attachmentPaths, String subject) throws Exception {
        boolean saved = false;
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disposition = bodyPart.getDisposition();
                
                // Check if it's an attachment
                if (disposition != null && (disposition.equalsIgnoreCase(Part.ATTACHMENT) || 
                                             disposition.equalsIgnoreCase(Part.INLINE))) {
                    String fileName = bodyPart.getFileName();
                    if (fileName != null) {
                        fileName = MimeUtility.decodeText(fileName);
                        // Basic sanitization
                        fileName = sanitizeFileName(fileName);
                        String fullPath = filepath + File.separator + fileName;
                        File file = new File(fullPath);
                        // Ensure directory exists
                        file.getParentFile().mkdirs(); 
                        // Save the attachment
                        FileOutputStream fos = new FileOutputStream(file);
                        IOUtils.copy(bodyPart.getInputStream(), fos);
                        fos.close();
                        attachmentPaths.append(fullPath).append(";"); // Append path
                        log.debug("Attachment saved: {}", fullPath);
                        saved = true;
                    }
                } else if (bodyPart.isMimeType("multipart/*")) {
                    // Recursively search nested multiparts
                    saved = saveAttachment(bodyPart, filepath, attachmentPaths, subject) || saved; // Pass BodyPart
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            // Handle embedded messages
            saved = saveAttachment((Part) part.getContent(), filepath, attachmentPaths, subject) || saved; // Pass Part
        }
        return saved;
    }

    private static String decodeFileName(String fileName) throws Exception {
        if (fileName == null) return null;
        
        String decodedName = MimeUtility.decodeText(fileName);
        if (isMessyCodeNew(decodedName)) {
            if (isMessyCodeNew(fileName)) {
                decodedName = transeferMessyCode(fileName);
            } else {
                decodedName = decodeBase64Ew(fileName);
            }
        }
        return decodedName;
    }

    private static String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[\\\\/:*?\"<>|]", "_")
                      .replaceAll("\\s+", "_");
    }

    public static boolean isMessyCodeNew(String str) {
        if (str == null || str.trim().isEmpty()) return false;
        Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
        Matcher m = p.matcher(str);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "")
                         .replaceAll("[`~\\$\\^\\+\\=<>\\|]", "");
        for (char c : temp.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && !isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    public static String transeferMessyCode(String str) {
        String[] charsets = new String[]{"GBK", "GB2312", "ISO-8859-1", "windows-1252", "GB18030", "Big5", "UTF-8"};
        for (String fromCharset : charsets) {
            for (String toCharset : charsets) {
                if (!fromCharset.equals(toCharset)) {
                    try {
                        String converted = new String(str.getBytes(fromCharset), toCharset);
                        if (!isMessyCodeNew(converted)) {
                            return converted;
                        }
                    } catch (UnsupportedEncodingException e) {
                        // Ignore and continue trying
                    }
                }
            }
        }
        return str;
    }

    public static String decodeBase64Ew(String eword) {
        try {
            if (!eword.startsWith("=?")) {
                return eword;
            }
            int start = 2;
            int pos = eword.indexOf('?', start);
            String charset = eword.substring(start, pos);
            int lpos = charset.indexOf('*');
            if (lpos >= 0) {
                charset = charset.substring(0, lpos);
            }
            String srcCharset = charset;
            charset = MimeUtility.javaCharset(charset);
            
            start = pos + 1;
            pos = eword.indexOf('?', start);
            String encoding = eword.substring(start, pos);
            
            String[] sarr = eword.split("\\=\\?" + srcCharset + "\\?" + encoding + "\\?");
            String rs = String.join("", sarr)
                            .replaceAll("\\?\\=", "")
                            .replaceAll("[\\n\\r\\t ]", "");
                            
            byte[] decode = Base64.getDecoder().decode(rs);
            return new String(decode, charset);
        } catch (Exception e) {
            return eword;
        }
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS ||
               ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS ||
               ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A ||
               ub == Character.UnicodeBlock.GENERAL_PUNCTUATION ||
               ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION ||
               ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    private static boolean isCJKChar(char ch) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(ch);
        return block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
            || block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            || block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
            || block == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            || block == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
            || block == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    public static Store connectMailServer(String protocol, String host, int port,
                                        String username, String password) throws MessagingException {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", protocol);
        props.setProperty("mail." + protocol + ".host", host);
        props.setProperty("mail." + protocol + ".port", String.valueOf(port));
        
        Session session = Session.getInstance(props);
        Store store = session.getStore(protocol);
        store.connect(host, port, username, password);
        return store;
    }
} 