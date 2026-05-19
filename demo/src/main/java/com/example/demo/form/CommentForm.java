package com.example.demo.form;

/**
 * コメント投稿時に画面から送られてくるデータを受け取るためのフォームクラス.
 * 
 * @author taku
 */
public class CommentForm {
    /** 
     * 記事ID.
     * 【思考プロセス：親IDの保持について】
     * 掲示板システムでは「どの記事に対してコメントをするのか」を特定する必要があります。
     * リレーショナルデータベースにおいて、コメント（子）は必ず親となる記事（親）のIDを
     * 外部キーとして保持しなければなりません。そのため、画面から投稿データを送る際にも、
     * この記事ID（articleId）をセットで渡す必要があります。
     */
    private String articleId;
    /** 名前 */
    private String name;
    /** コメント内容 */
    private String content;

    // 以下、Getter/Setter
    // String型で定義しておき、後でIntegerに変換するなどの柔軟性を持たせるのが一般的です。

    public String getArticleId() {
        return articleId; // 親記事のIDを取得します
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId; // 親記事のIDをセットします
    }

    public String getName() {
        return name; // コメント者名を取得します
    }

    public void setName(String name) {
        this.name = name; // コメント者名をセットします
    }

    public String getContent() {
        return content; // コメント内容を取得します
    }

    public void setContent(String content) {
        this.content = content; // コメント内容をセットします
    }

    @Override
    public String toString() {
        return "CommentForm [articleId=" + articleId + ", name=" + name + ", content=" + content + "]";
    }
}
