package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.domain.Article;
import com.example.demo.domain.Comment;
import com.example.demo.form.ArticleForm;
import com.example.demo.form.CommentForm;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.CommentRepository;

/**
 * 掲示板システムの制御を行うコントローラー.
 * 
 * @author taku
 */
@Controller
@RequestMapping("/bbs")
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    /** フォームの初期化（省略） */
    @ModelAttribute
    public ArticleForm setUpArticleForm() { return new ArticleForm(); }
    @ModelAttribute
    public CommentForm setUpCommentForm() { return new CommentForm(); }

    /** 記事一覧表示（省略） */
    @GetMapping("")
    public String index(Model model) {
        List<Article> articleList = articleRepository.findAll();
        for (Article article : articleList) {
            article.setCommentList(commentRepository.findByArticleId(article.getId()));
        }
        model.addAttribute("articleList", articleList);
        return "bbs";
    }

    /** 記事投稿（省略） */
    @PostMapping("/insertArticle")
    public String insertArticle(ArticleForm form) {
        Article article = new Article();
        article.setName(form.getName());
        article.setContent(form.getContent());
        articleRepository.insert(article);
        return "redirect:/bbs";
    }

    /** コメント投稿（省略） */
    @PostMapping("/insertComment")
    public String insertComment(CommentForm form) {
        Comment comment = new Comment();
        comment.setArticleId(Integer.parseInt(form.getArticleId()));
        comment.setName(form.getName());
        comment.setContent(form.getContent());
        commentRepository.insert(comment);
        return "redirect:/bbs";
    }

    /**
     * 記事を削除します（紐づくコメントも削除されます）.
     * 
     * @param articleId 削除する記事のID
     * @return 記事一覧画面へのリダイレクト
     */
    @PostMapping("/deleteArticle")
    public String deleteArticle(Integer articleId) {
        
        // 【思考プロセス：削除の順番に関する重要なルール】
        // データベースには「参照整合性」という、データの矛盾を防ぐルールがあります。
        // 今、コメントテーブルは「どの記事に紐づくか」という外部キー（article_id）を持っています。
        
        // ① まずは「子（コメント）」から削除します。
        // もし先に親（記事）を消そうとすると、データベース側から
        // 「まだこの記事を参照しているコメントが残っているから、勝手に親を消さないで！」と
        // エラー（外部キー制約違反）を出されてしまい、削除に失敗します。
        commentRepository.deleteByArticleId(articleId);
        
        // ② 子がいなくなったことを確認してから「親（記事）」を削除します。
        // これにより、データの不整合を起こすことなく、安全に削除を完了させることができます。
        // プログラミングにおいては、この「親子関係の順序」を常に意識することが非常に大切です。
        articleRepository.deleteById(articleId);
        
        // 削除完了後は一覧画面へ戻ります
        return "redirect:/bbs";
    }
}
