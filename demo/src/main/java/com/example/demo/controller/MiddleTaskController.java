package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.domain.Article;
import com.example.demo.repository.AdvancedArticleRepository;

/**
 * 【中級者課題：手順6】1回のSQL発行（JOIN結合）でN+1問題を解決する表示用コントローラー.
 * 
 * おすすめの作成順番：6.記事とコメント一覧を結合して表示(中級課題) に対応しています。
 * 初級仕様では「記事取得(1回) + 各記事のコメント取得(N回)」の計1+N回のSQLが発行されていましたが、
 * 本クラスでは専用のリポジトリ（AdvancedArticleRepository）を活用し、
 * データの件数に関わらず「常に1回」のSQL発行で済むように実装しています。
 * 
 * @author taku
 */
@Controller
@RequestMapping("/middle") // 初級の "/" と衝突を避けるため、一旦別のパス（/middle）に設定しています
public class MiddleTaskController {

    @Autowired
    private AdvancedArticleRepository advancedArticleRepository;

    /**
     * 掲示板の一覧画面を表示します（中級・JOIN版）.
     * 
     * @param model 画面へデータを渡すためのModelオブジェクト
     * @return 表示するHTMLテンプレート名
     */
    @GetMapping("")
    public String index(Model model) {
        
        // 【レビュー解説：なぜSQLを1回に減らせるのか】
        // 詳細は AdvancedArticleRepository 内のロジックを参照してください。
        // RDBの「結合（JOIN）」機能を利用することで、DBとの通信回数を劇的に削減（N+1問題の解決）しています。
        List<Article> articleList = advancedArticleRepository.findAllWithComments();
        
        model.addAttribute("articleList", articleList);
        
        // HTML側は初級と同じ構造（articleList内の各articleがcommentListを持っている）を
        // 期待しているため、同じテンプレートを使い回すことができます。
        return "bbs-display";
    }
}
