package diamond.cms.server.services;

import org.springframework.stereotype.Service;

import diamond.cms.server.model.Article;

@Service
public class ArticleService extends GenericService<Article>{

    @Override
    public Article update(Article entity) {
        entity.setUpdateTime(currentTime());
        return super.update(entity);
    }
}
