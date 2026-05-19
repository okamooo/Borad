package com.example.demo.form;

/**
 * 【初級者課題：手順2】記事を投稿する際に、画面（HTML）から送信されたデータを受け取るためのフォームクラス.
 * 
 * おすすめの作成順番：2. 記事のみ投稿 で使用します。
 * 
 * @author taku
 */
public class ArticlePostForm {
    /** 
     * 投稿者名.
     * HTMLの <input type="text" name="name"> の "name" 属性とこのフィールド名が一致している必要があります。
     * これによりSpringが自動的に入力値をこのフィールドにセット（バインド）してくれます。
     */
    private String name;
    
    /** 
     * 投稿内容.
     * HTMLの <textarea name="content"> の "content" 属性と一致させます。
     */
    private String content;

    // 以下、Getter/Setter
    // 外部（Springフレームワークやコントローラー）から値をセット・取得するために必ず用意します。

    public String getName() {
        return name; // 画面から送られてきた投稿者名を取得します。
    }

    public void setName(String name) {
        this.name = name; // 画面から送られてきた投稿者名をセットします。
    }

    public String getContent() {
        return content; // 画面から送られてきた投稿内容を取得します。
    }

    public void setContent(String content) {
        this.content = content; // 画面から送られてきた投稿内容をセットします。
    }

    @Override
    public String toString() {
        // 開発中に「フォームにどんな値が入ってきたか」をログなどで確認しやすくするために用意します。
        return "ArticlePostForm [name=" + name + ", content=" + content + "]";
    }
}
