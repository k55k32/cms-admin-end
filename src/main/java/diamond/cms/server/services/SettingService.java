package diamond.cms.server.services;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import diamond.cms.server.model.Setting;

@Service
public class SettingService extends GenericService<Setting>{

    public Map<String, String> findMap() {
        return this.findAll().stream().collect(Collectors.toMap(Setting::getName, Setting::getValue));
    }

}
