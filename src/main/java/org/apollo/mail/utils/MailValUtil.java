package org.apollo.mail.utils;

/**
 * @author liuf
 * @date 2023年08月09日 10:44
 */
public class MailValUtil {
    /**
     * 如果多个邮箱加起来的字符长度超长，进行截取
     * @param emailAddresses
     * @param maxLength
     * @return
     */
    public static String truncateEmails(String emailAddresses, int maxLength) {
        String[] emails = emailAddresses.split(",");
        StringBuilder result = new StringBuilder();
        int length = 0;

        for (String email : emails) {
            if (length + email.length() <= maxLength) {
                result.append(email).append(",");
                length += email.length() + 1; // 加1是为了计算逗号的长度
            } else {
                break;
            }
        }

        // 去除最后一个逗号
        if (result.length() > 0 && result.charAt(result.length() - 1) == ',') {
            result.deleteCharAt(result.length() - 1);
        }

        return result.toString();
    }
}