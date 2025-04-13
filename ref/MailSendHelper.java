package org.apollo.mail.utils.mail;

import com.gjzq.common.util.AESUtils;
import com.gjzq.common.util.CommonHelp;
import com.gjzq.common.util.MailUtils;
import com.gjzq.common.util.SystemUtil;
import org.apollo.mail.bean.param.mail.GjGzhsMailConfigParam;
import org.apollo.mail.bean.param.mail.glr.GjGlrzcMessageParam;
import org.apollo.mail.utils.GjGzhsSpringBeanUtil;
import com.gjzq.message.orm.entity.mail.MailEntity;
import com.gjzq.message.orm.entity.mail.config.MailSystemConfigEntity;
import com.gjzq.message.orm.entity.mail.config.MailSystemConfigEntityExample;
import com.gjzq.message.orm.mapper.mail.config.MailSystemConfigEntityMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author apollo.lyu
 * @date 2022年06月10日 9:39
 */
public class MailSendHelper {

    private static Logger log= LoggerFactory.getLogger(MailSendHelper.class);

    /**
     *
     * @param yjzt 邮件主题
     * @param info  邮件内容
     * @param vcfjcode  发件人   gjzq_yypt_systemparams编码配置
     * @param vcsjcode 收件人   gjzq_yypt_systemparams编码配置
     */
    public static    void sendError(String yjzt,String info,String vcfjcode,String vcsjcode) {
        MailSystemConfigEntityMapper mailEntityMapper = GjGzhsSpringBeanUtil.getBean(MailSystemConfigEntityMapper.class);
        String yjyx= SystemUtil.getSystemParams(vcfjcode);
        log.info("sendError 邮箱配置"+vcfjcode+"(Code)  yx: "+yjyx);
        String yjyxsjr= SystemUtil.getSystemParams(vcsjcode);
        log.info("sendError 邮箱配置"+vcsjcode+"(Code)  yx: "+yjyxsjr);
        MailSystemConfigEntityExample mailExample = new MailSystemConfigEntityExample();
        mailExample.createCriteria().andUsernameLike(yjyx + "%");
        log.info("CwcchdService.sendEmail 获取mailList开始: "+yjyx);
        List<MailEntity> mailList = hanlderMail(mailEntityMapper.selectByExample(mailExample));
        MailEntity mailEntity= CommonHelp.listGetOne(mailList, 0, MailEntity.class);
        log.info("CwcchdService.sendEmail par1：{}，开始连接", mailEntity.getUserName());
        log.info("CwcchdService.sendEmail par2：{}，开始连接", mailEntity.getPassword());
        try {
            GjGzhsMailConfigParam gzhsMailConfigParam = getWrapperMailConfig(yjzt, info, yjyxsjr, mailEntity);
            boolean cted = testMailConfig(mailEntity);
            if (!cted) {
                String msg="GzMailLocalChkService.testMailConfig  username:"+mailEntity.getUserName()+",password(加密):"+mailEntity.getPassword()+"连接失败！ 请检查密码";
                log.info(msg);
                throw new RuntimeException(msg);
            }
            sendMailbyParam(gzhsMailConfigParam);
        }catch (Exception e){
            e.printStackTrace();
            try {
                String yjyx2= SystemUtil.getSystemParams(vcfjcode+"2");
                log.info("sendError 邮箱配置"+vcfjcode+"2(Code)  yx: "+yjyx2);
                MailSystemConfigEntityExample ex = new MailSystemConfigEntityExample();
                ex.createCriteria().andUsernameLike(yjyx2 + "%");
                log.info("sendError. 切换邮箱开始中: "+yjyx2);
                MailEntity me = CommonHelp.listGetOne(hanlderMail(mailEntityMapper.selectByExample(mailExample)), 0, MailEntity.class);
                GjGzhsMailConfigParam gzhsMailConfigParam = getWrapperMailConfig(yjzt, info, yjyxsjr, me);
                sendMailbyParam(gzhsMailConfigParam);
            }catch (Exception e2){
                e.printStackTrace();
            }
        }

    }

