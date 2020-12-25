package ee.gaile.service.statistics;

import ee.gaile.entity.ProxyList;
import ee.gaile.service.proxy.ProxyListService;
import ee.gaile.service.proxy.SpeedTestTask;
import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.IRepeatListener;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest()
@ActiveProfiles("test")
class ProxyListServiceServiceTest {

    @Autowired
    private SpeedTestTask proxyListService;

    @Test
    void checkGetStatisticsGraph()  {
         proxyListService.doInBackground();
        SpeedTestSocket speedTestSocket = new SpeedTestSocket();


        //speedTestSocket.setProxyServer("http://192.252.215.5:16137");
        speedTestSocket.startDownload("http://ipv4.ikoula.testdebit.info/1M.iso");

// add a listener to wait for speedtest completion and progress
        speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

            @Override
            public void onCompletion(SpeedTestReport report) {
                // called when download/upload is complete
                System.out.println("[COMPLETED] rate in octet/s : " + report.getTransferRateOctet());
                System.out.println("[COMPLETED] rate in bit/s   : " + report.getTransferRateBit());
            }

            @Override
            public void onError(SpeedTestError speedTestError, String errorMessage) {
                // called when a download/upload error occur
            }

            @Override
            public void onProgress(float percent, SpeedTestReport report) {
                // called to notify download/upload progress
                System.out.println("[PROGRESS] progress : " + percent + "%");
                System.out.println("[PROGRESS] rate in octet/s : " + report.getTransferRateOctet());
                System.out.println("[PROGRESS] rate in bit/s   : " + report.getTransferRateBit());
            }
        });

        speedTestSocket.startDownloadRepeat("http://ipv4.ikoula.testdebit.info/1M.iso",
                20000, 2000, new
                        IRepeatListener() {
                            @Override
                            public void onCompletion(final SpeedTestReport report) {
                                // called when repeat task is finished
                                // called when download/upload is complete
                                System.out.println("[COMPLETED] rate in octet/s : " + report.getTransferRateOctet());
                                System.out.println("[COMPLETED] rate in bit/s   : " + report.getTransferRateBit());
                            }

                            @Override
                            public void onReport(final SpeedTestReport report) {
                                // called when a download report is dispatched
                                // called when download/upload is complete
                                System.out.println("[COMPLETED] rate in octet/s : " + report.getTransferRateOctet());
                                System.out.println("[COMPLETED] rate in bit/s   : " + report.getTransferRateBit());
                            }
                        });

    }
}
