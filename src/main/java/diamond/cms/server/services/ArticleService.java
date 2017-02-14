package diamond.cms.server.services;

import static diamond.cms.server.model.jooq.Tables.C_ARTICLE;
import static diamond.cms.server.model.jooq.Tables.C_CATALOG;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;

import diamond.cms.server.core.PageResult;
import diamond.cms.server.dao.Fields;
import diamond.cms.server.model.Article;
import diamond.cms.server.model.ArticleDetail;
import diamond.cms.server.model.ArticleTag;
import diamond.cms.server.model.Tag;
import diamond.cms.server.model.jooq.tables.CArticle;

@Service
public class ArticleService extends GenericService<Article>{

    @Autowired
    TagService tagService;

    @Autowired
    ArticleTagService articleTagService;

    @Override
    public Article get(String id) {
        List<String> ids = articleTagService.findTagIds(id).stream().map(ArticleTag::getTagId).collect(Collectors.toList());
        Article article  = super.get(id);
        article.setTagIds(ids.toArray(new String[ids.size()]));
        return article;
    }

    @Override
    public Article save(Article article) {
//        String [] tagIds = article.getTagIds();
        article = super.save(article);
        this.update(article);
//        String tagNames = saveArticleTags(article.getId(), tagIds);
        return article;
    }

    private String saveArticleTags(String articleId, String[] tagIds) {
        StringBuffer tagNames = new StringBuffer();
        if (tagIds != null) {
            List<Tag> tags = tagService.saveTagIfNotExists(tagIds);
            List<ArticleTag> tagList = new ArrayList<>();
            tags.forEach(tag -> {
                ArticleTag artTag = new ArticleTag();
                artTag.setArticleId(articleId);
                artTag.setTagId(tag.getId());
                tagList.add(artTag);
                tagNames.append(tag.getName() + ",");
            });
            articleTagService.deleteByArticleId(articleId);
            articleTagService.insert(tagList);
            if (tagNames.length() > 0) {
                return tagNames.substring(0, tagNames.length() - 1);
            }
        }
        return "";
    }

    @Override
    public Article update(Article entity) {
        String tagNames = saveArticleTags(entity.getId(), entity.getTagIds());
        entity.setTagNames(tagNames);
        entity.setUpdateTime(currentTime());
        entity.setCreateTime(null);
        if (entity.getBanner() == null) {
           entity.setBanner("");
        }
        return super.update(entity);
    }

    public PageResult<Article> page(PageResult<Article> page, Optional<Integer> status, Optional<String> catalog) {
        List<Optional<Condition>> conds = ImmutableList.of(
                status.map(C_ARTICLE.STATUS::eq),
                catalog.map(C_ARTICLE.CATALOG_ID::eq)
                );
        return searchPageByCondition(page, conds.stream().filter(Optional::isPresent).map(Optional::get));
    }

    @Override
    public int delete(String id) {
        return updateStatus(id, Article.STATUS_DELETE);
    }

    public Integer updateStatus(String id, int status) {
        return dao.execute(e -> {
           return e.update(C_ARTICLE).set(C_ARTICLE.STATUS, status)
           .where(C_ARTICLE.ID.eq(id))
           .execute();
        });
    }


    public PageResult<Article> page(PageResult<Article> page, Integer status, Optional<String> catalogId, String ...keywords) {
        CArticle table = C_ARTICLE;
        List<Condition> cons = new ArrayList<>();
        cons.add(table.STATUS.eq(Article.STATUS_PUBLISH));
        catalogId.ifPresent(cid -> {
            if ("-1".equals(cid)) {
                cons.add(table.CATALOG_ID.isNull().or(table.CATALOG_ID.eq("")));
            } else if (!"0".equals(cid)) {
                cons.add(table.CATALOG_ID.eq(cid));
            }
        });
        List<Condition> kwCons = new ArrayList<>();
        Arrays.asList(keywords).forEach(k -> {
            if (k.trim().length() > 0) {
                String kwLike = "%" + k + "%";
                kwCons.add(table.TITLE.like(kwLike));
                kwCons.add(table.CONTENT.like(kwLike));
                kwCons.add(table.TAG_NAMES.like(kwLike));
            }
        });
        cons.add(kwCons.stream().reduce((a, b) -> a.or(b)).orElse(DSL.trueCondition()));
        return searchPageByCondition(page, cons.stream());
    }