    /**
     * GZYY-CWHQ-YCYX  异常发件人
     * GZYY-CWHQ-YCYXKF 异常收件人
     * GZYY-CWHQ-YCYX  备用发件人
     * gjzq_yypt_systemparams
     * @param yjzt
     * @param info
     * 场外模块邮件解析落地提醒
     */
    public static    void sendCwError(String yjzt,String info) {
        MailSystemConfigEntityMapper mailEntityMapper = GjGzhsSpringBeanUtil.getBean(MailSystemConfigEntityMapper.class);
        String yjyx= SystemUtil.getSystemParams("GZYY-CWHQ-YCYX");
        log.info("sendError 邮箱配置GZYY-CWHQ-YCYX(Code)  yx: "+yjyx);
        String yjyxsjr= SystemUtil.getSystemParams("GZYY-CWHQ-YCYXKF");
        log.info("sendError 邮箱配置GZYY-CWHQ-YCYXKF(Code)  yx: "+yjyxsjr);
        MailSystemConfigEntityExample mailExample = new MailSystemConfigEntityExample();
        mailExample.createCriteria().andUsernameLike(yjyx + "%");
        log.info("CwcchdService.sendEmail 获取mailList开始: "+yjyx);
        List<MailEntity> mailList = hanlderMail(mailEntityMapper.selectByExample(mailExample));
        MailEntity mailEntity= CommonHelp.listGetOne(mailList, 0, MailEntity.class);
        log.info("sendCwError.sendEmail par1：{}，开始连接", mailEntity.getUserName());
        log.info("sendCwError.sendEmail par2：{}，开始连接", mailEntity.getPassword());
        try {
            GjGzhsMailConfigParam gzhsMailConfigParam = getWrapperMailConfig(yjzt, info, yjyxsjr, mailEntity);
            boolean cted = testMailConfig(mailEntity);
            if (!cted) {
                String msg="GzMailLocalChkService.testMailConfig  username:"+mailEntity.getUserName()+",password(加密):"+mailEntity.getPassword()+"连接失败！ 请检查密码";
                log.info(msg);
                throw new RuntimeException(msg);
            }
            sendMailbyParam(gzhsMailConfigParam);
        }catch (Exception e){
            e.printStackTrace();
            try {
                String yjyx2= SystemUtil.getSystemParams("GZYY-CWHQ-YCYX2");
                log.info("sendError 邮箱配置GZYY-CWHQ-YCYX2(Code)  yx: "+yjyx2);
                MailSystemConfigEntityExample ex = new MailSystemConfigEntityExample();
                ex.createCriteria().andUsernameLike(yjyx2 + "%");
                log.info("sendError. 切换邮箱开始中: "+yjyx2);
                MailEntity me = CommonHelp.listGetOne(hanlderMail(mailEntityMapper.selectByExample(ex)), 0, MailEntity.class);
                GjGzhsMailConfigParam gzhsMailConfigParam = getWrapperMailConfig(yjzt, info, yjyxsjr, me);
                sendMailbyParam(gzhsMailConfigParam);
            }catch (Exception e2){
                e.printStackTrace();
            }
        }
    }

