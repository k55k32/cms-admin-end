package diamond.cms.server.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import diamond.cms.server.model.EmailConfig;
import diamond.cms.server.model.jooq.Tables;
import diamond.cms.server.model.jooq.tables.CEmailConfig;
import diamond.cms.server.mvc.Const;

@Service
public class EmailConfigService extends GenericService<EmailConfig>{
    CEmailConfig table = Tables.C_EMAIL_CONFIG;
    public Optional<EmailConfig> getEnbale(){
        return dao.fetchOne(table.ENABLE.eq(Const.ENABLE));
    }

    public void enable(String id){
        dao.execute(e -> {
            return e.update(table).set(table.ENABLE, Const.DISABLE).execute();
        });
        enableChange(id, Const.ENABLE);
    }

    private void enableChange(String id, boolean isEnable) {
        dao.execute(e -> {
            return e.update(table).set(table.ENABLE, isEnable)
                    .where(table.ID.eq(id)).execute();
        });
    }

    @Override
    public EmailConfig save(EmailConfig config) {
        config.setCreateTime(currentTime());
        config = super.save(config);
        if (config.getEnable()) {
            enable(config.getId());
        }
        return config;
    }

    public void changeEnable(String id, Boolean enable) {
        if (enable) {
            this.enable(id);
        } else {
            enableChange(id, enable);
        }
    }
}
