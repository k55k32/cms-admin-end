package diamond.cms.server.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import diamond.cms.server.Const;
import diamond.cms.server.model.EmailConfig;
import diamond.cms.server.model.jooq.Tables;
import diamond.cms.server.model.jooq.tables.CEmailConfig;

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
        dao.execute(e -> {
            return e.update(table).set(table.ENABLE, Const.ENABLE)
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
}