    /**
     * 自定义收件人
     * @param yjzt
     * @param info
     * @param yjyxsjr
     */
    public static  void sendCwError(String yjzt,String info,String yjyxsjr) {
        MailSystemConfigEntityMapper mailEntityMapper = GjGzhsSpringBeanUtil.getBean(MailSystemConfigEntityMapper.class);
        String yjyx= SystemUtil.getSystemParams("GZYY-CWHQ-YCYX");
        log.info("sendError 邮箱配置GZYY-CWHQ-YCYX(Code)  yx: "+yjyx);
        MailSystemConfigEntityExample mailExample = new MailSystemConfigEntityExample();
        mailExample.createCriteria().andUsernameLike(yjyx + "%");
        log.info("CwcchdService.sendEmail 获取mailList开始: "+yjyx);
        List<MailEntity> mailList = hanlderMail(mailEntityMapper.selectByExample(mailExample));
        MailEntity mailEntity= CommonHelp.listGetOne(mailList, 0, MailEntity.class);
        log.info("CwcchdService.sendEmail par1：{}，开始连接", mailEntity.getUserName());
        log.info("CwcchdService.sendEmail par2：{}，开始连接", mailEntity.getPassword());
        try {
            GjGzhsMailConfigParam gzhsMailConfigParam = getWrapperMailConfig(yjzt, info, yjyxsjr, mailEntity);
            boolean cted = testMailConfig(mailEntity);
            if (!cted) {
                String msg="GzMailLocalChkService.testMailConfig  username:"+mailEntity.getUserName()+",password(加密):"+mailEntity.getPassword()+"连接失败！ 请检查密码";
                log.info(msg);
                throw new RuntimeException(msg);
            }
            sendMailbyParam(gzhsMailConfigParam);
        }catch (Exception e){
            e.printStackTrace();
            try {
                String yjyx2= SystemUtil.getSystemParams("GZYY-CWHQ-YCYX2");
                log.info("sendError 邮箱配置GZYY-CWHQ-YCYX2(Code)  yx: "+yjyx2);
                MailSystemConfigEntityExample ex = new MailSystemConfigEntityExample();
                ex.createCriteria().andUsernameLike(yjyx2 + "%");
                log.info("sendError. 切换邮箱开始中: "+yjyx2);
                MailEntity me = CommonHelp.listGetOne(hanlderMail(mailEntityMapper.selectByExample(mailExample)), 0, MailEntity.class);
                GjGzhsMailConfigParam gzhsMailConfigParam = getWrapperMailConfig(yjzt, info, yjyxsjr, me);
                sendMailbyParam(gzhsMailConfigParam);
            }catch (Exception e2){
                e.printStackTrace();
            }
        }
    }


    /**
     * 自定义邮件发送
     * @param yjzt
     * @param info
     * @param yjyxsjr
     */
    public static  void sendMailContext(String yjzt,String info,String yjyxsjr) {
        MailSystemConfigEntityMapper mailEntityMapper = GjGzhsSpringBeanUtil.getBean(MailSystemConfigEntityMapper.class);
        String yjyx= SystemUtil.getSystemParams("GZYY-CWHQ-YCYX");
        log.info("MailSendHelper 邮箱配置GZYY-CWHQ-YCYX(Code)  yx: "+yjyx);
        MailSystemConfigEntityExample mailExample = new MailSystemConfigEntityExample();
        //mailExample.createCriteria().andUsernameLike(yjyx + "%");
        mailExample.createCriteria().andDescrEqualTo(yjyx).andIsDeleteEqualTo("0");
        log.info("MailSendHelper.sendEmail 获取mailList开始: "+yjyx);
        List<MailEntity> mailList = hanlderMail(mailEntityMapper.selectByExample(mailExample));
        MailEntity mailEntity= CommonHelp.listGetOne(mailList, 0, MailEntity.class);
        log.info("MailSendHelper.sendEmail par1：{}，开始连接", mailEntity.getUserName());
        log.info("MailSendHelper.sendEmail par2：{}，开始连接", mailEntity.getPassword());
        try {
            GjGzhsMailConfigParam gzhsMailConfigParam = getWrapperMailConfig(yjzt, info, yjyxsjr, mailEntity);
            boolean cted = testMailConfig(mailEntity);
            if (!cted) {
                String msg="GzMailLocalChkService.testMailConfig  username:"+mailEntity.getUserName()+",password(加密):"+mailEntity.getPassword()+"连接失败！ 请检查密码";
                log.info(msg);
                throw new RuntimeException(msg);
            }
            sendMailbyParam(gzhsMailConfigParam);
        }catch (Exception e){
            e.printStackTrace();
            try {
                String yjyx2= SystemUtil.getSystemParams("GZYY-CWHQ-YCYX2");
                log.info("MailSendHelper 邮箱配置GZYY-CWHQ-YCYX2(Code)  yx: "+yjyx2);
                MailSystemConfigEntityExample ex = new MailSystemConfigEntityExample();
                //ex.createCriteria().andUsernameLike(yjyx2 + "%");
                ex.createCriteria().andDescrEqualTo(yjyx2).andIsDeleteEqualTo("0");
                log.info("MailSendHelper. 切换邮箱开始中: "+yjyx2);
                MailEntity me = CommonHelp.listGetOne(hanlderMail(mailEntityMapper.selectByExample(mailExample)), 0, MailEntity.class);
                GjGzhsMailConfigParam gzhsMailConfigParam = getWrapperMailConfig(yjzt, info, yjyxsjr, me);
                sendMailbyParam(gzhsMailConfigParam);
            }catch (Exception e2){
                e.printStackTrace();
            }
        }
    }

