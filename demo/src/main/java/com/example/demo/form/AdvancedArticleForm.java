package com.example.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 【上級者課題：手順7】記事投稿用のバリデーション機能付きフォームクラス.
 * 
 * おすすめの作成順番：7.エラーチェック処理(上級課題) に対応しています。
 * 既存のFormを一切変更せず、上級仕様の入力チェック要件を満たすために新規作成しました。
 * 
 * @author taku
 */
public class AdvancedArticleForm {

    /** 投稿者名（必須入力、50文字以内） */
    @NotBlank(message = "名前を入力して下さい")
    @Size(max = 50, message = "名前は50字以内で入力してください")
    private String name;

    /** 記事内容（必須入力） */
    @NotBlank(message = "内容を入力して下さい")
    private String content;

    // Getter/Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
