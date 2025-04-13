package org.apollo.mail.service.mail;

import com.gjzq.common.util.*;
import com.gjzq.common.util.mailAnalysis.MailImfo;
import org.apollo.mail.orm.entity.gzhs.mail.GjGzhsChkMailEntity;
import org.apollo.mail.orm.entity.gzhs.mail.GjGzhsChkMailEntityWithBLOBs;
import org.apollo.mail.orm.entity.gzhs.mail.GjGzhsMailIdsCheckEntity;
import org.apollo.mail.orm.entity.gzhs.mail.GjGzhsMailIdsCheckEntityExample;
import org.apollo.mail.orm.mapper.gzhs.GzhsCommonExecMapper;
import org.apollo.mail.orm.mapper.gzhs.mail.GjGzhsChkMailEntityMapper;
import org.apollo.mail.orm.mapper.gzhs.mail.GjGzhsMailIdsCheckEntityMapper;
import org.apollo.mail.utils.CastUtil;
import org.apollo.mail.utils.ExceptionUtil;
import org.apollo.mail.utils.IpUtils;
import org.apollo.mail.utils.biz.common.ValUtil;
import org.apollo.mail.utils.mail.MailCleanHelper;
import org.apollo.mail.utils.mail.MailHelper;
import org.apollo.mail.utils.mail.MailSendHelper;
import org.apollo.mail.utils.mail.MailValUtil;
import org.apollo.mail.utils.msg.WeChatComSender;
import com.gjzq.message.orm.entity.mail.MailEntity;
import com.gjzq.message.orm.entity.mail.config.MailSystemConfigEntity;
import com.gjzq.message.orm.entity.mail.config.MailSystemConfigEntityExample;
import com.gjzq.message.orm.mapper.mail.config.MailSystemConfigEntityMapper;
import com.sun.mail.pop3.POP3Folder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

/**
 *
 */
@Slf4j
@Service
public class GzhsCoreMailLocalChkService {

    @Resource(name = "transactionManager")
    private PlatformTransactionManager transactionManager;
    @Resource(name = "defaultExecutor")
    private ExecutorService executorService;
    @Autowired
    private GjGzhsChkMailEntityMapper gzhsChkMailEntityMapper;
    @Autowired
    private GjGzhsMailIdsCheckEntityMapper gzhsMailIdsCheckEntityMapper;
    @Autowired
    private GzhsCommonExecMapper gzCommonExecMapper;
    @Autowired
    private MailSystemConfigEntityMapper mailEntityMapper;


    @Value("${file.tmp.base-path}")
    private String tmpFilePath;

    @Value("${mail.tmp.base-path}")
    private String fileBasePath;

    private Pattern FILE_PATTERN  = Pattern.compile("^.*\\.(xls|xlsx|pdf|zip|rar|csv)");
    private final String  EMAIL_CTX_FILE_NAME  ="EMAIL_CTX.TXT";
    /**
     * 信创邮箱标记
     */
    private final String  EMAIL_XCMAIL_PREFIX  ="__xcmailflag_";

    private  ReentrantLock lock = new ReentrantLock();

    private  Date lastLockDate=null;

    private String subjectContent;

    public  void synGzMailLocal(String param) {
        /*synchronized (GzhsCoreMailLocalChkService.class){
            log.info("synGzMailLocal synchronized Started......");
            synGzMailLocalExec(param);
            log.info("synGzMailLocal synchronized End......");
        }*/
        synGzMailLocalExec(param);
    }

    public  void synGzMailLocalExec(String param) {
        String[] parArray = param.split(",");
        log.info("synGzMailLocal 定时任务执行开始......");

        String yjyx= SystemUtil.getSystemParams("GZYY-GZXCMAIL-YJYX");
        log.info("synGzMailLocal 估值模块 信创邮件落地邮箱 GZYY-GZXCMAIL-YJYX(Code)  yx: "+yjyx);


        /**邮件落地整个一套流程 start*/
        synchronized (GzhsCoreMailLocalChkService.class){
            if (mailSynLocalData(yjyx)) return;
        }
        /**邮件落地整个一套流程 end */


        /** 最后执行 清理部分 */
        //connectToEmail4LocalAfter(yjyx);
        connectToEmail4LocalAfterByExecutors(yjyx);
        log.info("场外邮件落地自动任务执行结束end，操作时间：{} ", DateUtil.formatDateByYYYYMMdd_HHmmss(new Date()));
    }


    /**
     * 邮件落地整个一套流程
     * @param yjyx
     * @return
     */
    private boolean mailSynLocalData(String yjyx) {
        Store store=null;
        //获取上一次读取的邮件id 获取上次uid记录 用于失败后撤回标记位
        String lastMailids=getGzMailids(EMAIL_XCMAIL_PREFIX+ yjyx);
        //提醒相关
        pwdTip();
        //测试用落地标记
        if (mailSynLocalForTestFlag()) return true;

        Date now = new Date();
        String nowStr = DateUtil.formatDateByYYYYMMdd_HHmmss(now);
        log.info("场外邮件落地自动任务执行开始begin，操作时间：{} ",nowStr);
        MailEntity mailEntity = getMailEntity(yjyx);
        try {
            if(mailEntity==null){
                log.info("场外邮件落地自动任务执行结束(没有配置邮件GZYY-CWHQ-YJYX)end，操作时间：{} ",nowStr);
                throw new RuntimeException("场外邮件落地自动任务执行结束(没有配置邮件GZYY-CWHQ-YJYX)end,操作时间："+nowStr);
            }

            boolean cted = testMailConfig(mailEntity);
            log.info("GzMailLocalChkService.testMailConfig 连接是否成功：{} ", cted);
            if(!cted){
                String info="testMailConfig 邮件连接失败！，邮箱："+ yjyx +",请查看日志！并排查（1.密码是否正确;2邮件提供服务不稳定;3网络环境），详细查看日志";
                sendGzmailExceptionMessage(info);
            }
            log.info("GzMailLocalChkService.testMailConfig 连接是否成功：{} ", "开始连接邮箱进行落地操作");
            connectToEmail4Local(mailEntity, store, yjyx, lastMailids);

        }catch (Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw,true));
            String infomsg = sw.toString();
            log.error(infomsg);

