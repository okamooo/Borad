package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.domain.Article;
import com.example.demo.domain.Comment;
import com.example.demo.form.AdvancedArticleForm;
import com.example.demo.form.AdvancedCommentForm;
import com.example.demo.repository.AdvancedArticleRepository;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.CommentRepository;

/**
 * 【上級者課題：手順7, 8】バリデーション制御と一括削除（CASCADE）を実現する独立コントローラー.
 * 
 * おすすめの作成順番：
 * 7. エラーチェック処理(上級課題)
 * 8. 1回のSQLで記事とコメントを一括で消す(上級課題)
 * に対応しています。
 * 
 * 既存のファイルを一切変更せず、専用のAdvancedArticleRepositoryを用いることで
 * コントローラーとリポジトリの責任の分離（疎結合）を美しく保っています。
 * 
 * @author taku
 */
@Controller
@RequestMapping("/advanced")
public class AdvancedBbsController {

    @Autowired
    private ArticleRepository articleRepository; // 既存（INSERT/DELETE用）

    @Autowired
    private CommentRepository commentRepository; // 既存（INSERT用）

    @Autowired
    private AdvancedArticleRepository advancedArticleRepository; // 新規（JOIN取得用）

    /**
     * 掲示板一覧を表示します（上級仕様）.
     */
    @GetMapping("")
    public String index(Model model, AdvancedArticleForm articleForm, AdvancedCommentForm commentForm) {
        // コントローラーは「リポジトリから取得して画面に渡す」という本来の責務に専念します。
        // SQLやマッピングの複雑な処理は AdvancedArticleRepository にカプセル化されています。
        List<Article> articleList = advancedArticleRepository.findAllWithComments();
        model.addAttribute("articleList", articleList);
        return "bbs-display";
    }

    /**
     * 記事を投稿します.
     */
    @PostMapping("/post-article")
    public String postArticle(@Validated AdvancedArticleForm form, BindingResult result, Model model, AdvancedCommentForm commentForm) {
        if (result.hasErrors()) {
            return index(model, form, commentForm);
        }
        
        Article article = new Article();
        article.setName(form.getName());
        article.setContent(form.getContent());
        articleRepository.insert(article);
        
        return "redirect:/advanced";
    }

    /**
     * コメントを投稿します（上級：【手順7】ピンポイントエラー制御）.
     */
    @PostMapping("/post-comment")
    public String postComment(@Validated AdvancedCommentForm form, BindingResult result, Model model, AdvancedArticleForm articleForm, RedirectAttributes redirectAttributes) {
        
        // 【上級思考プロセス：該当箇所に特定してエラーを戻す】
        if (result.hasErrors()) {
            return index(model, articleForm, form);
        }

        Comment comment = new Comment();
        comment.setArticleId(form.getArticleId());
        comment.setName(form.getName());
        comment.setContent(form.getContent());
        
        commentRepository.insert(comment);

        return "redirect:/advanced";
    }

    /**
     * 記事を削除します（上級：【手順8】CASCADEによる一括削除）.
     * 
     * 既存のRepository.deleteById()を1回呼ぶだけで完結します。
     */
    @PostMapping("/delete-article")
    public String deleteArticle(Integer id) {
        // Java側からは「記事の削除」という命令を1回送るだけです。
        // DB側のCASCADE設定、あるいはCTE等により紐づくコメントも削除される前提です。
        articleRepository.deleteById(id);
        return "redirect:/advanced";
    }
}
