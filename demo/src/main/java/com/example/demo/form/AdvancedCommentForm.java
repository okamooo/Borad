package com.example.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 【上級者課題：手順7】コメント投稿用のバリデーション機能付きフォームクラス.
 * 
 * おすすめの作成順番：7.エラーチェック処理(上級課題) に対応しています。
 * 親記事の特定と、ピンポイントなエラー表示を実現するためのフィールドを保持します。
 * 
 * @author taku
 */
public class AdvancedCommentForm {

    /** 
     * 親記事ID.
     * エラーが発生した際に「どの記事の枠内にエラーを出すか」を判定するために必須となります。
     */
    @NotNull
    private Integer articleId;

    /** 名前（必須入力、50文字以内） */
    @NotBlank(message = "名前を入力して下さい")
    @Size(max = 50, message = "名前は50字以内で入力してください")
    private String name;

    /** 内容（必須入力） */
    @NotBlank(message = "内容を入力して下さい")
    private String content;

    // Getter/Setter
    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

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
