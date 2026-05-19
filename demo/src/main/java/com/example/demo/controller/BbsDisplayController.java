package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.domain.Article;
import com.example.demo.domain.Comment;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.CommentRepository;

/**
 * 【初級者課題：手順1, 3】掲示板の表示機能のみを担当するコントローラー.
 * 
 * おすすめの作成順番：
 * 1. 記事一覧のみ表示
 * 3. コメントの表示
 * に対応しています。
 * 
 * @author taku
 */
@Controller // このクラスをSpring MVCのコントローラーとして登録し、HTTPリクエストを受け取れるようにします。
public class BbsDisplayController {

    @Autowired // データベースから記事を取得するために、ArticleRepositoryを注入（DI）します。
    private ArticleRepository articleRepository;

    @Autowired // 記事に紐づくコメントを取得するために、CommentRepositoryを注入（DI）します。
    private CommentRepository commentRepository;

    /**
     * 掲示板の一覧画面を表示します.
     * 
     * @param model 画面へデータを渡すための器（Modelオブジェクト）
     * @return 表示するHTMLテンプレートの名前
     */
    @GetMapping("/") // サイトのルートパス（トップ画面）へのアクセスを受け付けます。
    public String index(Model model) {
        
        // まず、データベースからすべての記事を最新順で取得します。
        // リポジトリ内のSQLで「ORDER BY id DESC」としているため、ここで最新の記事リストが手に入ります。
        List<Article> articleList = articleRepository.findAll();
        
        // 取得した記事のリストを一つずつ取り出して、それぞれに紐づくコメントを探しに行きます。
        // ここでのループ処理が、受講生の皆さんに意識してほしい重要なポイントです。
        for (Article article : articleList) {
            
            // 【レビュー解説：N+1問題について】
            // 1. まず「記事一覧を取得するSQL」が1回実行されます。（これが 1）
            // 2. 次に、取得した記事の数（N件）だけ、このループ内で「コメント取得SQL」が発行されます。（これが N）
            // つまり、記事が100件あれば、合計101回もデータベースにアクセスすることになります。
            // これを『N+1問題』と呼び、データが増えた時にシステムの動作が極端に重くなる典型的な原因です。
            // 今回は初学者の理解を優先して「オブジェクトの構造」通りに1つずつ取得していますが、
            // 実務ではJOIN（結合）等を使って、1回のSQLでまとめて取得するなどの工夫が求められます。
            List<Comment> commentList = commentRepository.findByArticleId(article.getId());
            
            // 取得したコメントのリストを、記事オブジェクト内のフィールドにセットします。
            // これにより、Article（親）の中にComment（子）が入った「Has-A関係」のデータ構造が完成します。
            article.setCommentList(commentList);
        }
        
        // 画面（Thymeleaf）で使えるように、Modelオブジェクトに記事リストを追加します。
        // 第一引数の "articleList" という名前を使って、HTML側からデータを取り出すことができます。
        model.addAttribute("articleList", articleList);
        
        // 表示するHTMLファイルの名称（bbs-display.html）を指定します。
        // resources/templates ディレクトリ内のファイルが参照されます。
        return "bbs-display";
    }
}
