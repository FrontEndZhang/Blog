package com.liu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.liu.entity.Article;
import com.liu.entity.Comment;
import com.liu.service.CommentService;
import com.liu.service.UserService;
  /** 
 * @ClassName: AboutController 
 * @author: lyd
 * @date: 2019��4��17�� ����1:32:20 
 * @describe:��ת����ҳ�������
 */
@Controller
public class AboutController {
	@Autowired
	private UserService userService;
	@Autowired
	private CommentService commentService;
	@RequestMapping("/about")
	public ModelAndView about()
	{
		ModelAndView modelAndView=new ModelAndView();
		String about=userService.getUser(1).getHtmlProfile();//��ȡ���˼�飬����չʾ
		Article article=new Article();
		article.setArticleId(0);
		List<Comment> comments=commentService.getCommentByAid(0);
		for(Comment comment:comments)
		{
			comment.setChildComment(commentService.getChildComment(comment.getCommentId()));//��ȡ�����µ�����
		}
		modelAndView.addObject("article",article);
		modelAndView.addObject("comments", comments);
		modelAndView.addObject("about", about);
		modelAndView.setViewName("/home/about");
		return modelAndView;
	}
}
