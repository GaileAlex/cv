package ee.gaile.service.mapper;

import ee.gaile.entity.proxy.ProxyEntity;
import ee.gaile.models.proxy.Proxy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProxyMapper {

    List<Proxy> map(List<ProxyEntity> proxyEntity);

    Proxy map(ProxyEntity proxyEntity);

}
