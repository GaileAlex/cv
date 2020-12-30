package ee.gaile.service.proxy;

import ee.gaile.entity.proxy.ProxyList;
import ee.gaile.models.proxy.ProxyListWrapper;
import ee.gaile.repository.proxy.ProxyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class ProxyService {
    private final ProxyRepository proxyRepository;

    public ProxyListWrapper getProxy(Integer pageSize, Integer page) {
        List<ProxyList> proxyLists = proxyRepository.findWithPaging(pageSize, page * pageSize);
        ProxyListWrapper responseWrapper = new ProxyListWrapper();
        responseWrapper.setProxyLists(proxyLists);
        responseWrapper.setTotal(proxyRepository.getTotal());

        return responseWrapper;
    }

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
                    proxyList.setIpAddress(String.valueOf(row.getCell(0)).trim());
                    proxyList.setPort(Double.valueOf(String.valueOf(row.getCell(1))).intValue());
                    proxyList.setProtocol(String.valueOf(row.getCell(2)).trim());
                    proxyList.setCountry(String.valueOf((row.getCell(3))).trim());
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
