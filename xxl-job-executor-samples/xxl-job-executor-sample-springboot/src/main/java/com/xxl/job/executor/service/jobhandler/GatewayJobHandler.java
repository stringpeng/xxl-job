package com.xxl.job.executor.service.jobhandler;

import com.alibaba.fastjson.JSON;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.executor.service.dto.GatewayJobDTO;
import com.xxl.job.executor.service.tunnel.GatewayHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author StringPeng
 * Created on 2019/6/22
 */
@JobHandler(value="GatewayJobHandler")
@Component
public class GatewayJobHandler extends IJobHandler {

    @Autowired
    GatewayHttpClient httpClient;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        boolean executeState = false;

        try{
            GatewayJobDTO dto = JSON.parseObject(s, GatewayJobDTO.class);
            if(StringUtils.isEmpty(dto.getBegin()) || StringUtils.isEmpty(dto.getEnd())) {
                LocalDate preDay = LocalDate.now().plusDays(-1);
                dto.setBegin(preDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")));
                dto.setEnd(preDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59")));
            }

            String body = getBody(dto.getFuncId(), dto.getBegin(), dto.getEnd());
            httpClient.execute(dto.getUrl(), dto.getAction(), body);

            executeState = true;
        }catch (Exception ex) {
            XxlJobLogger.log(ex);
        }
        return executeState ? SUCCESS : FAIL;
    }


    private String getBody(String funcId, String begin, String end) {
        return "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "<soap:Header>\n" +
                "<appKey>c6fefd0c-3885-6912-7e80-ca6ed9bc2fcd</appKey></soap:Header>\n" +
                "<soap:Body>\n" +
                "<HIPMessageClient xmlns=\"http://client.integrator.application.sinosoft.com\">\n" +
                "<message>\n" +
                "<![CDATA[<exchange>\n" +
                "<funcid>"+ funcId +"</funcid>\n" +
                "<errcode>1</errcode>\n" +
                "<group>\n" +
                "<head>\n" +
                "<row>\n" +
                "<DE08.10.052.00>420000867</DE08.10.052.00>\n" +
                "<DE09.00.210.00>" + begin + "</DE09.00.210.00>\n" +
                "<DE09.00.211.00>" + end + "</DE09.00.211.00>\n" +
                "</row>\n" +
                "</head>\n" +
                "</group>\n" +
                "</exchange>]]>\n" +
                "</message>\n" +
                "</HIPMessageClient>\n" +
                "</soap:Body>\n" +
                "</soap:Envelope>";
    }
}