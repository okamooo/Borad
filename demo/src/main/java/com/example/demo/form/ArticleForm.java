package com.example.demo.form;

/**
 * 記事投稿時に画面から送られてくるデータを受け取るためのフォームクラス.
 * 
 * @author taku
 */
public class ArticleForm {
    /** 投稿者名 (HTMLの name="name" と対応) */
    private String name;
    /** 投稿内容 (HTMLの name="content" と対応) */
    private String content;

    // 以下、Getter/Setter
    // Springがリクエストパラメータをこのオブジェクトに自動的にセット（バインド）するために必要です。

    public String getName() {
        return name; // 入力された名前を取得
    }

    public void setName(String name) {
        this.name = name; // 入力された名前をセット
    }

    public String getContent() {
        return content; // 入力された内容を取得
    }

    public void setContent(String content) {
        this.content = content; // 入力された内容をセット
    }

    @Override
    public String toString() {
        // 開発中のデバッグやログ出力でフォームの状態を確認しやすくするために定義します。
        return "ArticleForm [name=" + name + ", content=" + content + "]";
    }
}