            //手工实现回滚 提高性能  标记未落地成功
            updateYjyxMailid(EMAIL_XCMAIL_PREFIX+ yjyx, lastMailids);
            //gzmaillock
            //gzCommonExecMapper.updateBySql(" update GJZQ_YYPT_SYSTEMPARAMS  a set a.VC_CODENOTE='0' where  a.VC_CODE='GZYY-GZMAIL_LOCKSTA' ");
            //log.info("synGzMailLocal 估值模块场外落地本轮执行完毕 解锁 GZYY-GZMAIL_LOCKSTA==0 ");

            //异常处理 发生企业微信或邮件
            sendGzmailExceptionMessage(infomsg);
        }
        return false;
    }


    private  void connectToEmail4Local(MailEntity e, Store store,String yjyx,String lastMailids) throws Exception{
        Boolean islongIterFlag=false;
        /**每封邮件的间隔相关参数start**/
        String mfyjsj= SystemUtil.getSystemParams("GZYY-GZYQSJ-YJINTVEL");
        log.info("synGzMailLocal 估值模块场外落地每封邮件预期时间配置 GZYY-GZYQSJ-YJINTVEL(Code)  rs: "+mfyjsj);
        int intMfyjsj = CastUtil.castInt(mfyjsj, 180);
        log.info("synGzMailLocal 估值模块场外落地每封邮件预期时间配置 GZYY-GZYQSJ-YJINTVEL(Code)  intMfyjsj: "+intMfyjsj);
        /**每封邮件的间隔相关参数end **/

        /**环境参数 start**/
        //1DEV开发环境 2.TEST 测试环境 3.UAT 仿真环境 4.PROD 生产环境
        String env= SystemUtil.getSystemParams("GZYY-CWHQ-ENV");
        env=StringUtils.isNotBlank(env)?env:"";
        /**环境参数 end**/
        //邮件发生异常 提醒标记
        Boolean istipedFlag=false;

        Date startTime=new Date();

        //TransactionStatus status=transactionManager.getTransaction(new DefaultTransactionDefinition());
        log.info("GzMailLocalChkService.testMailConfig 连接是否成功：{} ", "connectToEmail4Local");
        //String allyjyx = preFjrStrs();
        Date now=new Date();
        Properties prop = MailUtils.getPropertiesByType(e.getServerType(),e.getHost(),e.getPort(),false);
        prop.setProperty("mail.mime.address.strict", "false");
        Session session = MailUtils.createSession(prop,e.getUserName(), AESUtils.aesDecrypt(e.getPassword()));
        store = session.getStore(e.getServerType());
        store.connect();
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_WRITE);
        log.info("connectToEmail收件箱已打开，开始获取邮件");
        //Message message[] = null;
        List<Message> msgs = getMessages(folder, EMAIL_XCMAIL_PREFIX+yjyx);
        String needgzMailids = getGzMailids(EMAIL_XCMAIL_PREFIX+yjyx);
        log.info("connectToEmail last umailids:"+lastMailids);
        log.info("connectToEmail needCommit umailids:"+needgzMailids);
        updateYjyxMailid(EMAIL_XCMAIL_PREFIX+yjyx, lastMailids);
        log.info("connectToEmail收件箱已打开，***********************getMessages");
        log.info("connectToEmail收件箱已打开，***********************getMessages size()="+msgs.size());
        String rulePath=SystemUtil.filePathTranslate(tmpFilePath+ File.separator+"mailchk"+File.separator+"local");
        log.info("connectToEmail收件箱已打开，***********************rulePath");
        Date lastTimeSjT=new Date();//上次时间  邮件遍历
        Date currSjTime=new Date();//本次遍历时间   邮件遍历
        int i=0;
        int k=0;
        for (Message msg : msgs) {
            String uidStr="";
            String mailUid="";
            i++;
            try {
                currSjTime=new Date();//本次遍历时间   邮件遍历
                Long myyjsjfzs = getDateDiffSeconds(currSjTime, lastTimeSjT);
                long diffMillSeconds = currSjTime.getTime() - lastTimeSjT.getTime();
                lastTimeSjT=new Date();//本次遍历时间   邮件遍历
                int myyjsjfzsInt= myyjsjfzs.intValue();
                log.info("connectToEmail收件箱已打开，try folder.getUID(msg)................");
                String uid = ((POP3Folder) folder).getUID(msg);
                uidStr="uid:"+uid;
                mailUid=""+uid;
                log.info("parsing mail.... total:{}, parsing {}th, uid:{}",msgs.size(),i,uid);
                boolean isconnected = store.isConnected();
                log.info("parsing mail.... isconnected:{}, parsing {}th, uid:{}",isconnected,i,uid);
                if(myyjsjfzsInt>intMfyjsj){
                    islongIterFlag=true;
                    log.info("环境："+env+",该封邮件："+uid+",实际落地时间："+myyjsjfzsInt+"秒（"+diffMillSeconds+"毫秒），>"+intMfyjsj+",邮件服务连接状态："+isconnected+",可能存在问题，遍历邮件时间过长，可能存在落地风险，请关注！");
                    try {
                        if(!isconnected){
                            WeChatComSender.sendMessage4GzCoreMailLocal("环境："+env+",该封邮件："+uid+",实际落地时间："+myyjsjfzsInt+"秒（"+diffMillSeconds+"毫秒），>"+intMfyjsj+",邮件服务连接状态："+isconnected+",本轮落地失败，并补偿记录中等待下一轮落地！");
                        }
                    }catch (Exception exp1){
                        log.error(exp1.getMessage());
                    }
                }else{
                    log.info("环境："+env+",该封邮件："+uid+",实际落地时间："+myyjsjfzsInt+"秒（"+diffMillSeconds+"毫秒），小于"+intMfyjsj+"！");
                }
                MailImfo re = new MailImfo((MimeMessage) msg);
                String yjzt = re.getSubject();
                String yjfsr = re.getFromAddr();
                GjGzhsChkMailEntityWithBLOBs r=new GjGzhsChkMailEntityWithBLOBs();
                r.setVcId(SystemUtil.newUUID());
                r.setVcMailuid(uid);
                String mailContent = re.getBodyText();
                //r.setVcMailsubject(re.getSubject());
                r.setVcMailsubject(MailHelper.getSubjectNew((MimeMessage)msg));
                r.setVcPfrom(re.getFromAddr());
                /*if(!allyjyx.contains(r.getVcPfrom())){
                    continue;
                }*/
                r.setVcPfromname(re.getFromNickName());
                /*String cc = re.getMailAddress("CC");
                String to = re.getMailAddress("TO");*/
                String cc = MailHelper.getMailAddress((MimeMessage) msg, "CC");
                String to = MailHelper.getMailAddress((MimeMessage) msg, "TO");
                //String bcc = re.getMailAddress("BCC");
                //String sentDate = re.getSentDate();
                Date sentDate = msg.getSentDate();
                //yyyy-MM-dd HH:mm:ss
                String yyyymm = DateUtil.formatDateByPattern(sentDate, "yyyy-MM");
                String dd = DateUtil.formatDateByPattern(sentDate, "dd");
                String hh = DateUtil.formatDateByPattern(sentDate, "HH");
                r.setVcSenddate(sentDate);
                r.setVcRecvym(yyyymm);
                r.setVcRecvd(dd);
                r.setVcRecvh(hh);
                r.setVcReceivedate(now);
                r.setUpdatetime(new Date());
                r.setVcSjr(MailValUtil.truncateEmails(to,3000));
                r.setVcCc(MailValUtil.truncateEmails(cc,3000));
                //判断是否存在附件
                boolean flag = MailHelper.isContainAttachment(msg);
                //String yyyymmdd = DateUtil.getTodayFormat(DateUtil.DATE_FORMAT_YYYYMMDD);
                String yyyymmdd = DateUtil.formatDateByPattern(sentDate, DateUtil.DATE_FORMAT_YYYYMMDD);
                String mailType=EMAIL_XCMAIL_PREFIX+yjyx;
                String filepath=rulePath+File.separator+mailType+File.separator+yyyymmdd+File.separator+uid+File.separator;
                r.setVcSffj("0");
                if(flag){
                    //List<String> fileNames = re.getFileNameList((Part) msg);
                    //Pattern.matches("^.*\\.(xls|xlsx|pdf|zip|rar)"
                    //fileNames = fileNames.stream().filter(s-> FILE_PATTERN.matcher(s).matches()).collect(Collectors.toList());
                    StringBuffer sbf=new StringBuffer();
                    r.setVcPath(filepath);
                    createDir(filepath);
                    MailHelper.saveAttachment(msg,filepath,sbf,r.getVcMailsubject());
                    r.setVcSffj("1");
                    //去除最后一个 ","
                    if (StringUtils.isNotEmpty(sbf)&&sbf.length()>1) {
                        sbf.deleteCharAt(sbf.length()-1);
                    }
                    if(StringUtils.isBlank(sbf)){
                        r.setVcSffj("0");
                    }else {
                        log.info("parsing mail saving mail fj:"+sbf);
                    }
                    r.setVcFj(sbf.toString());
                }
                if(StringUtils.isNotBlank(mailContent)){
                    //r.setVcMailcontent(mailContent);
                    createDir(filepath);
                    MailHelper.saveFileUTF8(mailContent,filepath,EMAIL_CTX_FILE_NAME);
                }
                //r.setVcMailtype(yjyx);
                r.setVcMailtype(EMAIL_XCMAIL_PREFIX+yjyx);
                gzhsChkMailEntityMapper.insert(r);
                k++;
            }catch (Exception excp){
                lastTimeSjT=new Date();//本次遍历时间   邮件遍历
                StringWriter sw = new StringWriter();
                excp.printStackTrace(new PrintWriter(sw,true));
                String infomsg = sw.toString();
                log.error(infomsg);
                try {
                    //提醒判断
                    if(!istipedFlag){
                        istipedFlag=true;
                        WeChatComSender.sendMessage4GzCoreMailLocal("环境："+env+",该封邮件："+uidStr+",本轮该封邮件落地发生失败，异常信息为："+excp.getMessage());
                    }
                }catch (Exception e2){
                    log.error(e2.getMessage());
                }
                continue;
            }
            //执行完后再提交
            updateYjyxMailid(EMAIL_XCMAIL_PREFIX+yjyx, needgzMailids);
           //
        }

        if(istipedFlag){
            log.info("实际落地时间发生异常，回滚前面操作！");
            //transactionManager.rollback(status);
            //手工实现回滚 提高性能
            updateYjyxMailid(EMAIL_XCMAIL_PREFIX+yjyx, lastMailids);
        }else{
            log.info("实际落地时间正常，提交前面操作！");
            //transactionManager.commit(status);
        }

    }

    public  void connectToEmail4LocalAfterByExecutors(String yjyx) {
        log.info("connectToEmail4LocalAfter 线程池模式 连接邮箱落地完毕后的操作");
        executorService.execute(() -> {
            connectToEmail4LocalAfter(yjyx);
        });
    }

    /**
     * connectToEmail4LocalAfter 连接邮箱落地完毕后的操作
     * @param yjyx
     */
    private void connectToEmail4LocalAfter(String yjyx) {
        try {
            MailEntity e = getMailEntity(yjyx);
            Properties prop = MailUtils.getPropertiesByType(e.getServerType(),e.getHost(),e.getPort(),false);
            prop.setProperty("mail.mime.address.strict", "false");
            Session session = MailUtils.createSession(prop,e.getUserName(), AESUtils.aesDecrypt(e.getPassword()));
            Store store = session.getStore(e.getServerType());
            store.connect();
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            /**
             * 删除历史数据
             */
            deleteMailData(store, folder, yjyx);
        } catch (MessagingException ex) {
            ExceptionUtil.logErrorException(log,ex);
        }

        sendGzmailYjReport(EMAIL_XCMAIL_PREFIX+ yjyx);

        /**
         * 删除估值主邮箱历史数据
         */
        try {
            deleteZctggzMasterMailMsg(yjyx);
        }catch (Exception em){
            StringWriter sw = new StringWriter();
            em.printStackTrace(new PrintWriter(sw,true));
            String infomsg = sw.toString();
            log.error(infomsg);
        }
    }

    /**
     * 邮件日报
     * @param type
     */
    private  void sendGzmailYjReport(String type) {
        try {
            Date now=new Date();
            String hhmm = DateUtil.formatDateByYYYYMMddHHmmss(now).substring(8,12);
            Integer hhmmint = CastUtil.castInt(hhmm);
            if(hhmmint>1810 && hhmmint <1820){
                sendGzmailYjReportExec(type);
            }
            if(hhmmint>2345 && hhmmint <2355){
                sendGzmailYjReportExec(type);
            }
        }catch (Exception e){}
    }

    /**
     * 根据邮件邮箱获取邮箱配置实体 MailEntity
     * @param yjyx
     * @return
     */
    private MailEntity getMailEntity(String yjyx) {
        MailSystemConfigEntityExample mailExample = new MailSystemConfigEntityExample();
        //mailExample.createCriteria().andUsernameLike(yjyx + "%");
        mailExample.createCriteria().andDescrEqualTo(yjyx).andIsDeleteEqualTo("0");
        log.info("GzMailLocalChkService.synGzMailLocal 获取mailList开始: "+ yjyx);
        List<MailEntity> mailList = hanlderMail(mailEntityMapper.selectByExample(mailExample));
        MailEntity mailEntity = CommonHelp.listGetOne(mailList, 0, MailEntity.class);
        return mailEntity;
    }


    /**
     * 发送估值邮件落地情况日报
     */
    private  void sendGzmailYjReportExec(String type) {
        String mailType=type.replaceAll(EMAIL_XCMAIL_PREFIX,"");
        Date now=new Date();
        String yyyy = DateUtil.formatDateByYYYYMMddHHmmss(now).substring(0,4);
        String mm = DateUtil.formatDateByYYYYMMddHHmmss(now).substring(4,6);
        String dd = DateUtil.formatDateByYYYYMMddHHmmss(now).substring(6,8);
        String yyyymm=yyyy+"-"+mm;
        String sql = " select  count(1) as CNT, to_char(min(a.vc_senddate),'yyyy-MM-dd hh24:mm:ss') as MINSENDDATE, to_char(max(a.vc_senddate),'yyyy-MM-dd hh24:mm:ss') as MAXSENDDATE from  GJZQ_YYPT_GZ_CHK_MAIL  a  where a.vc_mailtype='"+type+"' and  a.vc_recvym='"+yyyymm+"' and a.vc_recvd='"+dd+"'    ";
        List<Map<String, Object>> list =gzCommonExecMapper.selectBySql(sql);
        if(CollectionUtils.isNotEmpty(list)){
            if(list.get(0)!=null){
                String val1 =CastUtil.castString(list.get(0).get("CNT"));
                String val2 =CastUtil.castString(list.get(0).get("MINSENDDATE"));
                String val3 =CastUtil.castString(list.get(0).get("MAXSENDDATE"));
                //String val4 =CastUtil.castString(list.get(0).get("CURRTCNT"));
                String info="今日当前估值邮件落地统计：实际落地条数="+val1+", 今日最早邮件接收时间="+val2+", 今日当前最新邮件接收时间="+val3+", 落地邮箱="+mailType;
                WeChatComSender.sendMessage4GzCoreMailLocal(info);
                log.info(info);
            }
        }
    }

    /**
     * 测试用落地标记
     * @return
     */
    private boolean mailSynLocalForTestFlag() {
        String syspar_hostname= SystemUtil.getSystemParams("GZYY-MAILCHK-HOSTNAME");
        log.info("测试用落地邮件主机，主机参数：{} ",syspar_hostname);
        String hostName = IpUtils.getHostName();
        log.info("测试用落地邮件主机，本机主机参数：{} ",hostName);
        if(StringUtils.isNotBlank(syspar_hostname)){
            if(!CastUtil.castString(syspar_hostname).equals(CastUtil.castString(hostName))){
                log.info("已经配置测试用落地邮件主机，且主机参数不一致，跳过。");
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param vccode  参数编码
     * @param val 参数值
     */
    private void updateYyptSysparam(String vccode,String val) {
        String s1 = CastUtil.castString(vccode).toLowerCase(Locale.ROOT);
        String s2 = CastUtil.castString(val).toLowerCase(Locale.ROOT);
        if(s1.contains("delete")||s1.contains("truncate")){
            return;
        }
        if(s2.contains("delete")||s2.contains("truncate")){
            return;
        }
        gzCommonExecMapper.updateBySql(" update GJZQ_YYPT_SYSTEMPARAMS  a set a.VC_CODENOTE='"+val+"' where  a.VC_CODE='"+vccode+"' ");
    }

    /**
     * 获取当前参数
     * @param vcCode
     * @return
     */
    private String getYyptSysParamVal(String vcCode) {
        String rsVal="";
        String sql = " select a.VC_CODENOTE as VC_CODENOTE  from  GJZQ_YYPT_SYSTEMPARAMS  a  where  a.VC_CODE='"+vcCode+"' ";
        List<Map<String, Object>> list =gzCommonExecMapper.selectBySql(sql);
        if(CollectionUtils.isNotEmpty(list)){
            if(list.get(0)!=null){
                rsVal =CastUtil.castString(list.get(0).get("VC_CODENOTE"));
            }
        }
        return rsVal;
    }


    /**
     * 发生异常信息  企业微信  邮件
     * @param infomsg
     */
    private void sendGzmailExceptionMessage(String infomsg) {
        //1DEV开发环境 2.TEST 测试环境 3.UAT 仿真环境 4.PROD 生产环境
        String env= SystemUtil.getSystemParams("GZYY-CWHQ-ENV");
        env=StringUtils.isNotBlank(env)?env:"";
        //估值邮箱落地企业微信开关  0关闭 1开启 2邮箱关闭企业微信关闭 3邮箱关闭企业微信开启
        String qywxswth=  SystemUtil.getSystemParams("GZYY-YXLD-QYWXSWTH");
        boolean qywxswthNotBlankFlag = StringUtils.isNotBlank(qywxswth);
        log.info("估值邮箱落地企业微信开关(0关闭 1开启 2邮箱关闭企业微信关闭 3邮箱关闭企业微信开启) GZYY-YXLD-QYWXSWTH(Code)  GZYY-YXLD-QYWXSWTH: "+qywxswth);
        if(qywxswthNotBlankFlag){
            if(CastUtil.castString(qywxswth).equals("1")){
                MailSendHelper.sendCwError(env +"场外投后邮件落地异常告警", infomsg);
                WeChatComSender.sendMessage4GzCoreMailLocal(env +"场外投后邮件落地异常告警:\n\r"+ infomsg);
            }else if(CastUtil.castString(qywxswth).equals("3")) {
                //MailSendHelper.sendCwError(env+"场外投后邮件落地异常告警",infomsg);
                WeChatComSender.sendMessage4GzCoreMailLocal(env +"场外投后邮件落地异常告警:\n\r"+ infomsg);
            }else{
                log.info("估值邮箱落地企业微信、邮箱开关 已关闭: "+qywxswth);
            }
        }else{
            //只有邮箱的方式  兼容以前的配置
            MailSendHelper.sendCwError(env +"场外投后邮件落地异常告警", infomsg);
        }
    }


    /**
     *
     * @param yjyx
     * @param mailids
     */
    private void updateYjyxMailid(String yjyx, String mailids) {
        if(StringUtils.isNotBlank(mailids)){
            GjGzhsMailIdsCheckEntity gmc=new GjGzhsMailIdsCheckEntity();
            gmc.setVcMailids(mailids);
            gmc.setUpdatetime(new Date());
            GjGzhsMailIdsCheckEntityExample ex=new GjGzhsMailIdsCheckEntityExample();
            GjGzhsMailIdsCheckEntityExample.Criteria criteria2 = ex.createCriteria();
            criteria2.andVcMailtypeEqualTo(yjyx);
            gzhsMailIdsCheckEntityMapper.updateByExampleSelective(gmc,ex);
        }
    }

    /**
     *
     * @param yjyx 邮箱 例如zctggz
     * @return
     */
    public  String getGzMailids(String yjyx) {
        String rsMailids="";
        try {
            GjGzhsMailIdsCheckEntityExample exm=new GjGzhsMailIdsCheckEntityExample();
            GjGzhsMailIdsCheckEntityExample.Criteria criteria = exm.createCriteria();
            criteria.andVcMailtypeEqualTo(yjyx);
            List<GjGzhsMailIdsCheckEntity> eList = gzhsMailIdsCheckEntityMapper.selectByExampleWithBLOBs(exm);
            if(CollectionUtils.isNotEmpty(eList)){
                if(eList.get(0)!=null){
                    rsMailids =CastUtil.castString(eList.get(0).getVcMailids());
                }
            }
        }catch (Exception e){
            ExceptionUtil.logErrorException(log,e);
        }
        return rsMailids;
    }

    /**
     * 删除估值主邮箱过期内容
     * @param yjyx
     * @throws Exception
     */
    public  void deleteZctggzMasterMailMsg(String yjyx) throws Exception{
        //String yjyx= SystemUtil.getSystemParams("GZYY-GZXCMAIL-YJYX");
        Store store=null;
        String gzyxPort= SystemUtil.getSystemParams("GZYY-GZXCMAIL-MYJPORT");
        if(StringUtils.isBlank(gzyxPort)){
            gzyxPort="110";
        }
        if (StringUtils.isNotBlank(yjyx)) {
            yjyx=yjyx.replace("czctggz","zctggz");
            yjyx=yjyx.replace("czctggz2","zctggz");
            yjyx=yjyx.replace("zctggz2","zctggz");
        }
        MailSystemConfigEntityExample mailExample = new MailSystemConfigEntityExample();
        mailExample.createCriteria().andUsernameLike(yjyx + "%").andIsDeleteEqualTo("0");
        log.info("GzhsCoreMailLocalChkService.deleteZctggzMasterMailMsg 获取mailList开始: "+yjyx);
        List<MailEntity> mailList = hanlderMail(mailEntityMapper.selectByExample(mailExample));
        MailEntity e = CommonHelp.listGetOne(mailList, 0, MailEntity.class);
        Date now=new Date();
        Properties prop = MailUtils.getPropertiesByType(e.getServerType(),e.getHost(),gzyxPort,false);
        prop.setProperty("mail.mime.address.strict", "false");
        Session session = MailUtils.createSession(prop,e.getUserName(), AESUtils.aesDecrypt(e.getPassword()));
        store = session.getStore(e.getServerType());
        store.connect();
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_WRITE);
        log.info("deleteZctggzMasterMailMsg 收件箱已打开，开始获取邮件");
        deleteMsg(store,folder,false);
    }

    /**
     * 删除数据 历史邮件 归档文件
     * @param store
     * @param folder
     * @param yjyx
     */
    public  void deleteMailData(Store store,Folder folder,String yjyx) {
        try {
            deleteMsg(store,folder,true);
            deleteMailFile(yjyx);
        }catch (Exception e){
            log.error("deleteMailData Failure,"+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 邮件过期提醒
     */
    public static void pwdTip() {
        try {
            String par= SystemUtil.getSystemParams("GZYY-CWMAIL-PWDTIP");
            log.info("deleteMsg 估值模块邮件删除范围 GZYY-CWMAIL-PWDTIP(Code)  par: "+par);
            String val = CastUtil.castString(par);
            if(StringUtils.isNotBlank(val)){
                Date date = DateUtil.parseDateByYYYYMMDDHHmmss(val);
                Date now=new Date();
                boolean sameDay = DateUtils.isSameDay(date, now);
                long dateDiff = getDateDiffMin(date, now);
                log.info("pwdTip dateDiff min:"+dateDiff);
                if( sameDay && dateDiff>=-15&&dateDiff<=15 ){
                    //1DEV开发环境 2.TEST 测试环境 3.UAT 仿真环境 4.PROD 生产环境
                    String env= SystemUtil.getSystemParams("GZYY-CWHQ-ENV");
                    env=StringUtils.isNotBlank(env)?env:"";
                    log.info("pwdTip sameDay:"+sameDay+",满足过期提醒：dateDiff min:"+dateDiff);
                    MailSendHelper.sendCwError(env+"估值落地程序专用邮箱密码过期提醒","邮箱密码即将过期，请注意及时修改防范风险！");
                }

            }
        }catch (Exception e){
            ExceptionUtil.logErrorException(log,e);
        }
    }


    /**
     * 删除历史邮件
     * @param store
     * @param folder
     * @throws Exception
     */
    public  void deleteMsg(Store store,Folder folder,Boolean tipFlag) throws Exception {
        String par= SystemUtil.getSystemParams("GZYY-CWMAIL-DEL");
        log.info("deleteMsg 估值模块邮件删除范围 GZYY-CWMAIL-DEL(Code)  par: "+par);
        String[] parr = CastUtil.castString(par).split(",");
        Integer i1=-100;
        Integer i2=-28;
        if(parr.length==2){
            boolean f1 = NumberUtils.isDigits(CastUtil.castString(parr[0]).replaceAll("-",""));
            boolean f2 = NumberUtils.isDigits(CastUtil.castString(parr[1]).replaceAll("-",""));
            if(f1&&f2){
                int fv1 = NumberUtils.toInt(parr[0]);
                int fv2 = NumberUtils.toInt(parr[1]);
                if(fv1<0){
                    i1=fv1;
                }
                if(fv2<0){
                    i2=fv2;
                }
            }
        }
        Date now = new Date();
        //尽量深夜执行删除 保证安全
        String hhmm = DateUtil.formatDateByYYYYMMddHHmmss(now).substring(8,12);
        int mmInteger = CastUtil.castInt(DateUtil.formatDateByYYYYMMddHHmmss(now).substring(10,12));
        int hhmm_int = NumberUtils.toInt(hhmm, 2359);
        Boolean cleanUpMailFlag = isCleanUpMailFlagByMiniutes(mmInteger);
        log.info("cleanUpMailFlag: "+cleanUpMailFlag+",NowTime:"+DateUtil.formatDateByYYYYMMddHHmmss(now));
        if(hhmm_int<550&&mmInteger<20){
            log.info("deleting oldest mail : 在深夜(00:00-5:50) 执行历史邮件删除 ");
            Date date1 = DateUtil.addDate(now, i1);
            Date date2 = DateUtil.addDate(now,i2);
            String date1Str = DateUtil.formatDateByYYYYMMddHHmmss(date1);
            String date2Str = DateUtil.formatDateByYYYYMMddHHmmss(date2);;
            log.info("deleting oldest mail: date1Str={},date2Str={} ......",date1Str,date2Str);
            Message[] messages = MailUtils.getMessageFromPOP3ByDate(folder,
                    DateUtil.parseDateByYYYYMMDDHHmmss(date1Str),
                    DateUtil.parseDateByYYYYMMDDHHmmss(date2Str));
            int msgLength = messages.length;
            MailCleanHelper.deleteMessage(folder,messages);
            folder.close(true);
            store.close();
            if(tipFlag){
                WeChatComSender.sendMessage4GzCoreMailLocal("The old historical emails are being systematically purged from the system, starting from "+DateUtil.formatDateByYYYYMMdd_HHmmss(date1)+", and ending at "+DateUtil.formatDateByYYYYMMdd_HHmmss(date2)+". During this period, "+msgLength+" emails have already been deleted");
            }
            //log.info("The old historical emails are being systematically purged from the system, starting from "+date1+", and ending at "+date2+". During this period, "+msgLength+" emails have already been deleted");
            log.info("Old historical emails are being cleaned up from "+date1Str+" to "+date2Str+", of which "+msgLength+" have been cleared.");
        }else{
            log.info("deleting oldest mail finished: 不在深夜(00:00-5:50) 不执行历史邮件删除 ");
        }

    }

    public   Boolean  isCleanUpMailFlagByMiniutes(Integer mm){
        boolean flag1= mm>0&&mm<10;
        boolean flag2= mm>20&&mm<30;
        boolean flag3= mm>40&&mm<50;
        return flag1||flag2||flag3;
    }


    /**
     * 删除归档文件
     * @throws Exception
     */
    public  void deleteMailFile(String yjyx) throws Exception {
        String par= SystemUtil.getSystemParams("GZYY-CWFILE-DEL");
        log.info("deleteMsg 估值模块邮件归档删除范围 GZYY-CWFILE-DEL(Code)  par: "+par);
        String[] parr = CastUtil.castString(par).split(",");
        Integer i1=-270;
        Integer i2=-91;
        if(parr.length==2){
            boolean f1 = NumberUtils.isDigits(parr[0]);
            boolean f2 = NumberUtils.isDigits(parr[1]);
            if(f1&&f2){
                int fv1 = NumberUtils.toInt(parr[0]);
                int fv2 = NumberUtils.toInt(parr[1]);
                if(fv1<0){
                    i1=fv1;
                }
                if(fv2<0){
                    i2=fv2;
                }
            }
        }
        Date now = new Date();
        Date date1 = DateUtil.addDate(now, i1);
        Date date2 = DateUtil.addDate(now,i2);
        String date1Str = DateUtil.formatDateByYYYY_MM_DD(date1);
        String date2Str = DateUtil.formatDateByYYYY_MM_DD(date2);
        List<String> dates = DateUtil.getRegionDayForDateByYYYY_MM_DD(date1Str, date2Str);
        String rulePath=SystemUtil.filePathTranslate(tmpFilePath+ File.separator+"mailchk"+File.separator+"local");
        for (String date : dates) {
            String yyyymmdd = ValUtil.getYYYMMDDString(date);
            String filepath=rulePath+File.separator+yjyx+File.separator+yyyymmdd+File.separator;
            File dir = new File(filepath);
            if(dir.exists()){
                log.info("deleting oldest mailFile, filepath:"+filepath);
                FileUtils.deleteDir(dir);
            }
        }
    }


    /**
     * 时间差距
     * @param endDate
     * @param nowDate
     * @return
     */
    public static long getDateDiffMin(Date endDate, Date nowDate) {
        //long nd = 1000 * 24 * 60 * 60;
        //long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        long min = diff/nm;
        // 计算差多少天
        //long day = diff / nd;
        // 计算差多少小时
        //long hour = diff % nd / nh;
        // 计算差多少分钟
        //long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return min;
    }


    /**
     * 时间差距 (秒)
     * @param endDate
     * @param nowDate
     * @return
     */
    public static long getDateDiffSeconds(Date endDate, Date nowDate) {
        long nm = 1000 ;
        long diff = endDate.getTime() - nowDate.getTime();
        long sec = diff/nm;
        return sec;
    }




    /**
     *读取邮件
     */
    private String readEMailCtxFileUTF8(String rootPath, GjGzhsChkMailEntity r) {
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

    /**
     * 连通性测试
     * @param e
     * @return
     */
    public boolean testMailConfig(MailEntity e) {
        log.info("GzMailLocalChkService.testMailConfig par1：{}，开始连接", e.getUserName());
        log.info("GzMailLocalChkService.testMailConfig par2：{}，开始连接", e.getPassword());
        return MailUtils.testConnect(e.getServerType(), e.getHost(), e.getPort(), e.getUserName(), AESUtils.aesDecrypt(e.getPassword()), false);
    }

    /**
     * 上轮或前面遗漏邮件uid
     * @param folder
     * @param type
     * @param parSet
     * @return
     * @throws Exception
     */
    private  List<Message> getMissedMailMessages (Folder folder,String type,Set<String> parSet) throws Exception {
        List<Message> messages = new ArrayList<>();
        try {
            if(CollectionUtils.isNotEmpty(parSet)){
                Set<Integer> parIntSet=new TreeSet<>();
                for (String s : parSet) {
                    parIntSet.add(CastUtil.castInt(s));
                }
                int min = Collections.min(parIntSet);
                int max =  Collections.max(parIntSet);
                Message[] rsMsgs = folder.getMessages(min,max);
                for (Message rsMsg : rsMsgs) {
                    String uid = ((POP3Folder) folder).getUID(rsMsg);
                    if (parSet.contains(uid)) {
                        messages.add(rsMsg);
                    }
                }
                log.info("邮箱："+ type + "  邮箱最近(暂停遗漏的)(rsMsgs)："+rsMsgs.length+"内未解析的邮件个数(messages)："+messages.size());
            }
        }catch (Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw,true));
            String infomsg = sw.toString();
            log.error(infomsg);
        }
        return messages;
    }

    /**
     * 获取今日业务回复的未解析邮件
     * @return messages
     */
    private  List<Message> getMessages (Folder folder,String type) throws Exception {
        List<Message> messages = new ArrayList<>();
        //取邮箱最新的50条数据
        int count = folder.getMessageCount() > 49 ? folder.getMessageCount()-49 : 1;
        Message[] leastMsgs = folder.getMessages(count,folder.getMessageCount());
        //验证邮件数 防止业务邮件漏读
        Message[] needsChkMsgs = getChkMsgs(folder,leastMsgs,type);
        //找出未曾解析的邮件
        Set<String> ids = getMailUid(type);
        StringBuffer mailSubject = new StringBuffer();
        for (Message message : needsChkMsgs) {
            String uid = ((POP3Folder) folder).getUID(message);
            if (!ids.contains(uid)) {
                messages.add(message);
                mailSubject.append(message.getSubject()).append("\n");
            }
        }
        log.info("邮箱："+ type + "  邮箱最近"+needsChkMsgs.length+"内未解析的邮件个数："+messages.size());
        log.info("分别为：\n" + mailSubject);
        return messages;
    }



    private  void createDir (String filepath) {
        File dir = new File(filepath);
        if (!dir.exists()) {
            if (!filepath.endsWith(File.separator)) {
                filepath = filepath + File.separator;
            }
            if (dir.mkdirs()) {
                log.debug(filepath + " 目录创建成功！");
            } else {
                log.debug(filepath + " 目录创建失败！");
            }
        } else {
            log.debug(filepath + " 目录已存在！");
        }
    }


    /**
     * 获取当前解析过的邮件uid集合
     * @return ids
     */
    private  Set<String> getMailUid (String type) throws Exception {
        Date date = new Date();
        Date dateprev = DateUtils.addMonths(date, -1);
        Date dateprev2 = DateUtils.addMonths(date, -2);
        String d1 = DateUtil.formatDateByPattern(date, "yyyy-MM");
        String d2 = DateUtil.formatDateByPattern(dateprev, "yyyy-MM");
        String d3 = DateUtil.formatDateByPattern(dateprev2, "yyyy-MM");
        String countSql = "SELECT count(vc_mailuid) as CNT FROM GJZQ_YYPT_GZ_CHK_MAIL where vc_mailtype = '"+type+"'";
        List<Map<String, Object>> clist =gzCommonExecMapper.selectBySql(countSql);
        Integer len=CollectionUtils.isNotEmpty(clist)?CastUtil.castInt(clist.get(0).get("CNT")):0;
        Set<String> ids = new HashSet<>();
        String sql = "SELECT DISTINCT vc_mailuid as VC_MAILUID,vc_mailtype as VC_MAILTYPE FROM GJZQ_YYPT_GZ_CHK_MAIL where vc_mailtype = '"+type+"'";
        if(len>200000){
            log.info("len>200000 getMailUid()  adj ");
            sql = "SELECT DISTINCT vc_mailuid as VC_MAILUID,vc_mailtype as VC_MAILTYPE FROM GJZQ_YYPT_GZ_CHK_MAIL where vc_mailtype = '"+type+"'"+" and  vc_recvym in ('"+d1+"','"+d2+"','"+d3+"')  ";
        }
        List<Map<String, Object>> list =gzCommonExecMapper.selectBySql(sql);
        for (Map<String, Object> map : list) {
            ids.add(String.valueOf(map.get("VC_MAILUID")));
        }
        return ids;
    }

    private  Message[] getChkMsgs (Folder folder, Message[] messages,String type) throws Exception {
        Message[] msg = new Message[0];
        String mailids = "";

        //获取上一次读取的邮件id
        /*String sql = "SELECT DISTINCT vc_mailids as VC_MAILIDS, vc_max_mailid as VC_MAX_MAILID,vc_mailtype as VC_MAILTYPE FROM GJZQ_YYPT_GZ_CHK_MAILIDS where vc_mailtype = '"+type+"'";
        List<Map<String, Object>> list =gzCommonExecMapper.selectBySql(sql);
        if(CollectionUtils.isNotEmpty(list)){
            *//*for (Map<String, Object> map : list) {
                 mailids = (String) list.get(0).get("VC_MAILIDS");
            }*//*
            mailids =CastUtil.castString(list.get(0).get("VC_MAILIDS"));
        }*/

        GjGzhsMailIdsCheckEntityExample exm=new GjGzhsMailIdsCheckEntityExample();
        GjGzhsMailIdsCheckEntityExample.Criteria criteria = exm.createCriteria();
        criteria.andVcMailtypeEqualTo(type);
        List<GjGzhsMailIdsCheckEntity> eList = gzhsMailIdsCheckEntityMapper.selectByExampleWithBLOBs(exm);
        if(CollectionUtils.isNotEmpty(eList)){
            mailids =CastUtil.castString(eList.get(0).getVcMailids());
        }

        if(StringUtils.isNotBlank(mailids)){   //if (mailids != null && !"".equals(mailids)) {
            boolean flag = false;
            for (Message message : messages) {
                String mailid = ((POP3Folder) folder).getUID(message);
                if (mailids.contains(mailid)) {
                    flag = true;
                    msg = messages;
                    break;
                } else {
                    flag = false;
                }
            }
            if (!flag) {
                //从100条开始取 50条逐步增
                msg =  getMsgs(folder, mailids, 2);
            }
        } else {
            msg = messages;
        }
        String newMailids = "";
        for (Message message : msg) {
            String mailid = ((POP3Folder) folder).getUID(message);
            newMailids = newMailids + mailid + ",";
        }
        //更新记录
        if(StringUtils.isNotBlank(newMailids)){
            newMailids = newMailids.substring(0,newMailids.length()-1);
        }

        //String updateSql = "update T_VALUATION_SYS_CHECK_MAILIDS set MAILIDS = ?, updatetime = sysdate where mailtype = ?";
        //executeUpdateSql(updateSql, newMailids, type);
        GjGzhsMailIdsCheckEntity e=new GjGzhsMailIdsCheckEntity();
        e.setVcMailids(newMailids);
        e.setUpdatetime(new Date());
        GjGzhsMailIdsCheckEntityExample ex=new GjGzhsMailIdsCheckEntityExample();
        GjGzhsMailIdsCheckEntityExample.Criteria criteria2 = ex.createCriteria();
        criteria2.andVcMailtypeEqualTo(type);
        gzhsMailIdsCheckEntityMapper.updateByExampleSelective(e,ex);
        return msg;
    }

    /**
     *
     *
     * @param folder
     * @param mailids
     * @param count
     * @return
     * @throws Exception
     */
    private  Message[] getMsgs (Folder folder, String mailids, int count) throws Exception {
        Message[] msg;
        int start = folder.getMessageCount()- 50*count + 1;
        //fix bug  add at 20230322 start
        if(start<1){
            start=1;
            Message[] messages = folder.getMessages(start, folder.getMessageCount());
            return  messages;
        }
        //fix bug  add at 20230322  end
        Message[] messages = folder.getMessages(start, folder.getMessageCount());
        for (Message message : messages) {
            String maiid = ((POP3Folder)folder).getUID(message);
            if (!mailids.contains(maiid)) {
                count++;
                return getMsgs(folder,mailids,count);
            } else {
                msg = messages;
                return msg;
            }
        }
        return null;
    }

    private   List<MailEntity> hanlderMail(List<MailSystemConfigEntity> list) {
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



}