    public static GjGzhsMailConfigParam getWrapperMailConfig(String yjzt, String info, String yjyxsjr, MailEntity mailEntity) {
        GjGzhsMailConfigParam gzhsMailConfigParam = new GjGzhsMailConfigParam();
        gzhsMailConfigParam.setProtocol(mailEntity.getServerType());
        gzhsMailConfigParam.setHost(mailEntity.getHost());
        gzhsMailConfigParam.setPort(mailEntity.getPort());
        gzhsMailConfigParam.setUsername(mailEntity.getUserName());
        gzhsMailConfigParam.setPassword(mailEntity.getPassword());
        GjGlrzcMessageParam mailMessageParam = new GjGlrzcMessageParam();
        mailMessageParam.setVcNr(info);
        mailMessageParam.setVcBt(yjzt);
        //mailMessageParam.setVcCsr(vcCsr);
        mailMessageParam.setVcJsr(yjyxsjr);
        mailMessageParam.setVcFsr(mailEntity.getUserName());
        gzhsMailConfigParam.setMailMessageParam(mailMessageParam);
        return gzhsMailConfigParam;
    }

    /**
     * 连通性测试
     * @param e
     * @return
     */
    public static boolean testMailConfig(MailEntity e) {
        log.info("MailSendHelper.testMailConfig par1：{}，开始连接", e.getUserName());
        log.info("MailSendHelper.testMailConfig par2：{}，开始连接", e.getPassword());
        return MailUtils.testConnect(e.getServerType(), e.getHost(), e.getPort(), e.getUserName(), AESUtils.aesDecrypt(e.getPassword()), false);
    }

