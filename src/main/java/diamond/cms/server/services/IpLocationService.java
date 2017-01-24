package diamond.cms.server.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import diamond.cms.server.model.IpLocation;
import diamond.cms.server.model.jooq.Tables;
import diamond.cms.server.utils.IpUtil;

@Service
public class IpLocationService extends GenericService<IpLocation>{
    public IpLocation getOrSave(String ip) {
        return dao.getOptional(ip).orElseGet(() -> {
            IpLocation loc = new IpLocation();
            loc.setIp(ip);
            try {
                loc.setLocation(IpUtil.getLocation(ip));
                dao.insert(loc);
                return loc;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public List<IpLocation> saveOrList(Collection<String> ips) {
        List<IpLocation> hasIpLoc = dao.fetch(Tables.C_IP_LOCATION.IP.in(ips));
        List<String> hasIps = hasIpLoc.stream().map(IpLocation::getIp).collect(Collectors.toList());
        List<IpLocation> newIps = new ArrayList<>();
        new HashSet<>(ips).stream().filter(ip -> {
            return !hasIps.contains(ip);
        }).forEach(ip -> {
            IpLocation loc = new IpLocation();
            loc.setIp(ip);
            try {
                loc.setLocation(IpUtil.getLocation(ip));
                newIps.add(loc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        dao.insert(newIps);
        hasIpLoc.addAll(newIps);
        return hasIpLoc;
    }

}
