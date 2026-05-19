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

import com.example.demo.domain.Comment;

/**
 * 【初級者課題：手順3, 4, 5】commentsテーブルを操作するリポジトリ.
 * 
 * 3. コメントの表示、4. コメントの投稿、5. 記事とコメントの削除 の各ステップで使用します。
 * 
 * @author taku
 */
@Repository // データベースアクセスを担当するコンポーネントであることを宣言します
public class CommentRepository {

    @Autowired // データベース操作用のテンプレートをDIします
    private NamedParameterJdbcTemplate template;

    /**
     * Commentオブジェクトへのマッピング定義.
     * article_idカラムはarticleIdプロパティに自動的にマッピングされます（スネークケースからキャメルケースへの変換）.
     */
    private static final RowMapper<Comment> COMMENT_ROW_MAPPER = new BeanPropertyRowMapper<>(Comment.class);

    /**
     * 特定の記事に紐づくコメントをIDの降順で取得します.
     * 
     * @param articleId 記事ID
     * @return コメント一覧
     */
    public List<Comment> findByArticleId(Integer articleId) {
        // 記事と同様、新しいコメントが上に表示されるよう「ORDER BY id DESC」とします。
        // これにより、会話の最新の流れをユーザーが追いやすくなります。
        String sql = "SELECT id, name, content, article_id FROM comments WHERE article_id = :articleId ORDER BY id DESC";
        
        // パラメータを設定します
        SqlParameterSource param = new MapSqlParameterSource().addValue("articleId", articleId);
        
        // クエリを実行し、結果をリストで取得します
        return template.query(sql, param, COMMENT_ROW_MAPPER);
    }

    /**
     * コメントを登録します.
     * 
     * @param comment 登録するコメント情報
     */
    public void insert(Comment comment) {
        // 名前、内容、そしてどの記事に対するコメントかを示すarticle_idを保存します
        String sql = "INSERT INTO comments(name, content, article_id) VALUES(:name, :content, :articleId)";
        
        // ドメインオブジェクトからパラメータを自動生成します
        SqlParameterSource param = new BeanPropertySqlParameterSource(comment);
        
        // 登録を実行します
        template.update(sql, param);
    }

    /**
     * 特定の記事に紐づく全コメントを削除します.
     * 
     * @param articleId 記事ID
     */
    public void deleteByArticleId(Integer articleId) {
        // 記事本体が削除される際、整合性を保つために紐づくコメントも一括削除する必要があります
        String sql = "DELETE FROM comments WHERE article_id = :articleId";
        
        // パラメータを設定します
        SqlParameterSource param = new MapSqlParameterSource().addValue("articleId", articleId);
        
        // 削除を実行します
        template.update(sql, param);
    }
}