    /**
     * 发送邮件
     * @param cfg
     * @throws Exception
     */
    public static   void  sendMailbyParam (GjGzhsMailConfigParam cfg)  throws  Exception{
        Properties prop = MailUtils.getPropertiesByType(cfg.getProtocol(),cfg.getHost(),cfg.getPort(),false);
        Session session = MailUtils.createSession(prop,cfg.getUsername(), AESUtils.aesDecrypt(cfg.getPassword()));
        //使用javaMail发送邮件的5个步骤
        //1.创建定义整个应用程序所需要的环境信息的session对象
        //开启session的debug模式，这样可以查看到程序发送Email的运行状态
        session.setDebug(true);
        //2.通过session得到transport对象
        Transport ts=session.getTransport();
        //3.使用邮箱的用户名和授权码连上邮件服务器
        ts.connect(cfg.getHost(),cfg.getUsername(),AESUtils.aesDecrypt(cfg.getPassword()));
        //4.创建邮件：写文件
        //注意需要传递session
        MimeMessage message=new MimeMessage(session);
        //指明邮件的发件人
        String nickName= "估值模块告警";
        if(StringUtils.isNotBlank(nickName)){
            //设置自定义发件人昵称
            String nickcode="";
            try {
                nickcode=javax.mail.internet.MimeUtility.encodeText(nickName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            message.setFrom(new InternetAddress(nickcode+"<"+cfg.getUsername()+">"));
        }else{
            //不设置自定义发件人昵称
            message.setFrom(new InternetAddress(cfg.getUsername()));
        }
        //指明邮件的收件人
        GjGlrzcMessageParam mailMessageParam = cfg.getMailMessageParam();
        String vcJsr = mailMessageParam.getVcJsr();
        if (StringUtils.isNotBlank(vcJsr)){
            vcJsr=vcJsr.replaceAll(";",",");
        }
        String vcCsr = mailMessageParam.getVcCsr();
        if (StringUtils.isNotBlank(vcCsr)){
            vcCsr=vcCsr.replaceAll(";",",");
        }
        //主送
        message.setRecipients(Message.RecipientType.TO,vcJsr);
        //抄送
        message.setRecipients(Message.RecipientType.CC,vcCsr);
        //邮件标题
        message.setSubject(mailMessageParam.getVcBt());
        //邮件的文本内容
        message.setContent(mailMessageParam.getVcNr(),"text/html;charset=UTF-8");
        //5.发送邮件
        ts.sendMessage(message,message.getAllRecipients());
        //6.关闭连接
        ts.close();
        log.info("邮件发送完毕。");
    }


    private  static   List<MailEntity> hanlderMail(List<MailSystemConfigEntity> list) {
        List<MailEntity> mailList = new ArrayList<>();
        for (MailSystemConfigEntity entity : list) {
            MailEntity mailEntity = new MailEntity();
            mailEntity.setServerType(entity.getProtocol());
            mailEntity.setHost(entity.getHost());
            mailEntity.setPort(entity.getPort());
            mailEntity.setUserName(entity.getUsername());
            mailEntity.setPassword(entity.getPassword());
            mailList.add(mailEntity);
        }
        return mailList;
    }


    /**
     * 特殊债券邮件发送
     * @param yjzt
     * @param info
     * @param yjyxsjr
     * @param vcCsr
     */
    public static  void sendTszqMailContext(String yjzt,String info,String yjyxsjr,String vcCsr) {
        log.info("sendTszqMailContext 收件人："+yjyxsjr+",抄送人："+vcCsr);
        MailSystemConfigEntityMapper mailEntityMapper = GjGzhsSpringBeanUtil.getBean(MailSystemConfigEntityMapper.class);
        String yjyx= SystemUtil.getSystemParams("GZYY-TSYW-YJTS");
        log.info("MailSendHelper 邮箱配置GZYY-TSYW-YJTS(Code)  yx: "+yjyx);
        MailSystemConfigEntityExample mailExample = new MailSystemConfigEntityExample();
        MailSystemConfigEntityExample.Criteria criteria = mailExample.createCriteria();
        criteria.andDescrEqualTo(yjyx);
        criteria.andIsDeleteEqualTo("0");
        log.info("MailSendHelper.sendEmail 获取mailList开始: "+yjyx);
        List<MailEntity> mailList = hanlderMail(mailEntityMapper.selectByExample(mailExample));
        log.info("mailList  size:"+mailList.size());
        MailEntity mailEntity= CommonHelp.listGetOne(mailList, 0, MailEntity.class);
        log.info("MailSendHelper.sendEmail par1：{}，开始连接", mailEntity.getUserName());
        log.info("MailSendHelper.sendEmail par2：{}，开始连接", mailEntity.getPassword());
        try {
            log.info("send csr :"+vcCsr);
            GjGzhsMailConfigParam gzhsMailConfigParam = getWrapperMailTszqConfig(yjzt, info, yjyxsjr, vcCsr, mailEntity);
            boolean cted = testMailConfig(mailEntity);
            if (!cted) {
                String msg="GzMailLocalChkService.testMailConfig  username:"+mailEntity.getUserName()+",password(加密):"+mailEntity.getPassword()+"连接失败！ 请检查密码";
                log.info(msg);
                throw new RuntimeException(msg);
            }
            log.info("start sendTszqMailbyParam...");
            sendTszqMailbyParam(gzhsMailConfigParam);
            log.info("end sendTszqMailbyParam...");
        }catch (Exception e){
            e.printStackTrace();
            try {
                String yjyx2= SystemUtil.getSystemParams("GZYY-TSYW-YJTS");
                log.info("MailSendHelper 邮箱配置GZYY-TSYW-YJTS(Code)  yx: "+yjyx2);
                MailSystemConfigEntityExample ex = new MailSystemConfigEntityExample();
                MailSystemConfigEntityExample.Criteria criteria1 = ex.createCriteria();
                criteria1.andDescrEqualTo(yjyx2);
                criteria1.andIsDeleteEqualTo("0");
                log.info("MailSendHelper. 第二次尝试中: "+yjyx2);
                MailEntity me = CommonHelp.listGetOne(hanlderMail(mailEntityMapper.selectByExample(ex)), 0, MailEntity.class);
                GjGzhsMailConfigParam gzhsMailConfigParam = getWrapperMailConfig(yjzt, info, yjyxsjr, me);
                sendTszqMailbyParam(gzhsMailConfigParam);
            }catch (Exception e2){
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    public static GjGzhsMailConfigParam getWrapperMailTszqConfig(String yjzt, String info, String yjyxsjr,String vcCsr, MailEntity mailEntity) {
        GjGzhsMailConfigParam gzhsMailConfigParam = new GjGzhsMailConfigParam();
        gzhsMailConfigParam.setProtocol(mailEntity.getServerType());
        gzhsMailConfigParam.setHost(mailEntity.getHost());
        gzhsMailConfigParam.setPort(mailEntity.getPort());
        gzhsMailConfigParam.setUsername(mailEntity.getUserName());
        gzhsMailConfigParam.setPassword(mailEntity.getPassword());
        GjGlrzcMessageParam mailMessageParam = new GjGlrzcMessageParam();
        mailMessageParam.setVcNr(info);
        mailMessageParam.setVcBt(yjzt);
        mailMessageParam.setVcCsr(vcCsr);
        mailMessageParam.setVcJsr(yjyxsjr);
        mailMessageParam.setVcFsr(mailEntity.getUserName());
        gzhsMailConfigParam.setMailMessageParam(mailMessageParam);
        return gzhsMailConfigParam;
    }

    /**
     * 发送邮件
     * @param cfg
     * @throws Exception
     */
    public static void sendTszqMailbyParam (GjGzhsMailConfigParam cfg)  throws  Exception{
        Properties prop = MailUtils.getPropertiesByType(cfg.getProtocol(),cfg.getHost(),cfg.getPort(),false);
        Session session = MailUtils.createSession(prop,cfg.getUsername(), AESUtils.aesDecrypt(cfg.getPassword()));
        //使用javaMail发送邮件的5个步骤
        //1.创建定义整个应用程序所需要的环境信息的session对象
        //开启session的debug模式，这样可以查看到程序发送Email的运行状态
        session.setDebug(true);
        //2.通过session得到transport对象
        Transport ts=session.getTransport();
        //3.使用邮箱的用户名和授权码连上邮件服务器
        ts.connect(cfg.getHost(),cfg.getUsername(),AESUtils.aesDecrypt(cfg.getPassword()));
        //4.创建邮件：写文件
        //注意需要传递session
        MimeMessage message=new MimeMessage(session);
        //指明邮件的发件人
        String nickName= "估值模块提醒";
        if(StringUtils.isNotBlank(nickName)){
            //设置自定义发件人昵称
            String nickcode="";
            try {
                nickcode=javax.mail.internet.MimeUtility.encodeText(nickName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            message.setFrom(new InternetAddress(nickcode+"<"+cfg.getUsername()+">"));
        }else{
            //不设置自定义发件人昵称
            message.setFrom(new InternetAddress(cfg.getUsername()));
        }
        //指明邮件的收件人
        GjGlrzcMessageParam mailMessageParam = cfg.getMailMessageParam();
        String vcJsr = mailMessageParam.getVcJsr();
        if (StringUtils.isNotBlank(vcJsr)){
            vcJsr=vcJsr.replaceAll(";",",");
        }
        String vcCsr = mailMessageParam.getVcCsr();
        if (StringUtils.isNotBlank(vcCsr)){
            vcCsr=vcCsr.replaceAll(";",",");
        }
        //主送
        message.setRecipients(Message.RecipientType.TO,vcJsr);
        //抄送
        message.setRecipients(Message.RecipientType.CC,vcCsr);
        //邮件标题
        message.setSubject(mailMessageParam.getVcBt());
        //邮件的文本内容
        message.setContent(mailMessageParam.getVcNr(),"text/html;charset=UTF-8");
        //5.发送邮件
        ts.sendMessage(message,message.getAllRecipients());
        //6.关闭连接
        ts.close();
        log.info("邮件发送完毕。");
    }
}
