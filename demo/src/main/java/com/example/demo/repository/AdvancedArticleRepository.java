package com.example.demo.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Article;
import com.example.demo.domain.Comment;

/**
 * 【中級者課題：手順6】複雑なデータ取得（JOIN結合）を担当する独立リポジトリ.
 * 
 * おすすめの作成順番：6.記事とコメント一覧を結合して表示(中級課題) を支えるデータアクセス層です。
 * 既存のArticleRepositoryを一切変更せず、かつコントローラーにDBアクセスロジックを
 * 書かない（責任の分離を果たす）ために新規作成した上級専用のリポジトリです。
 * 
 * @author taku
 */
@Repository
public class AdvancedArticleRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    /**
     * 記事とコメントをJOINして1回のSQLで取得し、ネストしたオブジェクト構造にマッピングします.
     * 
     * @return 記事（コメント内包済み）のリスト
     */
    public List<Article> findAllWithComments() {
        String sql = "SELECT a.id AS a_id, a.name AS a_name, a.content AS a_content, "
                   + "c.id AS c_id, c.name AS c_name, c.content AS c_content, c.article_id AS c_article_id "
                   + "FROM articles a "
                   + "LEFT OUTER JOIN comments c ON a.id = c.article_id "
                   + "ORDER BY a.id DESC, c.id DESC";

        return template.query(sql, new ResultSetExtractor<List<Article>>() {
            @Override
            public List<Article> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Article> articleList = new ArrayList<>();
                Article currentArticle = null;
                int lastArticleId = -1;

                while (rs.next()) {
                    int articleId = rs.getInt("a_id");

                    if (articleId != lastArticleId) {
                        currentArticle = new Article();
                        currentArticle.setId(articleId);
                        currentArticle.setName(rs.getString("a_name"));
                        currentArticle.setContent(rs.getString("a_content"));
                        currentArticle.setCommentList(new ArrayList<Comment>());
                        
                        articleList.add(currentArticle);
                        lastArticleId = articleId;
                    }

                    int commentId = rs.getInt("c_id");
                    if (commentId != 0) {
                        Comment comment = new Comment();
                        comment.setId(commentId);
                        comment.setName(rs.getString("c_name"));
                        comment.setContent(rs.getString("c_content"));
                        comment.setArticleId(rs.getInt("c_article_id"));
                        
                        currentArticle.getCommentList().add(comment);
                    }
                }
                return articleList;
            }
        });
    }
}
