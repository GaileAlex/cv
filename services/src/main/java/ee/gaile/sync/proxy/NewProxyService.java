package ee.gaile.sync.proxy;

import ee.gaile.entity.proxy.ProxyList;
import ee.gaile.entity.proxy.ProxySite;
import ee.gaile.repository.proxy.ProxyRepository;
import ee.gaile.repository.proxy.ProxySitesRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Service for adding new proxies to DB
 *
 * @author Aleksei Gaile
 */
@Slf4j
@Service
@AllArgsConstructor
public class NewProxyService {
    private final ProxyRepository proxyRepository;
    private final ProxySitesRepository proxySitesRepository;

    /**
     * Searches for new proxies on sites, adds to the database
     */
    public void setNewProxy() {
        List<ProxyList> proxyLists = new ArrayList<>();

        List<ProxySite> proxySites = proxySitesRepository.findAll();

        for (ProxySite proxySite : proxySites) {
            try {
                Document doc = Jsoup.connect(proxySite.getUrl()).timeout(0).get();

                Elements table = doc.select("table");
                Elements rows = table.select("tr");

                for (int i = 1; i < rows.size(); i++) {
                    Element row = rows.get(i);

                    String[] parts = row.toString().split("[^0-9.0-9]");
                    ProxyList proxyList = new ProxyList();

                    for (String ip : parts) {

                        if (ip.matches("^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.)" +
                                "{3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$")) {
                            proxyList.setIpAddress(ip);
                            continue;
                        }
                        if (proxyList.getIpAddress() != null && !ip.equals("")) {
                            proxyList.setPort(Integer.valueOf(ip));
                            proxyList.setProtocol("SOCKS5");
                            proxyList.setCountry("unknown");
                            proxyLists.add(proxyList);
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                log.error("Site connection error {} ", proxySite.getUrl());
            }
        }

        int counter = 0;
        for (ProxyList proxyList : proxyLists) {
            // Ignore the error of adding an existing proxy to the database
            try {
                proxyRepository.save(proxyList);
                counter++;
            } catch (Exception ignored) {
            }
        }

        log.warn("Proxy saved - {}", counter);
    }

    /**
     * adds new proxies from the excel file
     * the first column of the file must contain the ip address
     * the second column must contain the port
     * <p>
     * optional:
     * third column - protocol
     * fourth column - country
     *
     * @throws IOException            - if Workbook not available
     * @throws InvalidFormatException - if Workbook not available
     */
    @PostConstruct
    private void readExcel() throws IOException, InvalidFormatException {
        String path = "proxy/Book.xlsx";
        ClassLoader classLoader = getClass().getClassLoader();
        File excel = new File(Objects.requireNonNull(classLoader.getResource(path)).getFile());

        if (excel.canRead()) {

            Workbook workbook = WorkbookFactory.create(excel);
            Sheet sheet = workbook.getSheetAt(0);
            List<ProxyList> proxyLists = new ArrayList<>();

            sheet.forEach(row -> {
                try {
                    ProxyList proxyList = new ProxyList();
                    row.getCell(0);
                    String ip = String.valueOf(row.getCell(0)).trim().split(":")[0];
                    proxyList.setIpAddress(ip);
                    String port;
                    try {
                        port = String.valueOf(row.getCell(1)).trim().split(":")[1];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        port = String.valueOf(row.getCell(1)).trim();
                    }
                    proxyList.setPort(Double.valueOf(port).intValue());

                    String protocol;
                    if (row.getCell(2) == null) {
                        protocol = "SOCKS5";
                    } else {
                        protocol = String.valueOf(row.getCell(2)).trim();
                    }
                    proxyList.setProtocol(protocol);

                    String country;
                    if (row.getCell(2) == null) {
                        country = "unknown";
                    } else {
                        country = String.valueOf((row.getCell(3))).trim();
                    }
                    proxyList.setCountry(country);

                    proxyLists.add(proxyList);
                } catch (NumberFormatException e) {
                    log.info("end of table");
                }
            });

            proxyLists.forEach((c) -> {
                try {
                    proxyRepository.save(c);
                } catch (Exception e) {
                    log.warn("duplicate key value");
                }
            });

            workbook.close();
        }
    }

}
