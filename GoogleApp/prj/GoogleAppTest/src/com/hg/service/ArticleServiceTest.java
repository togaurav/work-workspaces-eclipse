package com.hg.service;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.dev.LocalDatastoreService;
import com.google.appengine.tools.development.ApiProxyLocalImpl;
import com.google.apphosting.api.ApiProxy;
import com.hg.constant.TypeConstant;
import com.hg.dao.ArticleDao;
import com.hg.dao.TypeDao;
import com.hg.dto.Topic;
import com.hg.pojo.Article;
import com.hg.pojo.Comment;
import com.hg.pojo.Type;
import com.hg.pojo.User;

import core.old.TestUtil;

public class ArticleServiceTest extends TestUtil {

    @Override
    @BeforeTest
    public void setUp() throws Exception {
        super.setUp();
        ApiProxyLocalImpl proxy = (ApiProxyLocalImpl) ApiProxy.getDelegate();
        proxy.setProperty(LocalDatastoreService.NO_STORAGE_PROPERTY, Boolean.TRUE.toString());
    }

    @Override
    @AfterTest
    public void tearDown() throws Exception {
        ApiProxyLocalImpl proxy = (ApiProxyLocalImpl) ApiProxy.getDelegate();
        LocalDatastoreService datastoreService = (LocalDatastoreService) proxy.getService("datastore_v3");
        datastoreService.clearProfiles();
        super.tearDown();
    }

    @DataProvider(name = "ArticleDp")
    public Object[][] ArticleDp() throws Exception {

        Article article = new Article("Title", new User("atea"));
        article.setSummary("Summary");
        article.setContent("Content");

        // Comment cmt = new Comment("Title", "Comment", usr, article);
        Comment cmt = new Comment("Title", "Comment", new User("atea"), article);

        Object[][] testCase = new Object[1][2];
        testCase[0][0] = article;
        testCase[0][1] = cmt;
        return testCase;
    }

    @Test(dataProvider = "ArticleDp", groups = "Article")
    public void testArticle(Article article, Comment cmt) throws Exception {

        getBean("SystemSetup");
        TypeDao typeDao = getBean("TypeDao");
        typeDao.insert(new Type("Life"));
        typeDao.insert(new Type("GAE"));
        Assert.assertEquals(typeDao.count(), 3);

        ArticleDao articleDao = getBean("ArticleDao");
        articleDao.insert(article);
        Query query = new Query(Article.class.getSimpleName());
        Assert.assertEquals(DatastoreServiceFactory.getDatastoreService().prepare(query).countEntities(), 1);

        ArticleService as = getBean("ArticleService");
        List<Topic> ts = as.getList(0, false);
        Assert.assertEquals(ts.size(), 1);

        Topic t = as.getById(article.getId());
        Assert.assertNotNull(t);

        Assert.assertTrue(as.updReadCnt(article.getId(), "127.0.0.1"));
        // DaoManager.closeSession();//needn't
        Article act = articleDao.findAllById(article.getId());
        Assert.assertEquals(act.getReadIP().size(), 1);
        t = as.getById(article.getId());
        Assert.assertEquals(t.getReadCnt(), 1);

        t = as.getById(article.getId());
        Assert.assertEquals(t.getType(), TypeConstant.DEFAULT);
        t.setType("GAE");
        as.updateArticle(t);
        Assert.assertEquals(typeDao.findByName("GAE").getCountArticle(), 1);

    }

}
