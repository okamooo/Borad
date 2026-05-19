package com.example.demo.domain;

import java.util.List;

/**
 * 記事情報を表すドメインクラス.
 * 
 * @author taku
 */
public class Article {
    /** 投稿ID (主キー) */
    private Integer id;
    /** 名前 */
    private String name;
    /** 記事内容 */
    private String content;
    /** 
     * コメントリスト.
     * データベースの設計（リレーショナルモデル）では、Comment側がArticleのIDを外部キーとして持ちますが、
     * Java（オブジェクト指向モデル）では「記事が複数のコメントを持っている（Has-A関係）」という構造を
     * 直接表現するために、ここにListを持たせます。これにより、記事オブジェクトからそのコメントへ
     * 直感的にアクセスできるようになります。
     */
    private List<Comment> commentList;

    // 以下、Getter/Setter
    // オブジェクトの状態を安全に操作・取得するために定義します。

    public Integer getId() {
        return id; // IDを取得します
    }

    public void setId(Integer id) {
        this.id = id; // IDをセットします
    }

    public String getName() {
        return name; // 名前を取得します
    }

    public void setName(String name) {
        this.name = name; // 名前をセットします
    }

    public String getContent() {
        return content; // 記事内容を取得します
    }

    public void setContent(String content) {
        this.content = content; // 記事内容をセットします
    }

    public List<Comment> getCommentList() {
        return commentList; // コメントリストを取得します
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList; // コメントリストをセットします
    }

    @Override
    public String toString() {
        // デバッグ時にオブジェクトの中身を確認しやすくするため、toStringをオーバーライドします
        return "Article [id=" + id + ", name=" + name + ", content=" + content + ", commentList=" + commentList + "]";
    }
}
