package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.executor.core.datax.DataTransferTypeEnum;
import com.xxl.job.executor.core.model.XxlJobInfoDataxExt;
import com.xxl.job.executor.dao.XxlJobInfoDataxDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataXJob {
    private static Logger logger = LoggerFactory.getLogger(DataXJob.class);
    @Value("${datax.directory.template}")
    String template;
    @Value("${datax.directory.config}")
    String config;
    @Value("${datax.dir}")
    String dataxDir;

    @Resource
    XxlJobInfoDataxDao jobInfoDataxDao;

    @XxlJob("DataXHandler")
    public void dataxHandler() throws Exception {
        XxlJobHelper.log("Start DataX Job.");
        long jobId = XxlJobHelper.getJobId();

        // 生成配置文件
        createConfigFile(jobId);

        String path = new File(config).getCanonicalPath();

        String command = "sh " + path + "/" + jobId + ".sh";
        exec(command);
    }

    private void createConfigFile(long jobId) throws Exception {

        XxlJobInfoDataxExt xxlJobInfoDataxExt = jobInfoDataxDao.loadById(jobId);
        if (xxlJobInfoDataxExt == null) {
            throw new Exception("DataX数据不存在:" + jobId);
        }

        DataTransferTypeEnum dataTransferType = DataTransferTypeEnum.match(xxlJobInfoDataxExt.getDataxType(), null);

        List<String> fields = Arrays.stream(xxlJobInfoDataxExt.getDataxTarFields().split(",")).map(field -> "\\\"" + field + "\\\"").collect(Collectors.toList());

        String command = XxlJobHelper.getJobParam() + "\n";
        command += "SRC_USER_NAME=\"" + xxlJobInfoDataxExt.getDataxSrcUsername() + "\"\n" +
                "SRC_USER_PWD=\"" + xxlJobInfoDataxExt.getDataxSrcPassword() + "\"\n" +
                "SRC_HOST_IP=\"" + xxlJobInfoDataxExt.getDataxSrcHost() + "\"\n" +
                "SRC_HOST_PORT=" + xxlJobInfoDataxExt.getDataxSrcPort() + "\n" +
                "SRC_DB=\"" + xxlJobInfoDataxExt.getDataxSrcDb() + "\"\n" +
                "TAR_USER_NAME=\"" + xxlJobInfoDataxExt.getDataxTarUsername() + "\"\n" +
                "TAR_USER_PWD=\"" + xxlJobInfoDataxExt.getDataxTarPassword() + "\"\n" +
                "fields_map=\"" + String.join(",", fields) + "\"\n" +
                "TAR_HOST_IP=\"" + xxlJobInfoDataxExt.getDataxTarHost() + "\"\n" +
                "TAR_HOST_PORT=\"" + xxlJobInfoDataxExt.getDataxTarPort() + "\"\n" +
                "TAR_DB=\"" + xxlJobInfoDataxExt.getDataxTarDb() + "\"\n" +
                "TAR_TABLENAME=\"" + xxlJobInfoDataxExt.getDataxTarTable() + "\"\n" +
                "TAR_PRE_SQL=\"" + xxlJobInfoDataxExt.getDataxTarPreSql() + "\"\n" +
                "SRC_SQL=\"" + xxlJobInfoDataxExt.getDataxSrcSql() + "\" \n"
        ;

        String templatePath = new File(template).getCanonicalPath();
        String configPath = new File(config).getCanonicalPath();

        command += "eval \"cat <<EOF\n" +
                "$(< " + templatePath + "/" + dataTransferType.getFile() + ")\n" +
                "EOF\n" +
                "\"  > " + configPath + "/" + jobId + ".json\n";

        command += "eval \"python " + dataxDir + "/bin/datax.py " + config + "/" + jobId + ".json\"";

        File file = new File(config + "/" + jobId + ".sh");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write(command);
        writer.close();
        XxlJobHelper.log("配置文件生成完成");
    }

    private void exec(String command) throws Exception {
        int exitValue = -1;
        BufferedReader bufferedReader = null;
        try {
            // command process
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(command);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            //Process process = Runtime.getRuntime().exec(command);

            BufferedInputStream bufferedInputStream = new BufferedInputStream(process.getInputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));

            // command log
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                XxlJobHelper.log(line);
            }

            // command exit
            process.waitFor();
            exitValue = process.exitValue();
        } catch (Exception e) {
            XxlJobHelper.log(e);
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }

        if (exitValue != 0) {
            XxlJobHelper.handleFail("datax command exit value("+exitValue+") is failed");
        }
    }
}