    private PageResult<Article> searchPageByCondition(PageResult<Article> page, Stream<Condition> cond) {
        page = dao.fetch(page, e -> {
            return e.select(Fields.all(C_ARTICLE.fields(),C_CATALOG.NAME.as("catalogName")))
            .from(C_ARTICLE)
            .leftJoin(C_CATALOG).on(C_ARTICLE.CATALOG_ID.eq(C_CATALOG.ID))
            .where(cond.collect(Collectors.toList()))
            .orderBy(C_ARTICLE.CREATE_TIME.desc());
        }, Article.class);
        List<Article> articles = page.getData();
        if (!articles.isEmpty()) {
            Map<String,Article> articlesMap = articles.stream().collect(Collectors.toMap(Article::getId, a -> {
                a.setTags(new ArrayList<>());
                return a;
            }));
            List<ArticleTag> articleTags = articleTagService.findTags(articlesMap.keySet());
            articleTags.forEach(articleTag -> {
                articlesMap.get(articleTag.getArticleId()).getTags().add(articleTag.getTag());
            });
        }
        return page;
    }

    public Article saveDraft(Article article) {
        if (article.getId() == null) {
            article = save(article);
        } else {
            article = update(article);
        }
        return article;
    }

    public ArticleDetail getDetail(String id) {
        CArticle article = C_ARTICLE.as("a");
        CArticle before = C_ARTICLE.as("b");
        CArticle next = C_ARTICLE.as("n");
        CArticle inner = C_ARTICLE.as("i");
        ArticleDetail a = dao.execute(e -> {
            return e.select(Fields.all(article.fields(),
                before.ID.as("beforeId"),
                before.TITLE.as("beforeTitle"),
                next.ID.as("nextId"),
                next.TITLE.as("nextTitle")
                )).from(article)
                .leftJoin(before).on(before.ID.eq(e.select(inner.ID).from(inner).where(article.CREATE_TIME.ge(inner.CREATE_TIME)).and(inner.ID.ne(article.ID).and(inner.STATUS.eq(Article.STATUS_PUBLISH))).orderBy(inner.CREATE_TIME.desc()).limit(0, 1)))
                .leftJoin(next).on((next.ID.eq(e.select(inner.ID).from(inner).where(article.CREATE_TIME.le(inner.CREATE_TIME)).and(inner.ID.ne(article.ID).and(inner.STATUS.eq(Article.STATUS_PUBLISH))).orderBy(inner.CREATE_TIME).limit(0, 1))))
                .where(article.ID.eq(id))
                .fetchOne(r -> {
                    return dao.mapperEntityEx(r, ArticleDetail.class);
                });
        });
        List<Tag> tags = articleTagService.findTags(a.getId());
        a.setTags(tags);
        return a;
    }

    public Integer updateCreateTime(String id, Long time) {
        return dao.execute(e -> {
            return e.update(C_ARTICLE)
            .set(C_ARTICLE.CREATE_TIME, new Timestamp(time))
            .where(C_ARTICLE.ID.eq(id))
            .execute();
        });
    }

    public List<Article> findIdTitle() {
        return dao.execute(e -> {
            return e.select(Fields.all(C_ARTICLE.ID, C_ARTICLE.TITLE))
            .from(C_ARTICLE)
            .fetchInto(Article.class);
        });
    }

    public List<Article> findArticleSite() {
        CArticle t = C_ARTICLE;
        return dao.execute(e -> {
            return e.select(Fields.all(t.ID, t.TITLE, t.UPDATE_TIME))
            .from(t)
            .where(t.STATUS.eq(Article.STATUS_PUBLISH))
            .fetchInto(Article.class);
        });
    }

    public void recovery(String id) {
        updateStatus(id, Article.STATUS_UNPUBLISH);
    }

    public void buildTagNames() {
        List<Article> articles = findAll();
        List<ArticleTag> tags = articleTagService.findTags(articles.stream().map(Article::getId).collect(Collectors.toList()));
        Map<String,Article> articleMap = articles.stream().collect(Collectors.toMap(Article::getId, a -> {
            a.setTags(new ArrayList<>());
            return a;
        }));
        tags.forEach(t -> {
            Article art = articleMap.get(t.getArticleId());
            if (art != null) {
                art.getTags().add(t.getTag());
            }
        });
        articles.forEach(a -> {
            List<String> tagNames = a.getTags().stream().map(Tag::getName).collect(Collectors.toList());
            a.setTagNames(String.join(",", tagNames));
        });
        dao.update(articles);
    }

    public List<Article> findAll(int status) {
        final Field<?> catalogName = C_CATALOG.NAME.as("catalogName");
        List<Article> list = dao.execute(e -> {
            return e.select(Fields.all(C_ARTICLE.fields(),catalogName))
            .from(C_ARTICLE)
            .leftJoin(C_CATALOG).on(C_ARTICLE.CATALOG_ID.eq(C_CATALOG.ID))
            .where(C_ARTICLE.STATUS.eq(status))
            .orderBy(C_ARTICLE.CREATE_TIME.desc());
        }).fetch(r -> {
            Article art = r.into(Article.class);
            art.setCatalogName(r.get(catalogName, String.class));
            return art;
        });
        return list;
    }

}
