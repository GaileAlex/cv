package ee.gaile.service.proxy;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class SpeedTestTask  {

    @Async
    public CompletableFuture<String> doInBackground(Void... params) {

        SpeedTestSocket speedTestSocket = new SpeedTestSocket();
        speedTestSocket.setProxyServer("socks5://68.183.159.95:20074");

        // add a listener to wait for speedtest completion and progress
        speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

            @Override
            public void onCompletion(SpeedTestReport report) {
                // called when download/upload is finished
                log.error("speedtest[COMPLETED] rate in octet/s : " + report.getTransferRateOctet());
                log.error("speedtest[COMPLETED] rate in bit/s   : " + report.getTransferRateBit());
            }

            @Override
            public void onError(SpeedTestError speedTestError, String errorMessage) {
                // called when a download/upload error occur
                log.error("speedtest[COMPLETED] rate in octet/s : " + speedTestError.toString());
            }

            @Override
            public void onProgress(float percent, SpeedTestReport report) {
                // called to notify download/upload progress
                log.error("speedtest[PROGRESS] progress : " + percent + "%");
                log.error("speedtest[PROGRESS] rate in octet/s : " + report.getTransferRateOctet());
                log.error("speedtest[PROGRESS] rate in bit/s   : " + report.getTransferRateBit());
            }
        });

        speedTestSocket.startDownload("https://gaile.asuscomm.com/.well-known/pki-validation/6B6528A9FA960F389DDA53A78B1E051E.txt");

        return null;
    }
}
