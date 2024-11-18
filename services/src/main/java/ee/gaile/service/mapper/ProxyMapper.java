package ee.gaile.service.mapper;

import ee.gaile.entity.proxy.ProxyEntity;
import ee.gaile.models.proxy.Proxy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProxyMapper {

    List<Proxy> mapToProxies(List<ProxyEntity> proxyEntity);

    Proxy mapToProxy(ProxyEntity proxyEntity);

    List<ProxyEntity> mapToProxyEntities(List<Proxy> proxy);

    ProxyEntity mapToProxyEntity(Proxy proxy);

}
