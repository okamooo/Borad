package com.example.demo.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Article;

/**
 * articlesテーブルを操作するリポジトリ.
 * 
 * @author taku
 */
@Repository // Springにリポジトリとして認識させ、DIコンテナに登録します
public class ArticleRepository {

    @Autowired // Spring JDBCのテンプレートを注入します
    private NamedParameterJdbcTemplate template;

    /**
     * Articleオブジェクトの内容を基にRowMapperを作成します.
     * DBの列名とJavaのフィールド名を自動でマッピングしてくれる便利なクラスです.
     */
    private static final RowMapper<Article> ARTICLE_ROW_MAPPER = new BeanPropertyRowMapper<>(Article.class);

    /**
     * 全ての記事をIDの降順で取得します.
     * 
     * @return 記事一覧
     */
    public List<Article> findAll() {
        // 新しい投稿（IDが大きいもの）が一番上に来るように「ORDER BY id DESC」を指定します。
        // これにより、掲示板としてのユーザビリティ（最新情報の優先表示）を確保します。
        String sql = "SELECT id, name, content FROM articles ORDER BY id DESC";
        
        // クエリを実行し、結果をリスト形式で返します
        return template.query(sql, ARTICLE_ROW_MAPPER);
    }

    /**
     * 記事を登録します.
     * 
     * @param article 登録する記事情報
     */
    public void insert(Article article) {
        // プレースホルダ（:変数名）を使用することで、SQLインジェクションを防ぎ安全に値を挿入します
        String sql = "INSERT INTO articles(name, content) VALUES(:name, :content)";
        
        // オブジェクトのプロパティ名をそのままプレースホルダに対応させる便利なクラスを使用します
        SqlParameterSource param = new BeanPropertySqlParameterSource(article);
        
        // 更新系処理（INSERT）を実行します
        template.update(sql, param);
    }

    /**
     * 記事を削除します.
     * 
     * @param id 削除する記事のID
     */
    public void deleteById(Integer id) {
        // IDを指定して特定の記事を削除します
        String sql = "DELETE FROM articles WHERE id = :id";
        
        // シンプルなパラメータ指定には MapSqlParameterSource を使用します
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        
        // 削除処理を実行します
        template.update(sql, param);
    }
}
