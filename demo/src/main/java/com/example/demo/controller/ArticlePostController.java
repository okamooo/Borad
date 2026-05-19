package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.domain.Article;
import com.example.demo.form.ArticlePostForm;
import com.example.demo.repository.ArticleRepository;

/**
 * 【初級者課題：手順2】記事の投稿機能のみを担当するコントローラー.
 * 
 * おすすめの作成順番：2. 記事のみ投稿 に対応しています。
 * 既存の表示機能（BbsDisplayController）とは独立して作成します。
 * 
 * @author taku
 */
@Controller // Spring MVCのコントローラーとして登録します。
public class ArticlePostController {

    @Autowired // 記事をデータベースに保存するため、ArticleRepositoryを注入（DI）します。
    private ArticleRepository articleRepository;

    /**
     * 記事の投稿処理を行います.
     * 
     * @param form 画面から送信された入力データ（投稿者名と内容）が格納されたフォームオブジェクト
     * @return 投稿完了後のリダイレクト先パス
     */
    @PostMapping("/step2/post-article") // HTMLのformタグで action="/step2/post-article" method="post" とした際に呼ばれます。
    public String postArticle(ArticlePostForm form) {
        
        // 1. データベース保存用のドメインオブジェクト（Article）を新しく作ります。
        Article article = new Article();
        
        // 2. フォーム（入力用）からドメイン（保存用）へデータを移し替えます。（手動マッピング）
        // どこにどの値が入るのか、データの流れを明示的にすることでプログラムが追いやすくなります。
        article.setName(form.getName());       // フォームの名前をドメインへセット
        article.setContent(form.getContent()); // フォームの内容をドメインへセット
        
        // 3. リポジトリにドメインオブジェクトを渡し、データベースへ保存（INSERT）するよう命令します。
        articleRepository.insert(article);
        
        // 【レビュー解説：Redirect After Post (PRGパターン) について】
        // 処理が終わった後、そのまま "bbs-display.html" などのHTMLを直接返してはいけません。
        // もしHTMLを直接返すと、ユーザーがブラウザの「更新（F5）」ボタンを押した際に、
        // 直前に行った「投稿データ（POST）」がもう一度サーバーへ送信されてしまい、同じ記事が2重に投稿されるバグが発生します。
        // これを防ぐために "redirect:/" を返し、ブラウザに「次はトップページ(/)へ移動(GET)してね」と命令します。
        // 一度リダイレクトを挟むことで、更新ボタンを押しても「トップページを表示する」という安全な動作だけが行われます。
        // Web開発において、データを更新した後は必ずリダイレクトする、という鉄則です。
        return "redirect:/"; 
    }
}
