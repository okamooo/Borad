package com.example.demo.form;

/**
 * 【初級者課題：手順4】コメントを投稿する際に、画面から送られてくるデータを受け取るためのフォームクラス.
 * 
 * おすすめの作成順番：4. コメントの投稿 で使用します。
 * 
 * @author taku
 */
public class CommentPostForm {
    /** 
     * 記事ID.
     * 【思考プロセス：親IDの保持について】
     * 掲示板システムでは「どの記事に対してコメントをするのか」を特定する必要があります。
     * リレーショナルデータベースにおいて、コメント（子）は必ず親となる記事（親）のIDを
     * 外部キー（article_id）として保持しなければなりません。そのため、画面から投稿データを送る際にも、
     * 「この記事に対するコメントですよ」という情報を伝えるために、この記事ID（articleId）を
     * 画面から引き継がせる必要があります。
     */
    private String articleId; // 画面からは文字列として送られてくるためString型で定義します。
    
    /** コメント投稿者の名前 */
    private String name; // <input name="name"> と対応します。
    
    /** コメント内容 */
    private String content; // <textarea name="content"> と対応します。

    // 以下、Getter/Setter
    // Springがリクエストパラメータを適切にセット・取得するために定義します。

    public String getArticleId() {
        return articleId; // セットされた記事IDを返します。
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId; // 記事IDをフィールドに保存します。
    }

    public String getName() {
        return name; // セットされた名前を返します。
    }

    public void setName(String name) {
        this.name = name; // 名前をフィールドに保存します。
    }

    public String getContent() {
        return content; // セットされた内容を返します。
    }

    public void setContent(String content) {
        this.content = content; // 内容をフィールドに保存します。
    }

    @Override
    public String toString() {
        // デバッグ時に、どの記事に対して誰が何を投稿しようとしたかを確認するために用意します。
        return "CommentPostForm [articleId=" + articleId + ", name=" + name + ", content=" + content + "]";
    }
}
