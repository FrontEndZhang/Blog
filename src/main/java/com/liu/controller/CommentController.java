package com.liu.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.liu.Utils.DateUtil;
import com.liu.Utils.ResolveToc;
import com.liu.Utils.ResponseUtil;
import com.liu.Utils.UploadUtil;
import com.liu.entity.Article;
import com.liu.entity.Comment;
import com.liu.service.ArticleService;
import com.liu.service.CommentService;

import net.sf.json.JSONObject;

 /** 
 * @ClassName: CommentController 
 * @author: lyd
 * @date: 2019��4��17�� ����1:52:04 
 * @describe:���ۿ�����
 */
@Controller
public class CommentController {
	@Resource
	private CommentService commentService;
	@Autowired
	private ArticleService articleService;
	ResolveToc resolveToc=new ResolveToc();
	@RequestMapping("/insert_comment")
	public String insert_comment(Comment comment,HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		String lastPath="static/pic/comment/";//�ϴ���ͷ���·��
		String absolutePath="static/pic/comment/default.jpg";//Ĭ��ͷ��·��
		JSONObject jsonObject=new JSONObject();
		String request_ip=request.getRemoteAddr();//��ȡ�������۵�Ip
//		if(comment.getCommentArticleId().equals(""))
//		System.out.println("aaaa");
		Date date=new Date();
		comment.setCommentCreateTime(date);
		comment.setCommentIp(request_ip);
		comment.setCommentLikeCount(0);
		comment.setCommentRole(0);
		if(comment.getCommentAvatarImage().getOriginalFilename().length()!=0)
		{		
			absolutePath=UploadUtil.UploadbgImage(comment.getCommentAvatarImage(),lastPath, request);//�ϴ������ͷ��
			comment.setCommentAvatarPath(absolutePath);//����ͷ��·��
		}else
		{
			comment.setCommentAvatarPath(absolutePath);
		}
		if(commentService.insertComment(comment)!=null)//����������Ϣ
		{
			Comment commentTemp=new Comment();
			commentTemp.setCommentAuthorName(comment.getCommentAuthorName());
			commentTemp.setCommentContent(comment.getCommentContent());
			commentTemp.setCommentAvatarPath(absolutePath);
			articleService.addCommentCount(comment.getCommentArticleId());
			List<Article>articles=articleService.lisRecenttArticle(5);//ˢ��session�е�����
			for(Article article1:articles)
			{
				article1.setSummary(resolveToc.summary(article1.getHtmlContent()));
			}
			request.getSession().getServletContext().setAttribute("articles", articles);
			jsonObject.put("success", true);
			jsonObject.put("msg", "���۳ɹ�");
			jsonObject.put("comment", commentTemp);
			jsonObject.put("commentCreateTime", DateUtil.dateformatstring(date));
			
		}else
			{
			jsonObject.put("success", false);
			jsonObject.put("msg", "����ʧ��");
			}
		ResponseUtil.write(response, jsonObject);//���ظ�ǰ̨��������ʾ
		return null;
	}
	/**
	 * 
	* @Title: update_like  
	* @Description: �޸�����ϲ����
	 */
	@RequestMapping("/update_like")
	public String update_like(Comment comment,HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		commentService.updateLike(comment);
		return null;
	}
}
