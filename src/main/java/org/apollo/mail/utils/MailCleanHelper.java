package org.apollo.mail.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;

/**
 * @author liuf
 * @date 2022年08月09日 10:44
 */
public class MailCleanHelper {
    private static Logger log= LoggerFactory.getLogger(MailCleanHelper.class);

    /**
     * 批量删除邮件
     * @param messages 要解析的邮件列表
     */
    public static void deleteMessage(Folder folder, Message[] messages) throws MessagingException, IOException {
        if (messages == null || messages.length < 1)   {
            return;
        }
        // 循环邮件
        for (int i = 0, count = messages.length; i < count; i++) {
            /**
             *   邮件删除
             */
            Message message = messages[i];
            // set the DELETE flag to true
            //String uid = ((POP3Folder) folder).getUID(message);
            //System.out.println(uid);
            message.setFlag(Flags.Flag.DELETED, true);
            log.info("删除中：第"+(i+1)+"封，总计"+messages.length+"封。");
        }
    }
}