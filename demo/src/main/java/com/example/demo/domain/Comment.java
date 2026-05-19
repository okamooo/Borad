package com.example.demo.domain;

/**
 * コメント情報を表すドメインクラス.
 * 
 * @author taku
 */
public class Comment {
    /** コメントID (主キー) */
    private Integer id;
    /** 名前 */
    private String name;
    /** コメント内容 */
    private String content;
    /** 記事ID (外部キー) */
    private Integer articleId;

    // 以下、Getter/Setter
    // カプセル化の原則に基づき、フィールドへの直接アクセスを制限し、メソッド経由で操作します。

    public Integer getId() {
        return id; // コメントIDを取得
    }

    public void setId(Integer id) {
        this.id = id; // コメントIDをセット
    }

    public String getName() {
        return name; // 名前を取得
    }

    public void setName(String name) {
        this.name = name; // 名前をセット
    }

    public String getContent() {
        return content; // コメント内容を取得
    }

    public void setContent(String content) {
        this.content = content; // コメント内容をセット
    }

    public Integer getArticleId() {
        return articleId; // 紐づく記事IDを取得
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId; // 紐づく記事IDをセット
    }

    @Override
    public String toString() {
        // 開発中のログ出力などで状態を把握しやすくするために実装します
        return "Comment [id=" + id + ", name=" + name + ", content=" + content + ", articleId=" + articleId + "]";
    }
}
